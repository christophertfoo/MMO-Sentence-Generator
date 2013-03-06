package edu.hawaii.ctfoo.lang_generator.sentence;

import edu.hawaii.ctfoo.lang_generator.Generator;
import edu.hawaii.ctfoo.lang_generator.ParseToken;
import edu.hawaii.ctfoo.lang_generator.Tree;
import edu.hawaii.ctfoo.lang_generator.entity.Instance;
import edu.hawaii.ctfoo.lang_generator.entity.Player;

/**
 * A {@link Sentence} where the event is finding a group for an {@link Instance}
 * .
 * 
 * @author Christopher
 * 
 */
public class FindGroupSentence extends Sentence {

    /**
     * The array of valid classes for the FindGroupSentence's objects.
     */
    private static final Class<?>[] validObjectClasses = { Instance.class };

    /**
     * The array of valid classes for the FindGroupSentence's subjects.
     */
    private static final Class<?>[] validSubjectClasses = { Player.class };

    /**
     * Creates a new FindGroupSentence with the default values (empty);
     */
    public FindGroupSentence() {
        super(validObjectClasses, validSubjectClasses);
    }

    /**
     * Creates a new FindGroupSentence and fills it with values based on the
     * given FindGroup {@link Tree} node.
     * 
     * @param findGroupToken
     *            The FindGroup Tree node used to populate the
     *            FindGroupSentence.
     */
    public FindGroupSentence(Tree<ParseToken> findGroupToken) {
        this();

        if (findGroupToken.getNode().getType().equalsIgnoreCase("findgroup")) {
            this.readSubObj(findGroupToken, "object");
            this.readSubObj(findGroupToken, "subject");
        }
    }

    @Override
    /**
     * Returns the generated sentence in String form.
     */
    public String toString() {

        if (this.eventObject.size() < 1 && this.eventSubject.size() < 1) {
            return "< Error in FindGroupSentence: No object nor subject found >";
        }

        if (this.checkObject() && this.checkSubject()) {
            StringBuilder builder = new StringBuilder();
            if (this.checkObject() && this.checkSubject()) {

                Generator.appendCommaList(this.eventSubject, builder, "or");
                if (builder.length() > 0) {
                    builder.append(" ");
                }
                builder.append("LFG");
                if (this.eventObject.size() > 0) {
                    builder.append(" for ");
                    Generator.appendCommaList(this.eventObject, builder, "or");
                }
                builder.append(".");
            }
            return builder.toString();
        } else {
            return "< Error in FindGroupSentence: Invalid subject or object found >";
        }
    }
}
