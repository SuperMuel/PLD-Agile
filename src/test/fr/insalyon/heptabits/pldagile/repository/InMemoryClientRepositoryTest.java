package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.IdGenerator;
import fr.insalyon.heptabits.pldagile.model.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryClientRepositoryTest {

    IdGenerator idGenerator;
    InMemoryClientRepository repo;
    @BeforeEach
    void setUp() {
        idGenerator = new IdGenerator();
        repo = new InMemoryClientRepository(idGenerator);
        repo.create("Raphaël", "SIMAR", "0123456789");
        repo.create("Chloé", "CHABAL", "1234567890");
    }

    @Test
    void create() {
        Client c = repo.create("MALLET", "Samuel", "0201502256");
        assertNotNull(repo.findById(3));
        assertEquals(c.getId(), 3);
    }

    @Test
    void findInexistingClientByIdIsNull() {
        assertNull(repo.findById(69));
    }

    @Test
    void findByName() {
        assertEquals(repo.findByName("Raph", null).getFirst().getFirstName(), "Raphaël");
    }

    @Test
    void findByLastName() {
        assertEquals(repo.findByName(null, "SI").getFirst().getLastName(), "SIMAR");
    }

    @Test
    void findByLastNameAndName() {
        assertEquals(repo.findByName("Chlo", "CHA").getFirst().getFirstName(), "Chloé");
    }

    @Test
    void findAll() {
        assertEquals(repo.findAll().get(0).getFirstName(), "Chloé");
        assertEquals(repo.findAll().get(1).getFirstName(), "Raphaël");
        assertEquals(repo.findAll().size(), 2);
    }

    @Test
    void update() {
        Client c1 = new Client(1, "Raphael", "SIMAR", "0845691235");
        Client c2 = repo.update(c1);
        assertEquals(c2.getFirstName(), "Raphael");
    }

    @Test
    void delete() {
        Client c1 = repo.findById(1);
        int sizeBefore = repo.findAll().size();
        repo.delete(c1);
        assertEquals(repo.findAll().size(), sizeBefore-1);
    }
}