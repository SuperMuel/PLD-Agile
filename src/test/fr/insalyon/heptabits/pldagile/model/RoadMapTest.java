package fr.insalyon.heptabits.pldagile.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RoadMapTest {
    Intersection intersection1;
    Intersection intersection2;
    RoadMap roadMap;

    LocalDateTime firstDeliveryDt;
    LocalDateTime secondDeliveryDt;
    LocalDateTime thirdDeliveryDt;

    Delivery firstDelivery;
    Delivery secondDelivery;
    Delivery thirdDelivery;

    @BeforeEach
    void setUp() {
        roadMap = new RoadMap(0);
        intersection1 = new Intersection(1, 40, 40);
        intersection2 = new Intersection(2, 30, 30);

        firstDeliveryDt =  LocalDateTime.of(2020, 1, 1, 1, 0, 0);
        secondDeliveryDt = LocalDateTime.of(2020, 1, 1, 2, 0, 0);
        thirdDeliveryDt = LocalDateTime.of(2020, 1, 1, 3, 0, 0);

        firstDelivery = new Delivery(1, firstDeliveryDt , intersection1, 0, 1);
        secondDelivery = new Delivery(2, secondDeliveryDt, intersection2, 0, 1);
        thirdDelivery = new Delivery(3, thirdDeliveryDt, intersection1, 0, 1);

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