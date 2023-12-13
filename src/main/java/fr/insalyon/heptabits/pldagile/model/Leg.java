package fr.insalyon.heptabits.pldagile.model;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

/**
 * Represents a part of a RoadMap between two deliveries.
 *
 * Stores the intersections and the segments between them.
 *
 * The origin, the destination of the leg, as well as all the other intermediate
 * intersections are stored. Thus, the number of intersections is always equal to the number of segments + 1.
 *
 * The departure time is the time at which the courier leaves the origin of the leg.
 *
 * All fields are final.
 */
public record Leg(List<Intersection> intersections, List<Segment> segments, LocalTime departureTime) {

    /**
     * @return an immutable list of the intersections of the leg
     */
    public List<Intersection> intersections() {
        return Collections.unmodifiableList(intersections);
    }

    /**
     * @return an immutable list of the segments of the leg
     */
    public List<Segment> segments() {
        return Collections.unmodifiableList(segments);
    }


    /**
     * Creates a new leg.
     *
     * Describes a path between two deliveries. `intersections` should contain every intersection
     * where the courier will go through, including the origin and the destination of the leg.
     * `segments` should contain every segment between the intersections.
     * The number of intersections must be equal to the number of segments + 1.
     *
     * @param intersections the intersections of the leg. Must not be null and not empty.
     * @param segments the segments of the leg. Must not be null and not empty.
     * @param departureTime the departure time of the leg. Must not be null.
     */
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


    /**
     * @return the destination of the leg
     */
    public Intersection getDestination() {
        return intersections.getLast();
    }

    /**
     * @return the origin of the leg
     */
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

    public double getTotalLength() {
        return Segment.getTotalLength(segments);
    }
}

