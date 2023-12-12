package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.Delivery;
import fr.insalyon.heptabits.pldagile.model.DeliveryRequest;
import fr.insalyon.heptabits.pldagile.model.IdGenerator;
import fr.insalyon.heptabits.pldagile.model.RoadMap;
import fr.insalyon.heptabits.pldagile.repository.InMemoryRoadMapRepository;
import fr.insalyon.heptabits.pldagile.repository.MockRoadMapRepository;
import fr.insalyon.heptabits.pldagile.repository.RoadMapRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RoadMapServiceTest {

    RoadMapRepository roadMapRepository;
    MockRoadMapRepository mockRoadMapRepository;
    RoadMapService roadMapService;

    // Mock the dependencies of RoadMapService
    RoadMapOptimizer roadMapOptimizer;

    MockMapService mapService;

    RoadMap exampleRoadMap;

    @BeforeEach
    void setUp() {
        mapService = new MockMapService();

        IdGenerator idGenerator = new IdGenerator();

        roadMapRepository = new InMemoryRoadMapRepository(idGenerator);
        roadMapOptimizer = Mockito.mock(RoadMapOptimizer.class);
        roadMapService = new RoadMapService(roadMapRepository, roadMapOptimizer, mapService);

        mockRoadMapRepository = new MockRoadMapRepository(idGenerator, mapService);
        exampleRoadMap = mockRoadMapRepository.getAll().getFirst();

    }


    @Test
    void getDeliveryRequestsForCourierOnDate() {
        List<DeliveryRequest>requests = roadMapService.getDeliveryRequestsForCourierOnDate(exampleRoadMap.getCourierId(), exampleRoadMap.getDate());
        assertEquals(0, requests.size());

        roadMapRepository.create(exampleRoadMap.getDeliveries(), exampleRoadMap.getLegs());
        requests = roadMapService.getDeliveryRequestsForCourierOnDate(exampleRoadMap.getCourierId(), exampleRoadMap.getDate());

        assertEquals(exampleRoadMap.getDeliveries().size(), requests.size());

        // test with null date, should throw
        assertThrows(NullPointerException.class, () -> roadMapService.getDeliveryRequestsForCourierOnDate(exampleRoadMap.getCourierId(), null));

    }


    @Test
    void addRequestOnEmptyRoadMap() throws ImpossibleRoadMapException {
        Delivery exampleDelivery = exampleRoadMap.getDeliveries().getFirst();
        DeliveryRequest newRequest = new DeliveryRequest(exampleDelivery);

        assertEquals(0, roadMapRepository.getAll().size());

        when(roadMapOptimizer.optimize(any(),any(), any())).thenReturn(exampleRoadMap);

        roadMapService.addRequest(newRequest);

        assertEquals(1, roadMapRepository.getAll().size());
        verify(roadMapOptimizer).optimize(eq(List.of(newRequest)), any(), eq(roadMapService.getWarehouseDepartureTime()));
    }

    // addRequestthrowswhenoptimizationfails

    @Test
    void addRequestOnExistingRoadMap() throws ImpossibleRoadMapException {
        Delivery exampleDelivery = exampleRoadMap.getDeliveries().getFirst();

        roadMapRepository.create(exampleRoadMap.getDeliveries(), exampleRoadMap.getLegs());

        assertEquals(1, roadMapRepository.getAll().size());

        when(roadMapOptimizer.optimize(any(),any(), any())).thenReturn(exampleRoadMap);

        DeliveryRequest newRequest2 = new DeliveryRequest(exampleRoadMap.getDate(), exampleDelivery.clientId(), mapService.i4, exampleDelivery.timeWindow(), exampleDelivery.courierId());

        roadMapService.addRequest(newRequest2);

        assertEquals(1, roadMapRepository.getAll().size());
    }


    // verify two deliveries on same intersection at same timewindow throws
    @Test
    void addRequestOnExistingRoadMapWithSameIntersectionAndTimeWindow() throws ImpossibleRoadMapException {
        Delivery exampleDelivery = exampleRoadMap.getDeliveries().getFirst();

        roadMapRepository.create(exampleRoadMap.getDeliveries(), exampleRoadMap.getLegs());

        assertEquals(1, roadMapRepository.getAll().size());

        when(roadMapOptimizer.optimize(any(),any(), any())).thenReturn(exampleRoadMap);

        DeliveryRequest newRequest2 = new DeliveryRequest(exampleRoadMap.getDate(), exampleDelivery.clientId(), exampleDelivery.destination(), exampleDelivery.timeWindow(), exampleDelivery.courierId());

        assertThrows(ImpossibleRoadMapException.class, () -> roadMapService.addRequest(newRequest2));

        assertEquals(1, roadMapRepository.getAll().size());
    }

    @Test
    void aRoadMapAlreadyExistsCourierId() {
        RoadMapService roadMapService = new RoadMapService(roadMapRepository, roadMapOptimizer, mapService);
        assertFalse(roadMapService.aRoadMapAlreadyExists(exampleRoadMap.getCourierId(), exampleRoadMap.getDate()));
        assertFalse(roadMapService.aRoadMapAlreadyExists(87654567, exampleRoadMap.getDate()));

        roadMapRepository.create(exampleRoadMap.getDeliveries(), exampleRoadMap.getLegs());

        assertTrue(roadMapService.aRoadMapAlreadyExists(exampleRoadMap.getCourierId(), exampleRoadMap.getDate()));
        assertFalse(roadMapService.aRoadMapAlreadyExists(87654567, exampleRoadMap.getDate()));
    }

    @Test
    void aRoadMapAlreadyExistsDate() {
        RoadMapService roadMapService = new RoadMapService(roadMapRepository, roadMapOptimizer, mapService);
        assertFalse(roadMapService.aRoadMapAlreadyExists(exampleRoadMap.getCourierId(), exampleRoadMap.getDate()));
        assertFalse(roadMapService.aRoadMapAlreadyExists(exampleRoadMap.getCourierId(), LocalDate.of(1980, 1, 1)));

        roadMapRepository.create(exampleRoadMap.getDeliveries(), exampleRoadMap.getLegs());

        assertTrue(roadMapService.aRoadMapAlreadyExists(exampleRoadMap.getCourierId(), exampleRoadMap.getDate()));
        assertFalse(roadMapService.aRoadMapAlreadyExists(exampleRoadMap.getCourierId(), LocalDate.of(1980, 1, 1)));
    }

    @Test
    void deliveryRequestedAtWarehouseThrows(){
        DeliveryRequest request = new DeliveryRequest(exampleRoadMap.getDate(), exampleRoadMap.getCourierId(), mapService.getCurrentMap().getWarehouse(), exampleRoadMap.getDeliveries().getFirst().timeWindow(), exampleRoadMap.getCourierId());
        assertThrows(ImpossibleRoadMapException.class, () -> roadMapService.addRequest(request));
    }

}