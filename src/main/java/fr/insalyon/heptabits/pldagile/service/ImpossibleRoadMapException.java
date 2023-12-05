package fr.insalyon.heptabits.pldagile.service;

public class ImpossibleRoadMapException extends Exception{
    public ImpossibleRoadMapException(String message) {
        super(message);
    }

    public ImpossibleRoadMapException() {
        super("Impossible to create a road map with the given constraints.");
    }

}
