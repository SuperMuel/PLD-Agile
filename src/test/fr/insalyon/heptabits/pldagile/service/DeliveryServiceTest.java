package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.*;
import fr.insalyon.heptabits.pldagile.repository.InMemoryRoadMapRepository;
import fr.insalyon.heptabits.pldagile.repository.RoadMapRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryServiceTest {

    RoadMapRepository roadMapRepository;
    DeliveryService deliveryService;

    MapService mapService;

    RoadMapService roadMapService;


    Map map;
    Intersection i1;

    @BeforeEach
    void setUp() {
        mapService = new MockMapService();
       map = mapService.getCurrentMap();
       i1 = map.getIntersections().get(1L);


        IdGenerator idGenerator = new IdGenerator();
        roadMapRepository = new InMemoryRoadMapRepository(idGenerator);

        roadMapService = new RoadMapService(roadMapRepository, new NaiveRoadMapOptimizer(), mapService);

        deliveryService = new DeliveryService(roadMapRepository);
    }


    private void createNewDelivery(LocalDate date, long courierId) throws ImpossibleRoadMapException {
        TimeWindow timeWindow = new TimeWindow(8,9);
        long clientId = 1L;
        DeliveryRequest deliveryRequest = new DeliveryRequest(date, clientId, i1, timeWindow, courierId);
        roadMapService.addRequest(deliveryRequest);
    }


    @Test
    void getDeliveriesOnDate() throws ImpossibleRoadMapException {
        assertTrue(deliveryService.getDeliveriesOnDate(LocalDate.now()).isEmpty());

        LocalDate date = LocalDate.of(2021, 1, 1);
        long courierId = 1L;
        createNewDelivery(date, courierId);
        assertEquals(1, deliveryService.getDeliveriesOnDate(date).size());
        assertEquals(0, deliveryService.getDeliveriesOnDate(date.plusDays(23)).size());
    }

    @Test
    void getAll() throws ImpossibleRoadMapException {
        assertTrue(deliveryService.getAll().isEmpty());

        createNewDelivery(LocalDate.now(), 1L);

        assertEquals(1, deliveryService.getAll().size());

    }
}