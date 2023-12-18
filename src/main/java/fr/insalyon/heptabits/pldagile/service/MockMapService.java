package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.Intersection;
import fr.insalyon.heptabits.pldagile.model.Map;
import fr.insalyon.heptabits.pldagile.model.Segment;

import java.util.HashMap;
import java.util.List;

/**
 * A mock for the map service.
 */
public class MockMapService implements MapService {

    /**
     * Creates a new mock map service.
     */
    public MockMapService() {
    }

    final Intersection i1 = new Intersection(1, -2, -1);
    final Intersection i2 = new Intersection(2, 3, 1);
    final Intersection i3 = new Intersection(3, 2, 5);
    final Intersection i4 = new Intersection(4, 3, -2);
    final Intersection i5 = new Intersection(5, 6, 1);

    final int warehouseId = 3;
    final Intersection warehouse = i3;

    HashMap<Long, Intersection> intersections;
    List<Segment> segments;

    final Segment s12 = new Segment( i1, i2, "Street 12", 5.385164807134504);
    final Segment s23 = new Segment( i2, i3, "Street 23", 4.123105625617661);
    final Segment s24 = new Segment( i2, i4, "Street 24", 3.0);
    final Segment s25 = new Segment( i2, i5, "Street 25", 3.0);
    final Segment s31 = new Segment( i3, i1, "Street 31", 7.211102550927978);
    final Segment s35 = new Segment( i3, i5, "Street 35", 5.656854249492381);


    @Override
    public Map getCurrentMap() {
        intersections = new HashMap<>();

        intersections.put(i1.getId(), i1);
        intersections.put(i2.getId(), i2);
        intersections.put(i3.getId(), i3);
        intersections.put(i4.getId(), i4);
        intersections.put(i5.getId(), i5);

        segments = List.of(s12, s23, s24, s25, s31, s35);

        return new Map(1, intersections, segments, warehouseId);
    }
}
