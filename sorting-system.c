// author: Tyler (at) Trezise (dot ca)
// (Refer to system diagram for port wiring)
// OR = Optical (phototransistor) at Reflectivity station
// EX = infrared sensor at Exit of belt
// HE = Hall Effect sensor (sorted bin position)
// WH = white
// BL = black
// AL = aluminum
// ST = steel

#include <stdlib.h>
#include <inttypes.h>
#include <avr/interrupt.h>
#include <avr/io.h>

//=============================  STRUCTURES =================
typedef struct link
	{
	char objType;	// Object Types: 1-WHITE 2-BLACK 3-AL 4-STEEL
	char goalPos;
	struct link *next;
	} link;

//========================= SUBROUTINE PROTOTYPES ============
char classify(unsigned char input);
void findHome();
void stepper(char stepDir, int pulses);
void driveBelt(int dir);
void msDelay(int count);  // aka mTimer
void adjustBin(int offset);

void initLink(link **newLink, volatile unsigned char ADCVal);
void setup(link **h, link **t);
void clearQueue(link **h, link **t);
void enqueue(link **h, link **t, link **nL);
void dequeue(link **h, link **t, link **deQueuedLink); // FIXED version (provided version had bug)
int size(link **h, link **t);
char isEmpty(link **h);
char getObjType(link **aLink);
char getGoalPos(link **aLink);

//=============================  VARIABLES ============
volatile unsigned char ADC_result;
volatile unsigned char lowADC_result;
volatile unsigned char ADC_result_flag;
volatile unsigned char OR_flag=0;
volatile unsigned char EX_flag=0;
volatile unsigned char step[4];     // array of stepper drive sequences, see below
volatile unsigned char measured[4]; // counts of measured (at ADC) items
volatile unsigned char sorted[4];   // counts of sorted (dropped) items
volatile unsigned char foundHome=0;
volatile unsigned char BeltSpeedNorm=125;  // (90 was safe before stepper accel. implemented)
volatile unsigned char BeltSpeedSlow=36;
volatile unsigned char pauseFlag=0;
volatile unsigned char stopFlag=0;
volatile unsigned char flag=0;
static uint16_t switch1BitStream = 0;  //waveform of switch1 contact represented as binary
static uint16_t switch2BitStream = 0;  //waveform of switch2 contact represented as binary

int lastStep=0;     // holds last used stepper sequence
int stepDelayHolder;
int stepDelayMin=8; // 7 ok, 8 to be safe   -------  8
int stepDelay =15;  // 11 stalls, 12 mostly ok, but some errors -------  15
int dispDelay =500;
int debounceDelay =100;
int binPos;         // 0 is home pos, set by HE low causing INT4 to reset. 200 steps possible,
int offset; 		// 16 bit int is safe because 200 steps x 48 items = 9600 max

link *qHead; // ptr to the head of Queue
link *qTail; // ptr to tail of Queue
link *newLink;	// ptr to TYPE LINK
link *rtnLink;	// ptr to TYPE LINK

