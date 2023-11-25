package fr.insalyon.heptabits.pldagile.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    Client client;
    @BeforeEach
    void setUp() {
        client = new Client(0, "Client 1", "0123456789");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getName() {
        assertEquals("Client 1", client.getName());
    }

    @Test
    void getPhoneNumber() {
        assertEquals("0123456789", client.getPhoneNumber());
    }
}