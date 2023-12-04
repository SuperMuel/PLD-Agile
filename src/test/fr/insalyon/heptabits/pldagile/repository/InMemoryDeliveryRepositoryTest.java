package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.IdGenerator;
import fr.insalyon.heptabits.pldagile.model.Delivery;
import fr.insalyon.heptabits.pldagile.model.Intersection;
import fr.insalyon.heptabits.pldagile.model.TimeWindow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryDeliveryRepositoryTest {

    IdGenerator idGenerator;
    InMemoryDeliveryRepository repo;
    Intersection intersection1;
    Intersection intersection2;

    // timeWindow1 from 8h to 9h
    TimeWindow timeWindow1 = new TimeWindow(LocalTime.of(8, 0), LocalTime.of(9, 0));
    // dateTime1 at 8h15
    LocalDateTime dateTime1 = LocalDateTime.of(2023, 2, 25, 8, 15);

    // timeWindow2 from 9h to 10h
    TimeWindow timeWindow2 = new TimeWindow(LocalTime.of(9, 0), LocalTime.of(10, 0));

    // dateTime2 at 9h15
    LocalDateTime dateTime2 = LocalDateTime.of(2023, 2, 25, 9, 15);


    @BeforeEach
    void setUp() {
        idGenerator = new IdGenerator();
        repo = new InMemoryDeliveryRepository(idGenerator);
        intersection1 = new Intersection(1, 40, 40);
        intersection2 = new Intersection(2, 30, 30);
        repo.create(dateTime1, intersection1 , 1, 1, timeWindow1);
        repo.create(dateTime2, intersection2, 1, 1, timeWindow2);
    }

    @Test
    void findById() {
        assertNotNull(repo.findById(1));
    }

    @Test
    void findByIdNotFound() {
        assertNull(repo.findById(302384));
    }

    @Test
    void findAll() {
        assertEquals(repo.findAll().size(), 2);
    }

    @Test
    void create() {
        TimeWindow timeWindow = new TimeWindow(LocalTime.of(9, 0), LocalTime.of(10, 0));
        Delivery c = repo.create(LocalDateTime.of(2023, 2, 25, 9, 15), intersection2 , 1, 1, timeWindow);
        assertEquals(c.getId(), 3);
    }

    @Test
    void update() {
        Delivery d1 = new Delivery(1, LocalDateTime.of(2023, 2, 26, 9, 0), intersection1 , 2, 1, timeWindow1);
        Delivery d2 = repo.update(d1);
        assertEquals(d2.getCourierId(), 2);
    }

    @Test
    void delete() {
        Delivery d1 = repo.findById(1);
        int sizeBefore = repo.findAll().size();
        repo.delete(d1);
        assertEquals(repo.findAll().size(), sizeBefore-1);
    }
}