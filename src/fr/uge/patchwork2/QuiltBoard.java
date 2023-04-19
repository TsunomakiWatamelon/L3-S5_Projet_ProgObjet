package fr.uge.patchwork2;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents the quilt board of a player
 * @author Gabriel Radoniaina / Hervé Nguyen
 * @version 10/26/2022
 * 
 */
public class QuiltBoard {
  /**
   * Size of the quilt board
   */
  private static int SIZE = 9;
  /**
   * Array representing the state of each space of the quilt board
   */
  private final boolean[][] board;
  /*
   * List of placed patches on the board
   * 
   */
  private final ArrayList<PatchPlaced> patchplaced;

  /**
   * Initialize an empty quilt board
   */
  public QuiltBoard() {
    board = new boolean[SIZE][SIZE];
    patchplaced = new ArrayList<PatchPlaced>();

    for (var i = 0; i < SIZE; i++) {
      for (var j = 0; j < SIZE; j++) {
        board[i][j] = false;
      }
    }
  }

  /**
   * Getter for field size
   * 
   * @return the size of the quilt board
   */
  public static int getSIZE() {
    return SIZE;
  }

  /**
   * Getter for field board
   * 
   * @return array of boolean representing the squares occupied in the quilt board
   */
  public boolean[][] getBoard() {
    return board;
  }
  
  /**
   * Returns the number of patches placed in this quiltboard
   * @return the corresponding number
   */
  public int nbPatchPlaced() {
    return patchplaced.size();
  }

  /**
   * getter for patchplaced field
   * @return patchplaced field
   */
  public ArrayList<PatchPlaced> patchplaced() {
    return patchplaced;
  }

  /**
   * Returns true if the patch can be placed on the current quilt board If not,
   * returns false.
   * 
   * @param patch  patch to check
   * @param anchor anchor point for placement reference
   * @return boolean
   */
  public boolean canPlacePatch(Patch patch, Point anchor) {
    Objects.requireNonNull(patch, "patch cannot be null");
    Objects.requireNonNull(anchor, "anchor point cannot be null");

    if (anchor.x < 0 || (anchor.x + patch.sizeX() > SIZE))
      return false;
    if (anchor.y < 0 || (anchor.y + patch.sizeY() > SIZE))
      return false;

    var form = patch.form();

    for (var i = 0; i < patch.sizeY(); i++) {
      for (var j = 0; j < patch.sizeX(); j++) {
        if (form[i][j] && board[anchor.y + i][anchor.x + j]) {
          return false;
        }
          
      }
    }
    return true;
  }

  /**
   * Places the corresponding patch on the quilt board
   * 
   * @param patch  patch to place
   * @param anchor anchor point for placement reference
   */
  public void placePatch(Patch patch, Point anchor) {
    Objects.requireNonNull(patch, "patch cannot be null");
    Objects.requireNonNull(anchor, "anchor point cannot be null");
    if (anchor.x < 0 || (anchor.x + patch.sizeX() > SIZE))
      throw new IllegalArgumentException("Placed patch out of bound");
    if (anchor.y < 0 || (anchor.y + patch.sizeY() > SIZE))
      throw new IllegalArgumentException("Placed patch out of bound");

    var form = patch.form();

    for (var i = 0; i < patch.sizeY(); i++) {
      for (var j = 0; j < patch.sizeX(); j++) {
        if (form[i][j] && board[anchor.y + i][anchor.x + j])
          throw new IllegalArgumentException("Placing a patch at an already occupied position");
        if (form[i][j])
          board[anchor.y + i][anchor.x + j] = true;
      }
    }

    patchplaced.add(new PatchPlaced(patch, anchor));
  }

  /**
   * Returns the buttons located on the patches placed on the quilt board
   * 
   * @return the number of buttons
   */
  public int buttons() {
    var button = 0;
    for (var pp : patchplaced) {
      button += pp.patch().buttons();
    }
    return button;
  }

  /**
   * Counts the number of empty square in the quiltboard
   * 
   * @return The number of empty square
   */
  public int emptySquare() {
    int nb = 0;
    for (var i = 0; i < SIZE; i++) {
      for (var j = 0; j < SIZE; j++) {
        if (board[i][j] == false)
          nb++;
      }
    }
    return nb;
  }
  
  /**
   * Checks if there is a 7x7 square in the quiltboard at the location described by the left topmost corner
   * @param x x coordinate of the left topmost position
   * @param y y coordinate of the left topmost position
   * @return true if there is a 7x7 square at the position, if not then false
   */
  private boolean isSevenBySevenSquare(int x, int y) {
    if (x < 0 || x > 2)
      throw new IllegalArgumentException("x out of bounds for 7x7 square");
    if (y < 0 || y > 2)
      throw new IllegalArgumentException("x out of bounds for 7x7 square");
    
    for(var i = 0; i < 7; i++)
      for (var j = 0; j < 7; j++)
        if (board[y + i][x + j] == false)
          return false;
    return true;
  }
  
  /**
   * Checks if the current quiltboard has a full 7x7 square created
   * @return true if there is a 7x7 square placed, if not then false
   */
  public boolean hasSevenBySevenSquare() {
    for(var i = 0; i < 3; i++)
      for(var j = 0; j < 3; j++)
        if (isSevenBySevenSquare(i, j))
          return true;
    return false;
  }
}
