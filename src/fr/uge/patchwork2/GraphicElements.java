package fr.uge.patchwork2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Objects;

import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.Event;

/**
 * Contains multiple graphical functions using zen5 to draw numerous elements related to the game patchwork
 * @author Herve Nguyen and Gabriel Radoniaina
 *
 */
public class GraphicElements {
  
  /**
   * Clears a specified area
   * @param context ApplicationContext
   * @param x x coordinate of the top left corner of the area to clear
   * @param y y coordinate of the top left corner of the area to clear
   * @param width width of the area to clear
   * @param height height of the area to clear
   */
  private static void clear(ApplicationContext context, double x, double y, double width, double height) {
    if (x < 0 || y < 0 || width <= 0 || height <= 0)
      throw new IllegalArgumentException("Invalid area data");
    
    context.renderFrame(graphics -> {
      graphics.setColor(Color.WHITE);
      var area = new Rectangle2D.Double(x, y, width, height);
      graphics.fill(area);
    });
  }
  
  /**
   * Clears the whole screen
   * @param context ApplicationContext
   */
  public static void clearAll(ApplicationContext context) {
    Objects.requireNonNull(context);
    if (context.getScreenInfo().getHeight() <= 0 || context.getScreenInfo().getWidth() <= 0)
      throw new IllegalArgumentException("ApplicationContext have unusable characteristics");
    
    clear(context, 0, 0, context.getScreenInfo().getWidth(), context.getScreenInfo().getHeight());
  }
  
  /**
   * Clears the bottom window (bottom left)
   * @param context ApplicationContext
   */
  public static void clearBottomBar(ApplicationContext context) {
    Objects.requireNonNull(context);
    if (context.getScreenInfo().getHeight() <= 0 || context.getScreenInfo().getWidth() <= 0)
      throw new IllegalArgumentException("ApplicationContext have unusable characteristics");
    
    var height = context.getScreenInfo().getHeight();
    var width = context.getScreenInfo().getWidth();
    clear(context,
          0,
          height * 0.701,
          width * 0.798,
          height * 0.298);
  }
  
  /**
   * Clears the main window (Top left, largest space)
   * @param context ApplicationContext
   */
  public static void clearMainScreen(ApplicationContext context) {
    Objects.requireNonNull(context);
    if (context.getScreenInfo().getHeight() <= 0 || context.getScreenInfo().getWidth() <= 0)
      throw new IllegalArgumentException("ApplicationContext have unusable characteristics");
    
    var height = context.getScreenInfo().getHeight();
    var width = context.getScreenInfo().getWidth();
    GraphicElements.clear(context,
                          0,
                          0,
                          width * 0.798,
                          height * 0.698);
  }
  
  /**
   * Clears the space occupied by the illustration of the patch to be placed
   * @param context ApplicationContext
   * @param patchLimit limit of the size (surface, height and width) of the drawing
   */
  public static void clearPlacingPatch(ApplicationContext context, double patchLimit) {
    Objects.requireNonNull(context);
    if (context.getScreenInfo().getHeight() <= 0 || context.getScreenInfo().getWidth() <= 0)
      throw new IllegalArgumentException("ApplicationContext have unusable characteristics");
    if (patchLimit <= 0)
      throw new IllegalArgumentException("patchLimit cannot be <= 0");
    
    var height = context.getScreenInfo().getHeight();
    var width = context.getScreenInfo().getWidth();
    GraphicElements.clear(context,
                          width * 0.56,
                          height * 0.24,
                          patchLimit,
                          patchLimit);
  }
  
  /**
   * Clears the side player info window (Top right)
   * @param context ApplicationContext
   */
  public static void clearPlayerInfo(ApplicationContext context) {
    Objects.requireNonNull(context);
    if (context.getScreenInfo().getHeight() <= 0 || context.getScreenInfo().getWidth() <= 0)
      throw new IllegalArgumentException("ApplicationContext have unusable characteristics");
    
    var height = context.getScreenInfo().getHeight();
    var width = context.getScreenInfo().getWidth();
    GraphicElements.clear(context,
                          width * 0.801,
                          0,
                          width * 0.199,
                          height * 699);
  }
  
