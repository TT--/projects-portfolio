import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EditCatalog
    extends JDialog
    implements ActionListener {

  private Catalog theCatalog;

  private int numOfCDs;

  // The text area for displaying the catalog
  private JTextArea displayArea;
  private JComboBox comboBox;
  private JLabel sel = new JLabel("selected:");

  // The button for closing this dialog
  private JButton addCDButton;
  private JButton delCDButton;
  private JButton editCDButton;
  private JButton doneButton;

  // Parameter: the parent frame
  public EditCatalog(JFrame parent) {
    super(parent, true);
    setTitle("Edit Catalog");
    // don't let the user close the dialog with the Windows close button
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

    // use a fixed initial size - "pack" won't work due to scroll pane
    setSize(700, 300);

    setVisible(false); // not visible until parent calls showDialog
  } // end constructor

  //called from the mainGUI
  public void showDialog(Catalog cat) {
    theCatalog = cat;
    numOfCDs = theCatalog.getNumItems();
    String choices[] = new String[numOfCDs];

    // BorderLayout is the default for dialogs, so we don't have to call setLayout.
    Container contents = getContentPane();
    displayArea = new JTextArea("");
    displayArea.setEditable(false);

    //make a little title thing
    displayArea.setText("CDs \n --- \n");
    contents.add(new JScrollPane(displayArea), BorderLayout.CENTER);

    for (int CDNum = 0; CDNum < numOfCDs; CDNum++) {
      CD cd = (CD) theCatalog.getItem(CDNum);
      // display the title, composer and performer for the CD
      String nameCDshort = CDNum + "- " + cd.getTitle();
      String nameCD = CDNum + "- " + cd.displayString();
      displayArea.append("  " + nameCD + "\n");
      choices[CDNum] = nameCDshort;
    } // end for CDNum

    comboBox = new JComboBox(choices);
    //register the dialog as its listener
    addCDButton = new JButton("Add CD");
    addCDButton.addActionListener(this);
    delCDButton = new JButton("Delete Selected CD");
    delCDButton.addActionListener(this);
    editCDButton = new JButton("Edit Selected CD");
    editCDButton.addActionListener(this);
    doneButton = new JButton("Done");
    doneButton.addActionListener(this);

    JPanel eastPanel = new JPanel(new GridLayout(0, 1));
    JPanel but1Panel = new JPanel(); // default: centered flow layout
    but1Panel.add(addCDButton);
    JPanel but2Panel = new JPanel(); // default: centered flow layout
    but2Panel.add(delCDButton);
    JPanel but3Panel = new JPanel(); // default: centered flow layout
    but3Panel.add(editCDButton);
    JPanel but4Panel = new JPanel(); // default: centered flow layout
    but4Panel.add(doneButton);
    JPanel boxPanel = new JPanel(); // default: centered flow layout
    boxPanel.add(sel);
    boxPanel.add(comboBox);

    eastPanel.add(but1Panel);
    eastPanel.add(boxPanel);
    eastPanel.add(but2Panel);
    eastPanel.add(but3Panel);
    eastPanel.add(but4Panel);
    contents.add(eastPanel, BorderLayout.EAST);

    // ensure that the display area is initially scrolled to the top
    displayArea.setCaretPosition(0);

    // Let the user see this dialog
    setVisible(true);
  } // end showDialog

  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();

    if (source == addCDButton) {
      AddCD addCDDialog = new AddCD(this);
      addCDDialog.showDialog(theCatalog);
      if (numOfCDs != theCatalog.getNumItems()) {
        dispose(); //catalog has changed, so need to refresh everything
      }
    }
    else if (source == editCDButton) {
      int num = comboBox.getSelectedIndex();
      if (num < 0 || num > (numOfCDs - 1)) {
        JOptionPane.showMessageDialog(this, "selected CD num not present");
      }
      else {
        EditCD editCDDialog = new EditCD(this);
        editCDDialog.showDialog(theCatalog, num);
      }
    }
    else if (source == delCDButton) {
      int num = comboBox.getSelectedIndex();
      if (num < 0 || num > (numOfCDs - 1)) {
        JOptionPane.showMessageDialog(this, "selected CD num not present");
      }
      else {
        DeleteCD delCDDialog = new DeleteCD(this);
        delCDDialog.showDialog(theCatalog, num);
      }
      if (numOfCDs != theCatalog.getNumItems()) {
        dispose(); //catalog has changed, so need to refresh everything
      }
    }
    else if (source == doneButton) {
      setVisible(false);
    }
  }
} // end EditCatalog