//=========================   MAIN  =============================================
int main(){
	cli(); //disable interrupts globally

	step[0]=0b00110000; //aka "Step1"
	step[1]=0b00000110; //aka "Step3"
	step[2]=0b00101000; //aka "Step2"
	step[3]=0b00000101; //aka "Step4"

	DDRD=0xF0; // output for on board LEDs, input for External Interrupts INT[3..0]=IN,OR,EX,OI
	DDRE=0;    // input for HE and swtiches SW1 on PINE6, INT4=HE, INT7=SW2
	DDRC=0xFF; // output for STEPPER PC[5..0]=E1 L1 L2 E2 L3 L4
	DDRA=0xFF; // output for LEDs
	DDRF=0;    // input from ADC at PF0
	DDRB=0xFF; // output for belt motor PB[3..0]=IA EA EB IB, PWM at PB7 (p80)

	TCCR1B |= _BV(CS10);   // use TC1 for msDelay routine, with no prescale
	TCCR3B |= _BV(CS30);   // use TC3 for debounce polling, with no prescale
	TCCR3B |= _BV(WGM32);  // TC3 set WGM to CTC (Clear Timer on Compare) p142
	OCR3A = 5000;          // OCRA (largest compare Reg) set for 5000 cycles = 5ms (for switch)
	//OCR3B = 1000;        // OCRB set for x cycles = x ms (for EX)
	TIMSK3 |= _BV(OCIE3A); // enable compare A, B INT
	TIFR3 |=_BV(OCF3A);    // clear compare match flag, begin timer

	// ==== PWM ===>  set 8bit timer0 to FAST PWM (p113), TOV at MAX,
	// clear OC0A on Compare Match, set OC0A at TOP (diag. p107)
	TCCR0A |= _BV(WGM00) | _BV(WGM01) | _BV(COM0A1);
	// set Clock Select (prescaler) for clk/8 (p114) (good for belt motor)
	TCCR0B |= _BV(CS01);
	// set Output Compare Reg for duty cycle = OCR0A/255
	OCR0A = BeltSpeedNorm;   /// <==== END PWM ====

	EIMSK |= _BV(INT1);  // enable Ext INT1 - EX
	EIMSK |= _BV(INT2);  // enable Ext INT2 - OR
	EIMSK |= _BV(INT4);  // enable Ext INT4 - HE

	// 00-low 01-any edge 10-falling 11-rising
	EICRA |= _BV(ISC01) | _BV(ISC31);  // INT0,3 on Falling edge
	EICRA |= _BV(ISC11);  // INT1 on 
	EICRA |= _BV(ISC20) | _BV(ISC21);  // INT2 on Rising Edge
	EICRB |= _BV(ISC41);  // INT4 on Falling edge

	//ADC settings: Left Adjust, read from ADCH (ADLAR), Volt Ref =AVcc
	ADMUX |= _BV(ADLAR) | _BV(REFS0);
	ADCSRA |= _BV(ADEN) | _BV(ADIE); // enable ADC, enable INT on Conv Complete
	ADCSRB |= _BV(7);                // enable hi-speed mode

	setup(&qHead, &qTail);  //sets head & tail to NULL

	sei();	// enable interrupts globally

	findHome();
	driveBelt(1);

    // MAIN LOOP
	while(1){

		if (OR_flag){  // object enters OR
			lowADC_result=0xFF;  // start with high values
			ADC_result=0xFF;

			while((PIND & 0b00000100)==0b00000100){  // OR still high (still blocked, object still present)
				if (!ADC_result_flag) ADCSRA |= _BV(ADSC);  // flag set in ADC ISR, cleared below
				else{
					if (ADC_result < lowADC_result) lowADC_result = ADC_result;  //keep lowest 
					ADC_result_flag=0; //reset flag (ready for next conv)
				}
			} //end while - OR path clear
			
			PORTA =lowADC_result;  // used for determining ranges
			if (lowADC_result < 0xFF){  // if a good value was read, queue it
				initLink(&newLink, lowADC_result);
				enqueue(&qHead, &qTail, &newLink);
				measured[getObjType(&newLink)-1]++;
				PORTD=_BV(getObjType(&newLink)-1) <<4;
			}

			OR_flag=0;
		} //end if OR_flag
	
		if ((measured[0]+measured[1]+measured[2]+measured[3]==1) && !flag){
			OCR0A=BeltSpeedSlow;
			adjustBin(offset);
			flag=1;
			}
		else OCR0A = BeltSpeedNorm;

		// adjust bin starting here always if not using exit sensor
		// adjustBin(offset);

		while (pauseFlag){
			PORTA=0x80 | sorted[0]; //WH
			msDelay(dispDelay);
			PORTA=0x40 | sorted[1]; //BL
			msDelay(dispDelay);
			PORTA=0x20 | sorted[2]; //AL
			msDelay(dispDelay);
			PORTA=0x10 | sorted[3]; //ST
			msDelay(dispDelay);
		}
		while (stopFlag > 250){
			driveBelt(0);
			PORTA=0x80 | measured[0]; //WH
			msDelay(dispDelay);
			PORTA=0x40 | measured[1]; //BL
			msDelay(dispDelay);
			PORTA=0x20 | measured[2]; //AL
			msDelay(dispDelay);
			PORTA=0x10 | measured[3]; //ST
			msDelay(dispDelay);
		}

	} //end while(1) Main Loop
} //end main

//=================== INTERRUPT SERVICE ROUTINES ============

ISR(INT2_vect){ // Optical at Reflective at Rising edge
	OR_flag=1;
}

ISR(ADC_vect){
	ADC_result =ADCH;
	ADC_result_flag =1;
	}

ISR(INT4_vect){ // stepper HE sensor
	binPos =0;
	foundHome=1;
}

ISR(TIMER3_COMPA_vect){ //happens every 5ms

	if (stopFlag && stopFlag<255 ) stopFlag++; //once stop is detected, count up to some value (time for belt to clear)

	if (!isEmpty(&qHead)){ //if not empty, compute a bin offset for the 1st (head) item
			offset = getGoalPos(&qHead) - (binPos % 200);
			if (offset>=100) offset=-(200-offset);
			else if (offset<=-100) offset=200+offset;
	}
	else offset=0;

	//new BitStream is old BitStream shifted left once, and fed 1 for switch down, (high bits are don't care)
	switch1BitStream = (switch1BitStream<<1) | ((PINE & 0b01000000) == 0) | 0xE000; //sw1 on PINE6
	switch2BitStream = (switch2BitStream<<1) | ((PINE & 0b10000000) == 0) | 0xE000; //sw2 on PINE7

	if (switch1BitStream==0xF000){
		pauseFlag = !pauseFlag;
		driveBelt(!pauseFlag);
	}
	if (switch2BitStream==0xF000) stopFlag = 1;
} //end ISR TC3 COMPA

ISR(TIMER3_COMPB_vect){ //happens every x ms

} //end ISR TC3 COMPB

ISR(INT1_vect){ // EX  //, ISR_NOBLOCK
	//EX_flag=1;
	msDelay(4);
	if((PIND & 0b00000010)==0){  //EX still low, object present
		driveBelt(0);
		adjustBin(offset);
		driveBelt(1);
		if (!isEmpty(&qHead)){
			dequeue(&qHead, &qTail, &rtnLink);
			sorted[getObjType(&rtnLink)-1]++;
			if (isEmpty(&qHead)) flag=0;
		}
	} //end EX check 
}

