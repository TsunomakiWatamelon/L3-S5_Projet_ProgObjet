package fr.uge.patchwork2;


import java.awt.Point;
import java.util.Objects;

import fr.umlv.zen5.ApplicationContext;

/**
 * GraphicPatchPlaced, used for the patchcircle like a wrapper.
 * Contains methods that allows to draw easily the patch, but in the context of drawing it on the quiltboard.
 * 
 * The main difference between this class and GraphicPatch is that the blocksize here does not vary depending on the total size of the patch.
 * The purpose being to properly draw patches on the quiltboard.
 * 
 * The size of the block here must be synced with how a quiltboard is drawn
 * 
 * @author Herve Nguyen and Gabriel Radoniaina
 *
 */
public final class GraphicPatchPlaced implements GraphicPatch {
  private final Patch patch;
  private final Point anchor;
  private final ApplicationContext context;


  /**
   * Constructor for a GraphicPatchPlaced.
   * @param context ApplicationContext
   * @param patch the patch to wrap
   * @param anchor the anchor point (top-left) to locate the placement of the patch
   */
  public GraphicPatchPlaced(ApplicationContext context, Patch patch, Point anchor) {
      Objects.requireNonNull(patch);
      Objects.requireNonNull(context);
      Objects.requireNonNull(anchor);
      if (anchor.x < 0 || anchor.y < 0 || anchor.x >= 9 || anchor.y >= 9)
        throw new IllegalArgumentException("Coordinates out of bounds");
      this.context = context;
      this.patch = patch;
      this.anchor = anchor;
  }

  /**
   * getter for patch field
   * @return return the patch field
   */
  public Patch patch() {
    return patch;
  }


  /**
   * Draws the placed patch on the quiltboard (assuming it is there)
   */
  public void draw() {
    var form = patch.form();
    var offset = context.getScreenInfo().getHeight() * 0.01;
    var blockSize = (context.getScreenInfo().getHeight() * 0.68) / 9;
    for (int i = 0; i < form.length; i++) {
      for (int j = 0; j < form[0].length; j++) {
        if (form[i][j]) {
          double xPos = anchor.x * blockSize + j * blockSize + offset;
          double yPos = anchor.y * blockSize + i * blockSize + offset;
          drawPatchCore(context, patch, xPos, yPos, blockSize);
          drawPatchOutline(context, xPos, yPos, blockSize);
        }
      }
    }
  }
}
