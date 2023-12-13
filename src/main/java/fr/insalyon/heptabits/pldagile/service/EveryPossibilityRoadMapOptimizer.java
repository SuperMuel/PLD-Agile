package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.DeliveryRequest;
import fr.insalyon.heptabits.pldagile.model.Map;
import fr.insalyon.heptabits.pldagile.model.RoadMap;
import org.apache.commons.collections4.iterators.PermutationIterator;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EveryPossibilityRoadMapOptimizer implements RoadMapOptimizer {

    private final RoadMapBuilder roadMapBuilder;

    public EveryPossibilityRoadMapOptimizer(RoadMapBuilder roadMapBuilder) {
        this.roadMapBuilder = roadMapBuilder;
    }


    @Override
    public RoadMap optimize(Collection<DeliveryRequest> requests, Map map, LocalTime departureTime) throws ImpossibleRoadMapException {
        verifyRequests(requests, departureTime, map);

        RoadMap bestRoadMap = null;
        double bestRoadMapCost = Double.MAX_VALUE;

        List<DeliveryRequest> originalRequests = new ArrayList<>(requests);

        PermutationIterator<DeliveryRequest> iterator = new PermutationIterator<>(originalRequests);
        while (iterator.hasNext()) {
            List<DeliveryRequest> perm = iterator.next();
            try {
                // Try to build a valid road map from the permutation
                RoadMap roadMap = roadMapBuilder.buildRoadMapFromSortedRequests(perm, map);

                // Compute the cost of the road map
                double roadMapCost = roadMap.getTotalLength();
                if (roadMapCost < bestRoadMapCost) {
                    bestRoadMap = roadMap;
                    bestRoadMapCost = roadMapCost;
                }
            } catch (ImpossibleRoadMapException e) {
                // Ignore
            }
        }

        if(bestRoadMap == null) {
            throw new ImpossibleRoadMapException("No possible road map");
        }


        return bestRoadMap;
    }
}
