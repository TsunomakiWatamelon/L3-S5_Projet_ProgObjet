package fr.uge.patchwork2;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import fr.umlv.zen5.ApplicationContext;

/**
 * From an ApplicationContext, stores informations about the overall size and proportions of graphical elements and some elements themselves.
 * Basically represents the IO of the graphic mode with some game data processing algorithms
 * Only one instance of this class must be used for a game.
 * @author Hervé Nguyen and Gabriel Radoniaina
 *
 */
public class GraphicAlgorithm {
  /*  */
  private final ApplicationContext context;
  private final double width;
  private final double height;
  private final double buttonSize;
  
  /* Not in the constructor just because the constructor would be longer than 20 lines */
  /* Should be final, but it's not in the constructor for the aforementioned reason... */
  private GraphicButton nextButton;
  private GraphicButton previousButton;
  private GraphicButton selectButton;
  private GraphicButton skipButton;
  private GraphicButton quiltButton;
  private GraphicButton flipRButton;
  private GraphicButton flipLButton;
  private GraphicButton mirrorHButton;
  private GraphicButton mirrorVButton;
  private GraphicButton acceptButton;
  
  /* For statistics */
  private final Player player1; 
  private final Player player2;
  
  /* Manipulated game data */
  private final PatchCircle patchcircle;
  private final TimeBoard timeboard;
  private Player player;
  private Player opponent;

  /**
   * Constructor of the GraphicsAlgorithm class, loads data to run the game.
   * @param context ApplicationContext
   * @param player1 the player 1
   * @param player2 the player 2
   * @param patchcircle the patchcircle for the game
   * @param timeboard the timeboard
   */
  public GraphicAlgorithm(ApplicationContext context, Player player1, Player player2, PatchCircle patchcircle, TimeBoard timeboard) {
    this.context = Objects.requireNonNull(context);
    this.player1 = Objects.requireNonNull(player1); player = player1;
    this.player2 = Objects.requireNonNull(player2); opponent = player2;
    this.patchcircle = Objects.requireNonNull(patchcircle);
    this.timeboard = Objects.requireNonNull(timeboard);
    if (context.getScreenInfo().getHeight() <= 0 || context.getScreenInfo().getWidth() <= 0)
      throw new IllegalArgumentException("ApplicationContext have unusable characteristics");
    height = context.getScreenInfo().getHeight();
    width = context.getScreenInfo().getWidth();
    buttonSize = width * 0.035;
    setButtons();
  }
  
  /**
   * Initializes the buttons
   */
  private void setButtons() {
    skipButton = new GraphicButton(context, "Skip", 0.8 * width - buttonSize, 0.7 * height - buttonSize, buttonSize, buttonSize);
    nextButton = new GraphicButton(context, "Next patches", 0.79 * width - 2 * buttonSize, 0.7 * height - buttonSize, buttonSize, buttonSize);
    previousButton = new GraphicButton(context, "Previous patches", 0.78 * width - 3 * buttonSize, 0.7 * height - buttonSize, buttonSize, buttonSize);
    selectButton = new GraphicButton(context, "Select", 0.77 * width - 4 * buttonSize, 0.7 * height - buttonSize, buttonSize, buttonSize);
    quiltButton = new GraphicButton(context, "QuiltBoard", 0.76 * width - 5 * buttonSize, 0.7 * height - buttonSize, buttonSize, buttonSize);
    flipRButton = new GraphicButton(context, "Flip Right", width * 0.7 - buttonSize, height * 0.7 - buttonSize, buttonSize, buttonSize);
    flipLButton = new GraphicButton(context, "Flip Left", width * 0.69 - 2 * buttonSize, height * 0.7 - buttonSize, buttonSize, buttonSize);
    mirrorHButton = new GraphicButton(context, "Horizontal", width * 0.68 - 3 * buttonSize, height * 0.7 - buttonSize, buttonSize, buttonSize);
    mirrorVButton = new GraphicButton(context, "Vertical", width * 0.67 - 4 * buttonSize, height * 0.7 - buttonSize, buttonSize, buttonSize);
    acceptButton = new GraphicButton(context, "Accept", width * 0.66 - 5 * buttonSize, height * 0.7 - buttonSize, buttonSize, buttonSize);
  }
  
