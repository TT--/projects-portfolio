/******************************************************************************
 * This class represents the main GUI window for the CD catalog program.
 * It gives the user a "menu" of buttons to choose what to do.
 * Part of CD catalog program for CISC 323, winter 2005.
 *
 * author: Margaret Lam
 * modified by Tyler Trezise for ASSN5
 *****************************************************************************/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import hsa.*; // for simple text I/O

public class MainGUIWindow
    extends JFrame
    implements ActionListener {
  //CHANGE
  // The current catalog.  Customer requested to start with a blank catalog.
  private Catalog theCatalog = new Catalog();

  // A dialog for displaying the catalog.  It will be invisible until
  // we call its setVisible method.
  private UnsortedDisplayDialog catDisplayDialog =
      new UnsortedDisplayDialog(this);

  // A dialog for letting the user pick sorting choices.  It will be
  // invisible until we call its setVisible method.
  private SortDialog sortChoiceDialog =
      new SortDialog(this);

  // We declare this chooser and create it once to save time.  It is
  //invisible until we call a show method to make it appear on the screen.
  //This is more efficient than creating a new file chooser every time one
  //is needed. The parameter to the constructor is the name of the initial
  //folder. The name "." takes you to the folder where this program lives.
  private JFileChooser chooser = new JFileChooser(".");

  // The buttons to appear in this window
  private JButton displayButton; // display the unsorted catalog
  private JButton readButton; // read a catalog from a file
  private JButton sortButton; // sort and display the catalog
  private JButton exitButton; // exits the program
  private JButton editButton; // go to edit screen

  //CHANGE: save the catalog button
  private JButton saveButton;

  // Constructor: Sets up the window, with components and actions.
  public MainGUIWindow() {
    setTitle("CD Catalog Program");
    // The inner container to which we can add components
    Container contents = getContentPane();
    // Lay out components vertically.  That means a 1-column vertical layout
    contents.setLayout(new GridLayout(0, 1));

    // The spaces in the label's text area to force the window to be
    // a little wider than the text,
    contents.add(new JLabel("    Please select desired operation:    "));

    // Put each button inside a panel with a flow layout.
    // Please do not get the impression from this that every component
    // in a Swing window much be inside a panel -- this is a common
    // student misconception!  In this case, however, we don't want the
    // buttons to stretch, which they'll do if we add them
    // directly to the grid layout.  This way, the grid layout will
    // stretch the panels instead, which will have no effect on the
    // button centered inside each panel.

    readButton = new JButton("Read Catalog");
    JPanel readPanel = new JPanel(); // default: centered flow layout
    readPanel.add(readButton);
    contents.add(readPanel);

    editButton = new JButton("Edit Catalog");
    JPanel editPanel = new JPanel();
    editPanel.add(editButton);
    contents.add(editPanel);

    displayButton = new JButton("Display Catalog");
    JPanel displayPanel = new JPanel();
    displayPanel.add(displayButton);
    contents.add(displayPanel);

    sortButton = new JButton("Display in Sorted Order");
    JPanel sortPanel = new JPanel();
    sortPanel.add(sortButton);
    contents.add(sortPanel);

    //CHANGE: save button added
    saveButton = new JButton("Save Catalog");
    JPanel savePanel = new JPanel();
    savePanel.add(saveButton);
    contents.add(savePanel);

    exitButton = new JButton("Exit");
    JPanel exitPanel = new JPanel();
    exitPanel.add(exitButton);
    contents.add(exitPanel);

    // register listener for all buttons
    displayButton.addActionListener(this);
    readButton.addActionListener(this);
    editButton.addActionListener(this);
    sortButton.addActionListener(this);
    exitButton.addActionListener(this);
    //CHANGE: savebutton needs an "action listener" apparently
    saveButton.addActionListener(this);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    pack();
    setVisible(true);
  } // end constructor

  /****************************************************************************
   * Action-handling method for all of the buttons.  The method is called
   * whenever the user clicks a button.
   * Parameter: an object describing the button-click event; used to
   *   determine which button was clicked
   ***************************************************************************/
  public void actionPerformed(ActionEvent e) {
    // the source of the event (i.e. the button that was clicked)
    Object source = e.getSource();

    if (source == readButton) {
      if (theCatalog.hasChanged()) {
        int ans = JOptionPane.showConfirmDialog(this,
                                                "The catalog has changed, do you wish to continue?",
                                                "Catalog has changed", 0);
        if (ans != 0) { //if ans is not yes, then it is no
        }
        else {
          // The showOpenDialog method makes the file chooser visible and returns
          // only when the user has made a choice or clicked the "cancel" button.
          // The parameter is the "parent" frame for the file chooser -- this
          // frame.  Swing will position the file chooser on top of its parent.
          int returnVal = chooser.showOpenDialog(this);
          // If the user successfully chooses and existing file, rather than
          // clicking "cancel" or making some kind of error, the return value
          // from showOpenDialog will be the constant JFileChooser.APPROVE_OPTION
          if (returnVal == JFileChooser.APPROVE_OPTION) {
            // The getSelectedFile method returns a File object describing
            // the file the user just selected.  The hsa constructors will
            // accept a File object instead of a string file name.
            TextInputFile inFile = new TextInputFile(chooser.getSelectedFile());
            // Now read a catalog from the input file
            theCatalog = CatalogIO.readCatalog(inFile);
            JOptionPane.showMessageDialog(this, "file read successfully");
            //CHANGE:  now show the previously disabled option buttons
            displayButton.setEnabled(true);
            sortButton.setEnabled(true);
            saveButton.setEnabled(true);
          }
          else {
            JOptionPane.showMessageDialog(this, "no file chosen");
          } // end if
        }
      }
      else {
        // The showOpenDialog method makes the file chooser visible and returns
        // only when the user has made a choice or clicked the "cancel" button.
        // The parameter is the "parent" frame for the file chooser -- this
        // frame.  Swing will position the file chooser on top of its parent.
        int returnVal = chooser.showOpenDialog(this);
        // If the user successfully chooses and existing file, rather than
        // clicking "cancel" or making some kind of error, the return value
        // from showOpenDialog will be the constant JFileChooser.APPROVE_OPTION
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          // The getSelectedFile method returns a File object describing
          // the file the user just selected.  The hsa constructors will
          // accept a File object instead of a string file name.
          TextInputFile inFile = new TextInputFile(chooser.getSelectedFile());
          // Now read a catalog from the input file
          theCatalog = CatalogIO.readCatalog(inFile);
          JOptionPane.showMessageDialog(this, "file read successfully");
          //CHANGE:  now show the previously disabled option buttons
          displayButton.setEnabled(true);
          sortButton.setEnabled(true);
          saveButton.setEnabled(true);
        }
        else {
          JOptionPane.showMessageDialog(this, "no file chosen");
        } // end if
      }
    }
    else if (source == editButton) {
      //edit
      EditCatalog editCatalogDialog = new EditCatalog(this);
      editCatalogDialog.showDialog(theCatalog);
    }
    else if (source == displayButton) {
      if (theCatalog == null) {
        JOptionPane.showMessageDialog(this, "no catalog to display");
      }
      else {
        catDisplayDialog.showDialog(theCatalog);
      }
    }
    else if (source == sortButton) {
      if (theCatalog == null) {
        JOptionPane.showMessageDialog(this, "no catalog to sort");
      }
      else {
        sortChoiceDialog.showDialog(theCatalog);
      }
    }
    //CHANGE: save button click detected:
    else if (source == saveButton) {
      //save the catalog
      int returnVal2 = chooser.showSaveDialog(this);
      // If the user successfully chooses and existing file, rather than
      // clicking "cancel" or making some kind of error, the return value
      // from showsaveDialog will be the constant JFileChooser.APPROVE_OPTION
      if (returnVal2 == JFileChooser.APPROVE_OPTION) {
        TextOutputFile outFile = new TextOutputFile(chooser.getSelectedFile());
        // Now read a catalog from the input file
        CatalogIO.writeCatalog(theCatalog, outFile);
        JOptionPane.showMessageDialog(this, "catalog saved");
        theCatalog.setUnChanged();
      }
    }
    else if (source == exitButton) {
      if (theCatalog.hasChanged()) {
        int ans = JOptionPane.showConfirmDialog(this,
                                                "The catalog has changed, do you wish to continue?",
                                                "Catalog has changed", 0);
        if (ans != 0) { //if ans is not yes, then it is no
        }
        else {
          System.exit(0);
        }
      }
      else {
        System.exit(0);
      }
    }
    else {
      JOptionPane.showMessageDialog(this,
                                    "internal error: unknown button clicked");
    } // end if
  } // end actionPerformed

// Main method: creates the window
  public static void main(String args[]) {
    new MainGUIWindow();
  } // end main
} // end class MainGUIWindow
