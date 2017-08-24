/************************************************************************
 * Class to represent an entire CD.
 * Part of CD catalog program for CISC 323, winter 2005.
 * Queens University, Kingston, Ontario
 *
 * author: Margaret Lamb
 * modified: tt
 ***********************************************************************/
import java.util.Vector;

public class CD
    extends MusicItem {
  // Inherits title, composer and performer from Music (item?)
  // A CD also contains a collection of Tracks.
  // CHANGE: and a tag
  // Use a Vector to represent this.
  private Vector tracks = new Vector(); // initially empty

  // CONSTRUCTORS
  // First constructor: creates a new CD, given a title.  The composer
  // and performer are null.  Parameter is the title for the new CD.
  public CD(String t) {
    setTitle(t);
    // composer and performer default to null
    // no tracks yet
  } // end constructor

  //CHANGE: new constructor
  public CD(String t, String t2) {
    setTitle(t);
    setTag(t2);
    // composer and performer default to null
    // no tracks yet
  } // end constructor

//CHANGE: to include tags
  // Second constructor: specifies all three fields for a new CD.
  // Parameters are title, composer and performer
  public CD(String t, String c, String p) {
    //tag will be null
    setTitle(t);
    setComposer(c);
    setPerformer(p);
    // no tracks yet
  } // end constructor

  //CHANGE: new method
  // Second constructor: specifies all three fields for a new CD.
  // Parameters are title, composer and performer
  public CD(String t, String t2, String c, String p) {
    setTitle(t);
    setTag(t2);
    setComposer(c);
    setPerformer(p);
    // no tracks yet
  } // end constructor

  // ACCESSOR METHODS FOR THE TRACKS
  // returns the number of tracks this CD has
  public int getNumTracks() {
    return tracks.size();
  } // end getNumTracks

  // returns the i-th track from this CD, where i must be
  // the range [0..size-1]
  // Parameter is i -- the index of the track
  public Track getTrack(int i) {
    return (Track) tracks.get(i);
  } // end getTrack

  // MUTATOR METHODS FOR THE TRACKS

  // adds a track to the CD
  // Parameter is the new track
  public void addTrack(Track newTrack) {
    tracks.add(newTrack);
  } // end addTrack

  // deletes a track from the CD
  // Parameter: index of the track to be deleted
  public void deleteTrack(int index) {
    tracks.remove(index);
  } // end deleteTrack

  // OTHER METHODS
  // returns a String representation of the CD.  It will contain multiple
  // lines: one for the title, performer and composer of the CD, then
  // an additional indented line for each track
    public String toString() {
    // first, the title, composer and performer
    String result = displayString();

    // add a line for each track
    for (int i = 0; i < getNumTracks(); i++) {
      Track thisTrack = getTrack(i);
      result += "\n    " + i + ": " + thisTrack;
    }
    return result;
  } // end toString

  // Main method for testing
  public static void main(String[] args) {
    CD sgtPepper = new CD("Sgt. Pepper's Lonely Hearts Club Band",
                          null, "The Beatles");
    sgtPepper.addTrack(new Track("Sgt. Pepper's Lonely Hearts Club Band",
                                 "McCartney, Paul", null));
    sgtPepper.addTrack(new Track("Lucy in the Sky With Diamonds",
                                 "Lennon, John", null));
    sgtPepper.addTrack(new Track("When I'm Sixty-Four",
                                 "McCartney, Paul", null));
    sgtPepper.addTrack(new Track("Within You Without You",
                                 "Harrison, George", null));
    System.out.println(sgtPepper);
    System.out.println();

    sgtPepper.deleteTrack(1);
    System.out.println(sgtPepper);
  } // end main
} // end class CD
