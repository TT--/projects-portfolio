/************************************************************************
 * This class defines a dialog to allow the user to pick sorting
 * options.  It contains two combo boxes of sorting options and an "OK"
 * button.  When the user clicks the "OK" button, this dialog pops
 * up a "SortedDisplayDialog" to display the catalog.  When that
 * dialog is finished, this dialog disappears as well.
 *
 * When another frame wants to pop up one of these dialogs, it must:
 *    1. create the dialog, using the constructor (with the calling
 *       frame as a parameter)
 *    2. call the showDialog method with the catalog to be displayed
 *       as a parameter
 *  Other frames should NOT call the setVisible method,
 *  as this dialog will have no way of knowing what catalog to display.
 *
 * Part of CD catalog program for CISC 323, winter 2005.
 * Queens University, Kingston, Ontario
 *
 * author: Margaret Lamb
 ***********************************************************************/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SortDialog extends JDialog implements ActionListener {

  // The catalog this dialog is responsible for sorting
  private Catalog theCatalog;

  // A JComboBox is the familiar kind of drop-down list from which
  // the user can pick one option.

  // A combo box for choosing the kind of sorting to do.
  // The string array gives the labels for the three options.
  private JComboBox sortComboBox;
  private static final String[] sortingChoices =
      {"title","composer","performer"};
  private static final int[] sortingCodes = {
      Catalog.SORT_BY_TITLE,
      Catalog.SORT_BY_COMPOSER,
      Catalog.SORT_BY_PERFORMER};

  // A combo box for what to include in the sort (CDs or Tracks or both).
  private JComboBox includeComboBox;
  private static final String[] includeChoices =
      {"CDs only", "Tracks only", "Tracks and CDs"};

  // OK button: means to go ahead and sort
  private JButton OKButton;
  //CHANGE: need another button
  private JButton CancelButton;

  // Dialog for displaying the sorted catalog
  private SortedDisplayDialog displayDialog = new SortedDisplayDialog(this);

  /****************************************************************************
   * Constructor: Creates the dialog and lays out its components.  The dialog
   * will not be visible until its parent calls the showDialog method.
   *
   * Parameter: the parent frame.  This dialog will be positioned on top of
   * the parent frame and the parent frame will be disabled until this
   * dialog makes itself invisible (when the user clicks the "close" button).
   ***************************************************************************/
  public SortDialog(JFrame parent)  {
    super(parent, true); // initialize this as a modal dialog
    setTitle("Sorting Options");

    // Organize the layout in rows: a one-column grid layout
    Container contents = getContentPane();
    contents.setLayout(new GridLayout(0,1));

    // First row: a label plus the combo box for picking the sorting method
    JPanel row1 = new JPanel();
    row1.add(new JLabel("  sort by:  "));
    sortComboBox = new JComboBox(sortingChoices);
    row1.add(sortComboBox);
    contents.add(row1);

    // second row: label and combo box to specify what to include
    JPanel row2 = new JPanel();
    row2.add(new JLabel("  include:  "));
    includeComboBox = new JComboBox(includeChoices);
    row2.add(includeComboBox);
    contents.add(row2);

    // Finally, the OK button as the last row.  We could add the button
    // directly to the contents, but we use a panel to keep it from
    // stretching.
    JPanel row3 = new JPanel();
    OKButton = new JButton("OK");
    OKButton.addActionListener(this);
    //CHANGE: put this button beside the "OK" button
    CancelButton = new JButton("Cancel");
    CancelButton.addActionListener(this);
    row3.add(OKButton);
    row3.add(CancelButton);
    contents.add(row3);

    // don't let the user close the dialog with the Windows close button
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

    pack();
    setVisible(false); // not visible until parent calls showDialog
  } // end constructor


  /****************************************************************************
   * This method makes the dialog visible.  It also sets a link to the
   * catalog that will be displayed, for later use.
   * Parameter: the catalog that will be sorted and displayed
   ***************************************************************************/
  public void showDialog(Catalog cat) {
    theCatalog = cat;
    setVisible(true); // method from JDialog class
  } // end showDialog

  /****************************************************************************
   * This method is called when the user clicks the "OK" button.  It creates
   * a sorted version of the catalog and pops up another dialog to display it.
   *
   * Parameter: object describing the button-click event; not used.
   ***************************************************************************/
  public void actionPerformed(ActionEvent e) {
  //CHANGE: need to get the source now that there is two buttons
    Object source = e.getSource();
  //CHANGE: if cancels, hide this dialog, go back to mainGUI
    if (source == CancelButton) {
    setVisible(false);
    }
    else{

      // get the index of the sorting chosen
      int indexChosen = sortComboBox.getSelectedIndex();
      // determine the corresponding constant from the Catalog class
      int sortingConstant;
      if (indexChosen == 0)
        sortingConstant = Catalog.SORT_BY_TITLE;
      else if (indexChosen == 1)
        sortingConstant = Catalog.SORT_BY_COMPOSER;
      else // must be 2
        sortingConstant = Catalog.SORT_BY_PERFORMER;

        // get the index of the include option chosen
      indexChosen = includeComboBox.getSelectedIndex();
      // determine the boolean flags for the createSortedCopy method
      boolean includeCDs = indexChosen != 1; // 1 is tracks only
      boolean includeTracks = indexChosen != 0; // 0 is CDs only

      // create the sorted copy
      Catalog sortedCatalog = theCatalog.createSortedCopy(
          includeCDs,
          includeTracks,
          sortingConstant);

      displayDialog.setSortingCriteria(sortingConstant);
      displayDialog.showDialog(sortedCatalog);

      // Once the user is finished looking at the sorted display, make this
      // dialog go away.
      setVisible(false);
    }
  } // end actionPerformed


} // end SortDialog
