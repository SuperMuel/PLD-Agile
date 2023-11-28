package fr.insalyon.heptabits.pldagile;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdGeneratorTest {

    @Test
    void getNextId() {
        IdGenerator idGenerator = new IdGenerator();
        assertEquals(1, idGenerator.getNextId());
        assertEquals(2, idGenerator.getNextId());
        assertEquals(3, idGenerator.getNextId());
    }
}