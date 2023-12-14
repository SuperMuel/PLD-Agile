package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.*;

import java.util.List;

public interface RoadMapBuilder {
    RoadMap buildRoadMapFromSortedRequests(List<DeliveryRequest> sortedRequests, Map map) throws ImpossibleRoadMapException;

}
