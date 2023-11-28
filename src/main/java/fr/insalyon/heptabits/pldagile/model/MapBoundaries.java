package fr.insalyon.heptabits.pldagile.model;

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
