/************************************************************************
 * An abstract class to represent a musical item -- a whole CD or a
 * single track.  Each item will have a title, plus an optional
 * composer and performer.
 *
 * This class is designated abstract, not because it has abstract
 * methods, but because it is not intended to be instantiated directly.
 * Instead, the CD catalog program uses the subclasses of this class:
 * Track and CD.  There are two reasons for this class:
 *     1. To combine the functionality associated with the title, composer
 *        and performer, so that it doesn't have to be duplicated in the
 *        Track and CD classes.
 *     2. To make it possible for a Catalog to contain both CDs and
 *        Tracks.
 *
 * Part of CD catalog program for CISC 323, winter 2005.
 * Queens University, Kingston, Ontario
 *
 * author: Margaret Lamb
 * modified: tt
 * Message no. 643
Date: Sunday, February 13, 2005 4:37pm
The assignment asks for us to change the CD class to include a tag.
However, isn't it much more useful to include a tag in the MusicItem
class instead of the CD class?
Message no. 645
Author: Margaret Lamb (lambm-test)
Date: Sunday, February 13, 2005 6:05pm
There are different ways to make this change. If you find it more useful to
 put Tag in MusicItem and let CD inherit it, that's fine.
      Margaret
 ***********************************************************************/
public abstract class MusicItem {

  // INSTANCE VARIABLES
  protected String title; // the title of this piece of music

  // To keep things from getting too complicated, a piece of music may have at
  //CHANGE: corrected spelling
  // most one composer and one performer.
  // Composers and performers may not be empty.  If there is no composer
  // or performer, this is represented by null instead.
  protected String composer;
  protected String performer;
  //CHANGE: new tag variable
  protected String tag; // the CDID tag

  // ACCESSOR ("GET") METHODS
  // returns the title
  public String getTitle() {
    return title;
  } // end getTitle

  //CHANGE: new method
  public String getTag() {
    return tag;
  } // end getTag

  // returns this composer of this track, or null if there is no composer
  public String getComposer() {
    return composer;
  } // end getComposer

  // returns the performer of this track, or null if there is no performer
  public String getPerformer() {
    return performer;
  } // end getPerformer

  // MUTATOR ("SET") METHODS
  // Sets the title.  The title may not be empty or null.
  // Parameter is the new title.
  public void setTitle(String newTitle) {
    if (newTitle == null) {
      System.out.println("Error: can't set title to null");
      return;
    } // end if
    String trimmedTitle = newTitle.trim(); // remove leading & trailing blanks
    if (trimmedTitle.length() == 0) {
      System.out.println("Error: can't set title to empty string");
      return;
    } // end if
    title = newTitle;
  } // end setTitle

  //CHANGE: new method
   public void setTag(String newTag) {
     String trimmedTag = newTag.trim(); // remove leading & trailing blanks
     tag = newTag;
   } // end setTitle

  // Sets the composer.  Parameter is the new composer.  If it's
  // blank, we set the composer to null instead.
  public void setComposer(String newComp) {
    if (newComp == null) {
      composer = null;
    }
    else {
      String trimmedComp = newComp.trim();
      if (trimmedComp.length() == 0) {
        composer = null;
      }
      else {
        composer = trimmedComp;
      }
    } // end if
  } // end setComposer

  // Sets the performer.  Parameter is the new performer.  If it's
  // blank, we set the performer to null instead.
  public void setPerformer(String newPerf) {
    if (newPerf == null) {
      performer = null;
    }
    else {
      String trimmedPerf = newPerf.trim();
      if (trimmedPerf.length() == 0) {
        performer = null;
      }
      else {
        performer = trimmedPerf;
      }
    } // end if
  } // end setPerformer

  // OTHER METHODS

  // Returns a string representation of this piece of music.  This will
  // contain only the common elements, not elements specific to CDs or tracks.
    public String displayString() {
    String result = title;
    if (composer != null) {
      result += " (composer: " + composer + ")";
    }
    if (performer != null) {
      result += " (performer: " + performer + ")";
    }
    return result;
  } // end displayString

  //CHANGE: to include tags
  public String displayStringWithTag() {
    String result = title;
    if (tag != null) {
      result += " [" + tag + "]";
    }
    if (composer != null) {
      result += " (composer: " + composer + ")";
    }
    if (performer != null) {
      result += " (performer: " + performer + ")";
    }
    return result;
  } // end displayString

} // end class MusicItem
