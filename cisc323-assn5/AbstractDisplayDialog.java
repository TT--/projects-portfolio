/************************************************************************
 * This class defines the general structure of a dialog for displaying
 * a catalog to the user.  The program needs to display catalogs in two
 * different ways (sorted and unsorted), but these two kinds of dialogs
 * have a lot in common.  This class defines the common elements, so that
 * we don't ahve to write them twice.
 *
 * A display dialog has only two components: A large text area for the
 * actual display (with scroll bars) and a "close" button.  The display
 * is simple, unformatted text.
 *
 * When another frame wants to pop up one of these dialogs, it must:
 *    1. create the dialog, using the constructor (with the calling
 *       frame as a parameter)
 *    2. call the showDialog method with the catalog to be displayed
 *       as a parameter
 *  Other frames should NOT call the setVisible method directly,
 *  as this dialog will have no way of knowing what catalog to display.
 *
 * To create a concrete subclass of this class, all a programmer must
 * do is supply a "catalogToTextArea" method, which specifies how to
 * display the catalog in the text area.
 *
 * Part of CD catalog program for CISC 323, winter 2005.
 * Queens University, Kingston, Ontario
 *
 * author: Margaret Lamb
 ***********************************************************************/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public abstract class AbstractDisplayDialog extends JDialog implements ActionListener {

  // The text area for displaying the catalog
  private JTextArea displayArea;
  // The button for closing this dialog
  private JButton closeButton;

  // Explanation: A display dialog may be started up by either a frame
  // (JFrame) or another dialog (JDialog).  So we need to provide two forms
  // of constructor.  Each of them sets the parent of this dialog, then
  // immediately calls the common method dialogSetup to do the real work.

  // Constructor 1
  // Parameter: the parent frame
  public AbstractDisplayDialog(JFrame parent) {
    super(parent, true);
    dialogSetup();
  } // end constructor 1


  // Constructor 2
  // Parameter: the parent dialog
  public AbstractDisplayDialog(JDialog parent) {
    super(parent, true);
    dialogSetup();
  } // end constructor 2

  /****************************************************************************
   * Helper method to do the initial setup for this dialog.  Called by both
   * constructors.  It lays out the dialog's components and registers action
   * listeners.  It does not put any text into the display area; that will
   * happen when the parent calls the showDialog method.  The dialog will not
   * be visible until then.
   ***************************************************************************/
  private void dialogSetup()  {
    setTitle("catalog contents");

    // Use a border layout: the display in the "center" position and the
    // close button on the bottom ("south").  BorderLayout is the default
    // for dialogs, so we don't have to call setLayout.
    Container contents = getContentPane();

    // The display area has no contents yet; the showDialog method will
    // add text to it.  The text may be too big to fit in the display area,
    // so we enclose the area in a "scroll pane" to add horizontal and
    // vertical scrol bars.
    displayArea = new JTextArea("");
    contents.add(new JScrollPane(displayArea), BorderLayout.CENTER);

    // add the close button and register the dialog as its listener
    closeButton = new JButton("close this window");
    closeButton.addActionListener(this);
    //CHANGE: make the close button inside a panel,
    //i think this will keep it small...
    JPanel closePanel = new JPanel(new BorderLayout());
    closePanel.add(closeButton, BorderLayout.WEST);
    contents.add(closePanel, BorderLayout.SOUTH);


    // don't let the user close the dialog with the Windows close button
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

    // use a fixed initial size.  The "pack" method won't work here because
    // the default size for a scroll pane is tiny.
    setSize(600,400);

    setVisible(false); // not visible until parent calls showDialog
  } // end constructor

  /****************************************************************************
   * This method contains the code that is specific to the particular kind
   * of display.  It must put text into the text area which shows the
   * contents of the catalog in the desired manner.
   *
   * Parameters:
   *   theCatalog: the catalog to display
   *   textArea: the text area for the display
   ***************************************************************************/
  public abstract void catalogToTextArea(Catalog theCatalog, JTextArea textArea);


  /****************************************************************************
   * This method puts text inside the display area
   * and then makes the dialog visible.
   * Parameter: the catalog to display inside the display area
   ***************************************************************************/
  public void showDialog(Catalog theCatalog) {
    // clear the display area, then call the method which will be supplied
    // by the concrete dialog class to put text in the display area
    displayArea.setText("");
    catalogToTextArea(theCatalog, displayArea);

    // ensure that the display area is initially scrolled to the top
    displayArea.setCaretPosition(0);

    // Let the user see this dialog
    setVisible(true);
  } // end showDialog

  /****************************************************************************
   * This method is called when the user clicks the "close" button.  It makes
   * this dialog go away.
   *
   * Parameter: object describing the button-click event; not used.
   ***************************************************************************/
  public void actionPerformed(ActionEvent e) {
    // Make this dialog invisible.  It still exists, but the user won't
    // be able to see it and will be able to interact with the parent
    // frame again.
    setVisible(false);
  } // end actionPerformed


} // end AbstractDisplayDialog
