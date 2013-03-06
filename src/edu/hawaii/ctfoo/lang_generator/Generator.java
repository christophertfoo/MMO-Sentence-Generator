package edu.hawaii.ctfoo.lang_generator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import edu.hawaii.ctfoo.lang_generator.sentence.Sentence;

/**
 * Reads in neo-Davidsonian like semantic representations from STDIN and prints
 * out the sentence forms of those representations. Also provides some String
 * manipulation function to be used in generating the sentences.
 * 
 * @author Christopher Foo
 * 
 */
public class Generator {

    /**
     * The {@link Parser} object used to parse the input from STDIN.
     */
    private Parser parser;

    /**
     * A {@link BufferedReader} to STDIN used to read in the representations.
     */
    private BufferedReader in;

    /**
     * Creates a new Generator and initializes its fields.
     */
    public Generator() {
        this.parser = new Parser();
        this.in = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Closes the input stream.
     */
    public void closeStream() {
        try {
            this.in.close();
        } catch (IOException e) {
            System.err.println("Error: Could not close input stream.");
        }
    }

    /**
     * Attempts to change the given string to it's plural form.
     * 
     * @param string
     *            The String that should be made into its plural form.
     * @return The plural form of the given string.
     */
    public static String pluralize(String string) {
        char lastChar = Character
                .toLowerCase(string.charAt(string.length() - 1));
        if (lastChar == 'y') {
            return string.substring(0, string.length() - 1) + "ies";
        } else if (lastChar == 's') {
            return string + "es";
        } else {
            return string + "s";
        }
    }

    /**
     * Appends the given list to the given builder as a comma separated list
     * terminated by the given terminator string.
     * 
     * @param objects
     *            The list of objects to be appended.
     * @param builder
     *            The {@link StringBuilder} to append the created list to.
     * @param terminator
     *            The terminator string of the entire list.
     * @param innerListTerminator
     *            The terminator strings of the sublists. The first will be used
     *            to terminate the first level sublists, the second the second
     *            level sublists, etc.
     */
    public static void appendCommaList(List<?> objects, StringBuilder builder,
            String terminator, String... innerListTerminator) {
        int listLength = objects.size();

        // Special case: Only 2 elements
        if (listLength == 2) {

            // If element is a list, recursive calls
            if (objects.get(0) instanceof List<?>) {
                if (innerListTerminator.length < 1) {
                    appendCommaList((List<?>) objects.get(0), builder, "and");
                    builder.append(" " + terminator + " ");
                    appendCommaList((List<?>) objects.get(1), builder, "and");
                } else {
                    appendCommaList((List<?>) objects.get(0), builder,
                            innerListTerminator[0], Arrays.copyOfRange(
                                    innerListTerminator, 1,
                                    innerListTerminator.length));
                    builder.append(" " + terminator + " ");
                    appendCommaList((List<?>) objects.get(1), builder,
                            innerListTerminator[0], Arrays.copyOfRange(
                                    innerListTerminator, 1,
                                    innerListTerminator.length));
                }
            }

            // Otherwise, just append special case;
            else {
                builder.append(objects.get(0) + " " + terminator + " "
                        + objects.get(1));
            }
            return;
        }

        // Either 1 or fewer elements or more than 2
        else {
            for (int i = 0; i < listLength; i++) {

                // If element is a list, recursive calls
                if (objects.get(i) instanceof List<?>) {
                    if (innerListTerminator.length < 1) {
                        appendCommaList((List<?>) objects.get(i), builder,
                                "and");
                    } else {
                        appendCommaList((List<?>) objects.get(i), builder,
                                innerListTerminator[0], Arrays.copyOfRange(
                                        innerListTerminator, 1,
                                        innerListTerminator.length));
                    }
                }

                // Otherwise, just append
                else {
                    builder.append(objects.get(i).toString().trim());
                }

                // Handle comma separators
                if (i < listLength - 2) {
                    builder.append(", ");
                } else if (i == listLength - 2) {
                    builder.append(", " + terminator + " ");
                }
            }
            return;
        }
    }

    /**
     * Appends the given list as a String to the given builder separated by the
     * delimiter.
     * 
     * @param objects
     *            The list of objects to append to the builder.
     * @param builder
     *            The {@link StringBuilder} to append to.
     * @param delimiter
     *            The delimiter to go between each element of the list.
     */
    public static void appendDelimiterList(List<?> objects,
            StringBuilder builder, String delimiter) {
        Iterator<?> listIterator = objects.listIterator();
        while (listIterator.hasNext()) {
            builder.append(listIterator.next().toString().trim());
            if (listIterator.hasNext()) {
                builder.append(delimiter);
            } else {
                // There is no next, exit the loop to save a check
                break;
            }
        }
    }

    /**
     * Runs the Language Generator.
     * 
     * @param args
     *            [0] = If "-t" option it will print out the parse trees of the
     *            entered representations.
     */
    public static void main(String[] args) {
        Generator generator = new Generator();
        boolean showTree = false;
        if (args.length > 0 && args[0].equalsIgnoreCase("-t")) {
            showTree = true;
        }
        try {
            String input = generator.in.readLine();
            while (input != null) {
                try {
                    generator.parser.parse(input);
                    List<Sentence> sentences = generator.parser
                            .generateSentences();
                    for (Sentence sentence : sentences) {
                        System.out.println(sentence);
                    }

                    if (showTree) {
                        System.out.println(generator.parser);
                    }
                } catch (CouldNotParseException e) {
                    System.err.println(e.getMessage());
                }
                input = generator.in.readLine();
            }
        }

        catch (IOException e2) {
            System.err.println("Error: Could not read from STDIN.");
        }

        finally {
            generator.closeStream();
        }
    }
}
