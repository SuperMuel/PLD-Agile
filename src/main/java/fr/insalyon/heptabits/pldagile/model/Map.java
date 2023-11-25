package fr.insalyon.heptabits.pldagile.model;

import java.util.ArrayList;
import java.util.List;

public class Map extends  BaseEntity{
    private final List<Intersection> intersections;

    private final List<Segment> segments;

    /**
     * Constructor
     * @param id the id of the map
     */
    public Map(long id){
        super(id);
        this.intersections = new ArrayList<>();
        this.segments = new ArrayList<>();
    }

    /**
     * Constructor
     * @param id the id of the map
     * @param intersections the list of intersections
     * @param segments the list of segments
     */
    public Map(long id, List<Intersection> intersections, List<Segment> segments) {
        super(id);
        this.intersections = intersections;
        this.segments = segments;
    }


    /**
     * Get an immutable view of intersections
     * @return the list of intersections
     */
    public List<Intersection> getIntersections() {
        //immutable view
        return List.copyOf(intersections);
    }

    /**
     * Get an immutable view of segments
     * @return the list of segments
     */
    public List<Segment> getSegments() {
        //immutable view
        return List.copyOf(segments);
    }


    /**
     * Add a segment to the map
     * @param segment the segment to add
     * @throws IllegalArgumentException if the segment id already exists
     */
    public void addSegment(Segment segment){
        // check if segment id isn't already in the list
        for (Segment s : segments) {
            if (s.getId() == segment.getId()) {
                throw new IllegalArgumentException("Segment id already exists");
            }
        }
        segments.add(segment);
    }

    /**
     * Add an intersection to the map
     * @param intersection the intersection to add
     * @throws IllegalArgumentException if the intersection id already exists
     */
    public void addIntersection(Intersection intersection){
        // check if intersection id isn't already in the list
        for (Intersection i : intersections) {
            if (i.getId() == intersection.getId()) {
                throw new IllegalArgumentException("Intersection id already exists");
            }
        }
        intersections.add(intersection);
    }



}
