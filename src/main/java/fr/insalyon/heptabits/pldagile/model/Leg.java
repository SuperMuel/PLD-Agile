package fr.insalyon.heptabits.pldagile.model;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

public record Leg(List<Intersection> getIntersections, List<Segment> getSegments, LocalTime departureTime) {
    public List<Intersection> getIntersections() {
        return Collections.unmodifiableList(getIntersections);
    }

    public List<Segment> getSegments() {
        return Collections.unmodifiableList(getSegments);
    }

    public Leg {
        if (getIntersections == null || getSegments == null || departureTime == null) {
            throw new IllegalArgumentException("Leg constructor: null argument");
        }

        if (getIntersections.isEmpty()) {
            throw new IllegalArgumentException("Leg constructor: empty getIntersections list");
        }

        if (getSegments.isEmpty()) {
            final String errorMessage = """
                    Leg constructor: empty getSegments list
                                        
                    A leg must have at least one segment. If you want to represent that you have multiple deliveries
                    at the same place, we should change the 'Delivery' model, and give it a "list of packages" attribute.
                                        
                    This would allow us to have multiple packages delivered at the same place at the same timeWindow.
                    """;
            throw new IllegalArgumentException(errorMessage);
        }

        if (getIntersections.size() != getSegments.size() + 1) {
            throw new IllegalArgumentException("Leg constructor: getIntersections and getSegments size mismatch");
        }

    }


    public Intersection getDestination() {
        return getIntersections.getLast();
    }

    public Intersection getOrigin() {
        return getIntersections.getFirst();
    }

    @Override
    public String toString() {
        return "Leg{" +
                "getIntersections=" + getIntersections + "\n" +
                ", getSegments=" + getSegments + "\n" +
                ", departureTime=" + departureTime + "\n" +
                '}';
    }
}
