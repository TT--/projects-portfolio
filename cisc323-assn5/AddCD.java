import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddCD
    extends JDialog
    implements ActionListener {

  private Catalog theCatalog;

  private JTextField nameField = new JTextField("", 10);
  private JTextField composerField = new JTextField("", 10);
  private JTextField performerField = new JTextField("", 10);
  private JTextField tagField = new JTextField("", 10);
  private JLabel nameLabel = new JLabel("Name:");
  private JLabel composerLabel = new JLabel("Composer:");
  private JLabel performerLabel = new JLabel("Performer:");
  private JLabel tagLabel = new JLabel("Tag:");
  private JButton okButton = new JButton("OK");
  private JButton cancelButton = new JButton("Cancel");
  private String newName;
  private String newTag;
  private String newComp;
  private String newPerf;

  // Parameter: the parent dialog
  public AddCD(JDialog parent) {
    super(parent, true);
    setTitle("Add CD");

    Container contents = getContentPane();
    contents.setLayout(new GridLayout(0, 1));

    //register the dialog as its listener
    okButton.addActionListener(this);
    cancelButton.addActionListener(this);
    JPanel panelTop = new JPanel();
    JPanel panelBot = new JPanel();
    panelTop.add(nameLabel);
    panelTop.add(nameField);
    panelTop.add(composerLabel);
    panelTop.add(composerField);
    panelTop.add(performerLabel);
    panelTop.add(performerField);
    panelTop.add(tagLabel);
    panelTop.add(tagField);

    panelBot.add(okButton);
    panelBot.add(cancelButton);

    contents.add(panelTop);
    contents.add(panelBot);
    // don't let the user close the dialog with the Windows close button
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    pack();
    setVisible(false);
  } // end constructor

//called from the editCatalog Dialog, takes a Catalog in
  public void showDialog(Catalog cat) {
    theCatalog = cat;
    nameField.setText("");
    composerField.setText("");
    performerField.setText("");
    tagField.setText("");

    // Let the user see this dialog
    setVisible(true);
  } // end showDialog

  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    if (source == okButton) {
      newName = nameField.getText();
      newTag = tagField.getText();
      newComp = composerField.getText();
      newPerf = performerField.getText();

      //cds must have a tag and title according to customer
      if (newName.equals("") || newTag.equals("")) {
        JOptionPane.showMessageDialog(this, "CDs must have a Title and Tag");
      }
      else {
        CD newCD = new CD(newName, newTag, newComp, newPerf);
        theCatalog.addItem(newCD);
        JOptionPane.showMessageDialog(this, "catalog updated");
        setVisible(false);
      }
    }

    else if (source == cancelButton) {
      setVisible(false);
    }
  }
} // end AddCD
