package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.IdGenerator;
import fr.insalyon.heptabits.pldagile.model.Delivery;
import fr.insalyon.heptabits.pldagile.model.Intersection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryDeliveryRepositoryTest {

    IdGenerator idGenerator;
    InMemoryDeliveryRepository repo;
    Intersection intersection1;
    Intersection intersection2;
    @BeforeEach
    void setUp() {
        idGenerator = new IdGenerator();
        repo = new InMemoryDeliveryRepository(idGenerator);
        intersection1 = new Intersection(1, 40, 40);
        intersection2 = new Intersection(2, 30, 30);
        repo.create(LocalDateTime.of(2023, 2, 25, 8, 0), intersection1 , 1, 1);
        repo.create(LocalDateTime.of(2023, 2, 25, 9, 0), intersection2, 1, 1);
    }

    @Test
    void findById() {
        assertNotNull(repo.findById(1));
    }

    @Test
    void findAll() {
        assertEquals(repo.findAll().size(), 2);
    }

    @Test
    void create() {
        Delivery c = repo.create(LocalDateTime.of(2023, 2, 25, 9, 0), intersection2 , 1, 1);
        assertNotNull(repo.findById(3));
        assertEquals(c.getId(), 3);
    }

    @Test
    void update() {
        Delivery d1 = new Delivery(1, LocalDateTime.of(2023, 2, 26, 9, 0), intersection1 , 2, 1);
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