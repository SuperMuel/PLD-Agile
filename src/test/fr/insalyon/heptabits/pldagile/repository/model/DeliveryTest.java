package fr.insalyon.heptabits.pldagile.model;

import fr.insalyon.heptabits.pldagile.model.Delivery;
import fr.insalyon.heptabits.pldagile.model.Intersection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class DeliveryTest {

    Delivery delivery;
    Intersection destination;

    LocalDateTime nowDt;
    @BeforeEach
    void setUp() {
        nowDt = LocalDateTime.now();
        destination = new Intersection(0, 0, 0);
        delivery = new Delivery(0, nowDt, destination, 12);
    }

    @Test
    void getDestination() {
        assertEquals(destination, delivery.getDestination());
    }

    @Test
    void getScheduledTime() {
        assertEquals(nowDt, delivery.getScheduledDateTime());
    }

    @Test
    void getScheduledDateTime() {
        assertEquals(nowDt, delivery.getScheduledDateTime());
    }

    @Test
    void getCourierId() {
        assertEquals(12, delivery.getCourierId());
    }
}