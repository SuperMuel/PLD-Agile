package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.DeliveryRequest;
import fr.insalyon.heptabits.pldagile.model.Map;
import fr.insalyon.heptabits.pldagile.model.RoadMap;

import java.time.LocalTime;
import java.util.Collection;

public interface RoadMapOptimizer {
        RoadMap optimize(Collection<DeliveryRequest> requests, Map map, LocalTime departureTime) throws ImpossibleRoadMapException;

}
