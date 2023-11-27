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

