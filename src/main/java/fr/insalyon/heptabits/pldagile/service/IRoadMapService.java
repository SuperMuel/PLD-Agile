package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.Delivery;
import fr.insalyon.heptabits.pldagile.model.DeliveryRequest;

public interface IRoadMapService {

    /**
     * Add a delivery request to the road map by calculating the best route.
     * The road map is then updated with the new delivery.
     * @param request the delivery request to add
     * @return the delivery created
     * @throws ImpossibleRoadMapException if it's not possible to add the delivery request to the road map.
     */
    public Delivery addRequest(DeliveryRequest request, long idCourier) throws ImpossibleRoadMapException;

}
