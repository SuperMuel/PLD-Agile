package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.IdGenerator;
import fr.insalyon.heptabits.pldagile.model.Courier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryCourierRepositoryTest {

    InMemoryCourierRepository inMemoryCourierRepository;

    @BeforeEach
    void setUp() {
        IdGenerator idGenerator = new IdGenerator();
        inMemoryCourierRepository = new InMemoryCourierRepository(idGenerator);
    }

    @Test
    void create() {
        inMemoryCourierRepository.create("John", "Doe", "johndoe.email.com", "0123456789");
        assertEquals(1, inMemoryCourierRepository.findAll().size());
        final Courier firstCourier = inMemoryCourierRepository.findAll().get(0);
        assertEquals("John", firstCourier.getFirstName());
        assertEquals("Doe", firstCourier.getLastName());
        assertEquals("johndoe.email.com", firstCourier.getEmail());
        assertEquals("0123456789", firstCourier.getPhoneNumber());
    }

    @Test
    void findById() {
        inMemoryCourierRepository.create("John", "Doe", "johndoe.email.com", "0123456789");
        final Courier courier = inMemoryCourierRepository.findById(1L);
        assertEquals(1L, courier.getId());
        assertEquals("John", courier.getFirstName());
        assertEquals("Doe", courier.getLastName());
        assertEquals("johndoe.email.com", courier.getEmail());
        assertEquals("0123456789", courier.getPhoneNumber());

    }

    @Test
    void findAll() {
        inMemoryCourierRepository.create("John", "Doe", "johndoe.email.com", "0123456789");
        inMemoryCourierRepository.create("Jane", "Doe", "janedoe.email.com", "0123456789");
        assertEquals(2, inMemoryCourierRepository.findAll().size());
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
        inMemoryCourierRepository.create("John", "Doe", "johndoe.email.com", "0123456789");
        inMemoryCourierRepository.create("Jane", "Doe", "janedoe.email.com", "0123456789");
        inMemoryCourierRepository.deleteById(1L);
        assertEquals(1, inMemoryCourierRepository.findAll().size());
        assertEquals("Jane", inMemoryCourierRepository.findAll().get(0).getFirstName());
    }
}