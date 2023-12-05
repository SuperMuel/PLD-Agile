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


    @BeforeEach
    void setUp() {
        intersections = List.of(new Intersection(1, 1, 1), new Intersection(2, 2, 2), new Intersection(3, 3, 3));
        segments = List.of(new Segment(1, 1, 1, "1", 1), new Segment(2, 2, 2, "2", 2));
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
        assertThrows(IllegalArgumentException.class, () -> new Leg(List.of(new Intersection(1, 1, 1)), segments, departureTime));
        assertThrows(IllegalArgumentException.class, () -> new Leg(intersections, List.of(new Segment(1, 1, 1, "1", 1)), departureTime));
    }

    @Test
    void getIntersections() {
        assertEquals(intersections, leg.getIntersections());
    }

    @Test
    void getSegments() {
        assertEquals(segments, leg.getSegments());
    }

    @Test
    void getDepartureTime() {
        assertEquals(departureTime, leg.getDepartureTime());
    }

    @Test
    void getDestination() {
        assertEquals(intersections.get(intersections.size() - 1), leg.getDestination());
    }

    @Test
    void getOrigin() {
        assertEquals(intersections.get(0), leg.getOrigin());
    }
}