package fr.insalyon.heptabits.pldagile.model;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

public record Leg(List<Intersection> intersections, List<Segment> segments, LocalTime departureTime) {
    @Override
    public List<Intersection> intersections() {
        return Collections.unmodifiableList(intersections);
    }

    @Override
    public List<Segment> segments() {
        return Collections.unmodifiableList(segments);
    }

    public Leg {
        if (intersections == null || segments == null || departureTime == null) {
            throw new IllegalArgumentException("Leg constructor: null argument");
        }

        if (intersections.isEmpty()) {
            throw new IllegalArgumentException("Leg constructor: empty intersections list");
        }

        if (segments.isEmpty()) {
            final String errorMessage = """
                    Leg constructor: empty segments list
                                        
                    A leg must have at least one segment. If you want to represent that you have multiple deliveries
                    at the same place, we should change the 'Delivery' model, and give it a "list of packages" attribute.
                                        
                    This would allow us to have multiple packages delivered at the same place at the same timeWindow.
                    """;
            throw new IllegalArgumentException(errorMessage);
        }

        if (intersections.size() != segments.size() + 1) {
            throw new IllegalArgumentException("Leg constructor: intersections and segments size mismatch");
        }

    }


    public Intersection getDestination() {
        return intersections.getLast();
    }

    public Intersection getOrigin() {
        return intersections.getFirst();
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