  /**
   * Clear the space occupied by a quiltboard on the screen
   * @param context ApplicationContext
   */
  public static void clearQuiltBoard(ApplicationContext context) {
    Objects.requireNonNull(context);
    if (context.getScreenInfo().getHeight() <= 0 || context.getScreenInfo().getWidth() <= 0)
      throw new IllegalArgumentException("ApplicationContext have unusable characteristics");
    
    var height = context.getScreenInfo().getHeight();
    GraphicElements.clear(context, height * 0.01, height * 0.01, height * 0.68, height * 0.68);
  }
  
  /**
   * Draws a square at the specified location with the given characteristics
   * @param context ApplicationContext
   * @param size size of the square
   * @param x x coordinate of the top left corner
   * @param y y coordinate of the top left corner
   * @param color color of the square
   */
  private static void drawSquare(ApplicationContext context, double size, double x, double y, Color color) {
    context.renderFrame(graphics -> {
      graphics.setColor(color);
      var square = new  Rectangle2D.Double(x, y, size, size);
      graphics.fill(square);
    });
  }
  
  /**
   * Draws a circle at the specified location with the given characteristics
   * @param context ApplicationContext
   * @param size size of the square
   * @param x x coordinate of the center
   * @param y y coordinate of the center
   * @param color color of the circle
   */
  private static void drawCircle(ApplicationContext context, double size, double x, double y, Color color) {
    context.renderFrame(graphics -> {
      graphics.setColor(color);
      var circle = new Ellipse2D.Double(x, y, size, size);
      graphics.fill(circle);
    });
  }
  
  /**
   * Draws a line at the specified location with the given characteristics
   * @param context ApplicationContext
   * @param size size of the square
   * @param x1 x coordinate of the first extremity
   * @param y1 y coordinate of the first extremity
   * @param x2 x coordinate of the second extremity
   * @param y2 y coordinate of the second extremity
   * @param color color of the line
   */
  private static void drawLine(ApplicationContext context, double x1, double y1, double x2, double y2, Color color) {
    context.renderFrame(graphics -> {
      graphics.setColor(color);
      graphics.setStroke(new BasicStroke(3f));
      var line = new Line2D.Double(x1, y1, x2, y2);
      graphics.draw(line);
    });
  }
  
  /**
   * Draws a fine line at the specified location with the given characteristics
   * @param context ApplicationContext
   * @param size size of the square
   * @param x1 x coordinate of the first extremity
   * @param y1 y coordinate of the first extremity
   * @param x2 x coordinate of the second extremity
   * @param y2 y coordinate of the second extremity
   * @param color color of the line
   */
  private static void drawFineLine(ApplicationContext context, double x1, double y1, double x2, double y2, Color color) {
    context.renderFrame(graphics -> {
      graphics.setColor(color);
      graphics.setStroke(new BasicStroke(1f));
      var line = new Line2D.Double(x1, y1, x2, y2);
      graphics.draw(line);
    });
  }
  
  /**
   * Draws the boundaries of the UI to separate multiples parts
   * @param context ApplicationContext
   */
  public static void drawUIBorder(ApplicationContext context) {
    Objects.requireNonNull(context);
    if (context.getScreenInfo().getHeight() <= 0 || context.getScreenInfo().getWidth() <= 0)
      throw new IllegalArgumentException("ApplicationContext have unusable characteristics");
    
    var height = context.getScreenInfo().getHeight();
    var width = context.getScreenInfo().getWidth();
    drawLine(context, 0, 0.7 * height, width, 0.7 * height, Color.BLACK);
    drawLine(context, 0.8 * width, 0, 0.8 * width, height, Color.BLACK);
  }
  
