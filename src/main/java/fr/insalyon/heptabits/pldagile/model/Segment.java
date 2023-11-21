package fr.insalyon.heptabits.pldagile.model;

public class Segment extends BaseEntity {
    private final float lengthMeters;
    private final long destination;
    private final long origin;
    private final String name;

    public Segment(long destination, float lengthMeters, String name, long origin) {
        super(0);
        this.lengthMeters = lengthMeters;
        this.origin = origin;
        this.name = name;
        this.destination = destination;
    }

    public float getLengthMeters() {
        return lengthMeters;
    }

    public long getDestination() {
        return destination;
    }

    public long getOrigin() {
        return origin;
    }

    public String getName() {
        return name;
    }
}