  /**
   * Do all necessary actions that happens during one turn for a player.
   * 
   * @param newPlayer Current player who is playing
   * @param newOpponent Opponent
   * @param tokenOneOnTwo Boolean that specifies if the player 1's token is placed ON the player 2's
   * @return tokenOneOnTwo for the next turn
   */
  public boolean action(Player newPlayer, Player newOpponent, boolean tokenOneOnTwo) {
    this.player = Objects.requireNonNull(newPlayer);
    this.opponent = Objects.requireNonNull(newOpponent);
    GraphicElements.clearAll(context);
    var chosenPatch = patchSelectionAlgorithm(player, opponent, patchcircle);
    GraphicElements.clearAll(context);
    if (chosenPatch.isEmpty()) {
      skipSelectionProcedure();
    }
    else {
      chosenPatchPlacementProccessing(chosenPatch.get());
      GraphicElements.clearAll(context);
      if (player.id() == 1 && opponent.location() == player.location())
        return true;
    }
    GraphicElements.clearAll(context);
    return false;
  }
  
  /**
   * Draws a "continue" button and waits the user to press continue
   */
  private void drawAndWaitContinueButton() {
    var continueButton = new GraphicButton(context, "Continue", 0.8 * width - buttonSize, 0.7 * height - buttonSize, buttonSize, buttonSize);
    continueButton.draw();
    for(;;) {
      /* Waiting for a click that in on the continue button */
      if (continueButton.contains(GraphicElements.getClick(context).getLocation())) {
        return;
      }
    }
  }
  
  /**
   * Draws all the necessary information for the start of a turn
   * And waits the player to click on the continue button to continue
   * @param tokenOneOnTwo true if and only if the time token of player 1 is ON player 2's
   */
  public void drawTurnStartInfo(boolean tokenOneOnTwo) {
    var turnInfo = player1.playerTurn(player2, tokenOneOnTwo) ? "It's player 1's turn" : "It's player 2's turn";
    GraphicElements.clearAll(context);
    GraphicElements.drawTimeBoard(context, timeboard, player1, player2, tokenOneOnTwo);
    GraphicElements.drawPlayerInfo(context, player1, player2, player);
    GraphicElements.drawUIBorder(context);
    GraphicElements.drawMessage(context, turnInfo, 1, width * 0.1, height * 0.75, null);
    drawAndWaitContinueButton();
  }
  
  /**
   * Clears side player info window and redraw with current informations
   */
  public void resetInfo() {
    GraphicElements.clearPlayerInfo(context);
    GraphicElements.drawPlayerInfo(context, player1, player2, player);
    GraphicElements.drawUIBorder(context);
  }
  
  /**
   * Shows the state of the quiltboard of the given player
   * And waits until the player clicks on continue (to proceed)
   * @param player The given player
   */
  private void showQuiltboardState(Player player){
    GraphicElements.clearMainScreen(context);
    GraphicElements.drawQuiltBoard(context, player.quiltboard());
    drawAndWaitContinueButton();
  }
  
  /**
   * From a list of GraphicPatch, returns the GraphicPatch located at the position pointed by location
   * If no GrapgicPatch exists there, then returns null.
   * @param graphicTrio In this method, graphicTrio should have at most 3 elements because they represent the patches presented on the screen.
   * @param location location of a click
   * @return the corresponding GraphicPatch or null
   */
  private GraphicPatchStandard getClickedGraphicPatch(ArrayList<GraphicPatchStandard> graphicTrio, Point2D.Float location) {
    if (graphicTrio.size() > 3)
      throw new IllegalArgumentException("Size of graphicTrio > 3");
    return graphicTrio.stream().filter(graphicPatch -> graphicPatch.contains(location)).findFirst().orElse(null); 
  }
  
