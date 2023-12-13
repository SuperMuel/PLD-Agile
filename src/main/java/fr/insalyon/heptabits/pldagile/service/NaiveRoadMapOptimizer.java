package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * A naive implementation of the RoadMapOptimizer interface.
 *
 * It sorts the requests by time window start, and then computes
 * the shortest path between each request.
 *
 */
public class NaiveRoadMapOptimizer implements RoadMapOptimizer {
    private final RoadMapBuilder roadMapBuilder;

    public NaiveRoadMapOptimizer(RoadMapBuilder roadMapBuilder) {
        this.roadMapBuilder = roadMapBuilder;
    }


    /**
     * Optimizes a road map.
     *
     * @param requests      The requests to optimize
     * @param map           The map to use
     * @param departureTime The departure time of the courier from the warehouse
     * @return The optimized road map
     * @throws ImpossibleRoadMapException if the road map is impossible to compute
     */
    @Override
    public RoadMap optimize(Collection<DeliveryRequest> requests, Map map, LocalTime departureTime) throws ImpossibleRoadMapException {
        verifyRequests(requests, departureTime, map);

        // Sort requests by timeWindow start
        List<DeliveryRequest> sortedRequests = new ArrayList<>(requests);
        sortedRequests.sort((r1, r2) -> r1.getTimeWindow().compareStartTo(r2.getTimeWindow()));
        //TODO : on same timewindows, apply tsp

        return roadMapBuilder.buildRoadMapFromSortedRequests(sortedRequests, map);
    }



}
