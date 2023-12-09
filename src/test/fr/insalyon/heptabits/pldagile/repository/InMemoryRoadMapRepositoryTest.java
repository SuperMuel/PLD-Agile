package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryRoadMapRepositoryTest {

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

    Delivery delivery1 = new Delivery( delivery1ScheduledDateTime, intersectionC, 1, 1, timeWindow);


    LocalDateTime delivery2ScheduledDateTime = LocalDateTime.of(2021, 1, 1, 1, 30);

    Delivery delivery2 = new Delivery( delivery2ScheduledDateTime, intersectionD, 1, 1, timeWindow);


    Leg firstLeg = new Leg(List.of(warehouse, intersectionB, intersectionC), List.of(segmentAB, segmentBC), LocalTime.of(1, 0));
    Leg secondLeg = new Leg(List.of(intersectionC, intersectionD), List.of(segmentCD), LocalTime.of(1, 15));

    Leg thirdLeg = new Leg(List.of(intersectionD, warehouse), List.of(segmentDA), LocalTime.of(1, 30));

    RoadMap roadMap;


    InMemoryRoadMapRepository inMemoryRoadMapRepository;

    IdGenerator idGenerator;


    @BeforeEach
    void setUp() {
        idGenerator = new IdGenerator();
        inMemoryRoadMapRepository = new InMemoryRoadMapRepository(idGenerator);
        roadMap = new RoadMap(1, List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg));
    }

    @Test
    void getAll() {
        inMemoryRoadMapRepository.create(List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg));
        assertEquals(1, inMemoryRoadMapRepository.getAll().size());
    }

    @Test
    void getById() {
        inMemoryRoadMapRepository.create(List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg));
        assertEquals(roadMap, inMemoryRoadMapRepository.getById(1));
    }

    @Test
    void getByIdNull() {
        assertNull(inMemoryRoadMapRepository.getById(1));
    }

    @Test
    void create() {
        inMemoryRoadMapRepository.create(List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg));
        assertEquals(roadMap, inMemoryRoadMapRepository.getById(1));
    }

    @Test
    void update() {
        RoadMap firstRoadMap = inMemoryRoadMapRepository.create(List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg));
        LocalDateTime newDelivery1ScheduledDateTime = delivery1ScheduledDateTime.plusMinutes(1);
        Delivery updatedDelivery1 = new Delivery( newDelivery1ScheduledDateTime, intersectionC, 1, 1, timeWindow);
        RoadMap updatedRoadmap = new RoadMap(firstRoadMap.getId(), List.of(updatedDelivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg));
        inMemoryRoadMapRepository.updateById(firstRoadMap.getId(), updatedRoadmap.getDeliveries(), updatedRoadmap.getLegs());

        assertEquals(inMemoryRoadMapRepository.getById(1).getDeliveries().getFirst(), updatedDelivery1);
        assertEquals(updatedRoadmap, inMemoryRoadMapRepository.getById(1));


    }

    @Test
    void updateNotFoundId(){
        assertThrows(IllegalArgumentException.class, () -> inMemoryRoadMapRepository.updateById(432434, List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg)));
    }

    @Test
    void delete() {
        inMemoryRoadMapRepository.create(List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg));
        inMemoryRoadMapRepository.delete(1);
        assertNull(inMemoryRoadMapRepository.getById(1));
    }
}