package fr.uge.patchwork2;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Represent the patches available during the game on a circle
 * @author Gabriel Radoniaina / Hervé Nguyen
 *
 */
public class PatchCircle {
  /**
   * List of patches in the patch circle
   */
  private final List<Patch> patch;
  
  /**
   * Constructor for PatchCircle
   */
  public PatchCircle() {
    patch = new ArrayList<>();
  }
  
  /**
   * returns the size of the PatchCircle
   * @return The size of the PatchCircle
   */
  public int size() {
    return patch.size();
  }
  
  
  /**
   * Getter for field patch
   * @return The field patch, which is a list of Patch
   */
  public List<Patch> getPatch() {
    return patch;
  }

  /**
   * Adds a patch to the PatchCircle
   * @param p patch to be added
   */
  private void addPatch(Patch p) {
    Objects.requireNonNull(p);
    patch.add(p);
  }
  
  /**
   * Picks a patch among the first three in the circle (compared to the neutral token at index 0) and returns it.
   * If the second or third patch is chosen, the patches that are now behind the neutral token will be send at the back of the List.
   * @param patchIndex index of the patch to be chosen
   * @return That Patch
   */
  public Patch choosePatch(int patchIndex) {
    if (patch.size() == 0)
      throw new IllegalStateException("PatchCircle is empty");
    if (patchIndex < 0)
      throw new IllegalArgumentException("patchIndex negative");
    if (patchIndex > 2)
      throw new IllegalArgumentException("patchIndex > 2, must be between 0 and 2");
    if (patchIndex >= patch.size())
      throw new IllegalStateException("Trying to chose a non existant patch");
    
    var selectedPatch = patch.remove(patchIndex);
    for (var i = 0; i < patchIndex; i++) {
      patch.add(patch.remove(0));
    }
    return selectedPatch;
  }
  
  /**
   * Method used in the graphical mode to "choose" a patch.
   * In the graphical mode, the chosen patch object is already available to the player.
   * But it is necessary to remove that patch from the patch circle
   * @param chosen chosen patch
   * @return the patch chosen which is now removed from the patchcircle
   */
  public Patch choosePatch(Patch chosen) {
    Objects.requireNonNull(patch);
    if (patch.isEmpty())
      throw new IllegalStateException("Chose a patch from an empty patch circle");
    if (!this.getTrio(1).contains(chosen)) /* Get the first 3 patch */
      throw new IllegalStateException("Chosen patch does not belong to the available patches to choose");
    patch.remove(chosen);
    return chosen;
  }
  
  /**
   * Returns true :
   *  - if the index of the chosen patch on the PatchCircle is a valid choice and can be bought
   *  - if 3 is chosen (equivalent to not choosing a Patch)
   * Otherwise return false (is invalid)
   * @param patchIndex index of the chosen patch (or can be 3)
   * @param buttonCount number of buttons held by the current player
   * @return true or false
   */
  public boolean choiceIsValid(int patchIndex, int buttonCount) {
    
    if (patch.size() == 0)
      return false;
    if (buttonCount < 0)
      throw new IllegalArgumentException("buttonCount cannot be negative");
    if (patchIndex == 3)
      return true;
    if (patchIndex < 0 || patchIndex > 3 || patchIndex >= patch.size() || buttonCount == 0)
      return false;
    
    var selectedPatch = patch.get(patchIndex);
    if (selectedPatch.price() > buttonCount)
      return false;
    
    return true;
  }
  
  /**
   * Creates and returns the PatchCircle for the first version
   * @return PatchCircle the newly created PatchCircle
   */
  public static PatchCircle newPatchCircleBasic() {
    var patchcircle = new PatchCircle();
    for (var i = 0; i < 20; i++) {
      patchcircle.addPatch(Patch.cubeBasic1());
      patchcircle.addPatch(Patch.cubeBasic2());
    }
    Collections.shuffle(patchcircle.getPatch());
    return patchcircle;
  }

  /**
   * Tries to build the patchcircle while reading a configuration file
   * @param path the path to the configuration file (usually located in the ressource folder)
   * @throws IOException (if the file is not present or other IO related issues)
   */
  private void buildPatchByFile(Path path) throws IOException {
    var rand = new Random();
    try(var reader = Files.newBufferedReader(path)) {
      String line; String[] patchCharacteristic;
      while((line = reader.readLine()) != null) {
        patchCharacteristic = line.split(" ");
        var button = Integer.parseInt(patchCharacteristic[0]);
        var price = Integer.parseInt(patchCharacteristic[1]);
        var time = Integer.parseInt(patchCharacteristic[2]);
        var sizeY = Integer.parseInt(patchCharacteristic[3]);
        var sizeX = Integer.parseInt(patchCharacteristic[4]);
        var form = new boolean[sizeY][sizeX];
        for (var i = 0; i < sizeY; i++) {
          for (var j = 0; j < sizeX; j++) {
            form[i][j] = patchCharacteristic[5 + i * sizeX + j].equals("0") ? false : true;
          }
        }
        patch.add(new Patch(button, price, time, form, sizeX, sizeY, new Color(rand.nextInt(50, 255),
                                                                               rand.nextInt(50, 255),
                                                                               rand.nextInt(50, 255))
                           )
        );
      }
    }
  }
  
  /**
   * Creates and returns the PatchCircle for the full version (has a fallback alternative if it doesn't succeed)
   * @return PatchCircle the newly created PatchCircle
   */
  public static PatchCircle newPatchCircleFull() {
    var patchcircle = new PatchCircle();
    try {
      patchcircle.buildPatchByFile(FileSystems.getDefault().getPath("ressource/patches"));
    } catch(IOException e) { /* fallback option */
      System.out.println("Error reading file, now using backup patchcircle");
      return newPatchCircleBasic();
    }
    Collections.shuffle(patchcircle.getPatch());
    return patchcircle;
  }
  
  /**
   * From the integer which asks the "nth" trio of the patchcircle
   * Returns a list containing the asked "trio".
   * 
   * The function can return a list which has less than 3 elements,
   * since it will also include the trailing "trio" of the patch circle. (where a trio is incomplete)
   * 
   * If the nth trio does not exist, an empty array will be returned
   *
   * @param n nth trio
   * @return the list of the patch of that nth trio.
   */
  public List<Patch> getTrio(int n){
    if (n <= 0)
      throw new IllegalArgumentException("0th trio or negative-th trio is not possible");
    var trio = new ArrayList<Patch>();
    
    /* Empty list if the nth trio asked is beyond what is avaialble in the patchcircle */
    if (patch.size() <= ((n-1) * 3))
      return trio;
    
    for (var i = 0; i < 3 && ((n-1) * 3 + i) < patch.size(); i++) {
      trio.add(patch.get((n-1) * 3 + i));
    }
    return trio;
    
  }
}
