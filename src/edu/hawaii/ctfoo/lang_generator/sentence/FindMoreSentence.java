package edu.hawaii.ctfoo.lang_generator.sentence;

import java.util.ArrayList;
import java.util.List;
import edu.hawaii.ctfoo.lang_generator.MatchFunctor;
import edu.hawaii.ctfoo.lang_generator.Generator;
import edu.hawaii.ctfoo.lang_generator.LogicOp;
import edu.hawaii.ctfoo.lang_generator.ParseToken;
import edu.hawaii.ctfoo.lang_generator.Tree;
import edu.hawaii.ctfoo.lang_generator.TypeMatcher;
import edu.hawaii.ctfoo.lang_generator.entity.Entity;
import edu.hawaii.ctfoo.lang_generator.entity.Instance;
import edu.hawaii.ctfoo.lang_generator.entity.Player;

/**
 * A {@link Sentence} where the event is looking for more members.
 * 
 * @author Christopher Foo
 * 
 */
public class FindMoreSentence extends Sentence {

  /**
   * The array of classes that are valid objects in the FindMoreSentence.
   */
  private static final Class<?>[] validObjectClasses = { Player.class };

  /**
   * The array of classes that are valid subjects in the FindMoreSentence.
   */
  private static final Class<?>[] validSubjectClasses = {};

  /**
   * The {@link Instance}s that more members are being sought for.
   */
  private List<List<Instance>> instances;

  /**
   * Creates a new FindMoreSentence filled with values based on the given FindMore token
   * {@link Tree} node.
   * 
   * @param findMoreToken The FindMore Tree node used to fill the FindMoreSentence.
   */
  public FindMoreSentence(Tree<ParseToken> findMoreToken) {
    super(validObjectClasses, validSubjectClasses);
    this.instances = new ArrayList<List<Instance>>();

    if (findMoreToken.getNode().getType().equalsIgnoreCase("findmore")) {

      this.readSubObj(findMoreToken, "object");
      this.readSubObj(findMoreToken, "subject");
      this.getInstances(findMoreToken);
    }
  }

  /**
   * Gets the {@link Instance}s that members are being sought for in the FindMore event.
   * 
   * @param findMoreToken The FindMore {@link Tree} node describing this FindMoreSentence.
   */
  private void getInstances(Tree<ParseToken> findMoreToken) {
    MatchFunctor<String, ParseToken> typeMatcher = new TypeMatcher();
    int index = 0;
    for (Tree<ParseToken> typeNode : findMoreToken.findDirect("instance", typeMatcher)) {
      ParseToken typeToken = typeNode.getNode();

      // Put in new list if OR
      if (typeToken.getLogic() == LogicOp.OR
          && (index >= this.instances.size() || this.instances.get(index).size() > 0)) {
        index++;
      }
      if (index >= this.instances.size()) {
        this.instances.add(new ArrayList<Instance>());
      }
      this.instances.get(index).add(new Instance(typeNode));
    }
  }

  @Override
  /**
   * Returns the generated sentence as a String.
   */
  public String toString() {
    if (this.eventObject.size() < 1 && this.eventSubject.size() < 1 && this.instances.size() < 1) {
      return "< Error in FindMoreSentence: No subject or object found >";
    }

    if (this.checkObject() && this.checkSubject()) {
      StringBuilder builder = new StringBuilder();
      Generator.appendCommaList(this.eventSubject, builder, "or");
      builder.append(" LF");
      int numMembersSmall = 0;
      int numMembersBig = 0;
      for (List<Entity> outerList : this.eventObject) {
        int numMembersTemp = 0;
        for (Entity object : outerList) {
          if (object instanceof Player) {
            numMembersTemp += ((Player) object).getQuantity();
          }
        }

        // If first set, initialize
        if (numMembersSmall == 0) {
          numMembersSmall = numMembersTemp;
          numMembersBig = numMembersTemp;
        }

        // We found the number of members before
        else {
          if (numMembersTemp < numMembersSmall) {
            numMembersSmall = numMembersTemp;
          }
          else if (numMembersTemp > numMembersBig) {
            numMembersBig = numMembersTemp;
          }
        }
      }

      if (numMembersSmall > 0) {
        if (numMembersSmall == numMembersBig) {
          builder.append(numMembersSmall);
        }
        else {
          builder.append(numMembersSmall + "-" + numMembersBig);
        }
      }

      builder.append("M ");
      Generator.appendCommaList(this.eventObject, builder, "or");
      if (this.instances.size() > 0) {
        if (this.eventObject.size() > 0) {
          builder.append(" ");
        }
        builder.append("for ");
        Generator.appendCommaList(this.instances, builder, "or");
      }
      builder.append(".");
      return builder.toString().trim();
    }

    else {
      return "< Error in FindMoreSentence: Invalid subject or object found >";
    }
  }
}
