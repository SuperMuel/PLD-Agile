package fr.insalyon.heptabits.pldagile.model;

import java.util.HashMap;
import java.util.List;


public class Map extends BaseEntity {

    private final HashMap<Long, Intersection> intersections;

    private final HashMap<Integer, Long> intersectionsHashMap;

    public final int COURIER_SPEED = 250;

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

        List<Intersection> intersectionsList = this.getIntersections().values().stream().toList();
        intersectionsHashMap = new HashMap<>();
        int iterator = 0;

        for(Intersection m : intersectionsList){
            intersectionsHashMap.put(iterator, m.getId());
            iterator++;
        }

        this.boundaries = computeBoundaries();

    }


    public HashMap<Long, Intersection> getIntersections() {
        //immutable view
        return new HashMap<>(intersections);
    }

    public HashMap<Integer, Long> getIntersectionsHashMap() {
        //immutable view
        return new HashMap<>(intersectionsHashMap);
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

    public Segment getSegmentByOriginAndDestination(long originId, long destinationId){
        for(Segment s : segments){
            if(s.getOriginId() == originId && s.getDestinationId() == destinationId){
                return s;
            }
        }
        return null;
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

    public double[][] getAdjacencyMatrix() {
        List<Intersection> intersectionsList = this.getIntersections().values().stream().toList();
        double[][] cost = new double[intersectionsList.size()][intersectionsList.size()];

        for (java.util.Map.Entry i :intersectionsHashMap.entrySet()){
            for(java.util.Map.Entry j: intersectionsHashMap.entrySet()){
                if(i.getKey() == j.getKey()) cost[(int)i.getKey()][(int)j.getKey()] = -1;
                else {
                    Segment chosenSegment = this.getSegmentByOriginAndDestination((long)i.getValue(), (long)j.getValue());
                    if(chosenSegment != null){
                        cost[(int)i.getKey()][(int)j.getKey()] = chosenSegment.getLength() / COURIER_SPEED;
                    } else {
                        cost[(int)i.getKey()][(int)j.getKey()] = -1;
                    }
                }
            }
        }

        return cost;
    }

    public int getKeyIntersection(Intersection intersection){
        for(java.util.Map.Entry i :intersectionsHashMap.entrySet()){
            if(i.getValue().equals(intersection.getId())) {
                return (int) i.getKey();
            }
        }
        return -1;
    }

    public Intersection getIntersectionByKey(int key){
        for(java.util.Map.Entry i :intersectionsHashMap.entrySet()){
            if(i.getKey().equals(key)) {
                return (Intersection) intersections.get(i.getValue());
            }
        }
        return null;
    }
}
