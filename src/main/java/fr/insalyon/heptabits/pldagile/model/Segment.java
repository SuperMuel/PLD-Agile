package fr.insalyon.heptabits.pldagile.model;

import java.util.List;
import java.util.Objects;

public record Segment(Intersection origin, Intersection destination, String name, double length) {

    @Override
    public String toString() {
        return "Segment{" +
                "origin=" + origin +
                ", destination=" + destination +
                ", name='" + name + '\'' +
                ", length=" + length +
                '}';
    }


    static public double getTotalLength(List<Segment> segments) {
        double totalLength = 0;
        for (Segment segment : segments) {
            totalLength += segment.length();
        }
        return totalLength;
    }

    public long getOriginId() {
        return origin.getId();
    }

    public long getDestinationId() {
        return destination.getId();
    }

    public String getName(){ return name;}
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Segment segment = (Segment) o;
        return Double.compare(length, segment.length) == 0 && Objects.equals(origin, segment.origin) && Objects.equals(destination, segment.destination) && Objects.equals(name, segment.name);
    }
}
