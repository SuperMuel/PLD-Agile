package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.*;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link RoadMapBuilder} interface.
 * <p>
 * This implementation is responsible for building a valid road map from an ordered list of delivery requests.
 */
public class RoadMapBuilderImpl implements RoadMapBuilder {

    private final IdGenerator idGenerator;

    private final Duration deliveryDuration;
    private final LocalTime minDepartureTime;

    private final double courierSpeedMs;

    /**
     * Creates a new road map builder.
     *
     * @param idGenerator       the ID generator
     * @param deliveryDuration  the duration of a delivery
     * @param minDepartureTime  the minimum departure time
     * @param courierSpeedMs    the speed of the courier in m/s
     */
    public RoadMapBuilderImpl(IdGenerator idGenerator, Duration deliveryDuration, LocalTime minDepartureTime, double courierSpeedMs) {
        this.idGenerator = idGenerator;
        this.deliveryDuration = deliveryDuration;
        this.minDepartureTime = minDepartureTime;
        this.courierSpeedMs = courierSpeedMs;
    }


    /**
     * Creates a valid road map from an ordered list of delivery requests.
     *
     * The road map starts at the warehouse, then goes to each delivery location in the order of the list of delivery
     * requests, and finally goes back to the warehouse.
     *
     * The courier takes the shortest path between each location. If it arrives before the start of the time window,
     * it waits until the start of the time window. If it arrives after the end of the time window, the road map is
     * invalid and an exception is thrown.
     *
     * If no path is found between two locations, the road map is invalid and an exception is thrown.
     *
     * @param sortedRequests the sorted list of delivery requests
     * @param map            the map on which the road map is built
     * @return the built road map
     * @throws ImpossibleRoadMapException if the road map cannot be built
     */
    @Override
    public RoadMap buildRoadMapFromSortedRequests(List<DeliveryRequest> sortedRequests, Map map) throws ImpossibleRoadMapException {
        // TODO : handle error where there are no requests
        if(sortedRequests.isEmpty()){
            throw new IllegalArgumentException("List of requests is empty. Can't build a roadMap.");
        }

        List<Leg> legs = new ArrayList<>();
        List<Delivery> deliveries = new ArrayList<>();

        // We start at the warehouse
        Intersection previousIntersection = map.getWarehouse();
        LocalTime previousTime = minDepartureTime;
        for (DeliveryRequest request : sortedRequests) {
            List<Intersection> path = map.getShortestPath(previousIntersection, request.getDestination()); // TODO : make this return an Itinerary object
            if (path == null) {
                throw new ImpossibleRoadMapException("No path found between " + previousIntersection + " and " + request.getDestination());
            }

            List<Segment> segments = map.getShortestSegmentsBetween(path);

            // Compute the time it takes to go from the previous intersection to the first intersection of the path
            double totalDistance = Segment.getTotalLength(segments);
            Duration duration = Duration.ofSeconds((long) (totalDistance / courierSpeedMs));


            LocalTime arrivalAtDeliveryLocationTime = previousTime.plus(duration)
                    .plus(deliveryDuration.dividedBy(2)); // The time to get out of the vehicle and get to the door

            // If arrival strictly before the start of the timewindow, we wait
            if (arrivalAtDeliveryLocationTime.isBefore(request.getTimeWindow().getStart())) {
                arrivalAtDeliveryLocationTime = request.getTimeWindow().getStart();
            }

            // If arrival strictly after the end of the timewindow
            if (arrivalAtDeliveryLocationTime.isAfter(request.getTimeWindow().getEnd())) {
                throw new ImpossibleRoadMapException("The computation let to a delivery after the end of the time window");
            }

            Leg leg = new Leg(path, segments, previousTime);
            legs.add(leg);

            Delivery delivery = request.toDelivery(arrivalAtDeliveryLocationTime); //TODO : remove deliveryId
            deliveries.add(delivery);

            previousTime = arrivalAtDeliveryLocationTime.plus(deliveryDuration.dividedBy(2)); // The time to get back in the vehicle
            previousIntersection = path.getLast();
        }

        // Add the last leg to get back at warehouse
        List<Intersection> path = map.getShortestPath(previousIntersection, map.getWarehouse());
        List<Segment> segments = map.getShortestSegmentsBetween(path);
        Leg leg = new Leg(path, segments, previousTime);
        legs.add(leg);

        return new RoadMap(idGenerator.getNextId(), deliveries, legs);
    }

}
