/************************************************************************
 * This class contains static methods for reading and writing catalogs
 * from and to text files.  These methods should be used only for
 * normal user catalogs containing CDs and not Tracks; they should
 * not be used for the temporary catalogs created for sorted listings.
 *
 * Part of CD catalog program for CISC 323, winter 2005.
 * Queens University, Kingston, Ontario
 *
 * author: Margaret Lamb
 * modified: tt
 ***********************************************************************/
import hsa.*; // for simple text I/O
import javax.swing.JOptionPane; // for error messages
public class CatalogIO {

  /************************************************************************
   * This method writes a catalog to a text file.  The format is
   * designed to be read back by this program, not to be optimal for
   * human reading.  Details of the format are described in the helper
   * methods below.
   * Note: This method is not actually used in the catalog program of
   * Assignment 1.  It's included here for your use in Assignment 2.
   * You don't have to include it or its helper methods in your UML
   * diagrams for Assignment 1.
   *
   * Parameters:
   *   cat: the catalog to write
   *   outFile: the text file to write to.
   * Additional effect: this method closes the output file when it's done
   ***********************************************************************/
  public static void writeCatalog(Catalog cat, TextOutputFile outFile) {
    // write each CD in the catalog
    for (int i = 0; i < cat.getNumItems(); i++)
      writeCD((CD)cat.getItem(i), outFile);
    outFile.close();
  } // end writeCatalog


  /************************************************************************
   * Helper method: writes a single CD to a text file.  The first four
   * lines are general information about the CD:
   *   line 1: title
   * //CHANGE
   *   line1.1: tag string
   *   line 2: composer (empty line if composer is null)
   *   line 3: performer (empty line if performer is null)
   *   line 4: number of tracks in the CD
   * This is followed by output for each of the tracks (format
   * described in writeTrack).
   *
   * Parameters:
   *   cd: the CD to write
   *   outFile: the text file to write to.
   ***********************************************************************/
  private static void writeCD(CD cd, TextOutputFile outFile) {
    // initial lines
    outFile.println(cd.getTitle());
    //CHANGE: getTag method
    outFile.println(cd.getTag());
    String composer = cd.getComposer();
    if (composer == null)
      outFile.println();
    else
      outFile.println(composer);
    String performer = cd.getPerformer();
    if (performer == null)
      outFile.println();
    else
      outFile.println(performer);
    int numTracks = cd.getNumTracks();
    outFile.println(numTracks);

    // details for each track
    for (int i = 0; i < numTracks; i++)
      writeTrack(cd.getTrack(i), outFile);
  } // end writeCD


  /************************************************************************
   * Helper method: writes a single Track to a text file.  The output
   * consists of 3 lines:
   *   line 1: title
   *   line 2: composer (empty line if composer is null)
   *   line 3: performer (empty line if performer is null)
   *
   * Parameters:
   *   track: the track to write
   *   outFile: the text file to write to.
   ***********************************************************************/
  public static void writeTrack(Track track, TextOutputFile outFile) {
    outFile.println(track.getTitle());
    String composer = track.getComposer();
    if (composer == null)
      outFile.println();
    else
      outFile.println(composer);
    String performer = track.getPerformer();
    if (performer == null)
      outFile.println();
    else
      outFile.println(performer);
  } // end writeTrack


  /************************************************************************
   * This method reads a catalog from a text file.  The expected format
   * is documented in the write methods above.  If the file does not
   * conform to the expect format, the program will abort with an error
   * message.
   *
   * Parameter: the text file to read from
   * Return result: the new catalog containing the CDs described in the
   *   input file
   * Additional effect: this method closes the input file when it's done
   ***********************************************************************/
  public static Catalog readCatalog(TextInputFile inFile) {
    // create an empty catalog to fill with the CDs from the file
    Catalog newCat = new Catalog();

    // read CDs until we hit the end of the file
    while (!inFile.eof()) {
      CD newCD = readCD(inFile);
      newCat.addItem(newCD);
    } // end while
    inFile.close();

    return newCat;
  } // end readCatalog


  /************************************************************************
   * Helper method: reads one CD from a text file
   *
   * Parameter: the text file to read from
   * Return result: a new CD read from the file
   ***********************************************************************/
  private static CD readCD(TextInputFile inFile) {
    // read the first lines, with general information about the CD
    String title = inFile.readLine();
    //CHANGE: read the tag string
    String tag = inFile.readLine();
    String composer = inFile.readLine();
    String performer = inFile.readLine();
    int numTracks = inFile.readInt();
    if (numTracks < 0) {
      JOptionPane.showMessageDialog(null,
                                    "Error: negative number of tracks for CD");
      System.exit(1);
    } // end if

    // create the CD, which initially has no tracks
    // (If composer or performer are blank, the constructor will
    //  convert them to nulls)
    CD newCD = new CD(title, tag, composer, performer);

    // now read the specified number of tracks from the file and add them to
    // the CD
    for (int i = 0; i < numTracks; i++) {
      Track newTrack = readTrack(inFile, tag);
      newCD.addTrack(newTrack);
    } // end for

    return newCD;
  } // end readCD


  /************************************************************************
   * Helper method: reads one track from a text file
   *
   * Parameter: the text file to read from
   * Return result: a new track read from the file
   ***********************************************************************/
  private static Track readTrack(TextInputFile inFile, String t) {
    String title = inFile.readLine();
    String composer = inFile.readLine();
    String performer = inFile.readLine();
    return new Track(title, t, composer, performer);
  } // end readTrack


  // main method for testing
  public static void main(String[] args) {
    // 1. create a small catalog
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

    // 2. write the catalog to test.txt
    writeCatalog(myCDcollection, new TextOutputFile("test.txt"));

    // 3. read it back and dump it to see if it's the same as the
    // original
    Catalog newCatalog = readCatalog(new TextInputFile("test.txt"));
    newCatalog.dump();

  } // end main
} // end class CatalogIO
