package org.insa.graphs.model.io;

import java.io.IOException;

/**
 * Exception thrown when a format-error is detected when reading a graph (e.g.,
 * non-matching check bytes).
 */
public class BadFormatException extends IOException {

    /**
     *
     */
    private static final long serialVersionUID = 1270945933549613579L;

    /**
     *
     */
    public BadFormatException() {
        super();
    }

    /**
     * Create a new format exception with the given message.
     *
     * @param message Message for the exception.
     */
    public BadFormatException(String message) {
        super(message);
    }

}