  /**
   * Draws the TimeBoard and it's elements (and time tokens)
   * @param context ApplicationContext
   * @param timeboard the timeboard
   * @param player1 player1
   * @param player2 player2
   * @param tokenOneOnTwo tokenOneOnTwo
   */
  public static void drawTimeBoard(ApplicationContext context, TimeBoard timeboard, Player player1, Player player2, boolean tokenOneOnTwo) {
    Objects.requireNonNull(context);
    if (context.getScreenInfo().getHeight() <= 0 || context.getScreenInfo().getWidth() <= 0)
      throw new IllegalArgumentException("ApplicationContext have unusable characteristics");
    
    Objects.requireNonNull(timeboard);
    Objects.requireNonNull(player1);
    Objects.requireNonNull(player2);
    drawTimeBoardPath(context);
    drawTimeBoardTiles(context);
    drawTimeBoardElements(context, timeboard);
    drawTimeToken(context, player1, player2, tokenOneOnTwo);
  }
  
  
  /**
   * Draws the path of the TimeBoard
   * @param context ApplicationContext
   */
  private static void drawTimeBoardPath(ApplicationContext context) {
    var height = context.getScreenInfo().getHeight();
    var width = context.getScreenInfo().getWidth();
    var size = 0.07 * height;
    var oX = 0.10 * width;
    var oY = 0.15 * height;
    drawLine(context, oX, oY, oX + 9 * size, oY, Color.BLACK);
    drawLine(context, oX, oY + size, oX + 8 * size, oY + size, Color.BLACK);
    drawLine(context, oX + 9 * size, oY, oX + 9 * size, oY + 6 * size, Color.BLACK);
    drawLine(context, oX + 8 * size, oY + size, oX + 8 * size, oY + 5 * size, Color.BLACK);
    drawLine(context, oX + 9 * size, oY + 6 * size, oX, oY + 6 * size, Color.BLACK);
    drawLine(context, oX + 8 * size, oY + 5 * size, oX + size, oY + 5 * size, Color.BLACK);
    drawLine(context, oX, oY + 6 * size, oX, oY + size, Color.BLACK);
    drawLine(context, oX + size, oY + 5 * size, oX + size, oY + 2 * size, Color.BLACK);
    drawLine(context, oX + size, oY + 2 * size, oX + 7 * size, oY + 2 * size, Color.BLACK);
    drawLine(context, oX + 7 * size, oY + 2 * size, oX + 7 * size, oY + 4 * size, Color.BLACK);
    drawLine(context, oX + 7 * size, oY + 4 * size, oX + 2 * size, oY + 4 * size, Color.BLACK);
    drawLine(context, oX + 2 * size, oY + 4 * size, oX + 2 * size, oY + 3 * size, Color.BLACK);
    drawLine(context, oX + 2 * size, oY + 3 * size, oX + 6 * size, oY + 3 * size, Color.BLACK);
  }
  
  /**
   * Draws the tiles of the TimeBoard (the grid)
   * @param context ApplicationContext
   */
  private static void drawTimeBoardTiles(ApplicationContext context) {
    var height = context.getScreenInfo().getHeight();
    var width = context.getScreenInfo().getWidth();
    var size = 0.07 * height;
    var oX = 0.10 * width;
    var oY = 0.15 * height;
    for (var i = 0; i < 8; i++) {
      drawFineLine(context, oX + (i + 1) * size, oY, oX + (i + 1) * size, oY + 6 * size, Color.BLACK);
    }
    for (var i = 0; i < 5; i++) {
      drawFineLine(context, oX, oY + (i + 1) * size, oX + 9 * size, oY + (i + 1) * size, Color.BLACK);
    }
  }
  
