/************************************************************************
 * This class defines a very simple dialog which will display a sorted
 * catalog.  Before calling showDialog, the parent should call the
 * setSortingCriteria method.
 *
 * Part of CD catalog program for CISC 323, winter 2005.
 * Queens University, Kingston, Ontario
 *
 * author: Margaret Lamb
 ***********************************************************************/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SortedDisplayDialog
    extends AbstractDisplayDialog
    implements ActionListener {

  // The criteria that was used to sort the current catalog.  If the
  // catalog was sorted by title, we do a straight list in title order.  If
  // the catalog was sorted by performer or composer, we print the performer
  // or composer names as section headers.
  // The value of this variable should be one of the three constants
  // defined in the Catalog class: SORT_BY_TITLE, SORT_BY_COMPOSER, or
  // SORT_BY_PERFORMER.
  private int sortingCriteria = Catalog.SORT_BY_TITLE;

  // Sets the sorting criteria for the catalog, to determine how it will
  // be displayed.
  // Parameter: a constant representing how the catalog was sorted.
  public void setSortingCriteria(int criteria) {
    sortingCriteria = criteria;
  } // end setSortingCriteria

  // Constructor: We expect this kind of dialog to be created by a SortDialog,
  // so we assume the parent will be a dialog.
  // Parameter:  the parent dialog
  public SortedDisplayDialog(JDialog parent) {
    super(parent);
  } // end constructor

  /****************************************************************************
   * This method puts text in the textArea to display an unsorted catalog.
   * This kind of catalog may contain CDs and/or Tracks.  If the catalog
   * contains a CD, we don't automatically list its Tracks; the display
   * includes only items that are included explicitly in the catalog
   *
   * Parameters:
   *   theCatalog: the catalog to display
   *   textArea: the text area for the display
   ***************************************************************************/
  public void catalogToTextArea(Catalog theCatalog, JTextArea textArea) {
    if (sortingCriteria == Catalog.SORT_BY_TITLE) {
      textArea.setText("CONTENTS OF CATALOG BY TITLE:\n\n");
      for (int itemNum = 0; itemNum < theCatalog.getNumItems(); itemNum++) {
        MusicItem thisItem = theCatalog.getItem(itemNum);
        String thisTag = thisItem.getTag();
        textArea.append(theCatalog.getItem(itemNum).displayStringWithTag() + "\n");
      } // end for
    }
    else if (sortingCriteria == Catalog.SORT_BY_COMPOSER) {
      textArea.setText("CONTENTS OF CATALOG BY COMPOSER:\n\n");
      String lastComposer = "";
      for (int itemNum = 0; itemNum < theCatalog.getNumItems(); itemNum++) {
        MusicItem thisItem = theCatalog.getItem(itemNum);
        String thisComposer = thisItem.getComposer();
        if (!lastComposer.equals(thisComposer)) {
          // new composer, so create new header
          textArea.append(thisComposer + "\n");
          lastComposer = thisComposer;
        } // end if
        // print the title, indented under the composer header
        textArea.append("\t" + thisItem.getTitle());
        //CHANGE: include tags
        textArea.append(" [" + thisItem.getTag() + "]\n");
      } // end for
    }
    else { // must be SORT_BY_PERFORMER
      textArea.setText("CONTENTS OF CATALOG BY PERFORMER:\n\n");
      String lastPerformer = "";
      for (int itemNum = 0; itemNum < theCatalog.getNumItems(); itemNum++) {
        MusicItem thisItem = theCatalog.getItem(itemNum);
        String thisPerformer = thisItem.getPerformer();
        if (!lastPerformer.equals(thisPerformer)) {
          // new composer, so create new header
          textArea.append(thisPerformer + "\n");
          lastPerformer = thisPerformer;
        } // end if
        // print the title, indented under the composer header
        textArea.append("\t" + thisItem.getTitle());
        //CHANGE: include tags
        textArea.append(" [" + thisItem.getTag() + "]\n");
      } // end for
    } // end if
  } // end catalogToTextArea

} // end SortedDisplayDialog
