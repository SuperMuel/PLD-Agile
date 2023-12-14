package fr.insalyon.heptabits.pldagile.model;

import java.util.Locale;

/**
 * Represents an intersection.
 * <p>
 * Stores the latitude and longitude of the intersection.
 * <p>
 * The id is inherited from BaseEntity.
 * <p>
 * All fields are final.
 */
public class Intersection extends BaseEntity {

    private final float latitude;
    private final float longitude;

    /**
     * Creates a new intersection.
     *
     * @param id the id of the intersection
     * @param latitude the latitude of the intersection
     * @param longitude the longitude of the intersection
     *
     * No validation is done on the latitude and longitude.
     */
    public Intersection(long id, float latitude, float longitude) {
        super(id);
        this.latitude = latitude;
        this.longitude = longitude;
    }


    /**
     * @return the latitude of the intersection
     */
    public float getLatitude() {
        return latitude;
    }

    /**
     * @return the longitude of the intersection
     */
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

    /**
     * @return a pretty printed version of the latitude and longitude.
     *
     * Example: "45.7541, 4.8576"
     */
    public String latLongPrettyPrint() {
        return String.format(Locale.FRANCE, "%.4f, %.4f", latitude, longitude);
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

