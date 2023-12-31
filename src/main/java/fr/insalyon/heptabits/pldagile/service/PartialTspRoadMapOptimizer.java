package fr.insalyon.heptabits.pldagile.service;

import org.apache.commons.collections4.iterators.PermutationIterator;

import fr.insalyon.heptabits.pldagile.model.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.lang.Double.MAX_VALUE;

/**
 * This class is used to find and return an optimal road map per time window. To do this, it starts by sorting the road
 * map by time window, then for each time window, it looks at which of all the possible permutations has the lowest
 * cost. Finally, it uses the RoadMapBuilder to check that the deadlines have been met.
 */
public class PartialTspRoadMapOptimizer implements RoadMapOptimizer {
    private final RoadMapBuilder roadMapBuilder;

    /**
     * Creates a new partial tsp road map optimizer.
     *
     * @param roadMapBuilder the road map builder to use
     */
    public PartialTspRoadMapOptimizer(RoadMapBuilder roadMapBuilder) {
        this.roadMapBuilder = roadMapBuilder;
    }


    /**
     * @param requests      all the delivery requests
     * @param departureTime the time of departure
     * @param map           the map
     * @throws ImpossibleRoadMapException if the road map is impossible to build
     */
    @Override
    public RoadMap optimize(Collection<DeliveryRequest> requests, Map map, LocalTime departureTime) throws ImpossibleRoadMapException {
        verifyRequests(requests, departureTime, map);

        // Sort requests by timeWindow start
        List<DeliveryRequest> sortedRequests = new ArrayList<>(requests);
        sortedRequests.sort((r1, r2) -> r1.getTimeWindow().compareStartTo(r2.getTimeWindow()));

        List<DeliveryRequest> sortedOptimizedRequests = new ArrayList<>();

        List<DeliveryRequest> requestsByTimeWindow = new ArrayList<>();
        TimeWindow timeWindow = sortedRequests.getFirst().getTimeWindow();
        Intersection start = map.getWarehouse();
        for (DeliveryRequest request : sortedRequests) {
            if (timeWindow.getStart() != request.getTimeWindow().getStart()) {
                sortedOptimizedRequests.addAll(getOptimizedItinerary(requestsByTimeWindow, map, start));
                requestsByTimeWindow.clear();
                start = sortedOptimizedRequests.getLast().getDestination();
                timeWindow = request.getTimeWindow();
            }
            requestsByTimeWindow.add(request);
        }
        sortedOptimizedRequests.addAll(getOptimizedItinerary(requestsByTimeWindow, map, start));

        return roadMapBuilder.buildRoadMapFromSortedRequests(sortedOptimizedRequests, map);
    }

    /**
     *
     *
     * @param requests  all the delivery requests of the same time-window
     * @param map       the map
     * @param start     the point of departure (= warehouse of the map or the last destination of the previous time-window)
     * @return the optimized itinerary of the time-window
     */
    public List<DeliveryRequest> getOptimizedItinerary(List<DeliveryRequest> requests, Map map, Intersection start) {
        // All the possible permutations in the time-window
        List<List<DeliveryRequest>> possiblePaths = generatePaths(requests);

        // Compute the cost of each permutation and return the one with the lowest cost
        List<DeliveryRequest> sortedRequests = new ArrayList<>();
        double minimumCost = MAX_VALUE;
        for (List<DeliveryRequest> possiblePath : possiblePaths) {
            //Initialisation pour chaque itinéraire
            double currentCost = 0;

            //Calcul de la distance entre le départ et le premier élément de la liste
            List<Intersection> firstStepIntersections = map.getShortestPath(start, possiblePath.getFirst().getDestination());
            List<Segment> firstSegments = map.getShortestSegmentsBetween(firstStepIntersections);
            for (Segment firstSegment : firstSegments) {
                currentCost += firstSegment.length();
            }

            //Calcul de la distance entre les différents éléments de la liste
            int j = 0;
            while (minimumCost > currentCost && j < possiblePath.size() - 1) {
                List<Intersection> itinerary = map.getShortestPath(possiblePath.get(j).getDestination(), possiblePath.get(j + 1).getDestination());
                List<Segment> itinerarySegments = map.getShortestSegmentsBetween(itinerary);
                for (Segment itinerarySegment : itinerarySegments) {
                    currentCost += itinerarySegment.length();
                }
                j++;
            }

            if (minimumCost > currentCost) {
                minimumCost = currentCost;
                sortedRequests = possiblePath;
            }
        }

        return sortedRequests;
    }

    /**
     * @param requests all the delivery requests of the same time-window
     * @return all the possible permutations
     */
    private List<List<DeliveryRequest>> generatePaths(List<DeliveryRequest> requests) {
        PermutationIterator<DeliveryRequest> iterator = new PermutationIterator<>(requests);
        List<List<DeliveryRequest>> allPathPossibilities = new ArrayList<>();

        while (iterator.hasNext()) {
            List<DeliveryRequest> perm = iterator.next();
            allPathPossibilities.add(perm);
        }

        return allPathPossibilities;
    }
}

