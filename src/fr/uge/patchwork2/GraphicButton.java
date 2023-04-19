package fr.uge.patchwork2;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

import fr.umlv.zen5.ApplicationContext;

/**
 * Represents a button in a screen.
 * It has a method to draw the button
 * and another method that allows to check if the coordinates of a hypothetical click is inside the area of the button
 * 
 * Used as an alternative to java.awt.button, where a Frame or Panel is required (unaccessible here by using zen5)
 * @author Herve Nguyen and Gabriel Radoniaina
 *
 */
public class GraphicButton {
  private final String label;
  private final Rectangle2D bounds; /* Used to check if clicks are located on the drawn button */
  private double width;
  private double height;
  private final ApplicationContext context;

  /**
   * Constructor for a graphic button, creates a button (but not drawn yet) with the
   * given characteristics
   * @param context ApplicationContext
   * @param label label
   * @param x x coordinate of the top-left corner of the button
   * @param y y coordinate of the top-left corner of the button
   * @param width width of the button
   * @param height height of the button
   */
  public GraphicButton(ApplicationContext context, String label, double x, double y, double width, double height) {
      Objects.requireNonNull(label);
      Objects.requireNonNull(context);
      if (   width <= 0
          || height <= 0
          || context.getScreenInfo().getWidth() < x + width 
          || context.getScreenInfo().getHeight() < y + height)
      {
        throw new IllegalArgumentException("Characteristics given are invalid");
      }
        
      if (x < 0 || y < 0 )
        throw new IllegalArgumentException("Coordinates out of bounds");
      this.label = label;
      this.width = width;
      this.height = height;
      this.bounds = new Rectangle2D.Double(x, y, width, height); /* Used to check if clicks are located on the drawn button */
      this.context = context;
  }

  /**
   * Draws the button, the text's size will be tweaked so that it will not go outside of the button's bounds
   */
  public void draw() {
    context.renderFrame(graphics -> {
      graphics.setColor(Color.LIGHT_GRAY);
      graphics.fill(bounds);
      graphics.setColor(Color.BLACK);
      int fontSize = 18;
      Font font = new Font("Arial", Font.BOLD, fontSize);
      graphics.setFont(font);
      FontMetrics metrics = graphics.getFontMetrics();
      while(metrics.stringWidth(label) > width || metrics.getHeight() > height) {
          fontSize--;
          font = new Font("Arial", Font.BOLD, fontSize);
          graphics.setFont(font);
          metrics = graphics.getFontMetrics();
      }
      var textWidth = graphics.getFontMetrics().stringWidth(label);
      var textHeight = graphics.getFontMetrics().getHeight();
      var textX = bounds.getX() + (width - textWidth) / 2;
      var textY = bounds.getY() + (height + textHeight) / 2;
      graphics.drawString(label, (int)textX, (int)textY);
    });
  }
  
  /**
   * Checks if a given point is within the area of the button
   * Useful to verify clicks
   * @param point point to check, usually the point of a click
   * @return true if it is within the area of the button, otherwise false.
   */
  public boolean contains(Point2D point) {
    Objects.requireNonNull(point);
    return bounds.contains(point);
  }
}
