package fr.uge.patchwork2;

import java.awt.Point;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

/**
 * Collection of methods required to process the gameplay algorithm
 * The specificity of these methods are that they make use of inputs from the terminal
 * @author Gabriel Radoniaina / Hervé Nguyen
 *
 */
public class TerminalAlgorithm {
  
  /**
   * Shows the state of the quiltboard of a given player
   * @param player the current player
   */
  private static void showStateQuiltboard(Player player) {
    System.out.println("# State of your quiltboard:");
    System.out.println(Displays.displayQuiltBoard(player.quiltboard()));
  }

  /**
   * From a given TimeBoardElement, processes the element with the appropriate action
   * @param player the current player
   * @param scanner scanner
   * @param element the TimeBoardElement to process
   */
  private static void processTimeBoardElement(Player player, Scanner scanner, TimeBoardElement element) {
    if (element == TimeBoardElement.button) {
      var added = player.quiltboard().buttons();
      System.out.println("You have crossed a button and have been awarded " + added + " buttons");
      player.updateButton(player.button() + added);
    }
    if (element == TimeBoardElement.specialPatch) {
      System.out.println("You have picked up a special patch!");
      placeSpecialPatches(player, scanner);
    }
  }
  
  /**
   * Makes the player place a special patch
   * @param player current player
   * @param scanner scanner
   */
  private static void placeSpecialPatches(Player player, Scanner scanner) {
    System.out.println("Please place your special patch");
    patchPlacementProcedure(player, Patch.specialPatch(), scanner, 0);

  }
  
  /**
   * From the distance of the move, processes actions when the player crosses TimeBoardElements
   * @param player current player
   * @param timeboard The TimeBoard of the current game
   * @param distance The distance of the move of the player
   * @param scanner scanner
   */
  private static void timeboardCrossingProcedure(Player player, TimeBoard timeboard, int distance, Scanner scanner) {
    if (distance <= 0)
      throw new IllegalArgumentException("distance <= 0");
    
    var finishIndex = player.location() + distance;
    if (player.location() + distance > TimeBoard.getSIZE() - 1)
      finishIndex = TimeBoard.getSIZE() - 1;
    var crossedElements = timeboard.elementCrossed(player.location(), finishIndex);
    
    crossedElements.stream().forEach(element -> processTimeBoardElement(player, scanner, element));
  }
  

  
  
  /**
   * Processes the appropriate action when the players skips the selection procedure.
   * If the player / user wished to skip and refrain from choosing a patch, this method will process the movement of the player on the timeboard accordingly.
   * The Player's position will be set in front of the opponent if the opponent hasn't finished (and the field `finished` will be set to true if arrived at the finish line).
   * @param player current player
   * @param opponent the current opponent
   * @param timeboard the timeboard
   * @param scanner scanner
   */
  private static void skipSelectionProcedure(Player player, Player opponent, TimeBoard timeboard, Scanner scanner) {
    System.out.println("## Moving your time token...");
    timeboardCrossingProcedure(player, timeboard, opponent.location() - player.location() + 1, scanner);
    var tilesCrossed = player.getTileCrossedSelectionSkip(opponent);
    player.skipSelectionUpdateLocation(opponent);
    System.out.println("You have crossed " + tilesCrossed + " tiles and have been awarded the same amount of buttons");
    player.updateButton(player.button() + tilesCrossed);
    if (player.location() == TimeBoard.getSIZE() - 1)
      player.finishPlayer();
  }
  
  
  /**
   * Shows the player the state of the patch he's trying to flip
   * @param patch
   */
  private static void showPatchFlipState(Patch patch) {
    System.out.println("# State of your patch:");
    System.out.println(Displays.displayPatch(patch));
    System.out.println("Do you want to flip/mirror your patch ?");
    System.out.println("Input 'L' to flip to the left, 'R' to the right");
    System.out.println("Input 'V' to mirror vertically, 'H' to mirror horizontally");
    System.out.println("Input 'Y' to stop");
  }
  
  /**
   * Let's the user flip the given Patch until he is satisfied
   * @param scanner scanner
   * @param patch the given patch
   * @param type type of the game
   * @return the "flipped" patch
   */
  private static Patch flipProcedure(Player player, Scanner scanner, Patch patch, int type) {
    showStateQuiltboard(player);
    if (type == 0)
      return patch;
    while(true) {
      showPatchFlipState(patch);
      var key = scanner.next().charAt(0);
      if (key == 'l' || key == 'L')
        patch = patch.flip(0);
      if (key == 'r' || key == 'R')
        patch = patch.flip(1);
      if (key == 'h' || key == 'H')
        patch = patch.mirror(0);
      if (key == 'v' || key == 'V')
        patch = patch.mirror(1);
      if (key == 'y' || key == 'Y') {
        showStateQuiltboard(player); /* show the state again after flipping for the player to not scroll up again */
        return patch;
      }
    }
  }
  
  /**
   * Asks the player to input valid coordinates
   */
  private static void askValidCoordinates() {
    System.out.println("# Please input valid coordinates to place your patch");
    System.out.println("# For x y, example for (x = 2 and y = 4) : 2 4\n");
  }
  
