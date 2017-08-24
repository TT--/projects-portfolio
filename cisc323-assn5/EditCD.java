import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*; // for Vector and Collections

//The Edit CD dialog
public class EditCD
    extends JDialog
    implements ActionListener {

  private Catalog theCatalog;
  private int numOfTracks;
  private CD currentDisc;

  //this vector is for displaying a vector of JTextFields
  Vector trackArea = new Vector();

  private JComboBox comboBox;

  // All of the buttons and labels
  private JTextField cdNameField = new JTextField("", 10);
  private JTextField cdComposerField = new JTextField("", 10);
  private JTextField cdPerformerField = new JTextField("", 10);
  private JTextField cdTagField = new JTextField("", 10);
  private JLabel trackNumsLabel = new JLabel("Tracks");
  private JLabel cdSectionLabel = new JLabel("CD:");
  private JLabel trackSectionLabel = new JLabel("Track: ");
  private JLabel nameLabel = new JLabel("Name:");
  private JLabel composerLabel = new JLabel("Composer:");
  private JLabel performerLabel = new JLabel("Performer:");
  private JLabel trackNameLabel = new JLabel("Name");
  private JLabel trackComposerLabel = new JLabel("Composer ");
  private JLabel trackPerformerLabel = new JLabel("Performer");
  private JLabel tagLabel = new JLabel("Tag:");
  private JButton delTrackButton = new JButton("Delete this Track");
  private JButton saveButton = new JButton("Save and Done");
  private JButton addTrackButton = new JButton("Add Track");
  private JButton cancelButton = new JButton("Cancel");
  private String newName;
  private String newTag;
  private String newComp;
  private String newPerf;

  // the constructor
  public EditCD(JDialog parent) {
    super(parent, true);
    setTitle("Edit CD");
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    setVisible(false); // not visible until parent calls showDialog
  } // end constructor

  //called from the EditCatalog
  public void showDialog(Catalog cat, int cdNumber) {
    theCatalog = cat;
    currentDisc = (CD) theCatalog.getItem(cdNumber);
    cdNameField.setText(currentDisc.getTitle());
    cdComposerField.setText(currentDisc.getComposer());
    cdPerformerField.setText(currentDisc.getPerformer());
    cdTagField.setText(currentDisc.getTag());

    numOfTracks = currentDisc.getNumTracks();
    String choices[] = new String[numOfTracks];

    Container contents = getContentPane();
    contents.setLayout(new BorderLayout());

    //this grid holds column labels, and all track info
    JPanel mainPanel = new JPanel(new GridLayout(numOfTracks + 1, 4));
    //add the titles
    mainPanel.add(trackNumsLabel);
    mainPanel.add(trackNameLabel);
    mainPanel.add(trackComposerLabel);
    mainPanel.add(trackPerformerLabel);

    for (int trackNum = 0; trackNum < numOfTracks; trackNum++) {
      //a track label
      JLabel trkLabel = new JLabel(Integer.toString(trackNum + 1));

      //make some textfields
      JTextField titleField = new JTextField(currentDisc.getTrack(trackNum).
                                             getTitle());
      JTextField composerField = new JTextField(currentDisc.getTrack(trackNum).
                                                getComposer());
      JTextField performerField = new JTextField(currentDisc.getTrack(trackNum).
                                                 getPerformer());
      //could setField columns here if desired, instead they size themselves
      //add the fields to the Vector
      trackArea.add(3 * trackNum, titleField);
      trackArea.add(3 * trackNum + 1, composerField);
      trackArea.add(3 * trackNum + 2, performerField);

      //add everything to a panel
      mainPanel.add(trkLabel);
      mainPanel.add( (JTextField) trackArea.get(3 * trackNum));
      mainPanel.add( (JTextField) trackArea.get(3 * trackNum + 1));
      mainPanel.add( (JTextField) trackArea.get(3 * trackNum + 2));

      String nameTrackShort = (trackNum + 1) + "- " +
          currentDisc.getTrack(trackNum).getTitle();
      choices[trackNum] = nameTrackShort;
    } // end for CDNum

    //all the GUI crap
    comboBox = new JComboBox(choices);
    delTrackButton.addActionListener(this);
    saveButton.addActionListener(this);
    addTrackButton.addActionListener(this);
    cancelButton.addActionListener(this);

    JPanel panelTop = new JPanel();
    panelTop.add(cdSectionLabel);
    panelTop.add(nameLabel);
    panelTop.add(cdNameField);
    panelTop.add(composerLabel);
    panelTop.add(cdComposerField);
    panelTop.add(performerLabel);
    panelTop.add(cdPerformerField);
    panelTop.add(tagLabel);
    panelTop.add(cdTagField);
    contents.add(panelTop, BorderLayout.NORTH);

    JPanel centrePanel = new JPanel();
    centrePanel.add(mainPanel);
    contents.add(centrePanel, BorderLayout.CENTER);

    JPanel panelBot = new JPanel();
    JPanel boxPanel = new JPanel();
    boxPanel.add(comboBox);
    panelBot.add(trackSectionLabel);
    panelBot.add(boxPanel);
    panelBot.add(delTrackButton);
    contents.add(panelBot, BorderLayout.SOUTH);

    JPanel panelRight = new JPanel(new GridLayout(0, 1));
    JPanel butPanel1 = new JPanel();
    JPanel butPanel2 = new JPanel();
    JPanel butPanel3 = new JPanel();
    butPanel1.add(addTrackButton);
    butPanel2.add(saveButton);
    butPanel3.add(cancelButton);
    panelRight.add(butPanel1);
    panelRight.add(butPanel2);
    panelRight.add(butPanel3);
    contents.add(panelRight, BorderLayout.EAST);

    pack();
    setVisible(true);
  } // end showDialog

  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();

    if (source == delTrackButton) {
      int num = comboBox.getSelectedIndex();
      if (num < 0 || num > (numOfTracks - 1)) {
        JOptionPane.showMessageDialog(this, "selected track num not present");
      }
      else {
        DeleteTrack delTrackDialog = new DeleteTrack(this);
        delTrackDialog.showDialog(theCatalog, currentDisc, num);
        if (numOfTracks != currentDisc.getNumTracks()) {
          dispose(); //a track has changed, so need to refresh everything
        }
      }
    }
    else if (source == saveButton) {
      newName = cdNameField.getText();
      newTag = cdTagField.getText();
      newComp = cdComposerField.getText();
      newPerf = cdPerformerField.getText();
      if (newName.equals("") || newTag.equals("")) {
        JOptionPane.showMessageDialog(this, "CDs must have a Title and Tag");
      }
      else {
        currentDisc.setTitle(newName);
        currentDisc.setPerformer(newPerf);
        currentDisc.setComposer(newComp);
        currentDisc.setTag(newTag);
        for (int trackNum = 0; trackNum < numOfTracks; trackNum++) {
          //the tracks take the tag from the CD if it has changed
          Track currentTrack = currentDisc.getTrack(trackNum);
          currentTrack.setTag(cdTagField.getText());
          //this stuff is necessary to pull the text back out of the textfields
          JTextField currentField1 = (JTextField) trackArea.get(3 * trackNum);
          String currentString1 = currentField1.getText();
          JTextField currentField2 = (JTextField) trackArea.get(3 * trackNum +
              1);
          String currentString2 = currentField2.getText();
          JTextField currentField3 = (JTextField) trackArea.get(3 * trackNum +
              2);
          String currentString3 = currentField3.getText();
          currentTrack.setTitle(currentString1);
          currentTrack.setComposer(currentString2);
          currentTrack.setPerformer(currentString3);
          setVisible(false);
        } // end for
      }
    }
    else if (source == addTrackButton) {
      AddTrack addTrackDialog = new AddTrack(this);
      addTrackDialog.showDialog(currentDisc);
      if (numOfTracks != currentDisc.getNumTracks()) {
        dispose(); //a track has changed, so need to refresh everything
      }
    }
    else if (source == cancelButton) {
      setVisible(false);
    }
  }
} // end EditCD
