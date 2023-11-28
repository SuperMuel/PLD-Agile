package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.IdGenerator;
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
        assertEquals(repo.findByName("Raph", null).get(0).getName(), "Raphaël");
    }

    @Test
    void findByLastName() {
        assertEquals(repo.findByName(null, "SI").get(0).getLastName(), "SIMAR");
    }

    @Test
    void findByLastNameAndName() {
        assertEquals(repo.findByName("Chlo", "CHA").get(0).getName(), "Chloé");
    }

    @Test
    void findAll() {
        assertEquals(repo.findAll().get(0).getName(), "Chloé");
        assertEquals(repo.findAll().get(1).getName(), "Raphaël");
        assertEquals(repo.findAll().size(), 2);
    }

    @Test
    void update() {
        Client c1 = new Client(1, "Raphael", "SIMAR", "0845691235");
        Client c2 = repo.update(c1);
        assertEquals(c2.getName(), "Raphael");
    }

    @Test
    void delete() {
        Client c1 = repo.findById(1);
        int sizeBefore = repo.findAll().size();
        repo.delete(c1);
        assertEquals(repo.findAll().size(), sizeBefore-1);
    }
}