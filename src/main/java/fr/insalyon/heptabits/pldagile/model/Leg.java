package fr.insalyon.heptabits.pldagile.model;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

public class Leg {
    public List<Intersection> getIntersections() {
        return Collections.unmodifiableList(intersections);
    }

    public List<Segment> getSegments() {
        return Collections.unmodifiableList(segments);
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    private final List<Intersection> intersections;
    private final List<Segment> segments;

    private final LocalTime departureTime;


    public Leg(List<Intersection> intersections, List<Segment> segments, LocalTime departureTime) {
        if(intersections == null || segments==null || departureTime == null){
            throw new IllegalArgumentException("Leg constructor: null argument");
        }

        if(intersections.isEmpty()){
            throw new IllegalArgumentException("Leg constructor: empty intersections list");
        }

        if(segments.isEmpty()){
            final String errorMessage = """
                    Leg constructor: empty segments list
                    
                    A leg must have at least one segment. If you want to represent that you have multiple deliveries
                    at the same place, we should change the 'Delivery' model, and give it a "list of packages" attribute.
                    
                    This would allow us to have multiple packages delivered at the same place at the same timeWindow.
                    """;
            throw new IllegalArgumentException(errorMessage);
        }

        if(intersections.size() != segments.size() + 1){
            throw new IllegalArgumentException("Leg constructor: intersections and segments size mismatch");
        }

        this.intersections = intersections;
        this.segments = segments;
        this.departureTime = departureTime;
    }


    public Intersection getDestination() {
        return intersections.get(intersections.size() - 1);
    }

    public Intersection getOrigin() {
        return intersections.get(0);
    }



}
