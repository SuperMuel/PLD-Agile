package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.DeliveryRequest;
import fr.insalyon.heptabits.pldagile.model.Map;
import fr.insalyon.heptabits.pldagile.model.RoadMap;

import java.util.Collection;

public interface RoadMapOptimizer {

        public RoadMap optimize(Collection<DeliveryRequest> requests, Map map) throws ImpossibleRoadMapException;
}
