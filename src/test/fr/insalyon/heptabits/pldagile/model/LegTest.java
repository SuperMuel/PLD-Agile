package fr.insalyon.heptabits.pldagile.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LegTest {

    Leg leg;

    List<Intersection> intersections;

    List<Segment> segments;

    LocalTime departureTime;

    Intersection i1 = new Intersection(1, 1,1);
    Intersection i2 = new Intersection(2, 2, 2);
    Intersection i3 = new Intersection(3, 3,3);

    Segment s12 = new Segment(i1,i2, "Segment 1-2", 1.2);
    Segment s23 = new Segment(i2,i3, "Segment 2-3", 2.3);


    @BeforeEach
    void setUp() {
        intersections = List.of(i1,i2,i3);
        segments = List.of(s12, s23);
        departureTime = LocalTime.of(1, 1, 1);
        leg = new Leg(intersections, segments, departureTime);
    }

    @Test
    void constructorNullArguments() {
        assertThrows(IllegalArgumentException.class, () -> new Leg(null, segments, departureTime));
        assertThrows(IllegalArgumentException.class, () -> new Leg(intersections, null, departureTime));
        assertThrows(IllegalArgumentException.class, () -> new Leg(intersections, segments, null));
    }

    @Test
    void constructorEmptyIntersections() {
        assertThrows(IllegalArgumentException.class, () -> new Leg(List.of(), segments, departureTime));
    }

    @Test
    void constructorEmptySegments() {
        assertThrows(IllegalArgumentException.class, () -> new Leg(intersections, List.of(), departureTime));
    }

    @Test
    void constructorIntersectionsSegmentsSizeMismatch() {
        assertThrows(IllegalArgumentException.class, () -> new Leg(List.of(i1), List.of(s12,s23), departureTime));
        assertThrows(IllegalArgumentException.class, () -> new Leg(List.of(i1,i2), List.of(s12,s23), departureTime));
        assertThrows(IllegalArgumentException.class, () -> new Leg(List.of(i1,i2,i3), List.of(s12), departureTime));
    }

    @Test
    void getIntersections() {
        assertEquals(intersections, leg.intersections());
    }

    @Test
    void getSegments() {
        assertEquals(segments, leg.segments());
    }

    @Test
    void getDepartureTime() {
        assertEquals(departureTime, leg.departureTime());
    }

    @Test
    void getDestination() {
        assertEquals(intersections.getLast(), leg.getDestination());
    }

    @Test
    void getOrigin() {
        assertEquals(intersections.getFirst(), leg.getOrigin());
    }


    @Test
    void equalsSameAttributes() {
        Leg leg2 = new Leg(intersections, segments, departureTime);
        assertEquals(leg, leg2);
    }

    @Test
    void equalsDifferentDepartureTime() {
        Leg leg2 = new Leg(intersections, segments, LocalTime.of(1, 1, 2));
        assertNotEquals(leg, leg2);
    }

    @Test
    void equalsDifferentSegments() {
        Leg leg1 = new Leg(List.of(i1,i2), List.of(s12), departureTime);
        Leg leg2 = new Leg(List.of(i1,i2), List.of(new Segment(i1,i2, "another name", 1.2)), departureTime);

        assertNotEquals(leg, leg2);

    }

    @Test
    void equalsDifferentIntersections() {
        List<Intersection> newIntersections = List.of(new Intersection(1, 343435, 1), new Intersection(2, 2, 2), new Intersection(3, 3, 3));
        Leg leg2 = new Leg(newIntersections, segments, departureTime);
        assertNotEquals(leg, leg2);
    }

}