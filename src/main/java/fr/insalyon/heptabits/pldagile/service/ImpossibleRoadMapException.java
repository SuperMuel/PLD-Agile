package fr.insalyon.heptabits.pldagile.service;

/**
 * Exception thrown when it is impossible to create a road map with the given constraints.
 */
public class ImpossibleRoadMapException extends Exception{

    /**
     * Creates a new impossible road map exception.
     *
     * @param message the error message
     */
    public ImpossibleRoadMapException(String message) {
        super(message);
    }

}
