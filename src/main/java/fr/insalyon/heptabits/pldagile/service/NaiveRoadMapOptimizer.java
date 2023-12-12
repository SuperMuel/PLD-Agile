package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class NaiveRoadMapOptimizer implements RoadMapOptimizer {
    private final RoadMapBuilder roadMapBuilder;

    /**
     * Default constructor
     * Courier speed is 15 km/h
     * Delivery duration is 5 minutes
     */
    public NaiveRoadMapOptimizer(RoadMapBuilder roadMapBuilder) {
        this.roadMapBuilder = roadMapBuilder;
    }


    private boolean aRequestIsBeforeDeparture(Collection<DeliveryRequest> requests, LocalTime departureTime) {
        for (DeliveryRequest request : requests) {
            if (request.getTimeWindow().getEnd().isBefore(departureTime)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public RoadMap optimize(Collection<DeliveryRequest> requests, Map map, LocalTime departureTime) throws ImpossibleRoadMapException {
        if (requests.isEmpty()) {
            throw new IllegalArgumentException("No requests");
        }

        if (aRequestIsBeforeDeparture(requests, departureTime)) {
            throw new IllegalArgumentException("One request's time window ends before the courier's departure. The user shouldn't have been able to create such a request.");
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

        // Sort requests by timeWindow start
        List<DeliveryRequest> sortedRequests = new ArrayList<>(requests);
        sortedRequests.sort((r1, r2) -> r1.getTimeWindow().compareStartTo(r2.getTimeWindow()));
        //TODO : on same timewindows, apply tsp


        return roadMapBuilder.buildRoadMapFromSortedRequests(sortedRequests, map);
    }



}
