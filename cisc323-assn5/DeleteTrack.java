import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DeleteTrack
    extends JDialog
    implements ActionListener {

  private Catalog theCatalog;
  private CD currentCD;
  private int selectedTrack;

  private JLabel qLabel = new JLabel("Are you certain?");
  private JButton okButton = new JButton("OK");
  private JButton cancelButton = new JButton("Cancel");

  // Parameter: the parent dialog
  public DeleteTrack(JDialog parent) {
    super(parent, true);
    setTitle("Delete Track");

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
   * called from editCD dialog, takes a Catalog, and the CD that the track is
   * in so that it can then delete that track
   ***************************************************************************/
  public void showDialog(Catalog cat, CD theCD, int indexNum) {
    theCatalog = cat;
    currentCD = theCD;
    selectedTrack = indexNum;
    // Let the user see this dialog
    setVisible(true);
  } // end showDialog

  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    if (source == okButton) {
      currentCD.deleteTrack(selectedTrack);
      JOptionPane.showMessageDialog(this, "track deleted");
      setVisible(false);
    }
    else if (source == cancelButton) {
      setVisible(false);
    }
  }
} // end DeleteTrack
