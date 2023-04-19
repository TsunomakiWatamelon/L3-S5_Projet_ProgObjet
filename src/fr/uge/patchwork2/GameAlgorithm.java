package fr.uge.patchwork2;

import java.util.Objects;
import java.util.Scanner;

import fr.umlv.zen5.ApplicationContext;

/**
 * Methods that constitutes the main game algorithm
 * @author Gabriel Radoniaina / Hervé Nguyen
 *
 */
public class GameAlgorithm {
  
  private PatchCircle patchcircle;
  private TimeBoard timeboard;
  private final Player player1;
  private final Player player2;
  private Player currentPlayer;
  private Player currentOpponent;
  private boolean game;
  private boolean tokenOneOnTwo;
  private boolean specialTileTaken;
  private final int type;
  
  /**
   * Initializes the object of GameAlgorithm class, some of the initialized data depends on the type
   * @param type type of the game (basic = 0 or full = 1)
   */
  public GameAlgorithm(int type) {
    if (type > 1 || type < 0)
      throw new IllegalArgumentException("game type invalid");
    
    if (type == 0) {
      patchcircle = PatchCircle.newPatchCircleBasic();
      timeboard = TimeBoard.newBasicTimeBoard();
    } else {
      patchcircle = PatchCircle.newPatchCircleFull();
      timeboard = TimeBoard.newFullTimeBoard();
    }
    this.type = type;
    player1 = Player.newPlayer(1); player2 = Player.newPlayer(2);
    game = true;
    tokenOneOnTwo = true;
    specialTileTaken = false;

  }
  
  /**
   * From the known information, selects the player who needs to play
   */
  private void turnSelection() {
    currentPlayer = player1.playerTurn(player2, tokenOneOnTwo) ? player1 : player2;
    currentOpponent = player1.playerTurn(player2, tokenOneOnTwo) ? player2 : player1;
  }
  
  /**
   * Displays the basic information for the start of the turn
   */
  private void terminaDisplayInfoTurnStart() {
    if (player1.playerTurn(player2, tokenOneOnTwo))
      System.out.println("### IT IS PLAYER 1's TURN! ###\n");
    else
      System.out.println("### IT IS PLAYER 2's TURN! ###\n");
    System.out.println(Displays.displayStartOfTurn(timeboard, patchcircle, currentPlayer, player1, player2, tokenOneOnTwo));
  }
  
  /**
   * Performs the action of the current player
   * @param scanner scanner
   */
  private void terminalPerformPlayerAction(Scanner scanner) {
    tokenOneOnTwo = TerminalAlgorithm.action(currentPlayer, currentOpponent, scanner, patchcircle, timeboard, type);
  }
  
  /**
   * Checks if the current player had achieved the conditions to get the special tile and if so, updates the informations
   * @param terminal, boolean that specifies if the method is called during terminal or graphic mode. (true = terminal)
   */
  private void specialTileProcedure(boolean terminal) {
    if (!specialTileTaken && currentPlayer.quiltboard().hasSevenBySevenSquare()) {
      if (terminal) {
        System.out.println(Displays.displaySpecialTileMessage());
      }
      currentPlayer.takeSpecialTile();
      specialTileTaken = true;
    }
  }
  
  /**
   * Checks if the game ending condition are met and updates the game if they are
   */
  private void checkEndConditions() {
    if (player1.finished() && player2.finished())
      game = false;
  }
  
  /**
   * Game algorithm for the terminal mode
   * @param scanner scanner
   */
  public void terminalMode(Scanner scanner) {
    Objects.requireNonNull(scanner);
    
    while (game) {
      turnSelection();
      terminaDisplayInfoTurnStart();
      terminalPerformPlayerAction(scanner);
      specialTileProcedure(true);
      checkEndConditions();
      Tools.sleep(300); // delay to not see the program flash through instantly after a turn
      System.out.println("\n\n\n\n\n");
    }
    System.out.println(Displays.displayScore(player1, player2));
    System.out.println(Displays.displayResult(player1, player2));
  }
  
  /**
   * Game algorithm for Graphic mode (Zen5 + Java AWT)
   * @param context ApplicationContext
   */
  public void graphicMode(ApplicationContext context) {
    Objects.requireNonNull(context);
    if (context.getScreenInfo().getHeight() <= 0 || context.getScreenInfo().getWidth() <= 0)
      throw new IllegalArgumentException("ApplicationContext have unusable characteristics");
    var graphicGame = new GraphicAlgorithm(context, player1, player2, patchcircle, timeboard);
    while(game) {
      turnSelection();
      graphicGame.drawTurnStartInfo(tokenOneOnTwo);
      GraphicElements.clearAll(context);
      tokenOneOnTwo = graphicGame.action(currentPlayer, currentOpponent, tokenOneOnTwo);
      specialTileProcedure(false);
      checkEndConditions();
    }
    GraphicElements.clearAll(context);
    GraphicElements.drawEndMessage(context, player1, player2);
    GraphicElements.getClick(context);
  }
}
