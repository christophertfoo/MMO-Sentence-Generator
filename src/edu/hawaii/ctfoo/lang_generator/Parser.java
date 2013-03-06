package edu.hawaii.ctfoo.lang_generator;

import java.util.ArrayList;
import java.util.List;

import edu.hawaii.ctfoo.lang_generator.sentence.BuySentence;
import edu.hawaii.ctfoo.lang_generator.sentence.FindGroupSentence;
import edu.hawaii.ctfoo.lang_generator.sentence.FindMoreSentence;
import edu.hawaii.ctfoo.lang_generator.sentence.SellSentence;
import edu.hawaii.ctfoo.lang_generator.sentence.Sentence;

/**
 * Used for parsing semantic representations in String form into
 * {@link ParseToken}s, {@link Tree}s, and ultimately {@link Sentence}s.
 * 
 * @author Christopher Foo
 * 
 */
public class Parser {
    /**
     * The {@link ParseTokens} resulting from the initial parse of the string
     */
    private List<ParseToken> tokens;

    /**
     * The IDs of parent tokens in the {@link tokens} list.
     */
    private List<String> parentIds;

    /**
     * The parse trees derived from the {@link tokens} list.
     */
    private List<Tree<ParseToken>> parseTrees;

    /**
     * Create a new Parser and initialize all of the lists and fields.
     */
    public Parser() {
        this.tokens = new ArrayList<ParseToken>();
        this.parentIds = new ArrayList<String>();
        this.parseTrees = new ArrayList<Tree<ParseToken>>();
    }

    /**
     * Returns all {@link ParseToken}s from the latest call to
     * {@link parseToTokens} that are direct children (1 level, child not
     * grand-child or further) of the token with the given ID.
     * 
     * @param parentId
     *            The ID of the parent token that the direct children should be
     *            found for.
     * @return A list of {@link ParseToken}s that are the direct children of the
     *         token with the given ID.
     */
    private List<ParseToken> getDirectChildrenTokens(String parentId) {
        ArrayList<ParseToken> children = new ArrayList<ParseToken>();
        for (ParseToken token : this.tokens) {
            if (token.getParent() == parentId
                    || (token.getParent() != null && token.getParent().equals(
                            parentId))) {
                children.add(token);
            }
        }

        return children;
    }

    /**
     * Gets a list of {@link ParseToken}s that are the roots of parsed semantic
     * structures.
     * 
     * @return A list of {@link ParseToken}s that are the roots of the parsed
     *         strings.
     */
    public List<ParseToken> getRootTokens() {
        return this.getDirectChildrenTokens(null);
    }

    /**
     * Finds the {@link ParseToken} with the given ID.
     * 
     * @param id
     *            The ID of the {@link ParseToken} to find.
     * @return The token with the matching ID or null if it was not found.
     */
    public ParseToken findId(String id) {
        if (id == null) {
            return null;
        }
        for (ParseToken token : this.tokens) {
            if (token.getId() != null && token.getId().equals(id)) {
                return token;
            }
        }
        return null;
    }

    /**
     * Parses the arguments of a single token in a semantic representation
     * passed to the Parser.
     * 
     * @param token
     *            The {@link ParseToken} object that is currently being built by
     *            the Parser.
     * @param args
     *            The arguments of the token.
     * @throws CouldNotParseException
     *             If the arguments do not match the expected patterns.
     */
    private void parseArgs(ParseToken token, String[] args)
            throws CouldNotParseException {
        if (args.length > 2) {
            throw new CouldNotParseException();
        } else if (args.length == 1) {
            String arg = args[0].trim();
            if (Character.isLowerCase(arg.charAt(0))
                    && !this.parentIds.contains(arg)) {
                token.setId(arg);
                this.parentIds.add(arg);
            }

            else {
                throw new CouldNotParseException();
            }
        }

        else if (args.length == 2) {
            String arg1 = args[0].trim();
            String arg2 = args[1].trim();
            // Must have a parent in this case, the first argument is the ID of
            // the parent
            if (Character.isLowerCase(arg1.charAt(0))
                    && this.parentIds.contains(arg1.trim())) {
                token.setParent(arg1);

                // If it is a parent itself, the second argument will be a lower
                // case
                if (Character.isLowerCase(arg2.charAt(0))) {

                    // Can't have duplicates with the same ID
                    if (this.parentIds.contains(arg2)) {
                        throw new CouldNotParseException();
                    }

                    else {
                        token.setId(arg2);
                        this.parentIds.add(arg2);
                    }
                }

                else {
                    token.setValue(arg2.replace("\"", "").replace("'", "")
                            .trim());
                }
            }

            else {
                throw new CouldNotParseException();
            }
        }

        else {
            throw new CouldNotParseException();
        }
    }

