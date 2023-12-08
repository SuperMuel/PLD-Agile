package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.Delivery;
import fr.insalyon.heptabits.pldagile.model.DeliveryRequest;

public interface IRoadMapService {

    public void addRequest(DeliveryRequest newRequest) throws ImpossibleRoadMapException;

}
