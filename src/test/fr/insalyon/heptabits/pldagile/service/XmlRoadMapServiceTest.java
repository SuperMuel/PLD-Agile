package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.Client;
import fr.insalyon.heptabits.pldagile.model.IdGenerator;
import fr.insalyon.heptabits.pldagile.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;

import java.io.File;
import java.io.FileWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class XmlRoadMapServiceTest {

    XmlRoadMapService xmlRoadMapService;

    MockMapService mockMapService;

    RoadMapRepository roadMapRepository;

    CourierRepository courierRepository;

    ClientRepository clientRepository;

    IXmlRoadMapsSerializer xmlRoadMapsSerializer;

    @BeforeEach
    void setUp() {
        mockMapService = new MockMapService();
        roadMapRepository = new MockRoadMapRepository(new IdGenerator(), mockMapService);

        courierRepository = new InMemoryCourierRepository(new IdGenerator());
        clientRepository = new InMemoryClientRepository(new IdGenerator());

        xmlRoadMapsSerializer = Mockito.mock(IXmlRoadMapsSerializer.class);
        xmlRoadMapService = new XmlRoadMapService(roadMapRepository, mockMapService, courierRepository, clientRepository, xmlRoadMapsSerializer);
    }

    @Test
    void exportRoadMapsToXml() {
        FileWriter fileWriter = Mockito.mock(FileWriter.class);

        xmlRoadMapService.exportRoadMapsToXml(fileWriter);
        when(xmlRoadMapsSerializer.serialize(roadMapRepository.getAll())).thenReturn("xml");
        Mockito.verify(xmlRoadMapsSerializer).serialize(roadMapRepository.getAll());
    }

}