package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;



public class NaiveRoadMapOptimizer implements RoadMapOptimizer {
    @Override
    public RoadMap optimize(Collection<DeliveryRequest> requests, Map map, LocalTime departureTime) throws ImpossibleRoadMapException {
        final Duration DELIVERY_DURATION = Duration.ofMinutes(5);
        final double COURIER_SPEED_MS = 15.0 / 3.6; // 15 km/h


        // Verify that all requests are after departure
        for(DeliveryRequest request : requests) {
            if (request.getTimeWindow().getEnd().isBefore(departureTime) || request.getTimeWindow().getEnd().equals(departureTime)) {
                throw new ImpossibleRoadMapException("Timewindow ends before or at departure");
            }
        }

        // Trier les requêtes par timewindow
        List<DeliveryRequest> sortedRequests = new ArrayList<DeliveryRequest>(requests);
        sortedRequests.sort((r1, r2) -> r1.getTimeWindow().compareStartTo(r2.getTimeWindow()));


        List<Leg> legs = new ArrayList<Leg>();
        List<Delivery> deliveries = new ArrayList<Delivery>();

        // We start at the warehouse
        Intersection previousIntersection = map.getWarehouse();
        LocalTime previousTime = departureTime;
        for (DeliveryRequest request : sortedRequests) {
            List<Intersection> path = map.getShortestPath(previousIntersection, request.getDestination()); // TODO : make this return an Itinerary object
            if (path == null) {
                throw new ImpossibleRoadMapException("No path found between " + previousIntersection + " and " + request.getDestination());
            }

            List<Segment> segments = map.getSegmentsBetween(path);

            // Compute the time it takes to go from the previous intersection to the first intersection of the path
            double totalDistance = Segment.getTotalLength(segments);
            Duration duration = Duration.ofSeconds((long) (totalDistance / COURIER_SPEED_MS));


            LocalTime arrivalAtDeliveryLocationTime = previousTime.plus(duration)
                    .plus(DELIVERY_DURATION.dividedBy(2)); // The time to get out of the vehicle and get to the door

            // If arrival strictly before the start of the timewindow, we wait
            if(arrivalAtDeliveryLocationTime.isBefore(request.getTimeWindow().getStart())){
                arrivalAtDeliveryLocationTime = request.getTimeWindow().getStart();
            }

            // If arrival strictly after the end of the timewindow
            if(arrivalAtDeliveryLocationTime.isAfter(request.getTimeWindow().getEnd())){
                throw new ImpossibleRoadMapException("The computation let to a delivery after the end of the time window");
            }

            Leg leg = new Leg(path, segments, previousTime);
            legs.add(leg);

            Delivery delivery =request.toDelivery(-1, arrivalAtDeliveryLocationTime);
            deliveries.add(delivery);

            previousTime = arrivalAtDeliveryLocationTime.plus(DELIVERY_DURATION.dividedBy(2)); // The time to get back in the vehicle
            previousIntersection = path.getLast();
        }

        // Add the last leg to get back at warehouse
        List<Intersection> path = map.getShortestPath(previousIntersection, map.getWarehouse());
        List<Segment> segments = map.getSegmentsBetween(path);
        Leg leg = new Leg(path, segments, previousTime);
        legs.add(leg);

        return new RoadMap(-1, deliveries, legs);
    }
}
