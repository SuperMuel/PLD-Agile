package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.DeliveryRequest;
import fr.insalyon.heptabits.pldagile.model.Map;
import fr.insalyon.heptabits.pldagile.model.RoadMap;

import java.time.LocalTime;
import java.util.Collection;

/**
 * An interface for the road map optimizer.
 */
public interface RoadMapOptimizer {
        /**
         * Optimizes a road map.
         *
         * @param requests the delivery requests
         * @param map the map
         * @param departureTime the departure time
         * @return the optimized road map
         * @throws ImpossibleRoadMapException if it is impossible to create a road map with the given constraints
         */
        RoadMap optimize(Collection<DeliveryRequest> requests, Map map, LocalTime departureTime) throws ImpossibleRoadMapException;

}
