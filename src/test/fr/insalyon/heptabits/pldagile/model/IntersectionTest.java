package fr.insalyon.heptabits.pldagile.model;

import fr.insalyon.heptabits.pldagile.model.Intersection;
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

    @Test
    void testEquals() {
        Intersection A = new Intersection(0, 0, 1);
        Intersection B = new Intersection(0, 0, 1);

        assertEquals(A, B);
    }

    @Test
    void testNotEquals() {

        assertNotEquals(new Intersection(0, 0, 0), new Intersection(0, 0, 1));
        assertNotEquals(new Intersection(0, 0, 0), new Intersection(0, 1, 0));
        assertNotEquals(new Intersection(0, 0, 0), new Intersection(1, 0, 0));
        assertNotEquals(new Intersection(0, 0, 0), new Intersection(1, 1, 1));

        assertNotEquals(new Intersection(0, 0, 0), null);
        assertNotEquals(new Intersection(0, 0, 0), new Object());
    }
}