ISR(BADISR_vect){}

//========================= SUBROUTINES ===================
void adjustBin(int amount){
	if (amount > 0) stepper(0, abs(amount)); //0=CW
	else stepper(1, abs(amount)); //1=CCW
} //end adjustBin

void findHome(){
	while (!foundHome) stepper (0,1);
} // end findHome

void stepper(char stepDir, int pulses){
	stepDelayHolder =stepDelay;  // time delay decrementer
	int j = 0;  // loop counter
	while (j < pulses){
		if (stepDir ==0){ //CW
			lastStep++;
			if (lastStep ==4) lastStep=0;
			binPos++;
		}
		else if (stepDir ==1){ //CCW
			lastStep--;
			if (lastStep<0) lastStep=3;
			binPos--;
		}
		else {}
		PORTC=step[lastStep];
		// accel: reduce delay until some minimum
		if (stepDelayHolder >stepDelayMin) stepDelayHolder--;
		msDelay(stepDelayHolder);
		j++;
	}// end while
	return;
} //end stepper

// belt motor outputs PB[3..0]=IA EA EB IB where (EA=EB=0)
void driveBelt(int dir){
	if (dir ==1) { //CW  IA=1 IB=0
	PORTB &= 0b10000000;
	PORTB |= 0b00001000;
	}
	else if (dir ==2){ // CCW IA=0 IB=1
	PORTB &= 0b10000000;
	PORTB |= 0b00000001;
	}
	else if (dir ==0){ // stop IA=0 IB=0
	PORTB &= 0b10000000;
	}
	else {}
	return;
} //end driveBelt

void msDelay(int count){

	int k = 0;  // loop counter
	 TCCR1B |= _BV(WGM12); // TC1 set WGM to CTC (Clear Timer on Compare) p142
	 OCR1A = 1000; // Set Output Compare Reg for 1000 cycles = 1ms
	 TCNT1 = 0;
	 TIFR1|=_BV(OCF1A); // Clear compare match flag (begin timer)
		 while(k<count){
		 	if((TIFR1 & 0x02)==0x02){ //  Check for compare match flag
		 		TIFR1 |=_BV(OCF1A); // clear the interrupt flag
		 		k++;
		 	}
		 } // end while
	 return;
	 
 } // msDelay 

void setup(link **h,link **t){
	*h = NULL;
	*t = NULL;
	return;
} //end setup

char getObjType(link **aLink){
	return (*aLink)->objType;
} //end getObjType

char getGoalPos(link **aLink){
	return (*aLink)->goalPos;
} //end getGoalPos

// allocates memory for a link, needs address of pointer to link and ADC value
void initLink(link **newLink, volatile unsigned char ADCVal) {
	*newLink = malloc(sizeof(link));
	(*newLink)->next = NULL;
	(*newLink)->objType = classify(ADCVal);
	(*newLink)->goalPos = ((*newLink)->objType -1) *50;  //0-WH, 50-BL, 100-AL, 150-ST
	return;
	} //end initLink

// ADD LINK TO TAIL, needs address of pointers to head, tail, and link to be enqueued
void enqueue(link **h, link **t, link **nL){
	if (*t != NULL){   //Queue not empty
		(*t)->next = *nL;  //change last link to point to new one
		*t = *nL;   //tail becomes added link
	}
	else{			//Queue is empty
		*h = *nL;
		*t = *nL;
	}
	return;
} // end enqueue

// REMOVE from HEAD, assign it to deQueuedLink
void dequeue(link **h, link **t, link **deQueuedLink){
	*deQueuedLink = *h;

	if (*h != NULL){	// check if not empty
		//link *temp;
		//temp = *h;
		*h = (*h)->next;
		if (*h==NULL) *t=NULL;  //was last item, so now empty
		//free(temp);
	}
	return;
} //end dequeue

// clearQueue deallocates all memory consumed by the queue
void clearQueue(link **h, link **t){
	link *temp;
	while (*h != NULL){
		temp = *h;
		*h = (*h)->next;
		free(temp);
	}
	*t = NULL;  //set tail to NULL
	return;
} //end clearQueue

// size returns INTEGER number of links in the queue
int size(link **h, link **t){
	link *temp;			// store the link while traversing the queue
	int numElements =0;
	temp = *h;			// point to the first item in the list

	while(temp != NULL){
		numElements++;
		temp = temp->next;
	}
	return(numElements);
} //end size

// isEmpty returns 1 if the queue is empty, 0 if the queue is NOT empty
char isEmpty(link **h) {return(*h == NULL);} //end isEmpty

// FOR OCR0A = 125 at setup corner by door - Mon12th
char classify(unsigned char input){
	if (input < 105){return 3;}  // AL (13-45)
	if (input < 200){return 4;} // STEEL (121-166)
	if (input < 233){return 1;} // WHITE (219-220)
	else {return 2;}  //BLACK (229-231)
} //end classify
