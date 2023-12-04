package fr.insalyon.heptabits.pldagile.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class DeliveryTest {

    Delivery delivery;
    Intersection destination;

    LocalDateTime nowDt;

    TimeWindow timeWindow = new TimeWindow(LocalDateTime.now().toLocalTime(), LocalDateTime.now().plusHours(1).toLocalTime());

    @BeforeEach
    void setUp() {
        nowDt = LocalDateTime.now();
        destination = new Intersection(0, 0, 0);
        delivery = new Delivery(0, nowDt, destination, 12, 1, timeWindow);
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

    @Test
    void getClientId() {
        assertEquals(1, delivery.getClientId());
    }

    @Test
    void getTimeWindow() {
        assertEquals(timeWindow, delivery.getTimeWindow());
    }
}

