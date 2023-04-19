package fr.uge.patchwork2;

import java.util.List;
import java.util.Objects;

/**
 * Class used for creating strings to display the states of the game
 * @author Gabriel Radoniaina / Hervé Nguyen
 *
 */
public class Displays {
  
  /**
   * Generates a string that shows the score of both players
   * @param player1 player one
   * @param player2 player two
   * @return the corresponding string
   */
  public static String displayScore(Player player1, Player player2) {
    Objects.requireNonNull(player1);
    Objects.requireNonNull(player2);
    return "Player 1's score is " + player1.score() + "\n" + "Player 2's score is " + player2.score() + "\n";
  }
  
  /**
   * Generates a string that shows the result of the game by calculating the scores
   * @param player1 player one
   * @param player2 player two
   * @return the corresponding string
   */
  public static String displayResult(Player player1, Player player2) {
    Objects.requireNonNull(player1);
    Objects.requireNonNull(player2);
    if (player1.score() > player2.score())
      return "Player 1 has won !";
    else if (player1.score() < player2.score())
      return "Player 2 has won !";
    else
      return "It's a draw !";
  }
  
  /**
   * Generates a string that compromises of the informations necessary at the start of a turn
   * It compromises of :
   * - The state of the timeboard
   * - The number of buttons currently held by the player
   * - The list of available patches to buy
   * - The state of his quiltboard
   * @param timeboard timeboard
   * @param patchcircle patchcircle
   * @param currentPlayer the current player
   * @param player1 player one
   * @param player2 player two
   * @param tokenOneOnTwo boolean that states if when player one and two are at the same index, who is on top.
   * @return the corresponding string
   */
  public static String displayStartOfTurn(
      TimeBoard timeboard,
      PatchCircle patchcircle,
      Player currentPlayer,
      Player player1,
      Player player2,
      boolean tokenOneOnTwo) 
  {
    Objects.requireNonNull(player1);
    Objects.requireNonNull(player2);
    Objects.requireNonNull(timeboard);
    Objects.requireNonNull(patchcircle);
    Objects.requireNonNull(currentPlayer);
    var sb = new StringBuilder();
    sb.append(Displays.displayTimeBoardState(timeboard, player1, player2, tokenOneOnTwo));
    sb.append("# You currently have " ).append(currentPlayer.button()).append(" buttons\n");
    sb.append("# Patches you can choose :\n");
    sb.append(Displays.displayPatchCircle(patchcircle));
    sb.append("# State of your quiltboard:\n");
    return sb.append(Displays.displayQuiltBoard(currentPlayer.quiltboard())).toString();
  }
  
  /**
   * Auxiliary method to generate a string that represents either a patch or a quilt board
   * 
   * Example for a 2x2 partially covered grid :
   * ╔══╦══╗
   * ║**║**║
   * ╠══╬══╣
   * ║  ║  ║
   * ╚══╩══╝
   * @param board array of boolean that represents occupied positions
   * @param size_x horizotal size of the array
   * @param size_y vertical size of the array
   * @return the corresponding string
   */
  public static String displayGrid(boolean[][] board, int size_x, int size_y) {
    Objects.requireNonNull(board);
    var grid = new StringBuilder(); var floorSeparator = new StringBuilder();
    
    grid.append("╔══").append("╦══".repeat(size_x - 1)).append("╗\n");
    floorSeparator.append("╠══").append("╬══".repeat(size_x - 1)).append("╣\n");
    
    for(var i = 0; i < size_y; i++) {
      grid.append("║");
      for(var j = 0; j < size_x; j++) {
        if(board[i][j]) // occupied
          grid.append("**║");
        else
          grid.append("  ║");
        }
        grid.append("\n");
        if(i < size_y-1)
          grid.append(floorSeparator);
    }
    return grid.append("╚══").append("╩══".repeat(size_x - 1)).append("╝\n").toString();
  }

    /**
	 * Generates a string that represents the current state of the given quilt board
	 * @param quiltBoard the quilt board to generate the string
	 * @return the string that represents the state of quilt board
	 */
	public static String displayQuiltBoard(QuiltBoard quiltBoard) {
		Objects.requireNonNull(quiltBoard, "quiltBoard shouldn't be null");
		return displayGrid(quiltBoard.getBoard(), QuiltBoard.getSIZE(), QuiltBoard.getSIZE());
	}
	
	/**
	 * Generate a string that represents the given patch
	 * @param patch patch
	 * @return the corresponding string
	 */
	public static String displayPatch(Patch patch) {
		Objects.requireNonNull(patch, "patch shouldn't be null");
		return displayGrid(patch.form(), patch.sizeX(),  patch.sizeY());
	}
	
