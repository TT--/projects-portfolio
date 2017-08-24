/************************************************************************
 * Class to represent a collection of MusicItem objects (CDs and Tracks).
 * This class has two related purposes.  First, it is used to represent
 * the user's complete collection of CDs; in this case, it will contain
 * CD objects only.  Second, it is used to hold lists of CDs and/or Tracks
 * being sorted.
 *
 * Part of CD catalog program for CISC 323, winter 2005.
 * Queens University, Kingston, Ontario
 *
 * author: Margaret Lamb
 * edited: TT for assn5
 ***********************************************************************/
import java.util.*; // for Vector and Collections

public class Catalog {

  private boolean hasChanged = false;

  // A Catalog is simply a collection of MusicItems, stored in a Vector
  Vector elements = new Vector();

  // No constructor needed: catalog is initially empty

  // ACCESSOR METHODS
  public boolean hasChanged() {
    return hasChanged;
  } // end

  // returns the number of items in the catalog
  public int getNumItems() {
    return elements.size();
  } // end getNumItems

  // returns the i-th item from catalog, where i must be
  // in the range [0..size-1]
  // Parameter is i -- the index of the item
  public MusicItem getItem(int i) {
    return (MusicItem) elements.get(i);
  } // end getItem

  // MUTATOR METHODS
  public void setUnChanged() {
      hasChanged = false;
    } // end

  // adds a MusicItem to the catalog
  // Parameter is item to add
  public void addItem(MusicItem newItem) {
    elements.add(newItem);
    hasChanged = true;
  } // end addItem

  public void removeItem(int indexOfItem) {
    elements.removeElementAt(indexOfItem);
    hasChanged = true;
  } //

  // OTHER METHODS
  // Prints the catalog to the standard output, for debugging.
  // Includes all information from each CD and tracks within the
  // CDs.  With a large data structure such as a catalog, a method
  // like this can be more efficient and easy to write than toString.
  public void dump() {
    for (int i = 0; i < getNumItems(); i++) {
      System.out.println(getItem(i));
    }
  } // end dump

  // Constants used by createSortedCopy, to specify the kind of sorting:
  public static final int SORT_BY_TITLE = 1;
  public static final int SORT_BY_COMPOSER = 2;
  public static final int SORT_BY_PERFORMER = 3;

  /****************************************************************************
   * This method copies all or part of this catalog to a new catalog and
   * sorts it.  The parameters are flags telling the method what to copy
   * and how to sort.
   *
   * Perameters:
   *   copyCDs: If this is true, the CDs from this catalog are copied to the
   *     new catalog
   *   copyTracks: If this is true, the Tracks from this catalog are copied
   *     to the new catalog.  This include all of the tracks from each CD
   *     in the catalog.
   *   sortCriteria: Tells how to sort the new catalog.  Must be one of the
   *     constants SORT_BY_TITLE, SORT_BY_COMPOSER, or SORT_BY_PERFORMER.
   * Return value: the new catalog
   ***************************************************************************/
  public Catalog createSortedCopy(boolean copyCDs, boolean copyTracks,
                                  int sortCriteria) {
    // Copy each item from this catalog in turn.  The copyItem method
    // does all the appropriate checks before copying
    Catalog newCatalog = new Catalog();
    for (int i = 0; i < getNumItems(); i++) {
      copyItem(getItem(i), newCatalog, copyCDs, copyTracks, sortCriteria);
    } // end for

    // Now sort the catalog in the manner requested.
    // The Collections.sort method sorts a Vector, using a Comparator
    // to compare pairs of elements during the sorting process.
    if (sortCriteria == SORT_BY_TITLE) {
      Collections.sort(newCatalog.elements, new TitleComparator());
    }
    else if (sortCriteria == SORT_BY_COMPOSER) {
      Collections.sort(newCatalog.elements, new ComposerComparator());
    }
    else if (sortCriteria == SORT_BY_PERFORMER) {
      Collections.sort(newCatalog.elements, new PerformerComparator());
    }
    return newCatalog;
  } // end createSortedCopy

