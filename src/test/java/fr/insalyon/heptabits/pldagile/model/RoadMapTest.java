package fr.insalyon.heptabits.pldagile.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class RoadMapTest {
    Intersection intersection1;
    Intersection intersection2;
    RoadMap roadMap;

    Delivery firstDelivery;
    Delivery secondDelivery;
    Delivery thirdDelivery;

    @BeforeEach
    void setUp() {
        roadMap = new RoadMap(0);
        intersection1 = new Intersection(1, 40, 40);
        intersection2 = new Intersection(2, 30, 30);

        firstDelivery = new Delivery(1, LocalTime.of(1, 0, 0), intersection1);
        secondDelivery = new Delivery(2, LocalTime.of(2, 0, 0), intersection2);
        thirdDelivery = new Delivery(3, LocalTime.of(3, 0, 0), intersection2);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addDeliveryKeepsOrder() {
        roadMap.addDelivery(secondDelivery);
        roadMap.addDelivery(firstDelivery);

        assertEquals(roadMap.getDeliveryCount(), 2);
        assertEquals(roadMap.getDelivery(0), firstDelivery);
    }

    @Test
    void removeDelivery() {
        roadMap.addDelivery(firstDelivery);
        roadMap.addDelivery(secondDelivery);

        roadMap.removeDelivery(firstDelivery);

        assertEquals(roadMap.getDeliveryCount(), 1);
        assertEquals(roadMap.getDelivery(0), secondDelivery);
    }

    @Test
    void getDelivery() {
        roadMap.addDelivery(firstDelivery);
        roadMap.addDelivery(secondDelivery);
        roadMap.addDelivery(thirdDelivery);

        assertEquals(roadMap.getDelivery(0), firstDelivery);
        assertEquals(roadMap.getDelivery(1), secondDelivery);
        assertEquals(roadMap.getDelivery(2), thirdDelivery);

    }

    @Test
    void getDeliveries() {
        roadMap.addDelivery(firstDelivery);
        roadMap.addDelivery(secondDelivery);
        roadMap.addDelivery(thirdDelivery);

        var deliveries = roadMap.getDeliveries();
        assertEquals(deliveries.size(), 3);

        assertEquals(deliveries.get(0), firstDelivery);
        assertEquals(deliveries.get(1), secondDelivery);
        assertEquals(deliveries.get(2), thirdDelivery);
    }

    @Test
    void getDeliveryCount() {
        roadMap.addDelivery(firstDelivery);
        roadMap.addDelivery(secondDelivery);
        roadMap.addDelivery(thirdDelivery);

        assertEquals(roadMap.getDeliveryCount(), 3);
    }
}