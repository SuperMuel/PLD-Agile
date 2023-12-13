package fr.insalyon.heptabits.pldagile.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import fr.insalyon.heptabits.pldagile.model.*;
import org.junit.jupiter.api.BeforeEach;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class CommonRoadMapOptimizerTest {

    private static Stream<RoadMapOptimizer> implementations() {
        RoadMapBuilder roadMapBuilder = new RoadMapBuilderImpl(new IdGenerator(), Duration.ofMinutes(5), LocalTime.of(7, 45), 15 / 3.6);
        return Stream.of(new NaiveRoadMapOptimizer(roadMapBuilder), new EveryPossibilityRoadMapOptimizer(roadMapBuilder));
    }


    long courierId = 1;
    long courier2Id = 2;

    long clientId = 1;
    long client2Id = 2;

    LocalDate date;

    MapService mapService;
    Map map;

    Intersection i1;
    Intersection i2;
    Intersection i3;
    Intersection i4;
    Intersection i5;
    Intersection warehouse;

    LocalTime departureTime = LocalTime.of(7, 45, 0);
    TimeWindow timeWindow = new TimeWindow(8, 9); // 8h-9h



    void setUpMap() {
        mapService = new MockMapService();
        map = mapService.getCurrentMap();

        i1 = map.getIntersections().get(1L);
        i2 = map.getIntersections().get(2L);
        i3 = map.getIntersections().get(3L);
        i4 = map.getIntersections().get(4L);
        i5 = map.getIntersections().get(5L);
        warehouse = map.getWarehouse();
    }

    @BeforeEach
    void setUp() {
        date = LocalDate.now();
        setUpMap();
    }

    @ParameterizedTest
    @MethodSource("implementations")
    void emptyRequestListThrows(RoadMapOptimizer optimizer) {
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(List.of(), map, departureTime));
    }

