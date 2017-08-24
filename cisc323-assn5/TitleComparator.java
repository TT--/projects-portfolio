/************************************************************************
 * A Comparator for sorting MusicItem objects by title.
 * Queens University, Kingston, Ontario
 *
 * author: Margaret Lamb
 ***********************************************************************/
import java.util.*; // for Vector and Comparator
public class TitleComparator implements Comparator {

  /***********************************************************************
   * This is a comparison method that may be used by a sorting method.
   * It defines how pairs of objects are compared; in effect, it gives a
   * meaning for "greater than", "less than", or "equal to".  This class
   * will be used for sorting objects by title, so we compare titles
   * only, ignoring the other contents of the objects.
   *
   * Parameters:
   *   obj1, obj2: the objects to compare.  These must both be MusicItems
   *               (CDs or Tracks).  They are declared as
   *               Objects to agree with the Comparator interface.
   * Return value:
   *   negative if obj1 < obj2 (if obj1's title should sort before obj2's title)
   *   equal if obj1 == obj2 (if their titles are equal)
   *   positive if obj1 > obj2 (if obj1's title should sort after obj2's title)
   ***********************************************************************/
  public int compare(Object obj1, Object obj2) {
    MusicItem music1 = (MusicItem) obj1;
    MusicItem music2 = (MusicItem) obj2;
    return music1.getTitle().compareTo(music2.getTitle());
  } // end compare

} // end class TitleComparator
