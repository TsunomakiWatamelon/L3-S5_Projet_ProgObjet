package fr.uge.patchwork2;

import java.util.Objects;

/**
 * Represents a player
 * @author Gabriel Radoniaina / Hervé Nguyen
 * @version 11/26/2022
 */
public class Player {
  
  /**
   * button buttons held by the player
   */
  private int button;
  /**
   * location integer representing the position of the player (his time token) on the time board
   */
  private int location;
  /**
   * quiltboard the representation of the quilt board of the player
   */
  private final QuiltBoard quiltboard;
  /**
   * finished true if the player cannot make any more actions, else false
   */
  private boolean finished;
  /**
   * id integer used to differentiate different players with same fields
   */
  private final int id;
  
  /**
   * specialTile used to know if the current player has obtained the special tile
   */
  private boolean specialTile;
  
  /**
   * specialPatches represents the number of special patches picked up by the current player
   */
  private int specialPatches;
  
  /**
   * getter for button field
   * @return the button field
   */
  public int button() {
    return button;
  }

  /** 
   * getter for the location field
   * @return the location field
   */
  public int location() {
    return location;
  }

  /**
   * getter for the quiltboard field
   * @return the quiltboard field
   */
  public QuiltBoard quiltboard() {
    return quiltboard;
  }

  /**
   * getter for the finished field
   * @return the finished field
   */
  public boolean finished() {
    return finished;
  }

  /**
   * getter for the id field
   * @return the id field
   */
  public int id() {
    return id;
  }

  /**
   * getter for specialTile field
   * @return specialTile field
   */
  public boolean specialTile() {
    return specialTile;
  }


  /**
   * getter for specialPatches field
   * @return specialPatches field
   */
  public int specialPatches() {
    return specialPatches;
  }

  /**
   * Initializes the player based on given parameters (only to use in a method inside this class)
   * @param button buttons held by the player
   * @param location integer representing the position of the player (his time token) on the time board
   * @param quiltboard the representation of the quilt board of the player
   * @param finished true if the player cannot make any more actions, else false
   * @param id integer used to differentiate different players with same fields
   */
  public Player(int button, int location, QuiltBoard quiltboard, boolean finished, int id) {
    Objects.requireNonNull(quiltboard, "quiltboard cannot be null");
    if (button < 0)
      throw new IllegalArgumentException("number of buttons cannot be negative");
    if (location < 0) {
      throw new IllegalArgumentException("location cannot be lower than 0");
    }
    if (id != 1 && id != 2)
      throw new IllegalArgumentException("Unknown player id");
    this.button = button;
    this.location = location;
    this.quiltboard = quiltboard;
    this.finished = finished;
    this.id = id;
    this.specialTile = false;
  }
  
  /**
   * Initializes the player
   * @param id identifier
   * @return Player the new Player
   */
  public static Player newPlayer(int id) {
    if (id != 1 && id != 2)
      throw new IllegalArgumentException("Illegal player id");
    var button = 5;
    var location = 0;
    var quiltboard = new QuiltBoard();
    var finished = false;
    return new Player(button, location, quiltboard, finished, id);
  }

  /**
   * Updates an existing instance's button field with a new given value
   * @param newButton new value of the field button for the player
   */
	public void updateButton(int newButton) {
	  if (newButton < 0)
	    throw new IllegalArgumentException("Player cannot be in debt");
	  button = newButton;
	}
	
	/**
	 * Updates an existing instance's location field with a new given value
	 * @param newLocation new value of the field location for the player
	 */
  public void updateLocation(int newLocation) {
    if (newLocation < 0 || newLocation > (TimeBoard.getSIZE() - 1)) {
      throw new IllegalArgumentException("Player location out of bound in the timeboard");
    }
    location = newLocation;
    if (location == TimeBoard.getSIZE() - 1)
      this.finishPlayer();
  }
  
  /**
   * Updates the player and sets it as finished
   */
  public void finishPlayer() {
    finished = true;
  }
  
