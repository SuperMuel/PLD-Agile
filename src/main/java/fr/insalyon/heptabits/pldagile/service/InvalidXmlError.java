package fr.insalyon.heptabits.pldagile.service;

/**
 * This class is used to represent an error in the xml format
 */
public class InvalidXmlError extends Exception {
    /**
     * Creates a new invalid xml error
     *
     * @param message the error message
     */
    public InvalidXmlError(String message) {
        super(message);
    }
}
