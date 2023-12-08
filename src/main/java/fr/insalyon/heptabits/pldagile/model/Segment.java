package fr.insalyon.heptabits.pldagile.model;

import java.util.List;

public class Segment extends BaseEntity {

    private final long originId;
    private final long destinationId;
    private final String name;
    private final double length;

    public Segment(long id, long originId, long destinationId, String name, double length) {
        super(id);
        this.originId = originId;
        this.destinationId = destinationId;
        this.name = name;
        this.length = length;
    }

    public long getOriginId() {
        return originId;
    }

    public long getDestinationId() {
        return destinationId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Segment{" +
                "id=" + getId() +
                ", originId=" + originId +
                ", destinationId=" + destinationId +
                ", name='" + name + '\'' +
                '}';
    }


    public double getLength() {
        return length;
    }


    static public double getTotalLength(List<Segment> segments) {
        double totalLength = 0;
        for (Segment segment : segments) {
            totalLength += segment.getLength();
        }
        return totalLength;
    }



}
