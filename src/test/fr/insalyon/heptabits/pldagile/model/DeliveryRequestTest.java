package fr.insalyon.heptabits.pldagile.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryRequestTest {

    final long id = 0;
    final Intersection destination = new Intersection(0, 0, 0);
    final TimeWindow timeWindow = new TimeWindow(LocalTime.of(9, 0), LocalTime.of(10, 0));
    final long clientId = 0;
    final long courierId = 0;

    final LocalDate date = LocalDate.of(2020, 1, 1);

    DeliveryRequest deliveryRequest;


    @BeforeEach
    void setUp() {
        deliveryRequest = new DeliveryRequest(date, clientId, destination, timeWindow, courierId);
    }

    @Test
    void fromNullArguments(){
        assertThrows(IllegalArgumentException.class, () -> new DeliveryRequest(null, clientId, destination, timeWindow, courierId));
        assertThrows(IllegalArgumentException.class, () -> new DeliveryRequest(date, clientId, null, timeWindow,courierId));
        assertThrows(IllegalArgumentException.class, () -> new DeliveryRequest(date, clientId, destination, null,courierId));
    }



    @Test
    void fromDelivery(){
        final LocalDateTime scheduledDateTime = LocalDateTime.of(2020, 1, 1, 9, 0);
        final Delivery delivery = new Delivery(scheduledDateTime, destination, 0, clientId, timeWindow);
        deliveryRequest = new DeliveryRequest(delivery);
        assertEquals(deliveryRequest.getDate(), scheduledDateTime.toLocalDate());
        assertEquals(deliveryRequest.getDestination(), destination);
        assertEquals(deliveryRequest.getTimeWindow(), timeWindow);
        assertEquals(deliveryRequest.getClientId(), clientId);
    }

    @Test
    void fromNullDelivery(){
        assertThrows(IllegalArgumentException.class, () -> new DeliveryRequest(null));
    }

    @Test
    void getDestination() {
        assertEquals(destination, deliveryRequest.getDestination());
    }

    @Test
    void getTimeWindow() {
        assertEquals(timeWindow, deliveryRequest.getTimeWindow());
    }

    @Test
    void getDate() {
        assertEquals(date, deliveryRequest.getDate());
    }

    @Test
    void getClientId() {
        assertEquals(clientId, deliveryRequest.getClientId());
    }

    @Test
    void getCourierId() {
        assertEquals(courierId, deliveryRequest.getCourierId());
    }

    @Test
    void toDelivery() {
        final LocalTime scheduledTime = LocalTime.of(9, 0);
        final Delivery delivery = deliveryRequest.toDelivery(scheduledTime);
        assertEquals(deliveryRequest.getDate().atTime(scheduledTime), delivery.scheduledDateTime());
        assertEquals(deliveryRequest.getDestination(), delivery.destination());
        assertEquals(deliveryRequest.getTimeWindow(), delivery.timeWindow());
        assertEquals(deliveryRequest.getClientId(), delivery.clientId());
        assertEquals(deliveryRequest.getCourierId(), delivery.courierId());
    }
}