	/**
	 * Auxiliary method
	 * Generate a string that represents the selection of available patches with their characteristics from a List of Patches
	 * @param patch list of patches
	 * @return the corresponding string
	 */
	private static String displayPatchAvailable(List<Patch> patch) {
	  Objects.requireNonNull(patch);
		var value = new StringBuilder(); var k = 0; var iterator = patch.iterator();
		Patch element;
		while(iterator.hasNext() && k < 3) {
		  element = iterator.next();
		  
		  value.append("id : ").append(k)
		       .append("\n");
		  
			value.append("Time: ").append(element.time())
			     .append("\nButtons: ").append(element.buttons())
			     .append("\nPrice:").append(element.price())
			     .append("\n");
			value.append(displayPatch(element)).append("\n");
			
			k++;
		}
		return value.toString();
	}
	
	/**
	 * Generate a string that will represent the selection of available patches with their characteristics
	 * @param patchcircle The PatchCircle
	 * @return the corresponding string
	 */
	public static String displayPatchCircle(PatchCircle patchcircle) {
	  Objects.requireNonNull(patchcircle);
	  var value = new StringBuilder();
	  value.append(displayPatchAvailable(patchcircle.getPatch()));
	  return value.toString();
	}
	
	/**
	 * Generate a string that represents the given time board
	 * @param timeBoard the time board
	 * @return the corresponding string
	 */
	public static String displayTimeBoardPath(TimeBoard timeBoard) {
		Objects.requireNonNull(timeBoard, "timeBoard shouldn't be null");
		var value = new StringBuilder();
		var s = "═".repeat(TimeBoard.getSIZE()) + "\n";
		value.append(s);
		for(var element:timeBoard.path()) {
			if(element.equals(TimeBoardElement.button)) {
				value.append("©");
			}
			else if(element.equals(TimeBoardElement.specialPatch)){
			  value.append("■");
			}
			else
				value.append(" ");
		}
		value.append("\n").append(s);
		return value.toString();
	}
	
	/**
	 * Generate a string that represents the current positions of the two player's time tokens
	 * @param one player one
	 * @param two player two
	 * @param tokenOneOnTwo boolean used to check if the token of player one is on the token of player two during a position conflict
	 * @return the corresponding string
	 */
	public static String displayTimeToken(Player one, Player two, boolean tokenOneOnTwo) {
		Objects.requireNonNull(one, "one (player) shouldn't be null");
		Objects.requireNonNull(two, "two (player) shouldn't be null");
		var value = new StringBuilder();
		
		if(one.location() == two.location()) { // if player one is on the same location as player two
			var s1 = " ".repeat(one.location()) + "1\n";
			var s2 = " ".repeat(two.location()) + "2\n";
			if(tokenOneOnTwo)
				value.append(s1).append(s2);
			else
				value.append(s2).append(s1);
			return value.toString();
		}
		
		if(one.location() > two.location()) // if player 1 is ahead of player 2
			value.append(" ".repeat(two.location())).append("2").append(" ".repeat(one.location() - two.location())).append("1\n");
		else
			value.append(" ".repeat(one.location())).append("1").append(" ".repeat(two.location() - one.location())).append("2\n");
		return value.toString();
	}
	
	/**
	 * Generate a string the state of the time board and the time tokens of the players
	 * @param timeBoard the time board
	 * @param one player one
	 * @param two layer two
	 * @param tokenOneOnTwo boolean used to check if the token of player one is on the token of player two during a position conflict
	 * @return the corresponding string
	 */
	public static String displayTimeBoardState(TimeBoard timeBoard, Player one, Player two, boolean tokenOneOnTwo) {
		Objects.requireNonNull(timeBoard, "timeBoard shouldn't be null");
		Objects.requireNonNull(one, "one (player) shouldn't be null");
		Objects.requireNonNull(two, "two (player) shouldn't be null");
		var sb = new StringBuilder();
		
		return sb.append("╔═════════════════════════════════════╗\n")
             .append("║   Current state of the time board   ║\n")
             .append("╚═════════════════════════════════════╝\n")
             .append(displayTimeToken(one, two, tokenOneOnTwo))
		         .append(displayTimeBoardPath(timeBoard))
		         .toString();
	}
	
	/**
	 * Creates a congratulation String to the first player who has completed a 7x7 full square in his quiltboard
	 * @return the corresponding String
	 */
  public static String displaySpecialTileMessage() {
    var sb = new StringBuilder();
    sb.append("You are the first to have completed a 7 by 7 square !\n");
    sb.append("You have obtained the special tile which is worth 7 buttons!\n");
    return sb.toString();
  }
}
