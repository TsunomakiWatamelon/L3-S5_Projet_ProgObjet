package fr.uge.patchwork2;

import java.awt.Point;
import java.util.Objects;

/**
 * 
 * Represents a Patch placed on a quilt board
 * @param patch Patch
 * @param anchor Anchor point for the placed Patch
 * @author Gabriel Radoniaina / Hervé Nguyen
 *
 */
public record PatchPlaced(Patch patch, Point anchor) {
 /**
  * Constructor for PatchPlaced
  * @param patch Patch
  * @param anchor Anchor point for the placed Patch
  */
  public PatchPlaced {
    Objects.requireNonNull(patch, "Patch patch cannot be null");
    Objects.requireNonNull(anchor, "Point anchor cannot be null");
  }
}