  /**
   * Returns a boolean that says if the current instance has the turn priority over the Player in the parameter
   * @param player2 Second player to compare the priority
   * @param tokenOneOnTwo should be true if the token of the current player is on the second player's (to settle if equal location)
   * @return true if it's the turn of the current instance, otherwise false
   */
  public boolean playerTurn(Player player2, boolean tokenOneOnTwo) {
    Objects.requireNonNull(player2);
    if (location < player2.location())
      return true;
    if (location == player2.location())
      return tokenOneOnTwo;
    return false;
  }
  
  /**
   * Calculates the score the player currently has. (number of buttons held minus the amount of empty square in the quiltboard)
   * @return the score
   */
  public int score() {
    return button - 2 * quiltboard.emptySquare();
  }
  
  /**
   * Moves the player according to the chosen patch.
   * @param chosenPatch The chosen patch
   */
  public void movePlayer(Patch chosenPatch) {
    Objects.requireNonNull(chosenPatch);
    if ((this.location() + chosenPatch.time()) >= (TimeBoard.getSIZE() - 1)) {
      this.updateLocation(TimeBoard.getSIZE() - 1);
      this.finishPlayer();
    }
    else {
      this.updateLocation(this.location() + chosenPatch.time());
    }
  }
  
  /**
   * Makes the player take the special tile after obtaining the first 7x7 square in the quiltboard
   */
  public void takeSpecialTile() {
    if (specialTile)
      throw new IllegalStateException("Current player already has obtaine the special tile");
    specialTile = true;
    updateButton(button + 7);
  }
  
  /**
   * Makes the player "take" a special patch which increments the corresponding field
   */
  public void takeSpecialPatch() {
    specialPatches++;
  }
  
  /**
   * Checks if the current player can buy the given patch
   * @param chosenPatch the aforementioned patch
   * @return true if the player can buy the patch, otherwise false
   */
  public boolean graphicCanBuyPatch(Patch chosenPatch) {
    Objects.requireNonNull(chosenPatch);
    return (button - chosenPatch.price() >= 0);
  }
  
  /**
   * Moves the player's location according to the fact that the players did not chose to purchase a patch
   * @param opponent the opponent
   */
  public void skipSelectionUpdateLocation(Player opponent) {
    Objects.requireNonNull(opponent);
    if (opponent.location() < location())
      throw new IllegalStateException("Opponent is behind the player");
    if (opponent.finished()) {
      updateLocation(TimeBoard.getSIZE() - 1);
    } else {
      updateLocation(opponent.location() + 1);
    }
  }
  
  /**
   * Returns the number of crossed tiles on the timeboard if the player skipped the patch selection
   * @param opponent the opponent
   * @return the aforementioned value
   */
  public int getTileCrossedSelectionSkip(Player opponent) {
    Objects.requireNonNull(opponent);
    if (opponent.finished()) {
      return opponent.location() - location();
    } else {
      return opponent.location() - location() + 1;
    }
  }
  
  /**
   * Returns the number of crossed tiles on the timeboard if the player chose the given patch
   * @param patch the patch
   * @return the aforementioned value
   */
  public int getTileCrossedChosePatch(Patch patch) {
    Objects.requireNonNull(patch);
    if (patch.time() + location >= TimeBoard.getSIZE())
      return TimeBoard.getSIZE() - 1;
    return patch.time() + location;
  }
  
  /**
   * Get the index after crossing "distance" tiles. Used to include the case of arriving at the ned of the timeboard
   * @param distance the distance
   * @return destination index in the timeboard
   */
  public int getDestinationIndex(int distance) {
    if (distance <= 0)
      throw new IllegalArgumentException("Distance cannot be <= 0");
    var destination = location() + distance;
    if (location() + distance > TimeBoard.getSIZE() - 1)
      destination = TimeBoard.getSIZE() - 1;
    return destination;
  }
  
}
