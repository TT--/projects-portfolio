/************************************************************************
 * A Comparator for sorting MusicItem objects by performer.
 * Queens University, Kingston, Ontario
 *
 * author: Margaret Lamb
 ***********************************************************************/
import java.util.*; // for Vector and Comparator
public class PerformerComparator implements Comparator {

  /***********************************************************************
   * This is a comparison method that may be used by a sorting method.
   * It defines how pairs of objects are compared; in effect, it gives a
   * meaning for "greater than", "less than", or "equal to".  This class
   * will be used for sorting objects by performer.  If the performers are
   * exactly equal, we sort by title.
   *
   * Parameters:
   *   obj1, obj2: the objects to compare.  These must both be MusicItems
   *               (CDs or Tracks).  They are declared as
   *               Objects to agree with the Comparator interface.
   * Return value:
   *   negative if obj1 < obj2 (if obj1 should sort before obj2)
   *   equal if obj1 == obj2 (if their performers are equal)
   *   positive if obj1 > obj2 (if obj1 should sort after obj2)
   ***********************************************************************/
  public int compare(Object obj1, Object obj2) {
    MusicItem music1 = (MusicItem) obj1;
    MusicItem music2 = (MusicItem) obj2;
    // We must be cautious because some MusicItems may have a null
    // performer.  We consider two nulls equal, and a null is less
    // than any non-null string.
    String performer1 = music1.getPerformer();
    String performer2 = music2.getPerformer();


    if (performer1 == null) {
      if (performer2 == null)
        // null == null, so compare the titles
        return music1.getTitle().compareTo(music2.getTitle());
      else
        return -1; // null < non-null
    }
    else { // performer1 is not null
      if (performer2 == null)
        return 1; // non-null > null
      else {
        // two non-null strings, so a string comparison is safe
        int result = performer1.compareTo(performer2);
        if (result == 0) // performers are equal
          return music1.getTitle().compareTo(music2.getTitle());
        else
          return result;
      } // end if
    } // end if
  } // end compare

} // end class PerformerComparator
