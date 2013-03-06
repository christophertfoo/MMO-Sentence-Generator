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
 * A {@link Sentence} where the event is selling {@link Item}s.
 * 
 * @author Christopher Foo
 * 
 */
public class SellSentence extends Sentence {

    /**
     * The array of valid classes for the SellSentence's objects.
     */
    private static final Class<?>[] validObjectClasses = { Item.class };

    /**
     * The array of valid classes for the SellSentence's subjects.
     */
    private static final Class<?>[] validSubjectClasses = { Player.class };

    /**
     * The contact methods of the Sell event.
     */
    private List<List<String>> contactMethods;

    /**
     * Creates a new SellSentence with the default values (empty).
     */
    public SellSentence() {
        super(SellSentence.validObjectClasses, SellSentence.validSubjectClasses);
        this.contactMethods = new ArrayList<List<String>>();
    }

    /**
     * Creates a new SellSentence based on the Sell token at the given node.
     * 
     * @param sellToken
     *            The node with the sell token used to generate the
     *            SellSentence.
     */
    public SellSentence(Tree<ParseToken> sellToken) {
        this();

        if (sellToken.getNode().getType().equalsIgnoreCase("sell")) {

            this.readSubObj(sellToken, "object");

            this.readSubObj(sellToken, "subject");

            this.getContactMethods(sellToken);
        }
    }

    /**
     * Gets the contact methods for the SellSentence from the given Sell token
     * {@link Tree} node.
     * 
     * @param sellToken
     *            The {@link Tree} node of the contact methods to get.
     */
    private void getContactMethods(Tree<ParseToken> sellToken) {
        MatchFunctor<String, ParseToken> typeMatcher = new TypeMatcher();
        int index = 0;
        for (Tree<ParseToken> typeNode : sellToken.findDirect("contactmethod",
                typeMatcher)) {
            ParseToken typeToken = typeNode.getNode();

            // Put in new list if OR
            if (typeToken.getLogic() == LogicOp.OR
                    && (index >= this.contactMethods.size() || this.contactMethods
                            .get(index).size() > 0)) {
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
            return "< Error in SellSentence: No object to sell found >";
        }

        if (this.checkObject() && this.checkSubject()) {
            StringBuilder builder = new StringBuilder();
            Generator.appendCommaList(this.eventSubject, builder, "or");
            if (builder.length() > 0) {
                builder.append(" ");
            }
            builder.append("WTS ");
            Generator.appendCommaList(this.eventObject, builder, "or");
            if (this.contactMethods.size() > 0) {
                builder.append(", ");
                Generator.appendCommaList(this.contactMethods, builder, "or");
            }
            builder.append(".");
            return builder.toString();
        }

        else {
            return "< Error in SellSentence: Invalid object or subject found >";
        }
    }

}
