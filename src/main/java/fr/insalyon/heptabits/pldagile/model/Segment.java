package fr.insalyon.heptabits.pldagile.model;

import java.util.List;
import java.util.Objects;

/**
 * Represents a segment.
 * <p>
 * Stores the origin, the destination, the name and the length.
 * <p>
 * We consider an origin and a destination to be consistent with the map files we read. Howerver,
 * the segment is not oriented, so the origin and the destination are interchangeable.
 * <p>
 * All fields are final.
 */
public record Segment(Intersection origin, Intersection destination, String name, double length) {


    /**
     * @return the id of the origin of the segment
     */
    public long getOriginId() {
        return origin.getId();
    }

    /**
     * @return the id of the origin of the segment
     */
    public long getDestinationId() {
        return destination.getId();
    }


    /**
     * @param segments
     * @return the total length of the segments
     */
    static public double getTotalLength(List<Segment> segments) {
        return segments.stream().mapToDouble(Segment::length).sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Segment segment = (Segment) o;
        return Double.compare(length, segment.length) == 0 && Objects.equals(origin, segment.origin) && Objects.equals(destination, segment.destination) && Objects.equals(name, segment.name);
    }
}
