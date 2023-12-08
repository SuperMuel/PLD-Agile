package fr.insalyon.heptabits.pldagile.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SegmentTest {

    Intersection origin;
    Intersection destination;
    Segment segment;
    @BeforeEach
    void setUp() {
        origin = new Intersection(0, 0, 1);
        destination = new Intersection(1, 2, 3);

        segment = new Segment(0, origin.getId(), destination.getId(), "Rue 1", 12.1);
    }

    @Test
    void getOriginId() {
        assertEquals(0, segment.getOriginId());
    }

    @Test
    void getDestinationId() {
        assertEquals(1, segment.getDestinationId());
    }

    @Test
    void getName() {
        assertEquals("Rue 1", segment.getName());
    }

    @Test
    void getLength() {
        assertEquals(12.1, segment.getLength());
    }

    @Test
    void getTotalLength() {
        assertEquals(0.0, Segment.getTotalLength(List.of()));
        assertEquals(12.1, Segment.getTotalLength(List.of(segment)));

        Segment a = new Segment(0, origin.getId(), destination.getId(), "Rue 1", 4.0);
        Segment b = new Segment(1, origin.getId(), destination.getId(), "Rue 2", 5.1);

        assertEquals(9.1, Segment.getTotalLength(List.of(a, b)));



    }
}