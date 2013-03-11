package edu.hawaii.ctfoo.lang_generator.sentence;

import java.util.ArrayList;
import java.util.List;
import edu.hawaii.ctfoo.lang_generator.MatchFunctor;
import edu.hawaii.ctfoo.lang_generator.Generator;
import edu.hawaii.ctfoo.lang_generator.LogicOp;
import edu.hawaii.ctfoo.lang_generator.ParseToken;
import edu.hawaii.ctfoo.lang_generator.Tree;
import edu.hawaii.ctfoo.lang_generator.TypeMatcher;
import edu.hawaii.ctfoo.lang_generator.entity.Item;
import edu.hawaii.ctfoo.lang_generator.entity.Player;

/**
 * A {@link Sentence} where the event is buying {@link Item}s.
 * 
 * @author Christopher Foo
 * 
 */
public class BuySentence extends Sentence {

  /**
   * The array of valid classes for the BuySentence's objects.
   */
  private static final Class<?>[] validObjectClasses = { Item.class };

  /**
   * The array of valid classes for the BuySentence's subjects.
   */
  private static final Class<?>[] validSubjectClasses = { Player.class };

  /**
   * The contact methods for the Buy event.
   */
  private List<List<String>> contactMethods;

  /**
   * Creates a new BuySentence based on the given Buy {@link Tree} node.
   * 
   * @param buyToken The Buy node to build the BuySentence from.
   */
  public BuySentence(Tree<ParseToken> buyToken) {
    super(BuySentence.validObjectClasses, BuySentence.validSubjectClasses);
    this.contactMethods = new ArrayList<List<String>>();

    if (buyToken.getNode().getType().equalsIgnoreCase("buy")) {

      this.readSubObj(buyToken, "object");

      this.readSubObj(buyToken, "subject");

      this.getContactMethods(buyToken);
    }
  }

  /**
   * Gets the contact methods for the BuySentence based on the given Buy {@link Tree} node.
   * 
   * @param buyToken The Buy node to get the contact methods from.
   */
  private void getContactMethods(Tree<ParseToken> buyToken) {
    MatchFunctor<String, ParseToken> typeMatcher = new TypeMatcher();
    int index = 0;
    for (Tree<ParseToken> typeNode : buyToken.findDirect("contactmethod", typeMatcher)) {
      ParseToken typeToken = typeNode.getNode();

      // Put in new list if OR
      if (typeToken.getLogic() == LogicOp.OR
          && (index >= this.contactMethods.size() || this.contactMethods.get(index).size() > 0)) {
        index++;
      }
      if (index >= this.contactMethods.size()) {
        this.contactMethods.add(new ArrayList<String>());
      }
      this.contactMethods.get(index).add(typeNode.getNode().getValue());
    }
  }

  @Override
  /**
   * Returns the generated sentence as a String.
   */
  public String toString() {
    if (this.eventObject.size() < 1) {
      return "< Error in BuySentence: No object to buy found >";
    }

    if (this.checkObject() && this.checkSubject()) {
      StringBuilder builder = new StringBuilder();
      Generator.appendCommaList(this.eventSubject, builder, "or");
      if (builder.length() > 0) {
        builder.append(" ");
      }
      builder.append("WTB ");
      Generator.appendCommaList(this.eventObject, builder, "or");
      if (this.contactMethods.size() > 0) {
        builder.append(", ");
        Generator.appendCommaList(this.contactMethods, builder, "or");
      }
      builder.append(".");
      return builder.toString();
    }

    else {
      return "< Error in BuySentence: Invalid object or subject found >";
    }
  }

}
