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
        delivery = new Delivery(nowDt, destination, 12, 1, timeWindow);
    }

    @Test
    void getDestination() {
        assertEquals(destination, delivery.destination());
    }

    @Test
    void getScheduledTime() {
        assertEquals(nowDt, delivery.scheduledDateTime());
    }

    @Test
    void getScheduledDateTime() {
        assertEquals(nowDt, delivery.scheduledDateTime());
    }

    @Test
    void getCourierId() {
        assertEquals(12, delivery.courierId());
    }

    @Test
    void getClientId() {
        assertEquals(1, delivery.clientId());
    }

    @Test
    void getTimeWindow() {
        assertEquals(timeWindow, delivery.timeWindow());
    }

    @Test
    void toDeliveryRequest() {
        DeliveryRequest deliveryRequest = delivery.toDeliveryRequest();
        assertEquals(deliveryRequest.getDate(), nowDt.toLocalDate());
        assertEquals(deliveryRequest.getDestination(), destination);
        assertEquals(deliveryRequest.getTimeWindow(), timeWindow);
        assertEquals(deliveryRequest.getClientId(), 1);
    }
}

