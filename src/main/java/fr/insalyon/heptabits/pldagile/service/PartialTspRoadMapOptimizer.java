package fr.insalyon.heptabits.pldagile.service;

import org.apache.commons.collections4.iterators.PermutationIterator;

import fr.insalyon.heptabits.pldagile.model.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.lang.Double.MAX_VALUE;

public class PartialTspRoadMapOptimizer implements RoadMapOptimizer {
    private final RoadMapBuilder roadMapBuilder;

    public PartialTspRoadMapOptimizer(RoadMapBuilder roadMapBuilder) {
        this.roadMapBuilder = roadMapBuilder;
    }

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

    public List<DeliveryRequest> getOptimizedItinerary(List<DeliveryRequest> requests, Map map, Intersection start) {
        //Obtenir la liste de chemins possibles
        List<List<DeliveryRequest>> possiblePaths = generatePaths(requests);

        //Calcul des chemins pour avoir le plus optimal
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

    public List<List<DeliveryRequest>> generatePaths(List<DeliveryRequest> requests) {
        PermutationIterator<DeliveryRequest> iterator = new PermutationIterator<>(requests);
        List<List<DeliveryRequest>> allPathPossibilities = new ArrayList<>();

        while (iterator.hasNext()) {
            List<DeliveryRequest> perm = iterator.next();
            allPathPossibilities.add(perm);
        }

        return allPathPossibilities;
    }
}

