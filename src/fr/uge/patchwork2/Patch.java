package fr.uge.patchwork2;

import java.awt.Color;
import java.util.Objects;

/**
 * 
 * Representation of a patch
 * @author Gabriel Radoniaina / Hervé Nguyen
 * @param buttons buttons contained on the patch
 * @param price price of the patch (in buttons)
 * @param time time value related to the patch
 * @param form form of the patch
 * @param sizeX horizontal size of the form array
 * @param sizeY vertical size of the form array
 * @param color color of the patch
 *
 */
public record Patch(int buttons, int price, int time, boolean[][] form, int sizeX, int sizeY, Color color) {
  
  /**
   * Initializes a Patch based on given values.
   * This constructor shall not be used outside of this class
   * to get a new Patch object, static methods like `cube1` should be used.
   * @param buttons buttons contained on the patch
   * @param price price of the patch (in buttons)
   * @param time time value related to the patch
   * @param form form of the patch
   * @param sizeX horizontal size of the form array
   * @param sizeY vertical size of the form array
   * @param color color of the patch
   */   
  public Patch {
    Objects.requireNonNull(form);
    Objects.requireNonNull(color);
    if (sizeX <= 0)
      throw new IllegalArgumentException("sizeX <= 0");
    if (sizeY <= 0)
      throw new IllegalArgumentException("sizeY <= 0");
    if (time < 0)
      throw new IllegalArgumentException("time < 0");
    if (price < 0)
      throw new IllegalArgumentException("price < 0");
    if (buttons < 0)
      throw new IllegalArgumentException("button < 0");
  }
  
  /**
   * Flips an array of boolean according to the given parameters
   * @param form array of booleans representing the form of the patch
   * @param sizeX horizontal size of the array
   * @param sizeY vertical size of the array
   * @param type flip type (0 or 1, defines the flip direction)
   * @return
   */
  private static boolean[][] flipArray(boolean[][] form, int sizeX, int sizeY, int type) {
    Objects.requireNonNull(form);
    if (type != 0 && type != 1)
      throw new IllegalArgumentException("Unknown flip type");
    
    // flipping the former array
    var newBoard = new boolean[sizeX][sizeY];
    for(var i= 0; i < sizeX; i++) {
        for(var j = 0; j < sizeY; j++){
            if (type == 0)
              newBoard[i][j] = form[sizeY-1-j][i];
            else
              newBoard[i][j] = form[j][sizeX-i-1];
        }
    }
    return newBoard;
  }
  
  
  /**
   * Flips the current patch
   * @param type nature of the flip (left = 0, right = 1)
   * @return the newly flipped patch
   */
  public Patch flip(int type) {
    if (type != 0 && type != 1)
      throw new IllegalArgumentException("Unknown flip type");
    
    var newForm = flipArray(form, sizeX, sizeY, type);
    
    return new Patch(buttons, price, time, newForm, sizeY, sizeX, color);
  }

  /**
   * Mirrors an array of boolean according to the given parameters (axial symmetry)
   * @param form array of booleans representing the form of the patch
   * @param sizeX horizontal size of the array
   * @param sizeY vertical size of the array
   * @param type flip type (0 = horizontal, 1 = vertical)
   * @return
   */
  private static boolean[][] mirrorArray(boolean[][] form, int sizeX, int sizeY, int type){
    if (type != 0 && type != 1)
      throw new IllegalArgumentException("Unknown mirror type");
    var newBoard = new boolean[sizeY][sizeX];
    for(var i= 0; i < sizeY; i++) {
      for(var j = 0; j < sizeX; j++){
          if (type == 0)
            newBoard[i][j] = form[i][sizeX-1-j];
          else
            newBoard[i][j] = form[sizeY - 1 - i][j];
      }
    }
    return newBoard;
  }

  /**
   * Mirrors the current patch
   * @param type nature of the flip (0 = horizontal, 1 = vertical)
   * @return the newly flipped patch
   */
  public Patch mirror(int type) {
    if (type != 0 && type != 1)
      throw new IllegalArgumentException("Unknown mirror type");
    
    var newForm = mirrorArray(form, sizeX, sizeY, type);
    
    return new Patch(buttons, price, time, newForm, sizeX, sizeY, color);
  }
  
  /**
   * Creates a patch in a cube shape
   * @param size the size of the cube
   * @param buttons number of buttons on the patch
   * @param price price of the patch
   * @param time time value of the patch
   * @param color color of the patch
   * @return the newly created patch
   */
  private static Patch cube(int size, int buttons, int price, int time, Color color) {
    var form = new boolean[size][size];
    for (var i = 0; i < size; i++)
      for (var j = 0; j < size; j++)
        form[i][j] = true;
    return new Patch(buttons, price, time, form, size, size, color);
  }
  
  /**
   * Creates a special patch (1x1 patch obtained from crossing it first on the timeboard)
   * @return the special patch
   */
  public static Patch specialPatch() {
    /* Brown special patch */
    return cube(1, 0, 0, 0, new Color(102, 51, 0));
  }
  
  /**
   * Creates a new patch corresponding to the cube n°1 in basic mode
   * @return the created patch
   */
  public static Patch cubeBasic1() {
    return cube(2, 1, 3, 4, Color.ORANGE);
  }
  
  /**
   * Creates a new patch corresponding to the cube n°2 in basic mode
   * @return the created patch
   */
  public static Patch cubeBasic2() {
    /* Gold */
    return cube(2, 0, 2, 2, new Color(255, 204, 51));
  }

}
