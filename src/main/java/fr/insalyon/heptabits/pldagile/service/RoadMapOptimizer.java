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

        default void verifyRequests(Collection<DeliveryRequest> requests, LocalTime departureTime, Map map) throws ImpossibleRoadMapException{
                if (requests.isEmpty()) {
                        throw new IllegalArgumentException("No requests");
                }

                // Check if a request is before departure time
                for (DeliveryRequest request : requests) {
                        if (request.getTimeWindow().getEnd().isBefore(departureTime)) {
                                throw new IllegalArgumentException("One request's time window ends before the courier's departure. The user shouldn't have been able to create such a request.");
                        }
                }


                // Check if a request is made at warehouse
                for (DeliveryRequest request : requests) {
                        if (request.getDestination().equals(map.getWarehouse())) {
                                throw new IllegalArgumentException("A request is made at the warehouse");
                        }
                }

                // Assert that all requests are for the same day
                if (requests.stream().map(DeliveryRequest::getDate).distinct().count() != 1) {
                        // This could be an issue in the future if we want to make night deliveries. But for now, we don't.
                        throw new IllegalArgumentException("All requests must be for the same day");
                }

                // Assert that all requests are for the same courier
                if (requests.stream().map(DeliveryRequest::getCourierId).distinct().count() != 1) {
                        throw new IllegalArgumentException("All requests must be for the same courier");
                }
        }
}
