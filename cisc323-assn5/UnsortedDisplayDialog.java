/************************************************************************
 * This class defines a very simple dialog which will display an unsorted
 * catalog.
 *
 * Part of CD catalog program for CISC 323, winter 2005.
 * Queens University, Kingston, Ontario
 *
 * author: Margaret Lamb
 ***********************************************************************/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UnsortedDisplayDialog extends AbstractDisplayDialog implements ActionListener {

  // Constructor: no changes from AbstractDisplayDialog.  We expect this
  // kind of dialog to be created by the main window, so we assume the
  // parent will be a frame.
  // Parameter: parent frame
  public UnsortedDisplayDialog(JFrame parent) {
    super(parent);
  } // end constructor

  /****************************************************************************
   * This method puts text in the textArea to display an unsorted catalog.
   * This kind of catalog usually will only CDs -- no tracks.  For each
   * CD in the catalog, we'll display each track in the CD, indented under
   * the general information about the CD.
   *
   * Parameters:
   *   theCatalog: the catalog to display
   *   textArea: the text area for the display
   ***************************************************************************/
  public void catalogToTextArea(Catalog theCatalog, JTextArea textArea) {
    textArea.setText("CONTENTS OF CATALOG:\n\n");
    for (int CDNum = 0; CDNum < theCatalog.getNumItems(); CDNum++) {
      CD cd = (CD) theCatalog.getItem(CDNum);
      // display the title, composer and performer for the CD
      textArea.append(cd.displayStringWithTag() + "\n");
      // display all the tracks, indented using a tab character
      for (int trackNum = 0; trackNum < cd.getNumTracks(); trackNum++) {
        textArea.append("\t" + cd.getTrack(trackNum) + "\n");
      } // end for trackNum
    } // end for CDNum
  } // end catalogToTextArea

} // end UnsortedDisplayDialog