  /**
   * Draws the elements of the timeboard (specialpatches, buttons)
   * @param context ApplicationContext
   * @param timeboard the timeboard
   */
  private static void drawTimeBoardElements(ApplicationContext context, TimeBoard timeboard) {
    var height = context.getScreenInfo().getHeight();
    var width = context.getScreenInfo().getWidth();
    var tileSize = 0.07 * height;
    var originX = 0.10 * width; var originY = 0.15 * height;
    var xOffset = 0.05 * height; var yOffset = 0.01 * height;
    var elementSize = 0.018 * height;
    var path = timeboard.path();
    Point point;
    for (var i = 0; i < TimeBoard.getSIZE(); i++) {
      point = TimeBoard.path2DIndex(i);
      if (path[i] == TimeBoardElement.button)
        drawCircle(context, elementSize, originX + point.x * tileSize + xOffset, originY + point.y * tileSize + yOffset, Color.blue);
      if (path[i] == TimeBoardElement.specialPatch)
        drawSquare(context, elementSize, originX + point.x * tileSize + xOffset, originY + point.y * tileSize + yOffset, Color.orange);
    }
  }
  
  /**
   * Draws the time tokens of the two players
   * It also checks if they are are at the same position, if that is the case then
   * it will figure out which time token in on top of the other to determine which time token to show first
   * @param context ApplicationContext
   * @param player1 player1
   * @param player2 player2
   * @param tokenOneOnTwo tokenOneOnTwo
   */
  private static void drawTimeToken(ApplicationContext context, Player player1, Player player2, boolean tokenOneOnTwo) {
    var point1 = TimeBoard.path2DIndex(player1.location()); var point2 = TimeBoard.path2DIndex(player2.location());
    if (player1.location() == player2.location()) {
      if (!tokenOneOnTwo) {
        drawTimeTokenAux(context, point1, Color.CYAN, 0);
        drawTimeTokenAux(context, point2, Color.MAGENTA, 0.01);
        return;
      }
    }
    drawTimeTokenAux(context, point2, Color.MAGENTA, 0);
    drawTimeTokenAux(context, point1, Color.CYAN, 0.01);
  }
  
  /**
   * Simply draws the time token at the specified position and which the specified characteristics
   * @param context ApplicationContext
   * @param location the position of the time token in the 2D array of the timeboard
   * @param color Color of the time token
   */
  private static void drawTimeTokenAux(ApplicationContext context, Point location, Color color, double offset) {
    var height = context.getScreenInfo().getHeight();
    var width = context.getScreenInfo().getWidth();
    var oX = 0.10 * width;
    var oY = 0.16 * height;
    offset = offset * height;
    drawCircle(context, 0.045 * height, oX + location.x * (0.07 * height) + offset, oY + location.y * (0.07 * height), color);
  }
  
  /**
   * Draws a message at the specified location with the given parameters
   * @param context ApplicationContext
   * @param message the message
   * @param size size of the message
   * @param x x coordinate of the message
   * @param y y coordinate of the message
   * @param color color
   */
  public static void drawMessage(ApplicationContext context, String message, double size, double x, double y, Color color) {
    Objects.requireNonNull(message);
    Objects.requireNonNull(context);
    if (context.getScreenInfo().getHeight() <= 0 || context.getScreenInfo().getWidth() <= 0)
      throw new IllegalArgumentException("ApplicationContext have unusable characteristics");
    
    var height = context.getScreenInfo().getHeight();
    var width = context.getScreenInfo().getWidth();
    if (width < x || x < 0 || y < 0 || height < y)
      throw new IllegalArgumentException("Coordinates out of bounds");
    if (size <= 0)
      throw new IllegalArgumentException("Invalid size");
    int fontSize = (int) (Math.min(width, height) * 0.03 * size);
    context.renderFrame(graphics -> {
      graphics.setColor(color);
      graphics.setFont(new Font("Arial", Font.BOLD, fontSize));
      graphics.drawString(message, (int)x, (int)y);
    });
  }
  
  /**
   * Draws a message at the bottom window of the screen
   * 
   * The size and row can be specified
   * 
   * @param context ApplicationContext
   * @param message the message
   * @param size size of the text (coefficient)
   * @param row row of the text
   */
  public static void drawBottomMessage(ApplicationContext context, String message, double size, int row) {
    Objects.requireNonNull(message);
    Objects.requireNonNull(context);
    if (context.getScreenInfo().getHeight() <= 0 || context.getScreenInfo().getWidth() <= 0)
      throw new IllegalArgumentException("ApplicationContext have unusable characteristics");
    if (size <= 0)
      throw new IllegalArgumentException("Size cannot be <= 0");
    if (row < 0){
      throw new IllegalArgumentException("Row cannot be < 0");
    }
    var width = context.getScreenInfo().getWidth();
    var height = context.getScreenInfo().getHeight();
    drawMessage(context, message, size, width * 0.01, height * 0.74 + (0.05 * height + 0.01) * row, Color.BLACK);
  }
  
