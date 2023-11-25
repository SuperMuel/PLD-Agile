package fr.insalyon.heptabits.pldagile.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class XmlMapServiceTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void loadMap() {
    }

    @Test
    void getCurrentMap() {
    }

    @Test
    void getCurrentMapOnNotLoadedMap(){
        XmlMapService xmlMapService = new XmlMapService();
        assertThrows(IllegalStateException.class, xmlMapService::getCurrentMap);
    }

}