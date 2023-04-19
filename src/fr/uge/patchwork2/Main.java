package fr.uge.patchwork2;

import java.awt.Color;
import java.util.Scanner;

import fr.umlv.zen5.Application;

/**
 * Main class for the Patchwork game
 * @author Gabriel Radoniaina / Hervé Nguyen
 *
 */
public class Main {
  
  /**
   * Main method
   * @param args arguments given to the program 
   */
  public static void main(String[] args) {
    var scanner = new Scanner(System.in);
    System.out.println("### Game mode selection\n");
    while(true) {
      terminalMsg();
      graphicMsg();
      var str = scanner.next();
      if (str.equals("basic")){
        var game = new GameAlgorithm(0);
        game.terminalMode(scanner);
        break;
      }
      if (str.equals("full")){
        var game = new GameAlgorithm(1);
        game.terminalMode(scanner);
        break;
      }
      if (str.equals("gfx")) {
        var game = new GameAlgorithm(1);
        Application.run(Color.WHITE, context -> {
          game.graphicMode(context);
          context.exit(0);
        });
        break;
      }
    }
    scanner.close();
    return;
  }
  
  /**
   * Prints a message to explain how to launch one of the terminal modes
   */
  private static void terminalMsg(){
    System.out.println("## For the terminal mode :");
    System.out.println("# Input \"basic\" for the basic mode or \"full\" for the full game");
  }
  
  private static void graphicMsg() {
    System.out.println("## For the graphic mode :");
    System.out.println("# Input \"gfx\"");
  }

}

