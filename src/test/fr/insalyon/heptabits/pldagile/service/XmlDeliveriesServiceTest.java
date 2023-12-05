package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.*;
import fr.insalyon.heptabits.pldagile.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

class XmlDeliveriesServiceTest {

    IXmlMapParser parser;
    DocumentBuilderFactory documentBuilderFactory;
    DocumentBuilder documentBuilder;

    CourierRepository courierRepository;

    ClientRepository clientRepository;

    IdGenerator idGenerator;
    InMemoryDeliveryRepository repo;
    XmlDeliveriesService xmlDeliveriesService;
    Map map;

    private Map createExampleMap() {
        Intersection i1 = new Intersection(1, 1, 1);
        Intersection i2 = new Intersection(2, 2, 2);
        Intersection i3 = new Intersection(3, 3, 3);

        HashMap<Long, Intersection> intersections = new HashMap<>();
        intersections.put(i1.getId(), i1);
        intersections.put(i2.getId(), i2);
        intersections.put(i3.getId(), i3);

        Segment s1 = new Segment(1, i1.getId(), i2.getId(), "s1", 1);
        List<Segment> segments = new ArrayList<>();
        segments.add(s1);

        return new Map(42, intersections, segments, i1.getId());
    }

    @BeforeEach
    void setUp() throws ParserConfigurationException, IOException, SAXException {

        XmlMapParser mapParser = new XmlMapParser();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        XmlMapService mapService = new XmlMapService(mapParser, documentBuilder);
        mapService.loadMap(Path.of("src/main/resources/fr/insalyon/heptabits/pldagile/ExamplesMap/smallMap.xml"));

        idGenerator = new IdGenerator();
        repo = new InMemoryDeliveryRepository(idGenerator);
        courierRepository = new InMemoryCourierRepository(idGenerator);
        clientRepository = new InMemoryClientRepository(idGenerator);

        clientRepository.create("SIMAR", "Raphaël", "125532555");
        courierRepository.create("CHABAL", "Chloé", "ouizuet", "056151562");

        xmlDeliveriesService = new XmlDeliveriesService(repo, mapService, documentBuilder, courierRepository, clientRepository);
    }

    @Test
    void exportDeliveriesToXml() {
        Intersection intersection1 = new Intersection(1, 40, 40);
        Intersection intersection2 = new Intersection(2, 30, 30);
        TimeWindow timeWindow = new TimeWindow(LocalTime.parse("08:00"), LocalTime.parse("09:00"));

        repo.create(LocalDateTime.of(2023, 2, 25, 8, 0), intersection1 , 2, 1, timeWindow);
        repo.create(LocalDateTime.of(2023, 2, 25, 9, 0), intersection2, 2, 1, timeWindow);

        xmlDeliveriesService.exportDeliveriesToXml("src/test/resources/testExport.xml");
    }
    @Test
    void importDeliveriesFromXml() throws IOException, SAXException {
        xmlDeliveriesService.importDeliveriesFromXml("src/test/resources/testImport.xml");
        assertEquals(xmlDeliveriesService.getDeliveryRepository().findAll().size(), 3);
        assertEquals(xmlDeliveriesService.getDeliveryRepository().findAll().get(0).getDestination().getId(), 459797860L);
    }
}