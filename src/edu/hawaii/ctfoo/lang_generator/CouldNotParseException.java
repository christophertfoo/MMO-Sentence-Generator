package edu.hawaii.ctfoo.lang_generator;

/**
 * An {@link Exception} to be thrown when the parser cannot parse the given
 * string.
 * 
 * @author Christopher Foo
 * 
 */
public class CouldNotParseException extends Exception {

    /**
     * Automatically generated ID.
     */
    private static final long serialVersionUID = 8075760826309446797L;

    /**
     * Creates a new blank CouldNotParseException.
     */
    public CouldNotParseException() {
        super();
    }

    /**
     * Creates a new CouldNotParseException with the given message.
     * 
     * @param message
     *            The message for the Exception.
     */
    public CouldNotParseException(String message) {
        super(message);
    }

}
