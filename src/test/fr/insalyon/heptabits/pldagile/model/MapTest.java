package fr.insalyon.heptabits.pldagile.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapTest {

    Map map;

    Intersection i1 = new Intersection(1, -2, -1);
    Intersection i2 = new Intersection(2, 3, 1);
    Intersection i3 = new Intersection(3, 2, 5);
    Intersection i4 = new Intersection(4, 3, -2);
    Intersection i5 = new Intersection(5, 6, 1);

    int warehouseId = 3;
    final Intersection warehouse = i3;

    HashMap<Long, Intersection> intersections;
    List<Segment> segments;

    Segment s12 = new Segment( i1, i2, "Street 12", 5.385164807134504);
    Segment s23 = new Segment( i2, i3, "Street 23", 4.123105625617661);
    Segment s24 = new Segment( i2, i4, "Street 24", 3.0);
    Segment s25 = new Segment(i2, i5, "Street 25", 3.0);
    Segment s31 = new Segment( i3, i1, "Street 31", 7.211102550927978);
    Segment s35 = new Segment( i3, i5, "Street 35", 5.656854249492381);



    @BeforeEach
    void setUp() {
        intersections = new HashMap<>();

        intersections.put(i1.getId(), i1);
        intersections.put(i2.getId(), i2);
        intersections.put(i3.getId(), i3);
        intersections.put(i4.getId(), i4);
        intersections.put(i5.getId(), i5);

        segments = new ArrayList<>();
        segments.add(s12);
        segments.add(s23);
        segments.add(s24);
        segments.add(s25);
        segments.add(s31);
        segments.add(s35);

        map = new Map(0, intersections, segments, 3);

    }

    @Test
    void createMapWithInexistingWarehouse() {
        assertThrows(IllegalArgumentException.class, () -> new Map(0, intersections, segments, 42));
    }

    @Test
    void getIntersections() {
        assertEquals(5, map.getIntersections().size());
    }

    @Test
    void getSegments() {
        assertEquals(6, map.getSegments().size());
    }


    @Test
    void getWarehouseId() {
        assertEquals(warehouseId, map.getWarehouseId());
    }

    @Test
    void getWarehouse() {
        assertEquals(warehouse, map.getWarehouse());
    }

    @Test
    void getMinLatitude() {
        assertEquals(-2, map.getMinLatitude());
    }

    @Test
    void getMinLongitude() {
        assertEquals(-2, map.getMinLongitude());
    }

    @Test
    void getMaxLatitude() {
        assertEquals(6, map.getMaxLatitude());
    }

    @Test
    void getMaxLongitude() {
        assertEquals(5, map.getMaxLongitude());
    }

    @Test
    void getBoundaries() {
        assertEquals(-2, map.getBoundaries().minLatitude());
        assertEquals(-2, map.getBoundaries().minLongitude());
        assertEquals(6, map.getBoundaries().maxLatitude());
        assertEquals(5, map.getBoundaries().maxLongitude());
    }

    @Test
    void getOriginOf() {
        assertEquals(i1, map.getOriginOf(s12));
        assertEquals(i2, map.getOriginOf(s23));
        assertEquals(i2, map.getOriginOf(s24));
        assertEquals(i2, map.getOriginOf(s25));
        assertEquals(i3, map.getOriginOf(s31));
        assertEquals(i3, map.getOriginOf(s35));
    }


    @Test
    void getDestinationOf() {
        assertEquals(i2, map.getDestinationOf(s12));
        assertEquals(i3, map.getDestinationOf(s23));
        assertEquals(i4, map.getDestinationOf(s24));
        assertEquals(i5, map.getDestinationOf(s25));
        assertEquals(i1, map.getDestinationOf(s31));
        assertEquals(i5, map.getDestinationOf(s35));
    }

    @Test
    void getConnectedSegments() {
        assertEquals(2, map.getConnectedSegments(i1).size());
        assertEquals(4, map.getConnectedSegments(i2).size());
        assertEquals(3, map.getConnectedSegments(i3).size());
        assertEquals(1, map.getConnectedSegments(i4).size());
        assertEquals(2, map.getConnectedSegments(i5).size());
    }

    @Test
    void getNeighbors() {
        assertEquals(2, map.getNeighbors(i1).size());
        assertEquals(4, map.getNeighbors(i2).size());
        assertEquals(3, map.getNeighbors(i3).size());
        assertEquals(1, map.getNeighbors(i4).size());
        assertEquals(2, map.getNeighbors(i5).size());
    }

    @Test
    void getIslandNeighbors() {
        // An island is a set of getIntersections that are not connected to the rest of the map.

        // create island
        Intersection i6 = new Intersection(6, 10, 10);
        intersections.put(i6.getId(), i6);
        Map map = new Map(0, intersections, segments, warehouseId);

        assertEquals(0, map.getNeighbors(i6).size());
    }

    @Test
    void getShortestPath() {
        List<Intersection> path = map.getShortestPath(i1, i5);
        assertEquals(3, path.size());
        assertEquals(i1, path.getFirst());
        assertEquals(i2, path.get(1));
        assertEquals(i5, path.getLast());
    }

    @Test
    void getShortestPathOnSegment() {
        List<Intersection> path = map.getShortestPath(i1, i2);
        assertEquals(2, path.size());
        assertEquals(i1, path.getFirst());
        assertEquals(i2, path.getLast());
    }

    @Test
    void getShortestPathWithSameOriginAndDestination() {
        assertThrows(IllegalArgumentException.class, () -> map.getShortestPath(i1, i1));
    }

    @Test
    void getShortestPathWithInexistingOrigin() {
        assertThrows(IllegalArgumentException.class, () -> map.getShortestPath(new Intersection(42, 0, 0), i1));
    }

    @Test
    void getShortestPathWithInexistingDestination() {
        assertThrows(IllegalArgumentException.class, () -> map.getShortestPath(i1, new Intersection(42, 0, 0)));
    }

    @Test
    void getShortestPathWithInexistingOriginAndDestination() {
        assertThrows(IllegalArgumentException.class, () -> map.getShortestPath(new Intersection(42, 0, 0), new Intersection(43, 0, 0)));
    }

    @Test
    void getShortestPathOnIsland() {
        // An island is a set of getIntersections that are not connected to the rest of the map.

        // create island
        Intersection i6 = new Intersection(6, 10, 10);
        intersections.put(i6.getId(), i6);
        Map map = new Map(0, intersections, segments, warehouseId);

        assertNull(map.getShortestPath(i1, i6));
        assertNull(map.getShortestPath(i6, i1));

    }

    @Test
    void connectsIntersections() {
        assertTrue(map.connectsIntersections(s12, i1, i2));
        assertTrue(map.connectsIntersections(s12, i2, i1));

        assertTrue(map.connectsIntersections(s23, i2, i3));
        assertTrue(map.connectsIntersections(s23, i3, i2));

        assertTrue(map.connectsIntersections(s24, i2, i4));
        assertTrue(map.connectsIntersections(s24, i4, i2));

        assertTrue(map.connectsIntersections(s25, i2, i5));
        assertTrue(map.connectsIntersections(s25, i5, i2));

        assertTrue(map.connectsIntersections(s31, i3, i1));
        assertTrue(map.connectsIntersections(s31, i1, i3));

        assertTrue(map.connectsIntersections(s35, i3, i5));
        assertTrue(map.connectsIntersections(s35, i5, i3));

        assertFalse(map.connectsIntersections(s12, i1, i3));
        assertFalse(map.connectsIntersections(s12, i3, i1));

        assertFalse(map.connectsIntersections(s23, i1, i3));
        assertFalse(map.connectsIntersections(s23, i3, i1));
    }

    @Test
    void getShortestSegmentBetween() {
        Segment longSegment = new Segment( i1, i2, "Long street 42", 4200000);
        segments.add(longSegment);

        Map map = new Map(0, intersections, segments, warehouseId);

        assertEquals(2, map.getAllSegmentsBetween(i1,i2).size());
        assertEquals(s12, map.getShortestSegmentBetween(i1,i2));
    }

    @Test
    void getAllSegmentsBetween() {
        assertEquals(1, map.getAllSegmentsBetween(i1,i2).size());

        segments.add(new Segment( i1, i2, "Street 100", 5.0));
        Map map = new Map(0, intersections, segments, warehouseId);
        assertEquals(2, map.getAllSegmentsBetween(i1,i2).size());

    }

    @Test
    void getShortestSegmentsBetween() {
        List<Intersection> path = map.getShortestPath(i1, i5);
        List<Segment> segments = map.getShortestSegmentsBetween(path);
        assertEquals(2, segments.size());
        assertEquals(s12, segments.get(0));
        assertEquals(s25, segments.get(1));
    }

    @Test
    void getShortestSegmentsBetweenPathThatContainsSameIntersectionTwiceInARow() {
        List<Intersection> path = new ArrayList<>();
        path.add(i1);
        path.add(i2);
        path.add(i2);
        path.add(i5);

        assertThrows(IllegalArgumentException.class, () -> map.getShortestSegmentsBetween(path));
    }

    @Test
    void getShortestSegmentsBetweenPathThatContainsLessThanTwoIntersections() {
        List<Intersection> path = new ArrayList<>();
        path.add(i1);

        assertThrows(IllegalArgumentException.class, () -> map.getShortestSegmentsBetween(path));
    }

    @Test
    void getShortestSegmentsBetweenPathThatIsNull() {
        assertThrows(IllegalArgumentException.class, () -> map.getShortestSegmentsBetween(null));
    }





}