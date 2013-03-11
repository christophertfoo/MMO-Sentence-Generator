package edu.hawaii.ctfoo.lang_generator.entity;

import java.util.ArrayList;
import java.util.List;
import edu.hawaii.ctfoo.lang_generator.Generator;
import edu.hawaii.ctfoo.lang_generator.ParseToken;
import edu.hawaii.ctfoo.lang_generator.Tree;

/**
 * An Entity representing a player character in a MMORPG.
 * 
 * @author Christopher Foo
 * 
 */
public class Player extends Entity {

  /**
   * The classes of the player character.
   */
  private List<String> characterClasses;

  /**
   * The specializations of the player character's classes.
   */
  private List<String> characterSpecializations;

  /**
   * The race of the player character.
   */
  private String characterRace;

  /**
   * The role of the player character.
   */
  private String role;

  /**
   * The level of the player character.
   */
  private int level;

  /**
   * The average item level of the player character's items.
   */
  private int itemLevel;

  /**
   * The number of player characters.
   */
  private int quantity;

  /**
   * Creates a new Player based on the information in the {@link Tree} rooted by the given player
   * token.
   * 
   * @param playerToken The player token in the parse tree to build the Player from.
   */
  public Player(Tree<ParseToken> playerToken) {

    // Set default values
    this.characterClasses = new ArrayList<String>();
    this.characterSpecializations = new ArrayList<String>();
    this.characterRace = "";
    this.role = "";
    this.level = -1;
    this.itemLevel = -1;
    this.quantity = 1;

    if (playerToken.getNode().getType().equalsIgnoreCase("player")) {
      if (playerToken.getNode().isNegated()) {
        this.negated = true;
      }

      String type;
      String value;

      // Read all of the attributes for the player into the appropriate
      // fields
      for (Tree<ParseToken> playerAttributes : playerToken.getDirectChildren()) {

        type = playerAttributes.getNode().getType();

        value = playerAttributes.getNode().getValue();

        // Set class
        if (type.equalsIgnoreCase("class")) {
          this.characterClasses.add(value);
        }

        // Set specialization
        else if (type.equalsIgnoreCase("specialization")) {
          this.characterSpecializations.add(value);
        }

        // Set Race
        else if (type.equalsIgnoreCase("race")) {
          this.characterRace = value;
        }

        // Set Role
        else if (type.equalsIgnoreCase("role")) {
          this.role = value;
        }

        // Set level
        else if (type.equalsIgnoreCase("level")) {
          try {
            this.level = Integer.parseInt(value);
          }
          catch (NumberFormatException e) {
            System.err.println("Error: Could not parse \"" + value + "\" as an integer.");
          }
        }
        else if (type.equalsIgnoreCase("itemlevel")) {
          try {
            this.itemLevel = Integer.parseInt(value);
          }
          catch (NumberFormatException e) {
            System.err.println("Error: Could not parse \"" + value + "\" as an integer.");
          }
        }

        // Set quantity
        else if (type.equalsIgnoreCase("quantity")) {
          try {
            this.quantity = Integer.parseInt(value);
          }
          catch (NumberFormatException e) {
            System.err.println("Error: Could not parse \"" + value + "\" as an integer.");
          }
        }

        // Ignore other unrecognized attributes
      }
    }
  }

  /**
   * Gets the classes of the player character.
   * 
   * @return The classes of the player character in an array.
   */
  public String[] getCharacterClass() {
    return (String[]) this.characterClasses.toArray();
  }

  /**
   * Adds a new class to the player character.
   * 
   * @param characterClass The new class to add.
   */
  public void addCharacterClass(String characterClass) {
    this.characterClasses.add(characterClass);
  }

  /**
   * Gets the specializations of the player character's classes.
   * 
   * @return The specializations of the player character's classes in an array.
   */
  public String[] getCharacterSpecialization() {
    return (String[]) this.characterSpecializations.toArray();
  }

  /**
   * Adds a new specialization.
   * 
   * @param characterSpecialization The new specialization ot add.
   */
  public void addCharacterSpecialization(String characterSpecialization) {
    this.characterSpecializations.add(characterSpecialization);
  }

  /**
   * Gets the player character's race.
   * 
   * @return the characterRace The player character's race.
   */
  public String getCharacterRace() {
    return this.characterRace;
  }

  /**
   * Gets the player character's role.
   * 
   * @return The player character's role.
   */
  public String getRole() {
    return this.role;
  }

  /**
   * Gets the level of the player character.
   * 
   * @return The level of the player character.
   */
  public int getLevel() {
    return level;
  }

  /**
   * Gets the average item level of the player character's items.
   * 
   * @return The average item level of the player character's items.
   */
  public int getItemLevel() {
    return itemLevel;
  }

  /**
   * Gets the number of this type of player character.
   * 
   * @return The number of this type of player character.
   */
  public int getQuantity() {
    return this.quantity;
  }

  @Override
  /**
   * Returns a String representation of this Player character.
   */
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (this.characterClasses.size() < 1 && this.role.equals("")) {
      return "< Error: Incomplete player encountered >";
    }

    if (this.quantity > 1) {
      builder.append(this.quantity + " ");
    }
    if (this.level > -1) {
      builder.append("Level " + this.level + " ");
      if (this.itemLevel > -1) {
        builder.append("/ ");
      }
    }

    if (this.itemLevel > -1) {
      builder.append("Item Level " + this.itemLevel + " ");
    }

    if (this.characterClasses.size() > 0) {
      if (!this.characterRace.equals("")) {
        builder.append(this.characterRace + " ");
      }

      if (this.characterSpecializations.size() > 0) {
        Generator.appendDelimiterList(this.characterSpecializations, builder, " / ");
        builder.append(" ");
      }

      if (this.characterClasses.size() == 1 && this.quantity > 1) {
        builder.append(Generator.pluralize(this.characterClasses.get(0)));
      }

      else {
        Generator.appendDelimiterList(this.characterClasses, builder, " / ");
      }
    }

    else {
      if (this.quantity > 1) {
        builder.append(Generator.pluralize(this.role));
      }
      else {
        builder.append(this.role);
      }
    }
    return builder.toString().trim();
  }
}
