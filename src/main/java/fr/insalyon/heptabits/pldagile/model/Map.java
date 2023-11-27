package fr.insalyon.heptabits.pldagile.model;

import java.util.HashMap;
import java.util.List;

public class Map extends BaseEntity {

    private final HashMap<Long, Intersection> intersections;

    private final List<Segment> segments;

    private final long warehouseId;

    /**
     * Constructor
     *
     * @param id            the id of the map
     * @param intersections the list of intersections
     * @param segments      the list of segments
     * @param warehouseId   the id of the warehouse
     */
    public Map(long id, HashMap<Long, Intersection> intersections, List<Segment> segments, long warehouseId) {
        super(id);
        this.intersections = intersections;
        this.segments = segments;
        this.warehouseId = warehouseId;

        if (!intersections.containsKey(warehouseId)) {
            throw new IllegalArgumentException("Warehouse id must be an intersection");
        }
    }


    public HashMap<Long, Intersection> getIntersections() {
        //immutable view
        return new HashMap<>(intersections);
    }

    /**
     * Get an immutable view of segments
     *
     * @return the list of segments
     */
    public List<Segment> getSegments() {
        //immutable view
        return List.copyOf(segments);
    }


    public long getWarehouseId() {
        return warehouseId;
    }

    public Intersection getWarehouse() {
        return intersections.get(warehouseId);
    }

    public float getMinLatitude() {
        float min = Float.MAX_VALUE;
        for (Intersection i : intersections.values()) {
            if (i.getLatitude() < min) {
                min = i.getLatitude();
            }
        }
        return min;
    }

    public float getMinLongitude() {
        float min = Float.MAX_VALUE;
        for (Intersection i : intersections.values()) {
            if (i.getLongitude() < min) {
                min = i.getLongitude();
            }
        }
        return min;
    }

    public float getMaxLatitude() {
        float max = Float.MIN_VALUE;
        for (Intersection i : intersections.values()) {
            if (i.getLatitude() > max) {
                max = i.getLatitude();
            }
        }
        return max;
    }

    public float getMaxLongitude() {
        float max = Float.MIN_VALUE;
        for (Intersection i : intersections.values()) {
            if (i.getLongitude() > max) {
                max = i.getLongitude();
            }
        }
        return max;
    }

    @Override
    public String toString() {
        return "Map{" +
                "intersections=" + intersections +
                ", segments=" + segments +
                ", warehouseId=" + warehouseId +
                '}';
    }


}
