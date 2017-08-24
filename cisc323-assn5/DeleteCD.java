import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DeleteCD
    extends JDialog
    implements ActionListener {

  private Catalog theCatalog;
  private int selectedCD;

  private JLabel qLabel = new JLabel("Are you certain?");
  private JButton okButton = new JButton("OK");
  private JButton cancelButton = new JButton("Cancel");

  // Parameter: the parent dialog
  public DeleteCD(JDialog parent) {
    super(parent, true);
    setTitle("Delete CD");

    Container contents = getContentPane();
    contents.setLayout(new GridLayout(0, 1));
    //register the dialog as its listener
    okButton.addActionListener(this);
    cancelButton.addActionListener(this);
    JPanel panelTop = new JPanel();
    JPanel panelBot = new JPanel();
    panelTop.add(qLabel);

    panelBot.add(okButton);
    panelBot.add(cancelButton);

    contents.add(panelTop);
    contents.add(panelBot);
    // don't let the user close the dialog with the Windows close button
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    pack();
    setVisible(false);
  } // end constructor

  /****************************************************************************
   * called from editCatalog, takes the catalog in, and the index of CD so that
   * it can delete that CD
   ***************************************************************************/
  public void showDialog(Catalog cat, int indexNum) {
    theCatalog = cat;
    selectedCD = indexNum;
    // Let the user see this dialog
    setVisible(true);
  } // end showDialog

  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();

    if (source == okButton) {
      theCatalog.removeItem(selectedCD);
      JOptionPane.showMessageDialog(this, "catalog updated");
      setVisible(false);
    }
    else if (source == cancelButton) {
      setVisible(false);
    }
  }
} // end DeleteCD
