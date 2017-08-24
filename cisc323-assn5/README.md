# Cisc 323 (2005)

## Assn5

From the Assignment Five Description,

"It's possible that you made some errors in Assignment 4...If so, you will lose points for it in Assignment 4, but if you follow through consistently in Assignment 5 you won't be penalized again....As you are working on Assignment 5, it is quite possible that you will find problems in your design from Assignment 4...For this assignment, you may instead list for us the ways your implementation disagrees with your design and why you made these decisions. Please include this list at the beginning of your assignment, to make sure the grader sees it."

My implementation differs from the one proposed in my assignment four in these two ways:

**use of JComboBoxes to select tracks and CDs**

I didn't draw these in assignment four, because I had not considered exactly how I was going to allow the "Selection" of a track or CD at that time.

**the sequence of events**

If the user makes a change such as adding or deleting a CD or Track, then the particular editing dialog is closed so that the ComboBoxes in it may be redrawn when it is opened again.



The following functionality requested by the customer has been implemented:

- CDs must have a Title and Tag when added or modified, and

- there is a Confirmation Dialog before Reading or Exiting when the Catalog has changed (Boolean `hasChanged variable to the Catalog class)