  /**
   * Asks the player to place the given patch and places it if the choice is valid.
   * Otherwise it will loop until a satisfactory placement position is given.
   * @param player the current player
   * @param patch the given patch
   * @param scanner scanner
   * @param type represents the game mode the player is in (1 == full, 0 == simplified)
   */
  private static void placementSelection(Player player, Patch patch, Scanner scanner, int type) {
    int anchorX, anchorY;
    Point anchor;
    do {
      showStateQuiltboard(player);
      if (type == 1) { /* if full version played then we can use the flip procedure */
        patch = flipProcedure(player, scanner, patch, type);
        showStateQuiltboard(player); /* show the state again after flipping for the player to not scroll up again */
      }
      askValidCoordinates();
      try {
        anchorX = scanner.nextInt(); anchorY = scanner.nextInt();
      } catch(InputMismatchException e) {
          anchorX = -1; anchorY = -1; scanner.nextLine(); /* Skip the values read and make sure to restart the loop */
          System.out.println("# Input invalid, please retry\n");
          Tools.sleep(700);
      }
      anchor = new Point(anchorX, anchorY); /* Creating the point to test the condition */
    } while (!player.quiltboard().canPlacePatch(patch, anchor));
    
    player.quiltboard().placePatch(patch, anchor);
}
  
  /**
   * Asks the player to place the given patch and places it if the choice is valid.
   * Otherwise it will loop until a satisfactory placement position is given.
   * @param player the current player
   * @param patch the given patch
   * @param scanner scanner
   * @param type represents the game mode the player is in (1 == full, 0 == simplified)
   */
  private static void patchPlacementProcedure(Player player, Patch patch,Scanner scanner, int type) {
    placementSelection(player, patch, scanner, type);
    System.out.println("# State of your quiltboard after placing your patch:");
    System.out.println(Displays.displayQuiltBoard(player.quiltboard()));
  }
  
  /**
   * Procedure for placing a chosen patch from the patch circle on the player's quiltboard
   * Updates the player's information according to the procedure
   * @param player current player
   * @param scanner scanner to get input of the users
   * @param chosenPatchIndex the index of the patch chosen by the player
   * @param patchcircle the PatchCircle used during the game
   * @param timeboard the TimeBoard used during the game
   * @param type type of the game (basic = 0 or full = 1)
   */
  private static void chosenPatchPlacementProcedure(Player player, Scanner scanner, int chosenPatchIndex, PatchCircle patchcircle, TimeBoard timeboard, int type) {
    // Fetching the patch from the PatchCircle
    var chosenPatch = patchcircle.choosePatch(chosenPatchIndex);
    System.out.println("## Select the coordinates to place your patch!");
    // keep asking for coordinates until they are good to place the patch
    patchPlacementProcedure(player, chosenPatch, scanner, type);
    
    // Updating the button count after purchase
    player.updateButton(player.button() - chosenPatch.price());
    
    // Updating the button count if the player will cross one or multiple buttons on the timeboard
    // Place special patches if the players picks some up
    timeboardCrossingProcedure(player, timeboard, chosenPatch.time(), scanner);
    
    // Updating the player's location
    player.movePlayer(chosenPatch);
  }

  /**
   * Gets an input from the user and parses the input into an int.
   * That int will be the choice / selection of the user regarding the selection of patches on the patch circle.
   * The user can only chose the first 3 patches in front of the neutral token, ie : input `0` to `2`.
   * If the user chooses to skip the selection, he will need to input `3`.
   * @param player current player
   * @param scanner Scanner
   * @param patchcircle PatchCircle, used to fetch information about the available patches to choose
   * @return an int between 0 and 2 if the user chooses a patch, or 3 if he wishes to skip
   */
  private static int patchSelectionProcedure(Player player, Scanner scanner, PatchCircle patchcircle) {
    var chosenPatchIndex = -1;
    System.out.println("## Select your patch with his id !");
    System.out.println("## Or skip it (enter '3')");
    do {
      chosenPatchIndex = Tools.scannerGetInt(scanner, -1);
    } while(!patchcircle.choiceIsValid(chosenPatchIndex, player.button())); // 0 or 1 or 2 or 3
    return chosenPatchIndex;
  }
  
  /**
   * Function processes all the actions of a given player
   * @param player current player
   * @param opponent current opponent
   * @param scanner scanner
   * @param patchcircle patchcircle
   * @param timeboard timeboard
   * @param type type of the game (basic = 0 or full = 1)
   * @return boolean that describes if player one's token is on the player two's token after the turn
   */
  public static boolean action(Player player, Player opponent, Scanner scanner, PatchCircle patchcircle, TimeBoard timeboard, int type) {
    Objects.requireNonNull(player);
    Objects.requireNonNull(opponent);
    Objects.requireNonNull(scanner);
    Objects.requireNonNull(patchcircle);
    Objects.requireNonNull(timeboard);
    
    if (type > 1 || type < 0)
      throw new IllegalArgumentException("game type invalid");
    
    var chosenPatchIndex = patchSelectionProcedure(player, scanner, patchcircle);
    if (chosenPatchIndex == 3) {
      System.out.println("## You chose not to choose a patch\n");
      skipSelectionProcedure(player, opponent, timeboard, scanner);
      // a player is in front of the other, then player one is not on player two
      return false;
    }
    if (chosenPatchIndex != 3) {
      chosenPatchPlacementProcedure(player, scanner, chosenPatchIndex, patchcircle, timeboard, type);
      if (player.id() == 1 && opponent.location() == player.location())
        return true;
    }
    return false;
  }
}
