package fr.insalyon.heptabits.pldagile.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Map extends BaseEntity {

    private final HashMap<Long, Intersection> intersections;

    private final List<Segment> segments;

    private final long warehouseId;

    private final MapBoundaries boundaries;

    /**
     * Creates a new map.
     *
     * @param id            the id of the map
     * @param intersections the list of intersections
     * @param segments      the list of segments
     * @param warehouseId   the id of the warehouse
     *
     * @throws IllegalArgumentException if the warehouse id is not an intersection
     */
    public Map(long id, HashMap<Long, Intersection> intersections, List<Segment> segments, long warehouseId) {
        super(id);
        this.intersections = intersections;
        this.segments = segments;
        this.warehouseId = warehouseId;

        if (!intersections.containsKey(warehouseId)) {
            throw new IllegalArgumentException("Warehouse id must be an intersection");
        }

        // A cache, to avoid recomputing the boundaries every time
        this.boundaries = computeBoundaries();

    }

    /**
     * Get an immutable view of intersections
     *
     * @return the map of intersections
     */
    public java.util.Map<Long, Intersection> getIntersections() {
        //immutable view
        return Collections.unmodifiableMap(intersections);
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


    /**
     * @return the id of the warehouse
     */
    public long getWarehouseId() {
        return warehouseId;
    }

    /**
     * @return the warehouse
     */
    public Intersection getWarehouse() {
        return intersections.get(warehouseId);
    }


    /**
     *  Compute the boundaries of the map in a single loop.
     *  The boundaries are the minimum and maximum latitude and longitude of the map.
     *
     *  @return the boundaries of the map.
     */
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

    /**
     * @return the boundaries of the map
     */
    public MapBoundaries getBoundaries() {
        return boundaries;
    }


    /**
     * @return the minimum latitude of the map
     */
    public float getMinLatitude() {
        return boundaries.minLatitude();
    }

    /**
     * @return the minimum longitude of the map
     */
    public float getMinLongitude() {
        return boundaries.minLongitude();
    }

    /**
     * @return the maximum latitude of the map
     */
    public float getMaxLatitude() {
    return boundaries.maxLatitude();
    }

    /**
     * @return the maximum longitude of the map
     */
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


    /**
     * @return the origin of the segment
     * Note : a segment is not directed.
     */
    public Intersection getOriginOf(Segment segment) {
        return intersections.get(segment.getOriginId());
    }

    /**
     * @return the destination of the segment
     * Note : a segment is not directed.
     */
    public Intersection getDestinationOf(Segment segment) {
        return intersections.get(segment.getDestinationId());
    }

    /**
     * @return all the segments connected to the intersection.
     */
    public List<Segment> getConnectedSegments(Intersection intersection) {
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


    /**
     * @return all the neighbors of the intersection.
     */
    public List<Intersection> getNeighbors(Intersection intersection) {
        List<Intersection> neighbors = new ArrayList<>();
        for (Segment s : getConnectedSegments(intersection)) {
            if (getOriginOf(s).equals(intersection)) {
                neighbors.add(getDestinationOf(s));
            } else if (getDestinationOf(s).equals(intersection)) {
                neighbors.add(getOriginOf(s));
            }
        }
        return neighbors;
    }


    /**
     * Get the shortest distance between two intersections, or null if there is no segment between them.
     * <p>
     * This is useful since there can be multiple segments between two intersections.
     *
     * @param origin      the origin intersection
     * @param destination the destination intersection
     * @return the shortest distance between the two intersections
     */
    private double shortestDistanceBetween(Intersection origin, Intersection destination) {
        return getShortestSegmentBetween(origin, destination).length();
    }

    /**
     * Get the shortest path between two intersections, or null if there is no path between them.
     * <p>
     * Uses Dijkstra's algorithm.*
     *
     * @param origin      the origin intersection
     * @param destination the destination intersection
     * @return the shortest path between the two intersections. The path is a list of intersections, that
     *         starts with the origin and ends with the destination. The segments between the intersections
     *         are not included in the path, but can be obtained with getShortestSegmentsBetween(path).
     */
    public List<Intersection> getShortestPath(Intersection origin, Intersection destination) {
        if(origin.equals(destination)) {
            throw new IllegalArgumentException("Origin and destination must be different.");
        }
        if(!intersections.containsKey(origin.getId())) {
            throw new IllegalArgumentException("Origin must be an intersection of the map.");
        }
        if(!intersections.containsKey(destination.getId())) {
            throw new IllegalArgumentException("Destination must be an intersection of the map.");
        }

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
                    double newDistance = distances.get(current) + shortestDistanceBetween(current, neighbor);
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
        if (distances.get(destination) == Double.MAX_VALUE) {
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


    /**
     * @return true if the segment connects the two intersections, false otherwise.
     */
    public boolean connectsIntersections(Segment s, Intersection origin, Intersection destination) {
        return (getOriginOf(s).equals(origin) && getDestinationOf(s).equals(destination)) ||
                (getDestinationOf(s).equals(origin) && getOriginOf(s).equals(destination));
    }


    /**
     * Get the shortest segment between two intersections, or null if there is no segment between them.
     * <p>
     * This is useful since there can be multiple segments between two intersections.
     *
     * @param origin      the origin intersection
     * @param destination the destination intersection
     * @return the shortest segment between the two intersections
     */
    public Segment getShortestSegmentBetween(Intersection origin, Intersection destination) {
        Segment shortestSegment = null;
        double shortestDistance = Double.MAX_VALUE;
        for (Segment s : getConnectedSegments(origin)) {
            if (connectsIntersections(s, origin, destination)) {
                if (s.length() < shortestDistance) {
                    shortestSegment = s;
                    shortestDistance = s.length();
                }
            }
        }
        return shortestSegment;
    }


    /**
     * Get all the segments between two intersections.
     * <p>
     * This is useful since there can be multiple segments between two intersections.
     * <p>
     * Warning: this method doesn't find a path between the two intersections. It only returns the segments
     * that connect the two intersections.
     *
     * @param a      the origin intersection
     * @param b      the destination intersection
     * @return the segments between the two intersections
     */
    public List<Segment> getAllSegmentsBetween(Intersection a, Intersection b){
        List<Segment> segments = new ArrayList<>();
        for (Segment s : getConnectedSegments(a)) {
            if (connectsIntersections(s, a, b)) {
                segments.add(s);
            }
        }
        return segments;
    }


    /**
     * Get the shortest segments between a list of intersections.
     *
     * @param path      the list of intersections. Must not be null and contain at least two intersections.
     *                  Each pair of consecutive intersections must be connected by at least one segment.
     *                  Each pair of consecutive intersections must be different.
     * @return the shortest segments between the intersections. The segments are ordered in the same way as the
     * intersections.
     */
    public List<Segment> getShortestSegmentsBetween(List<Intersection> path){
        if (path == null) throw new IllegalArgumentException("Path cannot be null.");
        if (path.size() < 2) throw new IllegalArgumentException("Path must contain at least two intersections.");

        // If the path contains two consecutive intersections that are equal, throw an exception
        for (int i = 0; i < path.size() - 1; i++) {
            if (path.get(i).equals(path.get(i + 1))) {
                throw new IllegalArgumentException("Path cannot contain two consecutive equal intersections.");
            }
        }

        List<Segment> segments = new ArrayList<>();
        for (int i = 0; i < path.size() - 1; i++) {
            segments.add(getShortestSegmentBetween(path.get(i), path.get(i + 1)));
        }
        return segments;
    }
}