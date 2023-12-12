package fr.insalyon.heptabits.pldagile;

import fr.insalyon.heptabits.pldagile.model.IdGenerator;
import fr.insalyon.heptabits.pldagile.repository.*;
import fr.insalyon.heptabits.pldagile.service.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.nio.file.Path;

public class DependencyManager {
    private final CourierRepository courierRepository;

    private final ClientRepository clientRepository;

    private final IdGenerator idGenerator;

    private final TimeWindowRepository timeWindowRepository;

    private final MapService mapService;


    private final RoadMapRepository roadMapRepository;

    private final RoadMapService roadMapService;

    private final DeliveryService deliveryService;

    public DependencyManager() {
        idGenerator = new IdGenerator();
        courierRepository = new MockCourierRepository(getIdGenerator());
        clientRepository = new MockClientRepository(getIdGenerator());
        timeWindowRepository = new FixedTimeWindowRepository();
        roadMapRepository = new InMemoryRoadMapRepository(getIdGenerator());
        deliveryService = new DeliveryService(roadMapRepository);

        // MapService initialization
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

        this.mapService = mapService;
        roadMapService = new RoadMapService(roadMapRepository, new NaiveRoadMapOptimizer(), mapService);

    }

    public IdGenerator getIdGenerator() {
        return idGenerator;
    }

    public CourierRepository getCourierRepository() {
        return courierRepository;
    }

    public ClientRepository getClientRepository() {
        return clientRepository;
    }

    public TimeWindowRepository getTimeWindowRepository() {
        return timeWindowRepository;
    }

    public MapService getMapService() {
        return mapService;
    }


    public RoadMapRepository getRoadMapRepository() {
        return roadMapRepository;
    }

    public RoadMapService getRoadMapService() {
        return roadMapService;
    }

    public  DeliveryService getDeliveryService() {
        return deliveryService;
    }

}