  /**
   * Draws the information about the current players during the game
   * @param context ApplicationContext
   * @param player1 player1
   * @param player2 player2
   * @param player current player
   */
  public static void drawPlayerInfo(ApplicationContext context, Player player1, Player player2, Player player) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(player1);
    Objects.requireNonNull(player2);
    Objects.requireNonNull(player);
    if (context.getScreenInfo().getHeight() <= 0 || context.getScreenInfo().getWidth() <= 0)
      throw new IllegalArgumentException("ApplicationContext have unusable characteristics");
    
    drawPlayerName(context, player1, player2);
    drawPlayerStats(context, player1, player2);
    drawWhoIsPlaying(context, player);
  }
  
  /**
   * Draw the name of the players on the appropriate space
   * @param context ApplicationContext
   * @param player1 player1
   * @param player2 player2
   */
  private static void drawPlayerName(ApplicationContext context, Player player1, Player player2) {
    var height = context.getScreenInfo().getHeight();
    var width = context.getScreenInfo().getWidth();
    drawMessage(context, "Player 1", 0.8, width * 0.82, 0.05 * height, Color.BLACK);
    drawMessage(context, "Player 2", 0.8, width * 0.82, 0.35 * height, Color.BLACK);
    drawCircle(context, 0.045 * height, 0.95 * width, 0.05 * height, Color.CYAN);
    drawCircle(context, 0.045 * height, 0.95 * width, 0.35 * height, Color.MAGENTA);
  }
  
  /**
   * Draws the statistics of the players
   * @param context ApplicationContext
   * @param player1 player1
   * @param player2 player2
   */
  private static void drawPlayerStats(ApplicationContext context, Player player1, Player player2) {
    drawPlayerStatsAux(context, player1, 0);
    drawPlayerStatsAux(context, player2, 0.3);
  }
  
  /**
   * Draws the statistics of a given player
   * @param context ApplicationContext
   * @param player player
   * @param offset offset for the position so each player's stats may not be overlapping
   */
  private static void drawPlayerStatsAux(ApplicationContext context, Player player, double offset) {
    var height = context.getScreenInfo().getHeight();
    var width = context.getScreenInfo().getWidth();
    String pspecialTile = (player.specialTile()) ? "yes" : "no";
    drawMessage(context, "Buttons : " + player.button(), 0.6, width * 0.83, (0.08 + offset) * height, Color.BLACK);
    drawMessage(context, "Quitboard Buttons : " + player.quiltboard().buttons(), 0.6, width * 0.83, (0.10 + offset) * height, Color.BLACK);
    drawMessage(context, "Special Tile : " + pspecialTile, 0.6, width * 0.83, (0.12 + offset) * height, Color.BLACK);
    drawMessage(context, "Position in the timeboard : " + player.location(), 0.6, width * 0.83, (0.14 + offset) * height, Color.BLACK);
    drawMessage(context, "Patches in the quiltboard : " + player.quiltboard().nbPatchPlaced(), 0.6, width * 0.83, (0.16 + offset) * height , Color.BLACK);
    drawMessage(context, "Special patches : " + player.specialPatches(), 0.6, width * 0.83, (0.18 + offset) * height, Color.BLACK);
    drawMessage(context, "Score : " + player.score(), 0.7, width * 0.83, (0.22 + offset) * height, Color.BLACK);
  }
  
  /**
   * Draws a message alongside the player that states who is playing
   * @param context ApplicationContext
   * @param player the playing player
   */
  private static void drawWhoIsPlaying(ApplicationContext context, Player player) {
    var height = context.getScreenInfo().getHeight();
    var width = context.getScreenInfo().getWidth();
    var offset = (player.id() == 1) ? 0.0 : 0.3;
    drawMessage(context, "PLAYING", 0.7, width * 0.89, (0.05 + offset) * height, Color.RED);
  }
  
  /**
   * Draws the end message to announce the winner or the two losers and their scores...
   * @param context ApplicationContext
   * @param player1 Player 1
   * @param player2 Player 2
   */
  public static void drawEndMessage(ApplicationContext context, Player player1, Player player2) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(player1);
    Objects.requireNonNull(player2);
    if (context.getScreenInfo().getHeight() <= 0 || context.getScreenInfo().getWidth() <= 0)
      throw new IllegalArgumentException("ApplicationContext have unusable characteristics");
    if (!player1.finished() || !player2.finished())
      throw new IllegalArgumentException("Players haven't finished");
    if (player1.score() == player2.score())
      drawBottomMessage(context, "Both player have lost, no TIES. Losers.", 0.9, 0);
    else if (player1.score() > player2.score())
      drawBottomMessage(context, "Player 1 has won the game", 0.9, 0);
    else
      drawBottomMessage(context, "Player 2 has won the game", 0.9, 0);
    drawBottomMessage(context, "Player 1 Score : " + player1.score(), 0.9, 1);
    drawBottomMessage(context, "Player 2 Score : " + player2.score(), 0.9, 2);
  }
  
  /**
   * Refreshes the UI with updated info
   * @param context ApplicationContext
   * @param player1 Player 1
   * @param player2 Player 2
   * @param player Player who's currently playing
   */
  public static void refreshUI(ApplicationContext context, Player player1, Player player2, Player player) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(player1);
    Objects.requireNonNull(player2);
    Objects.requireNonNull(player);
    if (context.getScreenInfo().getHeight() <= 0 || context.getScreenInfo().getWidth() <= 0)
      throw new IllegalArgumentException("ApplicationContext have unusable characteristics");
    GraphicElements.clearAll(context);
    GraphicElements.drawPlayerInfo(context, player1, player2, player);
    GraphicElements.drawUIBorder(context);
  }
  
  
  /**
   * Draws the info of the given patch (This method should be used in context of the patch selection procedure)
   * @param context ApplicationContext
   * @param patch the patch
   * @param buttonOwned button owned by the player
   */
  public static void drawPatchInfo(ApplicationContext context, Patch patch, int buttonOwned) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(patch);
    if (context.getScreenInfo().getHeight() <= 0 || context.getScreenInfo().getWidth() <= 0)
      throw new IllegalArgumentException("ApplicationContext have unusable characteristics");
    if (buttonOwned < 0)
      throw new IllegalArgumentException("Buttonowned cannot be < 0");
    
    var height = context.getScreenInfo().getHeight();
    var width = context.getScreenInfo().getWidth();
    var canBuy = (buttonOwned >= patch.price()) ? "YES" : "NO";
    clearBottomBar(context);
    drawMessage(context, "Selected patch specifications :", 0.85, width * 0.1, height * 0.72, Color.BLACK);
    drawMessage(context, "- Buttons : " + patch.buttons(), 0.8, width * 0.12, height * 0.76, Color.BLACK);
    drawMessage(context, "- Price : " + patch.price(), 0.8, width * 0.12, height * 0.78, Color.BLACK);
    drawMessage(context, "- Time : " + patch.time(), 0.8, width * 0.12, height * 0.8, Color.BLACK);
    drawMessage(context, "Can buy the patch : " + canBuy, 0.8, width * 0.12, height * 0.82, Color.RED);
  }
  
  /**
   * Waits until a new click is registered (only pointer down)
   * @param context ApplicationContext
   * @return the click event
   */
  public static Event getClick(ApplicationContext context) {
    Objects.requireNonNull(context);
    if (context.getScreenInfo().getHeight() <= 0 || context.getScreenInfo().getWidth() <= 0)
      throw new IllegalArgumentException("ApplicationContext have unusable characteristics");
    Event event;
    do {
      event = context.pollOrWaitEvent(1000);
    } while(event == null || !event.getAction().equals(Event.Action.POINTER_DOWN));
    return event;
  }
  
  /**
   * Draws the grid of the quiltboard
   * @param context ApplicationContext
   */
  private static void drawQuiltBoardGrid(ApplicationContext context) {
    var height = context.getScreenInfo().getHeight();
    var blockSize = (context.getScreenInfo().getHeight() * 0.68) / 9;
    for (var i = 0; i < 10; i++)
      drawFineLine(context, height * 0.01 + i * blockSize, height * 0.01, height * 0.01 + i * blockSize, height * 0.69, Color.BLACK);
    for (var i = 0; i < 10; i++)
      drawFineLine(context, height * 0.01, height * 0.01 + i * blockSize, height * 0.69, height * 0.01 + i * blockSize, Color.BLACK);
  }
  
  /**
   * Draw the patches placed on the quiltboard the specified position
   * @param context ApplicationContext
   * @param patchplaced ArrayList of "PatchPlaced" which are patches associated with an anchor point (placement)
   */
  private static void drawPatchQuiltBoard(ApplicationContext context, ArrayList<PatchPlaced> patchplaced) {
    patchplaced.forEach(pp -> {
      new GraphicPatchPlaced(context, pp.patch(), pp.anchor()).draw();
    });
  }
  
  /**
   * Checks if the given click coordinates corresponds to a click on the quiltboard
   * @param context ApplicationContext
   * @param click the coordinates of the click
   * @return boolean, true if it is on the quiltboard, otherwise false.
   */
  public static boolean isClickOnQuiltBoard(ApplicationContext context, Point2D.Float click) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(click);
    if (context.getScreenInfo().getHeight() <= 0 || context.getScreenInfo().getWidth() <= 0)
      throw new IllegalArgumentException("ApplicationContext have unusable characteristics");
    
    var height = context.getScreenInfo().getHeight();
    var blockSize = (height * 0.68) / 9;
    var offset = height * 0.01;
    var xGrid = (int)((click.x - offset) / blockSize);
    var yGrid = (int)((click.y - offset) / blockSize);
    return ((0 <= xGrid && xGrid < 9) && (0 <= yGrid && yGrid < 9));
  }
  
  /**
   * Assuming the given click coordinates corresponds to a click on the quiltboard.
   * Returns the point that represents the square of the quiltboard that was clicked on.
   * @param context ApplicationContext
   * @param click the coordinates of the click
   * @return the aforementioned point
   */
  public static Point getAnchorClickPosition(ApplicationContext context, Point2D.Float click) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(click);
    if (context.getScreenInfo().getHeight() <= 0 || context.getScreenInfo().getWidth() <= 0)
      throw new IllegalArgumentException("ApplicationContext have unusable characteristics");
    
    Point anchor;
    if (!isClickOnQuiltBoard(context, click))
      throw new IllegalArgumentException("Given click is not on the quiltboard");
    
    var height = context.getScreenInfo().getHeight();
    var blockSize = (height * 0.68) / 9;
    var offset = height * 0.01;
    anchor = new Point((int)((click.x - offset) / blockSize),
                       (int)((click.y - offset) / blockSize));
    return anchor;
  }
  
  /**
   * Draw the quiltboard and it's content
   * @param context ApplicationContext
   * @param quiltboard QuiltBoard
   */
  public static void drawQuiltBoard(ApplicationContext context, QuiltBoard quiltboard) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(quiltboard);
    if (context.getScreenInfo().getHeight() <= 0 || context.getScreenInfo().getWidth() <= 0)
      throw new IllegalArgumentException("ApplicationContext have unusable characteristics");
    
    var height = context.getScreenInfo().getHeight();
    clear(context, height * 0.01, height * 0.01, height * 0.68, height * 0.68);
    drawPatchQuiltBoard(context, quiltboard.patchplaced());
    drawQuiltBoardGrid(context);
  }

}
