package fr.insalyon.heptabits.pldagile.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourierTest {

    Courier courier;
    String firstName = "John";
    String lastName = "Doe";
    String email = "JohnDoe@johndoe.com";
    String phoneNumber = "0123456789";
    @BeforeEach
    void setUp() {
        courier = new Courier(0, firstName, lastName, email, phoneNumber);
    }

    @Test
    void getFirstName() {
        assertEquals(firstName, courier.getFirstName());
    }

    @Test
    void getLastName() {
        assertEquals(lastName, courier.getLastName());
    }

    @Test
    void getEmail() {
        assertEquals(email, courier.getEmail());
    }

    @Test
    void getPhoneNumber() {
        assertEquals(phoneNumber, courier.getPhoneNumber());
    }

    @Test
    void testToString() {
        assertEquals(firstName + " " + lastName, courier.toString());
    }
}