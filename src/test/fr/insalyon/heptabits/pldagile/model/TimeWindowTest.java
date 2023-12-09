package fr.insalyon.heptabits.pldagile.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TimeWindowTest {

    TimeWindow timeWindow;

    @BeforeEach
    void setUp() {
        timeWindow = new TimeWindow(9, 10);
    }

    @Test
    void emptyTimeWindowThrows() {
        assertThrows(IllegalArgumentException.class, () -> new TimeWindow(0, 0));
        assertThrows(IllegalArgumentException.class, () -> new TimeWindow(1, 1));
        assertThrows(IllegalArgumentException.class, () -> new TimeWindow(23, 23));
    }

    @Test
    void constructorFromHours() {
        timeWindow = new TimeWindow(9, 10);
        assertEquals(timeWindow.getStart(), LocalTime.of(9, 0));
        assertEquals(timeWindow.getEnd(), LocalTime.of(10, 0));
    }

    @Test
    void constructorFromLocalTime() {
        timeWindow = new TimeWindow(LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertEquals(timeWindow.getStart(), LocalTime.of(9, 0));
        assertEquals(timeWindow.getEnd(), LocalTime.of(10, 0));
    }

    @Test
    void invalidHoursThrows() {
        assertThrows(DateTimeException.class, () -> new TimeWindow(-1, 0));
        assertThrows(DateTimeException.class, () -> new TimeWindow(0, -1));
        assertThrows(DateTimeException.class, () -> new TimeWindow(24, 0));
        assertThrows(DateTimeException.class, () -> new TimeWindow(0, 24));
    }


    @Test
    void negativeDurationThrows() {
        assertThrows(IllegalArgumentException.class, () -> new TimeWindow(10, 9));
        assertThrows(IllegalArgumentException.class, () -> new TimeWindow(LocalTime.of(10, 0), LocalTime.of(9, 59, 59)));
    }


    @Test
    void compareStartTo() {
        TimeWindow before = new TimeWindow(8, 9);
        TimeWindow after = new TimeWindow(10, 11);

        assertTrue(after.compareStartTo(before) > 0);
    }

    @Test
    void compareEndTo() {
        TimeWindow before = new TimeWindow(8, 9);
        TimeWindow after = new TimeWindow(10, 11);

        assertTrue(before.compareEndTo(after) < 0);
    }

    @Test
    void contains(){
        TimeWindow timeWindow = new TimeWindow(9, 10);
        assertTrue(timeWindow.contains(LocalTime.of(9, 0)));
        assertTrue(timeWindow.contains(LocalTime.of(9, 30)));
        assertTrue(timeWindow.contains(LocalTime.of(9, 59, 59)));
        assertTrue(timeWindow.contains(LocalTime.of(10, 0)));

        assertFalse(timeWindow.contains(LocalTime.of(8, 59, 59)));
        assertFalse(timeWindow.contains(LocalTime.of(10, 0, 1)));
    }


}