package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.DeliveryRequest;

public interface IRoadMapService {

    void addRequest(DeliveryRequest newRequest) throws ImpossibleRoadMapException;

}
