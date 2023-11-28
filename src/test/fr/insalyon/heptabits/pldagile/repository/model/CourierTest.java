package fr.insalyon.heptabits.pldagile.model;

import fr.insalyon.heptabits.pldagile.model.Courier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourierTest {
    Courier courier;

    @BeforeEach
    void setUp() {
        courier = new Courier(0, "Courier 1");
    }

    @Test
    void getName() {
        assertEquals("Courier 1", courier.getName());
    }

}