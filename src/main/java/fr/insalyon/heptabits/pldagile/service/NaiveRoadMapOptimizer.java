package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.*;

import java.time.Duration;
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

    private final double courierSpeedMs;
    private final Duration deliveryDuration;


    /**
     * Default constructor
     * Courier speed is 15 km/h
     * Delivery duration is 5 minutes
     */
    public NaiveRoadMapOptimizer() {
        this.deliveryDuration = Duration.ofMinutes(5);
        this.courierSpeedMs = 15.0 / 3.6; // 15 km/h
    }

    /**
     * Constructor
     *
     * @param courierSpeedMs   Courier speed in m/s
     * @param deliveryDuration Duration of a delivery
     */
    public NaiveRoadMapOptimizer(double courierSpeedMs, Duration deliveryDuration) {
        this.courierSpeedMs = courierSpeedMs;
        this.deliveryDuration = deliveryDuration;
    }


    /**
     * Checks if a request is made before the departure time
     *
     * @param requests      The requests to check
     * @param departureTime The departure time
     * @return true if a request is made before the departure time, false otherwise
     */
    private boolean aRequestIsBeforeDeparture(Collection<DeliveryRequest> requests, LocalTime departureTime) {
        for (DeliveryRequest request : requests) {
            if (request.getTimeWindow().getEnd().isBefore(departureTime)) {
                return true;
            }
        }
        return false;
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
        if (requests.isEmpty()) {
            throw new IllegalArgumentException("No requests");
        }

        if (aRequestIsBeforeDeparture(requests, departureTime)) {
            throw new IllegalArgumentException("One request's time window ends before the courier's departure. The user shouldn't have been able to create such a request.");
        }

        // Check if a request is made at warehouse
        for (DeliveryRequest request : requests) {
            if (request.getDestination().equals(map.getWarehouse())) {
                throw new IllegalArgumentException("A request is made at the warehouse");
            }
        }

        // Assert that all requests are for the same day
        if (requests.stream().map(DeliveryRequest::getDate).distinct().count() != 1) {
            // This could be an issue in the future if we want to make night deliveries. But for now, we don't.
            throw new IllegalArgumentException("All requests must be for the same day");
        }

        // Sort requests by timeWindow start
        List<DeliveryRequest> sortedRequests = new ArrayList<>(requests);
        sortedRequests.sort((r1, r2) -> r1.getTimeWindow().compareStartTo(r2.getTimeWindow()));
        //TODO : on same timewindows, apply tsp

        List<Leg> legs = new ArrayList<>();
        List<Delivery> deliveries = new ArrayList<>();

        // We start at the warehouse
        Intersection previousIntersection = map.getWarehouse();
        LocalTime previousTime = departureTime;
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

        return new RoadMap(-1, deliveries, legs);
    }
}
