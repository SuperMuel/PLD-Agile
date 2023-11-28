package fr.insalyon.heptabits.pldagile.model;

import fr.insalyon.heptabits.pldagile.model.Intersection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntersectionTest {

    Intersection intersection;

    @BeforeEach
    void setUp() {
        intersection = new Intersection(0, 0, 1);
    }


    @Test
    void getLatitude() {
        assertEquals(0, intersection.getLatitude());
    }

    @Test
    void getLongitude() {
        assertEquals(1, intersection.getLongitude());
    }
}