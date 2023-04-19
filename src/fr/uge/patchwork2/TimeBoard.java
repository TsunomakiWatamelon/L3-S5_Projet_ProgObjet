/**
 * 
 */
package fr.uge.patchwork2;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a time board
 * @author Gabriel Radoniaina / Hervé Nguyen
 *
 */
public class TimeBoard {
  /**
   * Size of the time board
   */
  private static int SIZE = 54;
  /**
   * Array containing the specific element at each index like buttons or special tiles
   */
  private final TimeBoardElement[] path;
  /**
   * Number of special patch to be placed on the timeboard
   */
  private static int speciaPatchNb = 5;
  /**
   * Number of buttons to be placed on the timeboard
   */
  private static int buttonNb = 9;
  
  /**
   * Initializes the time board
   */
  public TimeBoard() {
    path = new TimeBoardElement[SIZE];
    for (var i = 0; i < SIZE; i++) {
      path[i] = TimeBoardElement.empty;
    }
    for (var i = 0; i < buttonNb; i++) {
      path[5 + 6 * i] = TimeBoardElement.button;
    }
  }
  
  /**
   * Creates new a timeboard that corresponds to the basic mode of the game
   * @return the new timeboard
   */
  public static TimeBoard newBasicTimeBoard() {
    var newTimeBoard = new TimeBoard();
    return newTimeBoard;
  }
  
  /**
   * Creates new a timeboard that corresponds to the full mode of the game
   * @return the new timeboard
   */
  public static TimeBoard newFullTimeBoard() {
    var newTimeBoard = new TimeBoard();
    var path = newTimeBoard.path();
    for (var i = 0; i < speciaPatchNb; i++) {
      path[26 + 6 * i] = TimeBoardElement.specialPatch;
    }
    return newTimeBoard;
  }
  
  /**
   * Getter for the field SIZE
   * @return Field SIZE, represents the size of the time board.
   */
  public static int getSIZE() {
    return SIZE;
  }
  
  /**
   * Getter for field path
   * @return Field path, array that contains the corresponding element of the time board at each index
   */
  public TimeBoardElement[] path() {
    return path;
  }
  

  /**
   * Get the index in form of a point of integer for the 2D path from the index of a 1D path.
   * The 2D timeboard is a 9x6 rectangle.
   * This only works for a 9x6 path, going from north west then moving clockwise until it reaches the "center" at index 53.
   * The coordinates movement in the loop represents the path a time token will take.
   * @param index 1D index to convert into 2D index for the timeboard
   * @return the corresponding point
   */
  public static Point path2DIndex(int index) {
    if (index < 0 || SIZE <= index)
      throw new IllegalArgumentException("index out of bound");
    int x = 0, y = 0;
    for(var i = 0; i < index; i++) {
      if (i == index) break;
      if ((0 <= i && i < 8) || (25 <= i && i < 32) || (43 <= i && i < 48)) x++;
      else if ((8 <= i && i < 13) || (32 <= i && i < 35) || (48 <= i && i < 49)) y++;
      else if ((13 <= i && i < 21) || (35 <= i && i < 41) || (49 <= i && i < 53)) x--;
      else if ((21 <= i && i < 25) || (41 <= i && i < 43) || (49 <= i && i < 53)) y--;
    }
    return new Point(x, y);
  }



/**
   * Returns an array of TimeBoardElement that represents the elements found between startIndex+1 and finishIndex
   * @param startIndex index of the position of the time token of the player
   * @param finishIndex index of the final position of the time token after moving it
   * @return the list of crossed TimeBoardElements
   */
  public List<TimeBoardElement> elementCrossed(int startIndex, int finishIndex){
    if (startIndex < 0 || startIndex >= SIZE)
      throw new IllegalArgumentException("startIndex out of bound");
    if (finishIndex < 0 || finishIndex >= SIZE)
      throw new IllegalArgumentException("finishIndex out of bound");
    if (finishIndex <= startIndex)
      throw new IllegalArgumentException("finishIndex is lower or equal to startIndex");
    
    var crossed = new ArrayList<TimeBoardElement>();
    for (int i = startIndex + 1; i <= finishIndex; i++) {
      if (path[i] != TimeBoardElement.empty) {
        if (path[i] == TimeBoardElement.button) {
          crossed.add(TimeBoardElement.button);
        }
        if (path[i] == TimeBoardElement.specialPatch) {
          path[i] = TimeBoardElement.empty;
          crossed.add(TimeBoardElement.specialPatch);
        }
      }
    }
    return crossed;
  }
  
  /**
   * Sets empty the element of path at the given index.
   * @param index Index of the element to set empty.
   */
  public void setEmpty(int index) {
    if (index < 0)
      throw new IllegalArgumentException("index < 0");
    if (index >= SIZE)
      throw new IllegalArgumentException("index > SIZE (53)");
    path[index] = TimeBoardElement.empty;
  }
}