  /**
   * Draws the button related to the patch selection procedure
   * A select button is drawn when trioNumber == 1 which represents the first trio of the patchcircle.
   * They are the only possibly purchasable (under reservation that the player has enough button to buy them).
   * @param trioNumber nth trio of the patch circle
   */
  private void drawPatchSelectionButtons(int trioNumber) {
    if (trioNumber == 1) {
      selectButton.draw();
    }
    nextButton.draw();
    previousButton.draw();
    quiltButton.draw();
    skipButton.draw();
  }
  
  /**
   * From a list of at most 3 patches, add the corresponding GraphicPatch to the given graphicTrio ArrayList
   * Other parameters fed to the GraphicPatch constructor represents the location and size of the new GraphicPatch on the window
   * @param graphicTrio Aforementioned list to be added with at most 3 GraphicPatch
   * @param trio List of at most 3 patches
   */
  private void loadGraphicTrio(ArrayList<GraphicPatchStandard> graphicTrio, List<Patch> trio) {
    for (var i = 0; i < trio.size(); i++) {
      graphicTrio.add(new GraphicPatchStandard(context, trio.get(i), (i * width * 0.28 + width * 0.05), height * 0.3));
    }
  }
  
  /**
   * Procedure that happens when the player tries to choose a patch during the buying phase
   * 
   * If the given click location corresponds to a drawn graphic patch, draws the corresponding information of that patch.
   * 
   * If the players ends up selecting that patch (which means he is buying it). Then the method will return the Optional<Patch> of that patch.
   * 
   * Otherwise, in all other cases, will return Optional.empty()
   * @param location location of a click
   * @param player the player
   * @param graphicTrio
   * @param patchcircle
   * @param trioNumber
   * @return
   */
  private Optional<Patch> clickOnPatchProcedure(Point2D.Float location, Player player, ArrayList<GraphicPatchStandard> graphicTrio, PatchCircle patchcircle, int trioNumber) {
    var selected = getClickedGraphicPatch(graphicTrio, location);
    
    if (selected != null) { // If the click really corresponds to a drawn patch
      GraphicElements.drawPatchInfo(context, selected.patch(), player.button());
      var click = GraphicElements.getClick(context).getLocation();
      
      if (trioNumber == 1 && selectButton.contains(click)) { // if the player tries to buy a "buyable" patch
        if (player.graphicCanBuyPatch(selected.patch())) {
          return Optional.of(patchcircle.choosePatch(selected.patch()));
        }
      }
    }
    return Optional.empty();
  }
  
  /**
   * Algorithm during the patch selection (buy) phase/procedure.
   * 
   * Allows the player to browse through the patchcircle and view the properties of the patches.
   * View his quiltboard, and buy a patch or skip buying.
   * 
   * Patches are drawn in trio (at most) at a time. Which requires to load one trio of patch from the patchcircle per loop.
   * 
   * @param player The playing player
   * @param opponent The opponent of the playing player
   * @param player1 Player 1
   * @param player2 Player 2
   * @param patchcircle The patchcircle
   * @return Optional that would contain the patch bought or Optional.empty() if none were bought.
   */
  private Optional<Patch> patchSelectionAlgorithm(Player player, Player opponent, PatchCircle patchcircle) {
    var trioNumber = 1;
    for(;;) {
      var trio = patchcircle.getTrio(trioNumber);
      var graphicTrio = new ArrayList<GraphicPatchStandard>();
      GraphicElements.refreshUI(context, player1, player2, player);
      drawPatchSelectionButtons(trioNumber);
      loadGraphicTrio(graphicTrio, trio); /* Loading the patch trio into GraphicPatch to draw them */
      graphicTrio.stream().forEach(graphicPatch -> graphicPatch.draw());
      Point2D.Float location = GraphicElements.getClick(context).getLocation();
      var selectedPatch = clickOnPatchProcedure(location, player, graphicTrio, patchcircle, trioNumber); /* Check if the player clicked on the patch, and chose it */
      if (selectedPatch.isPresent())
        return selectedPatch; /* if the player ends up selecting the patch */
      /* Checking if the player tried to click one of the buttons */
      else if (nextButton.contains(location) && !patchcircle.getTrio(trioNumber + 1).isEmpty())   trioNumber++;
      else if (previousButton.contains(location) && trioNumber > 1)                               trioNumber--;
      else if (skipButton.contains(location))                                                     break;
      else if (quiltButton.contains(location))                                                    showQuiltboardState(player);
    }
    return Optional.empty();
  }
  
