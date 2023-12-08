package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.Intersection;
import fr.insalyon.heptabits.pldagile.model.Map;
import fr.insalyon.heptabits.pldagile.model.Segment;

import java.util.HashMap;
import java.util.List;

public class MockMapService implements MapService {

    Intersection i1 = new Intersection(1, -2, -1);
    Intersection i2 = new Intersection(2, 3, 1);
    Intersection i3 = new Intersection(3, 2, 5);
    Intersection i4 = new Intersection(4, 3, -2);
    Intersection i5 = new Intersection(5, 6, 1);

    int warehouseId = 3;
    final Intersection warehouse = i3;

    HashMap<Long, Intersection> intersections;
    List<Segment> segments;

    Segment s12 = new Segment(1, i1.getId(), i2.getId(), "Street 12", 5.385164807134504);
    Segment s23 = new Segment(2, i2.getId(), i3.getId(), "Street 23", 4.123105625617661);
    Segment s24 = new Segment(3, i2.getId(), i4.getId(), "Street 24", 3.0);
    Segment s25 = new Segment(4, i2.getId(), i5.getId(), "Street 25", 3.0);
    Segment s31 = new Segment(5, i3.getId(), i1.getId(), "Street 31", 7.211102550927978);
    Segment s35 = new Segment(6, i3.getId(), i5.getId(), "Street 35", 5.656854249492381);


    @Override
    public Map getCurrentMap() {
        intersections = new HashMap<>();

        intersections.put(i1.getId(), i1);
        intersections.put(i2.getId(), i2);
        intersections.put(i3.getId(), i3);
        intersections.put(i4.getId(), i4);
        intersections.put(i5.getId(), i5);

        segments = List.of(s12, s23, s24, s25, s31, s35);

        return new Map(1, intersections, segments, 3);
    }
}