/**
 * 
 */
package fr.uge.patchwork2;

/**
 * Represents the specific element that can possibly be place on the time board (including empty)
 * @author Gabriel Radoniaina / Hervé Nguyen
 *
 */
public enum TimeBoardElement {
  /**
   * There is no special element
   */
  empty,
  /**
   * A special patch
   */
  specialPatch,
  /**
   * A button
   */
  button
}
