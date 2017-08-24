/************************************************************************
 * Class to represent a single track from a CD.
 * Part of CD catalog program for CISC 323, winter 2005.
 * Queens University, Kingston, Ontario
 *
 * author: Margaret Lamb
 ***********************************************************************/
public class Track extends MusicItem {
  // No new instance variables -- just variables inherited from Music
  // CONSTRUCTORS
  // First constructor: creates a new Track, given a title.  The composer
  // and performer are null.  Parameter is the title for the new Track.
  public Track(String t) {
    setTitle(t);
    // composer and performer default to null
  } // end constructor

//CHANGE: new constructors
  public Track(String t, String t2) {
    setTitle(t);
    setTag (t2);
    // composer and performer default to null
  } // end constructor

  public Track(String t, String c, String p) {
    setTitle(t);
    setComposer(c);
    setPerformer(p);
  } // end constructor

//CHANGE: include tags
  // Second constructor: specifies all three fields for a new Track.
  // Parameters are title, composer and performer
  public Track(String t, String t2, String c, String p) {
    setTitle(t);
    setTag (t2);
    setComposer(c);
    setPerformer(p);
  } // end constructor

  // COPY METHOD
  // create an exact copy of the track
  public Track copy() {
    return new Track(title, tag, composer, performer);
  } // end copy

  // Returns a string representation of this track.
  public String toString() {
    // Since tracks have no instance variables besides those
    // defined in MusicItem, the displayString method from
    // MusicItem does exactly what we want.
    return displayString();
  } // end toString

  // Main method for testing
  public static void main(String[] args) {
    Track track1 = new Track("Sound of Silence");
    System.out.println(track1);
    track1.setPerformer("Simon & Garfunkel");
    System.out.println(track1);

    Track track2 = new Track("Peer Gynt", "Grieg", "London Symphony");
    System.out.println(track2);
    track2.setPerformer(" ");
    System.out.println(track2);

    System.out.println("title is " + track2.getTitle());
    System.out.println("composer is " + track2.getComposer());
    System.out.println("performer is " + track2.getPerformer());
  } // end main
} // end class Track
