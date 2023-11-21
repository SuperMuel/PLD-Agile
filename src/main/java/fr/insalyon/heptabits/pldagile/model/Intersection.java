package fr.insalyon.heptabits.pldagile.model;

public class Intersection extends BaseEntity {

    private final float latitude;
    private final float longitude;

    public Intersection(long id, float latitude, float longitude) {
        super(id);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float distance(Intersection other) {
        throw new UnsupportedOperationException();
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

}
