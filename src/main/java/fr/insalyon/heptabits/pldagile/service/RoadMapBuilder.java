package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.*;

import java.util.List;

/**
 * Interface for the road map builder.
 * <p>
 * The road map builder is responsible for building a valid road map from an ordered list of delivery requests.
 */
public interface RoadMapBuilder {

    /**
     * Builds a road map from an ordered list of delivery requests.
     *
     * @param sortedRequests the sorted list of delivery requests
     * @param map           the map on which the road map is built
     * @return the built road map
     * @throws ImpossibleRoadMapException if the road map cannot be built
     */
    RoadMap buildRoadMapFromSortedRequests(List<DeliveryRequest> sortedRequests, Map map) throws ImpossibleRoadMapException;

}
