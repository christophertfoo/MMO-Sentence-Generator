package edu.hawaii.ctfoo.lang_generator.entity;

import java.util.ArrayList;
import java.util.List;
import edu.hawaii.ctfoo.lang_generator.MatchFunctor;
import edu.hawaii.ctfoo.lang_generator.Generator;
import edu.hawaii.ctfoo.lang_generator.ParseToken;
import edu.hawaii.ctfoo.lang_generator.Tree;
import edu.hawaii.ctfoo.lang_generator.TypeMatcher;

/**
 * An entity representing an in-game Item.
 * 
 * @author Christopher Foo
 * 
 */
public class Item extends Entity {
  /**
   * The name of the item.
   */
  private String name;

  /**
   * The rarity / quality of the item.
   */
  private String rarity;

  /**
   * The type of the item (i.e. sword, shield, helmet, etc).
   */
  private String type;

  /**
   * The value of the item as a list of {@link MoneyAmount} objects.
   */
  private List<MoneyAmount> value;

  /**
   * The level of the item.
   */
  private int level;

  /**
   * The quantity of this item to buy / sell.
   */
  private int quantity;

  /**
   * Creates a new Item object based on the given Item {@link ParseToken} in the parse tree. Returns
   * a default Item object if the given node is not an Item node.
   * 
   * @param itemToken The item token to parse from.
   */
  public Item(Tree<ParseToken> itemToken) {

    // Set everything to defaults
    this.name = "";
    this.rarity = "";
    this.type = "";
    this.value = new ArrayList<MoneyAmount>();
    this.level = -1;
    this.quantity = 1;

    // If item token, read in attributes
    if (itemToken.getNode().getType().equalsIgnoreCase("item")) {

      if (itemToken.getNode().isNegated()) {
        this.negated = true;
      }

      String type;
      String value;

      // Read all of the attributes for the item into the appropriate
      // fields
      for (Tree<ParseToken> itemAttribute : itemToken.getDirectChildren()) {

        type = itemAttribute.getNode().getType();

        value = itemAttribute.getNode().getValue();

        // Set Name
        if (type.equalsIgnoreCase("name")) {
          this.name = value;
        }

        // Set Rarity
        else if (type.equalsIgnoreCase("rarity")) {
          this.rarity = value;
        }

        // Set Type
        else if (type.equalsIgnoreCase("type")) {
          this.type = value;
        }

        // Set Level
        else if (type.equalsIgnoreCase("level")) {
          try {
            this.level = Integer.parseInt(value);
          }
          catch (NumberFormatException e) {
            System.err.println("Error: Could not parse \"" + value + "\" as an integer.");
          }
        }

        // Set Quantity
        else if (type.equalsIgnoreCase("quantity")) {
          try {
            this.quantity = Integer.parseInt(value);
          }
          catch (NumberFormatException e) {
            System.err.println("Error: Could not parse " + value + " as an integer.");
          }
        }

        // Set Value
        else if (type.equalsIgnoreCase("value")) {
          MatchFunctor<String, ParseToken> typeMatcher = new TypeMatcher();

          // Get all underlying MoneyAmount elements
          for (Tree<ParseToken> moneyAmount : itemAttribute.findAll("moneyamount", typeMatcher)) {

            MoneyAmount amount = new MoneyAmount(moneyAmount);

            // If the MoneyAmount is complete add it, if not ignore
            // it
            if (amount.getDenomination() != -1 && !amount.getCurrency().equals("")) {
              this.addValue(amount);
            }
          }
        }

        // Ignore unrecognized attributes
      }
    }
  }

  /**
   * Gets the name of the Item.
   * 
   * @return The name of the Item.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the rarity / quality of the Item.
   * 
   * @return The rarity / quality of the Item.
   */
  public String getRarity() {
    return rarity;
  }

  /**
   * Gets the type of the Item (i.e. sword, shield, helmet, boots, etc.)
   * 
   * @return The type of the item.
   */
  public String getType() {
    return type;
  }

  /**
   * Gets the value of the Item as an array of {@link MoneyAmount} objects.
   * 
   * @return The value of the Item.
   */
  public MoneyAmount[] getValue() {
    return (MoneyAmount[]) this.value.toArray();
  }

  /**
   * Adds the given amount to the Item's value.
   * 
   * @param denomination The denomination of the added amount.
   * @param currency The currency of the added amount.
   */
  public void addValue(double denomination, String currency) {
    this.value.add(new MoneyAmount(denomination, currency));
  }

  /**
   * Adds the given amount to the Item's value.
   * 
   * @param amount The {@link MoneyAmount} to add.
   */
  public void addValue(MoneyAmount amount) {
    this.value.add(amount);
  }

  /**
   * Gets the level of the Item.
   * 
   * @return The level of the Item.
   */
  public int getLevel() {
    return level;
  }

  /**
   * Gets the quantity of the Item.
   * 
   * @return The quantity of the Item.
   */
  public int getQuantity() {
    return quantity;
  }

  @Override
  /**
   * Returns a String representation of the Item.
   */
  public String toString() {
    StringBuilder returnString = new StringBuilder();

    // Append quantity if it is there.
    if (this.quantity > 0) {
      returnString.append(this.quantity + " ");
    }

    // If has name, use it.
    if (!this.name.equals("")) {

      if (this.quantity > 1) {
        returnString.append(Generator.pluralize(this.name));
      }
      else {
        returnString.append(this.name);
      }

      returnString.append(" ");

      if (this.value.size() > 0) {
        returnString.append("for ");
      }

      Generator.appendCommaList(this.value, returnString, "and");
      return returnString.toString().trim();

    }

    // Otherwise use type if it has it.
    else if (!this.type.equals("")) {
      if (!this.rarity.equals("")) {
        returnString.append(this.rarity.toLowerCase() + " ");
      }
      if (this.level > -1) {
        returnString.append("Item Level " + this.level + " ");
      }
      if (this.quantity > 1) {
        returnString.append(Generator.pluralize(this.type.toLowerCase()));
      }
      else {
        returnString.append(this.type.toLowerCase());

      }
      returnString.append(" ");

      if (this.value.size() > 0) {
        returnString.append("for ");
      }

      Generator.appendCommaList(this.value, returnString, "and");
      return returnString.toString().trim();
    }

    // Item needs a name or type to be complete.
    else {
      return "< Error: Incomplete item encountered >";
    }
  }
}
