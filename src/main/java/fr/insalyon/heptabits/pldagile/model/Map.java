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
     *
     * @param origin      the origin intersection
     * @param destination the destination intersection
     * @return the shortest distance between the two intersections
     */
    private double shortestDistanceBetween(Intersection origin, Intersection destination) {
        return getShortestSegmentBetween(origin, destination).getLength();
    }

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

    public boolean connectsIntersections(Segment s, Intersection origin, Intersection destination) {
        return (getOriginOf(s).equals(origin) && getDestinationOf(s).equals(destination)) ||
                (getDestinationOf(s).equals(origin) && getOriginOf(s).equals(destination));
    }


    /**
     * Get the shortest segment between two intersections, or null if there is no segment between them.
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
                if (s.getLength() < shortestDistance) {
                    shortestSegment = s;
                    shortestDistance = s.getLength();
                }
            }
        }
        return shortestSegment;
    }

    public List<Segment> getAllSegmentsBetween(Intersection a, Intersection b){
        List<Segment> segments = new ArrayList<>();
        for (Segment s : getConnectedSegments(a)) {
            if (connectsIntersections(s, a, b)) {
                segments.add(s);
            }
        }
        return segments;
    }


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