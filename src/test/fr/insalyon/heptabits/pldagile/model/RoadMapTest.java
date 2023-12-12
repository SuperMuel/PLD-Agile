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
    Intersection iA = warehouse;
    Intersection iB = new Intersection(2, 1, 0);

    Intersection iC = new Intersection(3, 1, 1);

    Intersection iD = new Intersection(4, 0, 1);

    Segment segmentAB = new Segment( iA ,iB , "AB", 1);
    Segment segmentBC = new Segment( iB, iC, "BC", 1);
    Segment segmentCD = new Segment( iC, iD, "CD", 1);
    Segment segmentDA = new Segment( iD, iA, "DA", 1);


    TimeWindow timeWindow = new TimeWindow(LocalTime.of(1, 0), LocalTime.of(2, 0));
    LocalDateTime delivery1ScheduledDateTime = LocalDateTime.of(2021, 1, 1, 1, 15);

    Delivery delivery1 = new Delivery( delivery1ScheduledDateTime, iC, 1, 1, timeWindow);


    LocalDateTime delivery2ScheduledDateTime = LocalDateTime.of(2021, 1, 1, 1, 30);

    Delivery delivery2 = new Delivery( delivery2ScheduledDateTime, iD, 1, 1, timeWindow);


    Leg firstLeg = new Leg(List.of(warehouse, iB, iC), List.of(segmentAB, segmentBC), LocalTime.of(1, 0));
    Leg secondLeg = new Leg(List.of(iC, iD), List.of(segmentCD), LocalTime.of(1, 15));

    Leg thirdLeg = new Leg(List.of(iD, warehouse), List.of(segmentDA), LocalTime.of(1, 30));

    RoadMap roadMap;


    @BeforeEach
    void setUp() {
        roadMap = new RoadMap(1, List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg));
    }

    @Test
    void constructorNullArguments() {
        assertThrows(IllegalArgumentException.class, () -> new RoadMap(1, null, List.of(firstLeg, secondLeg)));
        assertThrows(IllegalArgumentException.class, () -> new RoadMap(1, List.of(delivery1, delivery2), null));
    }

    @Test
    void constructorEmptyDeliveries() {
        assertThrows(IllegalArgumentException.class, () -> new RoadMap(1, List.of(), List.of(firstLeg)));
    }

    @Test
    void constructorDifferentCouriersInDeliveries(){
        Delivery newDelivery1 = new Delivery( delivery1ScheduledDateTime, iC, 12, 2, timeWindow);
        assertThrows(IllegalArgumentException.class, () -> new RoadMap(1, List.of(newDelivery1, delivery2), List.of(firstLeg)));
    }

    @Test
    void constructorEmptyLegs() {
        assertThrows(IllegalArgumentException.class, () -> new RoadMap(1, List.of(delivery1, delivery2), List.of()));
    }

    @Test
    void constructorLegsDeliveriesSizeMismatch() {
        assertThrows(IllegalArgumentException.class, () -> new RoadMap(1, List.of(delivery1, delivery2), List.of(firstLeg)));
        assertThrows(IllegalArgumentException.class, () -> new RoadMap(1, List.of(delivery1), List.of(firstLeg, secondLeg, thirdLeg)));
    }

    @Test
    void constructorDepartureMustMachArrival() {
        Leg leg1 = new Leg(List.of(iC, iD), List.of(segmentCD), LocalTime.of(1, 15));
        Leg leg2 = new Leg(List.of(iD, warehouse), List.of(segmentDA), LocalTime.of(1, 30));
        assertThrows(IllegalArgumentException.class, () -> new RoadMap(1, List.of(delivery2), List.of(leg1, leg2)));
    }

    @Test
    void constructorLegsDeliveriesLegDeparturePointDoesNotMatchPreviousLegDestination() {
        Leg leg1 = new Leg(List.of(warehouse, iB, iC), List.of(segmentAB, segmentBC), LocalTime.of(1, 0));
        Leg leg2 = new Leg(List.of(iD, warehouse), List.of(segmentDA), LocalTime.of(1, 30));

        assertThrows(IllegalArgumentException.class, () -> new RoadMap(1, List.of(delivery1), List.of(leg1, leg2)));
    }

    @Test
    void constructorLegsDepartureTimeMustBeAfterPreviousLegDepartureTime() {
        Leg leg1 = new Leg(List.of(warehouse, iB, iC), List.of(segmentAB, segmentBC), LocalTime.of(1, 0));
        Leg leg2 = new Leg(List.of(iC, iD), List.of(segmentCD), LocalTime.of(1, 1));
        Leg leg3 = new Leg(List.of(iD, warehouse), List.of(segmentDA), LocalTime.of(1, 2));

        assertDoesNotThrow(() -> new RoadMap(1, List.of(delivery1,delivery2), List.of(leg1, leg2, leg3)));

        Leg leg4 = new Leg(List.of(warehouse, iB, iC), List.of(segmentAB, segmentBC), LocalTime.of(1, 0));
        Leg leg5 = new Leg(List.of(iC, iD), List.of(segmentCD), LocalTime.of(1, 1));
        Leg leg6 = new Leg(List.of(iD, warehouse), List.of(segmentDA), LocalTime.of(1, 1));

        assertThrows(IllegalArgumentException.class, () -> new RoadMap(1, List.of(delivery1,delivery2), List.of(leg4, leg5, leg6)));

        Leg leg7 = new Leg(List.of(warehouse, iB, iC), List.of(segmentAB, segmentBC), LocalTime.of(1, 0));
        Leg leg8 = new Leg(List.of(iC, iD), List.of(segmentCD), LocalTime.of(1, 1));
        Leg leg9 = new Leg(List.of(iD, warehouse), List.of(segmentDA), LocalTime.of(0, 0));

        assertThrows(IllegalArgumentException.class, () -> new RoadMap(1, List.of(delivery1,delivery2), List.of(leg7, leg8, leg9)));



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
        RoadMap a = new RoadMap(1, List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg));
        RoadMap b = new RoadMap(1, List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg));
        assertEquals(a, b);
    }



    @Test
    void differentIdEquals(){
        RoadMap a = new RoadMap(1, List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg));
        RoadMap b = new RoadMap(2, List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg));
        assertNotEquals(a, b);
    }

    @Test
    void differentDeliveriesEquals(){
        RoadMap a = new RoadMap(1, List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg));
        Delivery newDelivery1 = new Delivery(delivery1ScheduledDateTime, iC, 1, 2, timeWindow);

        RoadMap b = new RoadMap(1, List.of(newDelivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg));
        assertNotEquals(a, b);
    }

    @Test
    void differentLegsEquals(){
        RoadMap a = new RoadMap(1, List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg));
        Leg newLeg = new Leg(List.of(iD, warehouse), List.of(segmentDA), LocalTime.of(1, 45));

        RoadMap b = new RoadMap(1, List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, newLeg));
        assertNotEquals(a, b);
    }


    @Test
    void getCourierId() {
        assertEquals(1, roadMap.getCourierId());
    }

    @Test
    void getDate() {
        assertEquals(delivery1.scheduledDateTime().toLocalDate(), roadMap.getDate());
    }
}