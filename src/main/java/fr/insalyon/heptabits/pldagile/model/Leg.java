package fr.insalyon.heptabits.pldagile.model;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
        return intersections.getLast();
    }

    public Intersection getOrigin() {
        return intersections.getFirst();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Leg leg = (Leg) o;
        return Objects.equals(intersections, leg.intersections) && Objects.equals(segments, leg.segments) && Objects.equals(departureTime, leg.departureTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(intersections, segments, departureTime);
    }

    @Override
    public String toString() {
        return "Leg{" +
                "intersections=" + intersections + "\n" +
                ", segments=" + segments + "\n" +
                ", departureTime=" + departureTime + "\n" +
                '}';
    }
}
