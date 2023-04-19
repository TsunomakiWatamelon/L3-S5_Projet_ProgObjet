package fr.uge.patchwork2;

import java.util.Scanner;

/**
 * Collection of methods used by multiple other classes
 * @author Gabriel Radoniaina / Hervé Nguyen
 *
 */
public class Tools {
  
  /**
   * Pause the thread for a given amount of milliseconds
   * @param sleeptime duration of sleep in ms
   */
  public static void sleep(long sleeptime) {
    if (sleeptime < 0)
      throw new IllegalArgumentException("sleeptime is negative");
    try {
      Thread.sleep(sleeptime);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
  
  /**
   * From a Scanner, tries to fetch a integer value from the input of the user.
   * If an invalid String has been given by the user, valueDefault will be returned.
   * @param scanner scanner
   * @param valueDefault integer to return by default if the integer inputed is invalid
   * @return int integer parsed from the user or valueDefault if impossible to parse
   */
  public static int scannerGetInt(Scanner scanner, int valueDefault) {
    try {
       // scanner.nextLine() is used to skip the line if it cannot be parsed into an int
       // the use of matcher was considered but it could be confusing for the user
       return Integer.valueOf(scanner.nextLine());
    } catch(NumberFormatException e) {
       return valueDefault;
    }
  }
}
