package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
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


    InMemoryRoadMapRepository repository;

    IdGenerator idGenerator;


    @BeforeEach
    void setUp() {
        idGenerator = new IdGenerator();
        repository = new InMemoryRoadMapRepository(idGenerator);
        roadMap = new RoadMap(1, List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg));
    }

    @Test
    void getAll() {
        repository.create(List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg));
        assertEquals(1, repository.getAll().size());
    }

    @Test
    void getById() {
        repository.create(List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg));
        assertEquals(roadMap, repository.getById(1));
    }

    @Test
    void getByIdNull() {
        assertNull(repository.getById(1));
    }

    @Test
    void create() {
        repository.create(List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg));
        assertEquals(roadMap, repository.getById(1));
    }

    @Test
    void update() {
        RoadMap firstRoadMap = repository.create(List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg));
        LocalDateTime newDelivery1ScheduledDateTime = delivery1ScheduledDateTime.plusMinutes(1);
        Delivery updatedDelivery1 = new Delivery( newDelivery1ScheduledDateTime, iC, 1, 1, timeWindow);
        RoadMap updatedRoadmap = new RoadMap(firstRoadMap.getId(), List.of(updatedDelivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg));
        repository.updateById(firstRoadMap.getId(), updatedRoadmap.getDeliveries(), updatedRoadmap.getLegs());

        assertEquals(repository.getById(1).getDeliveries().getFirst(), updatedDelivery1);
        assertEquals(updatedRoadmap, repository.getById(1));


    }

    @Test
    void updateNotFoundId(){
        assertThrows(IllegalArgumentException.class, () -> repository.updateById(432434, List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg)));
    }

    @Test
    void delete() {
        repository.create(List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg));
        repository.delete(1);
        assertNull(repository.getById(1));
    }

    @Test
    void updateById() {

        assertTrue(repository.getAll().isEmpty());

        Delivery delivery1 = new Delivery( delivery1ScheduledDateTime, iC, 1, 1, timeWindow);

        repository.create(List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg));

        Delivery newDelivery1 = new Delivery(delivery1ScheduledDateTime, iC, 1, 1324, timeWindow);

        repository.updateById(1, List.of(newDelivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg));

        assertEquals(newDelivery1, repository.getById(1).getDeliveries().getFirst());

    }

    @Test
    void updateNonExistingRoadMap() {
        assertThrows(IllegalArgumentException.class, () -> repository.updateById(1, List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg)));
    }



    @Test
    void getByCourierAndDate() {
        LocalDate date = delivery1.scheduledDateTime().toLocalDate();
        long courierId = delivery1.courierId();
        assertNull(repository.getByCourierAndDate(courierId, date));

        repository.create(List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg));

        assertEquals(roadMap, repository.getByCourierAndDate(courierId, date));
        assertNull(repository.getByCourierAndDate(courierId, date.plusDays(1)));
        assertNull(repository.getByCourierAndDate(courierId + 1, date));
    }


    @Test
    void getByDate() {
        LocalDate date = delivery1.scheduledDateTime().toLocalDate();
        assertEquals(0, repository.getByDate(date).size());

        repository.create(List.of(delivery1, delivery2), List.of(firstLeg, secondLeg, thirdLeg));

        assertEquals(roadMap, repository.getByDate(date).getFirst());

        assertEquals(0, repository.getByDate(date.plusDays(1)).size());
    }
}