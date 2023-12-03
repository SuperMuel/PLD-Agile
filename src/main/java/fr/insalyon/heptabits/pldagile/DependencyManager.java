package fr.insalyon.heptabits.pldagile;

import fr.insalyon.heptabits.pldagile.model.IdGenerator;
import fr.insalyon.heptabits.pldagile.repository.*;
import fr.insalyon.heptabits.pldagile.service.MapService;
import fr.insalyon.heptabits.pldagile.service.XmlMapParser;
import fr.insalyon.heptabits.pldagile.service.XmlMapService;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.nio.file.Path;

public class DependencyManager {
    private final CourierRepository courierRepository;

    private final CourierRepository courierRepositoryInMemory;
    private final DeliveryRepository deliveryRepository;

    private final ClientRepository clientRepository;

    private final ClientRepository clientRepositoryInMemory;

    private final IdGenerator idGenerator;

    private final TimeWindowRepository timeWindowRepository;

    private final MapService mapService;

    private final SQLiteConnection connection;

    public DependencyManager() {
        idGenerator = new IdGenerator();
        connection = new SQLiteConnection("identifier.sqlite");
        courierRepository = new SqlCourierRepository(getConnection(), getIdGenerator());
        courierRepositoryInMemory = new InMemoryCourierRepository(getIdGenerator());
        deliveryRepository = new InMemoryDeliveryRepository(getIdGenerator());
        clientRepository = new SqlClientRepository(getConnection(), getIdGenerator());
        clientRepositoryInMemory = new InMemoryClientRepository(getIdGenerator());
        timeWindowRepository = new FixedTimeWindowRepository();


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
    }

    public IdGenerator getIdGenerator() {
        return idGenerator;
    }

    public CourierRepository getCourierRepository() {
        return courierRepository;
    }

    public DeliveryRepository getDeliveryRepository() {
        return deliveryRepository;
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

    public SQLiteConnection getConnection() {
        return connection;
    }

    public ClientRepository getClientRepositoryInMemory() {
        return clientRepositoryInMemory;
    }

    public CourierRepository getCourierRepositoryInMemory() {
        return courierRepositoryInMemory;
    }
}
