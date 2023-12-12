package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.DeliveryRequest;


/**
 * An interface for the road map service.
 */
public interface IRoadMapService {

    /**
     * Adds a delivery request to the road map service.
     *
     * @param newRequest the delivery request to add
     * @throws ImpossibleRoadMapException if it is impossible to create a road map with the given constraints
     */
    void addRequest(DeliveryRequest newRequest) throws ImpossibleRoadMapException;

}
