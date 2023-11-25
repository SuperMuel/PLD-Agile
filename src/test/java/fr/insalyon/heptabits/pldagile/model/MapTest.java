package fr.insalyon.heptabits.pldagile.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapTest {

    Map map;

    @BeforeEach
    void setUp() {
        map = new Map(0);
        // add segments and intersections
        Intersection i1 = new Intersection(0, 0, 0);
        Intersection i2 = new Intersection(1, 0, 1);
        Intersection i3 = new Intersection(2, 1, 0);
        Intersection i4 = new Intersection(3, 1, 1);
        Segment s1 = new Segment(0, i1.getId(), i2.getId(), "Street 1");
        Segment s2 = new Segment(1, i1.getId(), i3.getId(), "Street 2");
        Segment s3 = new Segment(2, i2.getId(), i4.getId(), "Street 3");
        Segment s4 = new Segment(3, i3.getId(), i4.getId(), "Street 4");

        map.addIntersection(i1);
        map.addIntersection(i2);
        map.addIntersection(i3);
        map.addIntersection(i4);

        map.addSegment(s1);
        map.addSegment(s2);
        map.addSegment(s3);
        map.addSegment(s4);
    }

    @AfterEach
    void tearDown() {
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
    void addSegment() {
        Segment s5 = new Segment(4, 0, 3, "Street 5");
        map.addSegment(s5);
        assertEquals(5, map.getSegments().size());
    }

    @Test
    void addDuplicateSegment() {
        Segment s5 = new Segment(4, 0, 3, "Street 5");
        map.addSegment(s5);
        assertThrows(IllegalArgumentException.class, () -> map.addSegment(s5));
    }

    @Test
    void addDuplicateIntersection() {
        Intersection i5 = new Intersection(4, 2, 2);
        map.addIntersection(i5);
        assertThrows(IllegalArgumentException.class, () -> map.addIntersection(i5));
    }



    @Test
    void addIntersection() {
        Intersection i5 = new Intersection(4, 2, 2);
        map.addIntersection(i5);
        assertEquals(5, map.getIntersections().size());
    }
}