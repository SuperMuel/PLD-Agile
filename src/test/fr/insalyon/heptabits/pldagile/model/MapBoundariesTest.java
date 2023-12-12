package fr.insalyon.heptabits.pldagile.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapBoundariesTest {

    MapBoundaries mapBoundaries;

    @BeforeEach
    void setUp() {
        mapBoundaries = new MapBoundaries(0, 1, 2, 3);
    }

    @Test
    void testToString() {
        assertEquals("MapBoundaries{minLatitude=0.0, maxLatitude=1.0, minLongitude=2.0, maxLongitude=3.0}", mapBoundaries.toString());
    }

    @Test
    void minLatitude() {
        assertEquals(0, mapBoundaries.minLatitude());
    }

    @Test
    void maxLatitude() {
        assertEquals(1, mapBoundaries.maxLatitude());
    }

    @Test
    void minLongitude() {
        assertEquals(2, mapBoundaries.minLongitude());
    }

    @Test
    void maxLongitude() {
        assertEquals(3, mapBoundaries.maxLongitude());
    }
}