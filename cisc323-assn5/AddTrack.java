import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddTrack
    extends JDialog
    implements ActionListener {

  private CD currentCD;

  private JTextField nameField = new JTextField("", 10);
  private JTextField composerField = new JTextField("", 10);
  private JTextField performerField = new JTextField("", 10);
  private JTextField tagField = new JTextField("", 10);
  private JLabel nameLabel = new JLabel("Name:");
  private JLabel composerLabel = new JLabel("Composer:");
  private JLabel performerLabel = new JLabel("Performer:");
  private JButton okButton = new JButton("OK");
  private JButton cancelButton = new JButton("Cancel");

  // Parameter: the parent dialog
  public AddTrack(JDialog parent) {
    super(parent, true);
    setTitle("Add Track");

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
   * caled from EditCD dialog, takes a CD object in to edit its tracks
   ***************************************************************************/
  public void showDialog(CD theCD) {
    currentCD = theCD;
    nameField.setText("");
    composerField.setText("");
    performerField.setText("");

    // Let the user see this dialog
    setVisible(true);
  } // end showDialog

  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();

    if (source == okButton) {
      Track newTrack = new Track(nameField.getText(), composerField.getText(), performerField.getText());
      currentCD.addTrack(newTrack);
      JOptionPane.showMessageDialog(this, "track added");
      setVisible(false);
    }
    else if (source == cancelButton) {
      setVisible(false);
    }
  }
} // end AddTrack
