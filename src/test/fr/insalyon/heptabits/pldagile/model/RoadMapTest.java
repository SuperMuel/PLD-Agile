package fr.insalyon.heptabits.pldagile.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoadMapTest {

    // warehouse = A ---- B
    //             |      |
    //             |      |
    // delivery2 = D ---- C = delivery1

    Intersection warehouse = new Intersection(1, 0, 0);
    Intersection intersectionB = new Intersection(2, 1, 0);

    Intersection intersectionC = new Intersection(3, 1, 1);

    Intersection intersectionD = new Intersection(4, 0, 1);

    Segment segmentAB = new Segment(1, 1, 2, "AB", 1);
    Segment segmentBC = new Segment(2, 2, 3, "BC", 1);
    Segment segmentCD = new Segment(3, 3, 4, "CD", 1);
    Segment segmentDA = new Segment(4, 4, 1, "DA", 1);


    TimeWindow timeWindow = new TimeWindow(LocalTime.of(1, 0), LocalTime.of(2, 0));
    LocalDateTime delivery1ScheduledDateTime = LocalDateTime.of(2021, 1, 1, 1, 15);

    Delivery delivery1 = new Delivery(1, delivery1ScheduledDateTime, intersectionC, 1, 1, timeWindow);


    LocalDateTime delivery2ScheduledDateTime = LocalDateTime.of(2021, 1, 1, 1, 30);

    Delivery delivery2 = new Delivery(2, delivery2ScheduledDateTime, intersectionD, 1, 1, timeWindow);


    Leg firstLeg = new Leg(List.of(warehouse, intersectionB, intersectionC), List.of(segmentAB, segmentBC), LocalTime.of(1, 0));
    Leg secondLeg = new Leg(List.of(intersectionC, intersectionD), List.of(segmentCD), LocalTime.of(1, 15));

    Leg thirdLeg = new Leg(List.of(intersectionD, warehouse), List.of(segmentDA), LocalTime.of(1, 30));

    RoadMap roadMap;


    @BeforeEach
    void setUp() {
        roadMap = new RoadMap(1, List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg),1);
    }

    @Test
    void constructorNullArguments() {
        assertThrows(IllegalArgumentException.class, () -> new RoadMap(1, null, List.of(firstLeg, secondLeg), 2));
        assertThrows(IllegalArgumentException.class, () -> new RoadMap(1, List.of(delivery1, delivery2), null,2));
    }
    /*
    @Test
    void constructorEmptyDeliveries() {
        assertThrows(IllegalArgumentException.class, () -> new RoadMap(1, List.of(), List.of(firstLeg, secondLeg), 2));
    }

    @Test
    void constructorEmptyLegs() {
        assertThrows(IllegalArgumentException.class, () -> new RoadMap(1, List.of(delivery1, delivery2), List.of(), 2));
    }*/

    @Test
    void constructorLegsDeliveriesSizeMismatch() {
        assertThrows(IllegalArgumentException.class, () -> new RoadMap(1, List.of(delivery1, delivery2), List.of(firstLeg), 2));
        assertThrows(IllegalArgumentException.class, () -> new RoadMap(1, List.of(delivery1), List.of(firstLeg, secondLeg, thirdLeg), 2));
    }

    @Test
    void constructorDepartureMustMachArrival() {
        Leg leg1 = new Leg(List.of(intersectionC, intersectionD), List.of(segmentCD), LocalTime.of(1, 15));
        Leg leg2 = new Leg(List.of(intersectionD, warehouse), List.of(segmentDA), LocalTime.of(1, 30));
        assertThrows(IllegalArgumentException.class, () -> new RoadMap(1, List.of(delivery2), List.of(leg1, leg2), 2));
    }

    @Test
    void constructorLegsDeliveriesLegDeparturePointDoesNotMatchPreviousLegDestination() {
        Leg leg1 = new Leg(List.of(warehouse, intersectionB, intersectionC), List.of(segmentAB, segmentBC), LocalTime.of(1, 0));
        Leg leg2 = new Leg(List.of(intersectionD, warehouse), List.of(segmentDA), LocalTime.of(1, 30));

        assertThrows(IllegalArgumentException.class, () -> new RoadMap(1, List.of(delivery1), List.of(leg1, leg2), 2));
    }

    @Test
    void getDeliveries() {
        assertEquals(List.of(delivery1, delivery2), roadMap.getDeliveries());
    }

    @Test
    void getLegs() {
        assertEquals(List.of(firstLeg, secondLeg, thirdLeg), roadMap.getLegs());
    }

    @Test
    void equals() {
        RoadMap a = new RoadMap(1, List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg), 2);
        RoadMap b = new RoadMap(1, List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg), 2);
        assertEquals(a, b);
    }



    @Test
    void differentIdEquals(){
        RoadMap a = new RoadMap(1, List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg),2);
        RoadMap b = new RoadMap(2, List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg),2);
        assertNotEquals(a, b);
    }

    @Test
    void differentDeliveriesEquals(){
        RoadMap a = new RoadMap(1, List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg),2);
        Delivery newDelivery1 = new Delivery(1, delivery1ScheduledDateTime, intersectionC, 1, 2, timeWindow);

        RoadMap b = new RoadMap(1, List.of(newDelivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg),2);
        assertNotEquals(a, b);
    }

    @Test
    void differentLegsEquals(){
        RoadMap a = new RoadMap(1, List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg),2);
        Leg newLeg = new Leg(List.of(intersectionD, warehouse), List.of(segmentDA), LocalTime.of(1, 45));

        RoadMap b = new RoadMap(1, List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, newLeg),2);
        assertNotEquals(a, b);
    }


}