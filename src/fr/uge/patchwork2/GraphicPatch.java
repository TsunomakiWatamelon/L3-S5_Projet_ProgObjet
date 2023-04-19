package fr.uge.patchwork2;

import java.awt.BasicStroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

import fr.umlv.zen5.ApplicationContext;

/**
 * Interface used to limit shared code between the two similar but differently used GraphicPatch
 * @author Herve Nguyen and Gabriel Radoniaina
 *
 */
public sealed interface GraphicPatch permits GraphicPatchPlaced, GraphicPatchStandard {
  /**
   * Draws the colored core of a block of a patch
   * @param context ApplicationContext
   * @param patch the aforementioned patch
   * @param xPos x coordinates of the top left corner of the patch
   * @param yPos y coordinates of the top left corner of the patch
   * @param blockSize size of one "block" / "square" of the patch
   */
  default void drawPatchCore(ApplicationContext context, Patch patch, double xPos, double yPos, double blockSize) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(patch);
    if (blockSize < 0 || xPos < 0 || yPos < 0)
      throw new IllegalArgumentException("Invalid values");
    context.renderFrame(graphics -> {
      graphics.setColor(patch.color());
      graphics.fill(new Rectangle2D.Double(xPos, yPos, blockSize, blockSize));
    });
  }
  
  /**
   * Draws the outline of a block of a patch
   * @param context ApplicationContext
   * @param xPos x coordinates of the top left corner of the patch
   * @param yPos y coordinates of the top left corner of the patch
   * @param blockSize size of one "block" / "square" of the patch
   */
  default void drawPatchOutline(ApplicationContext context, double xPos, double yPos, double blockSize) {
    Objects.requireNonNull(context);
    if (blockSize < 0 || xPos < 0 || yPos < 0)
      throw new IllegalArgumentException("Invalid values");
    context.renderFrame(graphics -> {
      graphics.setStroke(new BasicStroke(1f));
      graphics.draw(new Line2D.Double(xPos, yPos, xPos + blockSize, yPos));
      graphics.draw(new Line2D.Double(xPos, yPos, xPos, yPos + blockSize));
      graphics.draw(new Line2D.Double(xPos + blockSize, yPos, xPos + blockSize, yPos + blockSize));
      graphics.draw(new Line2D.Double(xPos, yPos + blockSize, xPos + blockSize, yPos + blockSize));
    });
  }
}