  /**
   * Draws all the buttons requirement for the patch placement procedure
   */
  private void drawPatchPlacementButtons() {
    flipRButton.draw();
    flipLButton.draw();
    mirrorHButton.draw();
    mirrorVButton.draw();
    acceptButton.draw();
  }
  
  /**
   * Checks if the given click location corresponds to any of the buttons (flip right, flip left, mirror horizontal, mirror vertical)
   * @param click location of the click
   * @return true if the click correspond to one of these buttons, otherwise false
   */
  private boolean clickedOnModifyPatch(Point2D.Float click) {
    return (    flipRButton.contains(click)     
            ||  flipLButton.contains(click)
            ||  mirrorHButton.contains(click)
            ||  mirrorVButton.contains(click));
  }
  
  /**
   * Assuming the given click location corresponds to one of the buttons (flip right, flip left, mirror horizontal, mirror vertical)
   * Will perform the corresponding action of the given patch and will return the resulting patch.
   * 
   * Will throw an exception if the click did not correspond to one of the 4 buttons
   * @param patch the given patch to modify
   * @param click location of the click
   * @return the resulting patch
   */
  private Patch getModifiedPatch(Patch patch, Point2D.Float click) {
    if (flipRButton.contains(click)) {
      
      return patch.flip(1);
    }
    else if (flipLButton.contains(click)) {
      return patch.flip(0);
    }
    else if (mirrorHButton.contains(click)){
      return patch.mirror(0);
    }
    else if (mirrorVButton.contains(click)){
      return patch.mirror(1);
    }
    throw new IllegalArgumentException("Click does not belong to any button");
  }
  
  /**
   * Draws the UI of the patch placement procedure apart of the buttons.
   * 
   * Draws the quiltboard of the player and the patch the player is trying to place.
   * 
   * This function does not contain the buttons for the sole reason that the given patch here can change because the player
   * can flip it / mirror it.
   * 
   * Which means that this method should be called at the start of every iteration of the loop in patchPlacementProcedure.
   * 
   * @param quiltboard The quiltboard
   * @param patch the patch to be placed
   */
  private void drawPatchPlacementProcedureUI(QuiltBoard quiltboard, Patch patch) {
    GraphicElements.clearBottomBar(context);
    GraphicElements.clearQuiltBoard(context);
    GraphicElements.drawUIBorder(context);
    GraphicElements.drawQuiltBoard(context, quiltboard);
    GraphicElements.clearPlacingPatch(context, Math.max(0.16 * height, 0.11 * width));
    resetInfo();
    new GraphicPatchStandard(context, patch, width * 0.57, height * 0.25).draw();
  }
  
  /**
   * Algorithm during the patch placement phase.
   * 
   * Loops until the player finally places the patch (he can modify it according to the rules).
   * 
   * @param patch patch to be placed
   * @param quiltboard the quiltboard of the player who's currently playing
   */
  private void patchPlacementProcedure(Patch patch, QuiltBoard quiltboard) {
    var localpatch = patch; var anchor = new Point(-1, -1); drawPatchPlacementButtons();
    for(;;) {
      drawPatchPlacementProcedureUI(quiltboard, localpatch);
      if (!anchor.equals(new Point(-1, -1))) new GraphicPatchPlaced(context, localpatch, anchor).draw(); /* draw the patch on the quiltboard if there is a valid anchor point */
      var click = GraphicElements.getClick(context).getLocation();
      if (clickedOnModifyPatch(click)) {
        localpatch = getModifiedPatch(localpatch, click);
        anchor.setLocation(-1, -1);
      }
      else if (acceptButton.contains(click) && !anchor.equals(new Point(-1, -1))) {
        quiltboard.placePatch(localpatch, anchor);
        break;
      }
      else if (GraphicElements.isClickOnQuiltBoard(context, click)) {
        anchor.setLocation(-1, -1);
        if (quiltboard.canPlacePatch(localpatch, GraphicElements.getAnchorClickPosition(context, click)))
          anchor = GraphicElements.getAnchorClickPosition(context, click);
      }
    }
  }
  
