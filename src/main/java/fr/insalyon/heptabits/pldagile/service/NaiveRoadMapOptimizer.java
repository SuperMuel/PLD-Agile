package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * A naive implementation of the RoadMapOptimizer interface.
 * <p>
 * It sorts the requests by time window start, and then computes
 * the shortest path between each request.
 *
 */
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


    @Override
    public RoadMap optimize(Collection<DeliveryRequest> requests, Map map, LocalTime departureTime) throws ImpossibleRoadMapException {
        verifyRequests(requests, departureTime, map);

        // Sort requests by timeWindow start
        List<DeliveryRequest> sortedRequests = new ArrayList<>(requests);
        sortedRequests.sort((r1, r2) -> r1.getTimeWindow().compareStartTo(r2.getTimeWindow()));

        return roadMapBuilder.buildRoadMapFromSortedRequests(sortedRequests, map);
    }



}
