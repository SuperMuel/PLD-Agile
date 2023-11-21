package fr.insalyon.heptabits.pldagile.model;

public class Segment extends BaseEntity {
    private final float lengthMeters;

    public Segment(long id, float lengthMeters) {
        super(id);
        this.lengthMeters = lengthMeters;
    }

    public float getLengthMeters() {
        return lengthMeters;
    }
}
