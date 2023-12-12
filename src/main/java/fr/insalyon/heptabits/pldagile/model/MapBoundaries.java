package fr.insalyon.heptabits.pldagile.model;

/**
 * Represents a map boundaries.
 *
 * Stores the minimum and maximum latitude and longitude.
 *
 * All fields are final.
 */
public record MapBoundaries(float minLatitude, float maxLatitude, float minLongitude, float maxLongitude) {

    @Override
    public String toString() {
        return "MapBoundaries{" +
                "minLatitude=" + minLatitude +
                ", maxLatitude=" + maxLatitude +
                ", minLongitude=" + minLongitude +
                ", maxLongitude=" + maxLongitude +
                '}';
    }

}
