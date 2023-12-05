package fr.insalyon.heptabits.pldagile.model;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Intersection)) return false;
        Intersection that = (Intersection) o;
        return that.getId() == getId()
                && Float.compare(that.latitude, latitude) == 0 &&
                Float.compare(that.longitude, longitude) == 0;
    }

}