  /**
   * Does all the data processing for placing the patch on the current player's quiltboard and the consequences :
   * - The IO for placing the patch on the player's quiltboard
   * - Updating the button count of the player
   * - Processing the consequences of moving the player on the timeboard according to the time value of the patch
   * - Move the player on the timeboard
   * @param player The playing player
   * @param patch The patch to be placed
   * @param timeboard The timeboard
   */
  private void chosenPatchPlacementProccessing(Patch patch) {
    player.updateButton(player.button() - patch.price());
    patchPlacementProcedure(patch, player.quiltboard());
    timeboardCrossingProcedure(player.getTileCrossedChosePatch(patch));
    player.movePlayer(patch);
  }
  
  /**
   * According to the given TimeBoardElement, do the appropriate action
   * @param player The current player
   * @param element The TimeBoardElement that is being encountered
   */
  private void processTimeBoardElement(TimeBoardElement element) {
    GraphicElements.clearMainScreen(context);
    GraphicElements.drawUIBorder(context);
    GraphicElements.clearBottomBar(context);
    resetInfo();
    if (element == TimeBoardElement.button) {
      var added = player.quiltboard().buttons();
      player.updateButton(player.button() + added);
      resetInfo();
      GraphicElements.drawBottomMessage(context, "You have crossed a button and have been awarded with " + added + " buttons", 0.8, 0);
      GraphicElements.drawBottomMessage(context, "This is the total number of buttons on the patches on your quiltboard", 0.8, 1);
      drawAndWaitContinueButton();
    }
    if (element == TimeBoardElement.specialPatch) {
      GraphicElements.drawBottomMessage(context, "You are the first player to cross the special patch, you will now need to place it.", 0.8, 0);
      drawAndWaitContinueButton();
      GraphicElements.clearMainScreen(context);
      patchPlacementProcedure(Patch.specialPatch(), player.quiltboard());
    }
  }
  
  /**
   * Tries to process all the TimeBoardElement the player is coming across during his move.
   * Fetches the list of TimeBoardElement met uses a stream to process them.
   * @param player Player who's currently playing
   * @param timeboard the timeboard
   * @param distance distance traveled
   */
  private void timeboardCrossingProcedure(int distance) {
    if (distance <= 0)
      throw new IllegalArgumentException("distance <= 0");
    
    var crossedElements = timeboard.elementCrossed(player.location(), player.getDestinationIndex(distance));
    
    crossedElements.stream().forEach(element -> processTimeBoardElement(element));
  }
  

  /**
   * Processes all necessary actions that happens when the current player refrain from buying a patch
   * 
   * @param player The current player who's playing
   * @param opponent The opponent
   * @param timeboard The timeboard
   */
  private void skipSelectionProcedure() {
    GraphicElements.clearAll(context);
    var tilesCrossed = player.getTileCrossedSelectionSkip(opponent);
    timeboardCrossingProcedure(tilesCrossed);
    player.skipSelectionUpdateLocation(opponent);
    GraphicElements.clearAll(context);
    GraphicElements.drawUIBorder(context);
    player.updateButton(player.button() + tilesCrossed);
    GraphicElements.drawTimeBoard(context, timeboard, player1, player2, false);
    GraphicElements.drawPlayerInfo(context, player1, player2, player);
    GraphicElements.drawBottomMessage(context, "You have crossed " + tilesCrossed + " tiles on the timeboard and have been awarded with the same amount", 0.8, 0);
    drawAndWaitContinueButton();
  }

  
}
