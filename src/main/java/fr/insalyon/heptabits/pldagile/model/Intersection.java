package fr.insalyon.heptabits.pldagile.model;

import java.util.Objects;

public class Intersection extends BaseEntity {

    private final float latitude;
    private final float longitude;

    public Intersection(long id, float latitude, float longitude) {
        super(id);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float distance(Intersection other) {

        // if other is null thorw exception
        if (other == null) {
            throw new IllegalArgumentException("other is null");
        }
        throw new UnsupportedOperationException();
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    
    @Override
    public String toString() {
        return "Intersection{" +
                "id=" + getId() +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

}

