/************************************************************************
 * A Comparator for sorting MusicItem objects by composer.
 * Queens University, Kingston, Ontario
 *
 * author: Margaret Lamb
 ***********************************************************************/
import java.util.*; // for Vector and Comparator
public class ComposerComparator implements Comparator {

  /***********************************************************************
   * This is a comparison method that may be used by a sorting method.
   * It defines how pairs of objects are compared; in effect, it gives a
   * meaning for "greater than", "less than", or "equal to".  This class
   * will be used for sorting objects by composer.  If the composers are
   * exactly equal, we sort by title.
   *
   * Parameters:
   *   obj1, obj2: the objects to compare.  These must both be MusicItems
   *               (CDs or Tracks).  They are declared as
   *               Objects to agree with the Comparator interface.
   * Return value:
   *   negative if obj1 < obj2 (if obj1 should sort before obj2)
   *   equal if obj1 == obj2 (if their composers are equal)
   *   positive if obj1 > obj2 (if obj1 should sort after obj2)
   ***********************************************************************/
  public int compare(Object obj1, Object obj2) {
    MusicItem music1 = (MusicItem) obj1;
    MusicItem music2 = (MusicItem) obj2;
    // We must be cautious because some MusicItems may have a null
    // composer.  We consider two nulls equal, and a null is less
    // than any non-null string.
    String composer1 = music1.getComposer();
    String composer2 = music2.getComposer();


    if (composer1 == null) {
      if (composer2 == null)
        // null == null, so compare the titles
        return music1.getTitle().compareTo(music2.getTitle());
      else
        return -1; // null < non-null
    }
    else { // composer1 is not null
      if (composer2 == null)
        return 1; // non-null > null
      else {
        // two non-null strings, so a string comparison is safe
        int result = composer1.compareTo(composer2);
        if (result == 0) // composers are equal
          return music1.getTitle().compareTo(music2.getTitle());
        else
          return result;
      } // end if
    } // end if
  } // end compare

} // end class ComposerComparator