//
//    @ParameterizedTest
//    @MethodSource("implementations")
//    void deliveryAtWarehouseThrows(RoadMapOptimizer optimizer) {
//        DeliveryRequest request = new DeliveryRequest(date, courierId, i1, timeWindow, courierId);
//        DeliveryRequest warehouseRequest = new DeliveryRequest(date, courierId, warehouse, timeWindow, courierId);
//        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(List.of(request, warehouseRequest), map, departureTime));
//        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(List.of(warehouseRequest), map, departureTime));
//        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(List.of(warehouseRequest, request), map, departureTime));
//    }
//
//    @ParameterizedTest
//    @MethodSource("implementations")
//    void aRequestIsBeforeDeparture(RoadMapOptimizer optimizer) {
//        TimeWindow soonTimeWindow = new TimeWindow(1, 2); // 1h-2h
//        LocalTime lateDepartureTime = LocalTime.of(8, 0, 0);
//
//        DeliveryRequest request = new DeliveryRequest(date, courierId, i4, soonTimeWindow, courierId);
//        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(List.of(request), map, lateDepartureTime));
//    }
//
//    @ParameterizedTest
//    @MethodSource("implementations")
//    void multipleCouriersThrows(RoadMapOptimizer optimizer) {
//        DeliveryRequest request1 = new DeliveryRequest(date, courierId, i4, timeWindow, courierId);
//        DeliveryRequest request2 = new DeliveryRequest(date, courierId, i3, timeWindow, courier2Id);
//        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(List.of(request1, request2), map, departureTime));
//    }
//
//    @ParameterizedTest
//    @MethodSource("implementations")
//    void multipleClientsAllowed(RoadMapOptimizer optimizer) {
//        DeliveryRequest request1 = new DeliveryRequest(date, clientId, i4, timeWindow, courierId);
//        DeliveryRequest request2 = new DeliveryRequest(date, client2Id, i1, timeWindow, courierId);
//        assertDoesNotThrow(() -> optimizer.optimize(List.of(request1, request2), map, departureTime));
//    }
//
//    @ParameterizedTest
//    @MethodSource("implementations")
//    void multipleDatesNotAllowed(RoadMapOptimizer optimizer) {
//        DeliveryRequest request1 = new DeliveryRequest(date, courierId, i4, timeWindow, courierId);
//        DeliveryRequest request2 = new DeliveryRequest(date.plusDays(1), courierId, i1, timeWindow, courierId);
//        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(List.of(request1, request2), map, departureTime));
//    }
//
//
//    @ParameterizedTest
//    @MethodSource("implementations")
//    void multipleRequestSamePlaceSameTimeWindowNotAllowed(RoadMapOptimizer optimizer) {
//        DeliveryRequest request1 = new DeliveryRequest(date, courierId, i4, timeWindow, courierId);
//        DeliveryRequest request2 = new DeliveryRequest(date, courierId, i4, timeWindow, courierId);
//        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(List.of(request1, request2), map, departureTime));
//    }
//
//
//    @ParameterizedTest
//    @MethodSource("implementations")
//    void successiveTimeWindowsAllowed(RoadMapOptimizer optimizer) {
//        TimeWindow timeWindow1 = new TimeWindow(8, 9);
//        TimeWindow timeWindow2 = new TimeWindow(9, 10);
//        DeliveryRequest request1 = new DeliveryRequest(date, courierId, i4, timeWindow1, courierId);
//        DeliveryRequest request2 = new DeliveryRequest(date, courierId, i1, timeWindow2, courierId);
//        assertDoesNotThrow(() -> optimizer.optimize(List.of(request1, request2), map, departureTime));
//    }
//
//    @ParameterizedTest
//    @MethodSource("implementations")
//    void oneRequest(RoadMapOptimizer optimizer) throws ImpossibleRoadMapException {
//        DeliveryRequest request = new DeliveryRequest(date, courierId, i4, timeWindow, courierId);
//
//        RoadMap roadMap = optimizer.optimize(List.of(request), map, departureTime);
//
//        List<Leg> legs = roadMap.getLegs();
//        List<Delivery> deliveries = roadMap.getDeliveries();
//
//        assertEquals(1, deliveries.size());
//        assertEquals(2, legs.size());
//
//        assertEquals(legs.getFirst().getOrigin(), warehouse);
//        assertEquals(legs.getFirst().getDestination(), i4);
//        assertEquals(legs.getLast().getOrigin(), i4);
//        assertEquals(legs.getLast().getDestination(), warehouse);
//
//        Delivery delivery = deliveries.getFirst();
//        assertEquals(delivery.destination(), i4);
//        assertTrue(timeWindow.contains(delivery.scheduledDateTime()));
//    }
//
//    @ParameterizedTest
//    @MethodSource("implementations")
//    void twoRequestsSameTimeWindow(RoadMapOptimizer optimizer) throws ImpossibleRoadMapException {
//        DeliveryRequest request1 = new DeliveryRequest(date, courierId, i4, timeWindow, courierId);
//        DeliveryRequest request2 = new DeliveryRequest(date, courierId, i1, timeWindow, courierId);
//
//        RoadMap roadMap = optimizer.optimize(List.of(request1, request2), map, departureTime);
//
//        List<Leg> legs = roadMap.getLegs();
//        List<Delivery> deliveries = roadMap.getDeliveries();
//
//        assertEquals(2, deliveries.size());
//        assertEquals(3, legs.size());
//
//        Delivery delivery1 = deliveries.getFirst();
//        Delivery delivery2 = deliveries.getLast();
//
//        Leg leg1 = legs.get(0);
//        Leg leg2 = legs.get(1);
//        Leg leg3 = legs.get(2);
//
//        assertEquals(leg1.getOrigin(), warehouse);
//        assertEquals(leg1.getDestination(), delivery1.destination());
//        assertEquals(leg2.getOrigin(), delivery1.destination());
//        assertEquals(leg2.getDestination(), delivery2.destination());
//        assertEquals(leg3.getOrigin(), delivery2.destination());
//        assertEquals(leg3.getDestination(), warehouse);
//
//        // Check that the delivery locations are i4 and i1
//        assertTrue((delivery1.destination().equals(i4) && delivery2.destination().equals(i1)) ||
//                (delivery1.destination().equals(i1) && delivery2.destination().equals(i4)));
//
//        assertTrue(timeWindow.contains(delivery1.scheduledDateTime()));
//        assertTrue(timeWindow.contains(delivery2.scheduledDateTime()));
//
//    }
//
//    @ParameterizedTest
//    @MethodSource("implementations")
//    void twoRequestsDifferentTimeWindow(RoadMapOptimizer optimizer) throws ImpossibleRoadMapException {
//        TimeWindow timeWindow1 = new TimeWindow(8, 9);
//        TimeWindow timeWindow2 = new TimeWindow(9, 10);
//        DeliveryRequest request1 = new DeliveryRequest(date, courierId, i4, timeWindow1, courierId);
//        DeliveryRequest request2 = new DeliveryRequest(date, courierId, i1, timeWindow2, courierId);
//
//        RoadMap roadMap = optimizer.optimize(List.of(request1, request2), map, departureTime);
//
//        List<Leg> legs = roadMap.getLegs();
//        List<Delivery> deliveries = roadMap.getDeliveries();
//
//        assertEquals(2, deliveries.size());
//        assertEquals(3, legs.size());
//
//        Delivery delivery1 = deliveries.getFirst();
//        Delivery delivery2 = deliveries.getLast();
//
//        assertEquals(delivery1.destination(), i4);
//        assertEquals(delivery2.destination(), i1);
//
//        assertTrue(timeWindow1.contains(delivery1.scheduledDateTime()));
//        assertTrue(timeWindow2.contains(delivery2.scheduledDateTime()));
//    }
//
////
////    @ParameterizedTest
////    @MethodSource("implementations")
////    void deliveryAfterTimeWindowThrows(RoadMapOptimizer optimizer) {
////        TimeWindow timeWindow = new TimeWindow(8, 9);
////        DeliveryRequest request = new DeliveryRequest(date, courierId, i4, timeWindow, courierId);
////
////        double courierSpeedMs = 0.00001; // Very slow courier, will arrive late
////
////        RoadMapBuilder roadMapBuilder = new RoadMapBuilderImpl(new IdGenerator(), Duration.ofMinutes(5), LocalTime.of(7, 45), courierSpeedMs);
////
////        NaiveRoadMapOptimizer optimizer = new NaiveRoadMapOptimizer(roadMapBuilder);
////
////        assertThrows(ImpossibleRoadMapException.class, () -> optimizer.optimize(List.of(request), map, departureTime));
////    }
//
//
//    @ParameterizedTest
//    @MethodSource("implementations")
//    void oneRequestIsOnIsland(RoadMapOptimizer optimizer) {
//        Intersection islandIntersection = new Intersection(6, 0, 0);
//        HashMap<Long, Intersection> intersections = new HashMap<>();
//        intersections.put(1L, i1);
//        intersections.put(2L, i2);
//        intersections.put(3L, i3);
//        intersections.put(4L, i4);
//        intersections.put(5L, i5);
//        intersections.put(6L, islandIntersection);
//
//
//        Map islandMap = new Map(1, intersections, map.getSegments(), warehouse.getId());
//
//        DeliveryRequest request = new DeliveryRequest(date, courierId, islandIntersection, timeWindow, courierId);
//        assertThrows(ImpossibleRoadMapException.class, () -> optimizer.optimize(List.of(request), islandMap, departureTime));
//    }
//

}
