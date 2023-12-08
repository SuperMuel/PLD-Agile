package fr.insalyon.heptabits.pldagile.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Map extends BaseEntity {

    private final HashMap<Long, Intersection> intersections;

    private final List<Segment> segments;

    private final long warehouseId;

    private final MapBoundaries boundaries;

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

        this.boundaries = computeBoundaries();

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


    private MapBoundaries computeBoundaries() {
        // compute boundaries in one loop
        float minLatitude = Float.MAX_VALUE;
        float minLongitude = Float.MAX_VALUE;
        float maxLatitude = Float.MIN_VALUE;
        float maxLongitude = Float.MIN_VALUE;

        for (Intersection i : intersections.values()) {
            if (i.getLatitude() < minLatitude) {
                minLatitude = i.getLatitude();
            }
            if (i.getLatitude() > maxLatitude) {
                maxLatitude = i.getLatitude();
            }
            if (i.getLongitude() < minLongitude) {
                minLongitude = i.getLongitude();
            }
            if (i.getLongitude() > maxLongitude) {
                maxLongitude = i.getLongitude();
            }
        }

        return new MapBoundaries(minLatitude, maxLatitude, minLongitude, maxLongitude);
    }

    public MapBoundaries getBoundaries() {
        return boundaries;
    }

    public float getMinLatitude() {
        return boundaries.minLatitude();
    }

    public float getMinLongitude() {
        return boundaries.minLongitude();
    }

    public float getMaxLatitude() {
    return boundaries.maxLatitude();
    }

    public float getMaxLongitude() {
      return boundaries.maxLongitude();
    }

    @Override
    public String toString() {
        return "Map{" +
                "intersections=" + intersections +
                ", segments=" + segments +
                ", warehouseId=" + warehouseId +
                '}';
    }

    public Intersection getOriginOf(Segment segment) {
        return intersections.get(segment.getOriginId());
    }

    public Intersection getDestinationOf(Segment segment) {
        return intersections.get(segment.getDestinationId());
    }

    public List<Segment> getOutgoingSegments(Intersection intersection) {
        List<Segment> outgoingSegments = new ArrayList<>();
        for (Segment s : segments) {
            if (getOriginOf(s).equals(intersection)) {
                outgoingSegments.add(s);
            } else if (getDestinationOf(s).equals(intersection)) {
                outgoingSegments.add(s);
            }
        }
        return outgoingSegments;
    }


    public List<Intersection> getNeighbors(Intersection intersection) {
        List<Intersection> neighbors = new ArrayList<>();
        for (Segment s : getOutgoingSegments(intersection)) {
            if (getOriginOf(s).equals(intersection)) {
                neighbors.add(getDestinationOf(s));
            } else if (getDestinationOf(s).equals(intersection)) {
                neighbors.add(getOriginOf(s));
            }
        }
        return neighbors;
    }


    private double distanceBetween(Intersection origin, Intersection destination) {
        for (Segment s : getOutgoingSegments(origin)) {
            if (getOriginOf(s).equals(origin) && getDestinationOf(s).equals(destination)) {
                return s.getLength();
            } else if (getDestinationOf(s).equals(origin) && getOriginOf(s).equals(destination)) {
                return s.getLength();
            }
        }
        throw new IllegalArgumentException("No segment between " + origin + " and " + destination);
    }

    public List<Intersection> getShortestPath(Intersection origin, Intersection destination) {
        HashMap<Intersection, Double> distances = new HashMap<>();
        HashMap<Intersection, Intersection> previous = new HashMap<>();
        HashMap<Intersection, Boolean> visited = new HashMap<>();
        for (Intersection i : intersections.values()) {
            distances.put(i, Double.MAX_VALUE);
            previous.put(i, null);
            visited.put(i, false);
        }
        distances.put(origin, 0.0);
        Intersection current = origin;
        while (current != null) {
            visited.put(current, true);
            for(Intersection neighbor : getNeighbors(current)) {
                if (!visited.get(neighbor)) {
                    double newDistance = distances.get(current) + distanceBetween(current, neighbor);
                    if (newDistance < distances.get(neighbor)) {
                        distances.put(neighbor, newDistance);
                        previous.put(neighbor, current);
                    }
                }
            }
            current = null;
            double minDistance = Double.MAX_VALUE;
            for (Intersection i : intersections.values()) {
                if (!visited.get(i) && distances.get(i) < minDistance) {
                    current = i;
                    minDistance = distances.get(i);
                }
            }
        }
        if (distances.get(destination) == Float.MAX_VALUE) {
            return null;
        }
        List<Intersection> path = new ArrayList<>();
        current = destination;
        while (current != null) {
            path.addFirst(current);
            current = previous.get(current);
        }
        return path;
    }

    public Segment getSegmentBetween(Intersection origin, Intersection destination) {
        for (Segment s : getOutgoingSegments(origin)) {
            if (getOriginOf(s).equals(origin) && getDestinationOf(s).equals(destination)) {
                return s;
            } else if (getDestinationOf(s).equals(origin) && getOriginOf(s).equals(destination)) {
                return s;
            }
        }
        throw new IllegalArgumentException("No segment between " + origin + " and " + destination);
    }

    public List<Segment> getSegmentsBetween(List<Intersection> path){
        List<Segment> segments = new ArrayList<>();
        for (int i = 0; i < path.size() - 1; i++) {
            segments.add(getSegmentBetween(path.get(i), path.get(i + 1)));
        }
        return segments;
    }

}