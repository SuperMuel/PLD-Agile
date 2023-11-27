package fr.insalyon.heptabits.pldagile.model;

public class MapBoundaries {
    final float minLatitude;
    final float maxLatitude;
    final float minLongitude;
    final float maxLongitude;

    public MapBoundaries(float minLatitude, float maxLatitude, float minLongitude, float maxLongitude) {
        this.minLatitude = minLatitude;
        this.maxLatitude = maxLatitude;
        this.minLongitude = minLongitude;
        this.maxLongitude = maxLongitude;
    }

    public float getMinLatitude() {
        return minLatitude;
    }

    public float getMaxLatitude() {
        return maxLatitude;
    }

    public float getMinLongitude() {
        return minLongitude;
    }

    public float getMaxLongitude() {
        return maxLongitude;
    }

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