  /****************************************************************************
   * Helper method for createSortedCopy.  Copies a single item into a
   * catalog.  If the parameters specify that this item should not be copied,
   * does nothing.  If the item is a CD and we're supposed to copy tracks,
   * copies all of the tracks from this CD into the catalog.
   *
   * One more complication: When copying tracks from a CD, any tracks with
   * null composers or performers inherit the composer or performer of the CD.
   * For example, if a track is part of a CD performed by the Beatles and
   * that track doesn't specify a different performer, that track is
   * performed by the Beatles.
   *
   * Parameters:
   *   item: the item being copied
   *   cat: the catalog
   *   copyCDs: If this is true, CDs should be copied to the catalog
   *   copyTracks: If this is true, Tracks should be copied
   *     to the catalog.  If item is a CD, this means all of the tracks
   *     from the CD.
   *   sortCriteria: Tells how we're going to sort the new catalog.  If
   *     we're going to sort by performer, don't bother copying items
   *     with null performers.  If we're going to sort by composer, don't
   *     bother to copying items with null composers.
   ***************************************************************************/
  private void copyItem(MusicItem item, Catalog cat,
                        boolean copyCDs, boolean copyTracks, int sortCriteria) {
    if (item instanceof Track && copyTracks ||
        item instanceof CD && copyCDs) {
      // The item itself is of the right type to be copied.  Now check
      // the sorting criteria to make sure the item should be copied.
      boolean shouldCopy = true;
      if (sortCriteria == SORT_BY_COMPOSER && item.getComposer() == null) {
        shouldCopy = false;
      }
      if (sortCriteria == SORT_BY_PERFORMER && item.getPerformer() == null) {
        shouldCopy = false;
      }
      if (shouldCopy) {
        cat.addItem(item);
      }
    } // end if

    // if this item is a CD and we're supposed to add tracks, add all of
    // the CD's tracks
    if (item instanceof CD && copyTracks) {
      // If we try to call CD methods on item, we'll get errors since
      // item is declared as a MusicItem.  Create an alias which is
      // declared as a CD.
      CD thisCD = (CD) item;
      for (int trackNum = 0; trackNum < thisCD.getNumTracks(); trackNum++) {
        Track thisTrack = thisCD.getTrack(trackNum);
        // copy composer or performer from parent CD if appropriate.  Make
        // a copy of the track first so the original catalog isn't changed.
        if (sortCriteria == SORT_BY_PERFORMER && thisTrack.getPerformer() == null) {
          thisTrack = thisTrack.copy();
          thisTrack.setPerformer(thisCD.getPerformer());
        }
        else if (sortCriteria == SORT_BY_COMPOSER && thisTrack.getComposer() == null) {
          thisTrack = thisTrack.copy();
          thisTrack.setComposer(thisCD.getComposer());
        }
        copyItem(thisTrack, cat, copyCDs, copyTracks, sortCriteria);
      } // end for
    } // end if
  } // end copyItem

  // main method for testing
  public static void main(String[] args) {
    Catalog myCDcollection = new Catalog();

    CD sgtPepper = new CD("Sgt. Pepper's Lonely Hearts Club Band",
                          null, "Beatles");
    sgtPepper.addTrack(new Track("Lucy in the Sky With Diamonds",
                                 "Lennon, John", null));
    sgtPepper.addTrack(new Track("When I'm Sixty-Four",
                                 "McCartney, Paul", null));
    sgtPepper.addTrack(new Track("Within You Without You",
                                 "Harrison, George", null));
    myCDcollection.addItem(sgtPepper);

    CD hip = new CD("In Between Evolution", null, "Tragically Hip");
    hip.addTrack(new Track("Heaven is a Better Place Today"));
    hip.addTrack(new Track("Summer's Killing Us"));
    hip.addTrack(new Track("Gus: The Polar Bear From Central Park"));
    myCDcollection.addItem(hip);

    System.out.println("ORIGINAL:");
    myCDcollection.dump();
    System.out.println();

    // Vary the parameters to createSortedCopy to test different modes
    System.out.println("SORTED:");
    Catalog newCatalog = myCDcollection.createSortedCopy(false, true,
        SORT_BY_PERFORMER);
    newCatalog.dump();

  } // end main
} // end class Catalog
