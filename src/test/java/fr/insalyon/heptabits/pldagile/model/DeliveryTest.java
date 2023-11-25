package fr.insalyon.heptabits.pldagile.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;

class DeliveryTest {

    Delivery delivery;
    Intersection destination;
    @BeforeEach
    void setUp() {
        destination = new Intersection(0, 0, 0);
        delivery = new Delivery(0,LocalTime.of(12,10,0) , destination);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getDestination() {
        assertEquals(destination, delivery.getDestination());
    }

    @Test
    void getScheduledTime() {
        assertEquals(LocalTime.of(12,10,0), delivery.getScheduledTime());
    }
}