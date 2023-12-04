package fr.insalyon.heptabits.pldagile.model;

import fr.insalyon.heptabits.pldagile.model.Intersection;
import fr.insalyon.heptabits.pldagile.model.Map;
import fr.insalyon.heptabits.pldagile.model.Segment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapTest {

    Map map;

    @BeforeEach
    void setUp() {
        HashMap<Long, Intersection> intersections = new HashMap<>();

        Intersection i1 = new Intersection(0, -1, 1);
        Intersection i2 = new Intersection(1, 2, 3);
        Intersection i3 = new Intersection(2, 4, 5);
        Intersection i4 = new Intersection(3, 6, 7);
        Segment s1 = new Segment(0, i1.getId(), i2.getId(), "Street 1", 1.1);
        Segment s2 = new Segment(1, i1.getId(), i3.getId(), "Street 2", 2.2);
        Segment s3 = new Segment(2, i2.getId(), i4.getId(), "Street 3", 3.3);
        Segment s4 = new Segment(3, i3.getId(), i4.getId(), "Street 4", 4.4);

        intersections.put(i1.getId(), i1);
        intersections.put(i2.getId(), i2);
        intersections.put(i3.getId(), i3);
        intersections.put(i4.getId(), i4);

        List<Segment> segments = new ArrayList<>();
        segments.add(s1);
        segments.add(s2);
        segments.add(s3);
        segments.add(s4);

        map = new Map(0, intersections, segments, 3);

    }

    @Test
    void createMapWithInexistingWarehouse() {
        HashMap<Long, Intersection> intersections = new HashMap<>();

        Intersection i1 = new Intersection(0, -1, 1);
        Intersection i2 = new Intersection(1, 2, 3);
        Segment s1 = new Segment(0, i1.getId(), i2.getId(), "Street 1", 1.1);

        intersections.put(i1.getId(), i1);
        intersections.put(i2.getId(), i2);

        List<Segment> segments = new ArrayList<>();
        segments.add(s1);


        assertThrows(IllegalArgumentException.class, () -> new Map(0, intersections, segments, 42));
    }

    @Test
    void getIntersections() {
        assertEquals(4, map.getIntersections().size());
    }

    @Test
    void getSegments() {
        assertEquals(4, map.getSegments().size());
    }


    @Test
    void getWarehouseId() {
        assertEquals(3, map.getWarehouseId());
    }

    @Test
    void getWarehouse() {
        assertEquals(3, map.getWarehouse().getId());
    }

    @Test
    void getMinLatitude() {
        assertEquals(-1, map.getMinLatitude());
    }

    @Test
    void getMinLongitude() {
        assertEquals(1, map.getMinLongitude());
    }

    @Test
    void getMaxLatitude() {
        assertEquals(6, map.getMaxLatitude());
    }

    @Test
    void getMaxLongitude() {
        assertEquals(7, map.getMaxLongitude());
    }
}