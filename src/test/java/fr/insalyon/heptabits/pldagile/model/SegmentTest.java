package fr.insalyon.heptabits.pldagile.model;

import fr.insalyon.heptabits.pldagile.model.Intersection;
import fr.insalyon.heptabits.pldagile.model.Segment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    @AfterEach
    void tearDown() {
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
}