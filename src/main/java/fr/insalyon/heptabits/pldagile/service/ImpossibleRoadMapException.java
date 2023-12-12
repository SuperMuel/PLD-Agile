package fr.insalyon.heptabits.pldagile.service;

/**
 * Exception thrown when it is impossible to create a road map with the given constraints.
 */
public class ImpossibleRoadMapException extends Exception{
    public ImpossibleRoadMapException(String message) {
        super(message);
    }

    public ImpossibleRoadMapException() {
        super("Impossible to create a road map with the given constraints.");
    }

}
