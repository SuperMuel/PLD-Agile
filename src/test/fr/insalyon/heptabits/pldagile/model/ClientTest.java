package fr.insalyon.heptabits.pldagile.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    Client client;
    @BeforeEach
    void setUp() {
        client = new Client(0, "Chloé", "CHABAL", "0123456789");
    }
    @Test
    void getName() {
        assertEquals("Chloé", client.getFirstName());
    }

    @Test
    void getLastName() {
        assertEquals("CHABAL", client.getLastName());
    }

    @Test
    void getPhoneNumber() {
        assertEquals("0123456789", client.getPhoneNumber());
    }
}