    /**
     * Parses the given semantic representation into tokens stored in the
     * {@link tokens} field.
     * 
     * @param string
     *            The semantic representation of the sentence.
     * @throws CouldNotParseException
     *             The given string does not match the expected semantic
     *             representation.
     */
    private void parseToTokens(String string) throws CouldNotParseException {

        // Make sure lists are clear
        this.parentIds.clear();
        this.tokens.clear();

        String stringCopy = new String(string).trim();
        int foundIndex;
        String[] args;
        while (!stringCopy.isEmpty()) {
            ParseToken token = new ParseToken();
            switch (stringCopy.charAt(0)) {
            case ',':
                token.setLogic(LogicOp.AND);

                // Check for negation after the AND
                int i = 1;

                // Skip the whitespace
                char currentChar = stringCopy.charAt(i);
                while (currentChar == ' ' || currentChar == '\t') {
                    currentChar = stringCopy.charAt(++i);
                }
                if (currentChar == '-') {
                    token.setNegated(true);
                    stringCopy = stringCopy.substring(i + 1).trim();
                } else {
                    stringCopy = stringCopy.substring(1).trim();
                }
                break;
            case ';':
                token.setLogic(LogicOp.OR);

                // Check for negation after the OR
                i = 1;

                // Skip the whitespace
                currentChar = stringCopy.charAt(i);
                while (currentChar == ' ' || currentChar == '\t') {
                    currentChar = stringCopy.charAt(++i);
                }
                if (currentChar == '-') {
                    token.setNegated(true);
                    stringCopy = stringCopy.substring(i + 1).trim();
                } else {
                    stringCopy = stringCopy.substring(1).trim();
                }
                break;
            case '-':
                token.setNegated(true);
                stringCopy = stringCopy.substring(1).trim();
                break;
            }

            foundIndex = stringCopy.indexOf('(');

            // No ( for the start of the argument list, syntax error
            if (foundIndex == -1) {
                throw new CouldNotParseException("Error: Syntax error in \""
                        + stringCopy + "\".  Could not parse.");
            }

            token.setType(Character.toUpperCase(stringCopy.charAt(0)) + stringCopy.substring(1, foundIndex));
            stringCopy = stringCopy.substring(foundIndex + 1).trim();

            foundIndex = stringCopy.indexOf(')');

            // No ) for the end of the argument list, syntax error
            if (foundIndex == -1) {
                throw new CouldNotParseException("Error: Syntax error in \""
                        + stringCopy + "\".  Could not parse.");
            }

            // Parse arguments
            args = stringCopy.substring(0, foundIndex).split(",");
            try {
                parseArgs(token, args);
            }

            catch (CouldNotParseException e) {
                throw new CouldNotParseException("Error: Syntax error in \""
                        + stringCopy + "\".  Could not parse.");
            }

            this.tokens.add(token);
            stringCopy = stringCopy.substring(foundIndex + 1).trim();
        }

        // Empty parentIds list, do not need it until parseToTokens is called
        // again.
        this.parentIds.clear();
    }

    /**
     * Recursive function used to parse the {@link tokens} into {@link Tree}
     * structures in the {@link parseTrees} field.
     * 
     * @param parent
     *            The parent of current token.
     * @param current
     *            The token that to parse into a Tree.
     */
    private void parseToTree(Tree<ParseToken> parent, ParseToken current) {
        Tree<ParseToken> thisNode;
        if (parent == null) {
            thisNode = new Tree<ParseToken>(current, null);
            this.parseTrees.add(thisNode);
        } else {
            thisNode = parent.addChild(current);
        }

        String rootId = current.getId();
        if (rootId != null) {
            for (ParseToken child : this.getDirectChildrenTokens(rootId)) {
                this.parseToTree(thisNode, child);
            }
        }
    }

    /**
     * Parses the entire {@link tokens} list into {@link Tree} structures stored
     * in the {@link parseTree} field.
     */
    private void parseToTree() {

        // Make sure the parseTree is clear
        this.parseTrees.clear();
        for (ParseToken root : this.getRootTokens()) {
            parseToTree(null, root);
        }

        // The tokens list is not needed any more
        this.tokens.clear();
    }

    /**
     * Parses the given semantic representation into its {@link Tree} form.
     * 
     * @param string
     *            The semantic representation in string form.
     * @throws CouldNotParseException
     *             If the given string does not match the expected semantic
     *             representation.
     */
    public void parse(String string) throws CouldNotParseException {
        this.parseToTokens(string); // Remove any whitespace
        this.parseToTree();
    }

    /**
     * Generates all of the {@link Sentence}s for the most recent parse.
     * 
     * @return A list of Sentences for the most recent parse.
     */
    public List<Sentence> generateSentences() {
        ArrayList<Sentence> sentences = new ArrayList<Sentence>();
        for (Tree<ParseToken> root : this.parseTrees) {
            String type = root.getNode().getType();
            if (type.equalsIgnoreCase("buy")) {
                sentences.add(new BuySentence(root));
            }

            else if (type.equalsIgnoreCase("sell")) {
                sentences.add(new SellSentence(root));
            }

            else if (type.equalsIgnoreCase("findgroup")) {
                sentences.add(new FindGroupSentence(root));
            }

            else if (type.equalsIgnoreCase("findmore")) {
                sentences.add(new FindMoreSentence(root));
            }

            else {
                // Unrecognized sentence, ignore.
            }
        }
        return sentences;
    }

    /**
     * Recursive function that prints the structure of the parse based on the
     * tokens parsed.
     * 
     * @param indentation
     *            The current indentation level for this token.
     * @param currentToken
     *            The current token.
     * @return A string representing the structure of the this token and its
     *         children from the parse.
     */
    private String printParse(String indentation, ParseToken currentToken) {
        StringBuilder returnString = new StringBuilder();

        returnString.append(indentation + currentToken.toString() + "\n");
        if (currentToken.getId() != null) {
            for (ParseToken token : this.getDirectChildrenTokens(currentToken
                    .getId())) {

                returnString.append(printParse(indentation + "\t", token));
            }
        }

        return returnString.toString();
    }

    @Override
    /**
     * If the tree is parsed, it will print the semantic tree based on the parse tree. 
     * If the tree is not parsed, but the tokens are it will print the semantic tree based
     * on the tokens.
     */
    public String toString() {
        StringBuilder returnString = new StringBuilder();
        if (this.parseTrees.size() > 0) {
            for (Tree<ParseToken> tree : this.parseTrees) {
                returnString.append(tree.toString() + "\n");
            }
        }

        else {

            for (ParseToken root : this.getRootTokens()) {
                returnString.append(this.printParse("", root));
            }
        }
        return returnString.toString();
    }
}
