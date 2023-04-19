package fr.uge.patchwork2;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.ScreenInfo;

/**
 * GraphicPatchStandard, used for the patchcircle like a wrapper.
 * Contains methods that allows to draw easily the patch
 * @author Hervé Nguyen and Gabriel Radoniaina
 *
 */
public final class GraphicPatchStandard implements GraphicPatch {
  private final Rectangle2D bounds; /* Used to check if clicks are located on the drawn patch */
  private final Patch patch;
  private final double x;
  private final double y;
  private final double blockSize;
  private final ApplicationContext context;


  /**
   * Constructor for a GraphicPatchStandard, used for the patchcircle and in the patch placement procedure to show the form of the patch (not on the quiltboard)
   * @param context ApplicationContext
   * @param patch the patch to wrap
   * @param x x coordinates in the screen of the patch's drawing
   * @param y y coordinates in the screen of the patch's drawing
   */
  public GraphicPatchStandard(ApplicationContext context, Patch patch, double x, double y) {
      Objects.requireNonNull(patch);
      Objects.requireNonNull(context);
      ScreenInfo screenInfo = context.getScreenInfo();
      var width = screenInfo.getWidth();
      var height = screenInfo.getHeight();
      
      if (x < 0 || y < 0 || x > width || y > height)
        throw new IllegalArgumentException("Coordinates out of bounds");
      
      var form = patch.form();
      var blockWidth = 0.10 * width / form[0].length; /* limiting the width to not got beyond 10% of the width of the screen */
      var blockHeight = 0.15 * height / form.length; /* limiting the height to not got beyond 15% of the height of the screen */
      this.context = context;
      this.blockSize = Math.min(blockWidth, blockHeight); /* choosing the smallest limit */
      this.bounds = new Rectangle2D.Double(x, y, blockSize * form[0].length, blockSize * form.length); /* Used to check if clicks are located on the drawn patch */
      this.patch = patch;
      this.x = x;
      this.y = y;
  }

  /**
   * Getter for the patch field
   * @return the patch field
   */
  public Patch patch() {
    return patch;
  }


  /**
   * Draws the button, the size will be tweaked to it won't be bigger than a certain threshold
   */
  public void draw() {
    var form = patch.form();
    for (int i = 0; i < form.length; i++) {
      for (int j = 0; j < form[0].length; j++) {
        if (form[i][j]) {
          double xPos = x + j * blockSize;
          double yPos = y + i * blockSize;
          drawPatchCore(context, patch, xPos, yPos, blockSize);
          drawPatchOutline(context, xPos, yPos, blockSize);
        }
      }
    }
  }
  
  /**
   * Checks if a given point is within the area of the button
   * Useful to verify clicks
   * @param point point to check, usually the point of a click
   * @return true if it is within the area of the button, otherwise false.
   */
  public boolean contains(Point2D point) {
    return bounds.contains(point);
  }
}
