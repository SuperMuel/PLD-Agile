package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.IdGenerator;
import fr.insalyon.heptabits.pldagile.model.Intersection;
import fr.insalyon.heptabits.pldagile.model.RoadMap;
import fr.insalyon.heptabits.pldagile.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


class XmlRoadMapsSerializerImplTest {

    MockMapService mockMapService;

    RoadMapRepository roadMapRepository;

    CourierRepository courierRepository;

    ClientRepository clientRepository;


    XmlRoadMapsSerializerImpl roadMapsSerializer;

    @BeforeEach
    void setUp() {
        mockMapService = new MockMapService();
        roadMapRepository = new MockRoadMapRepository(new IdGenerator(), mockMapService);

        courierRepository = new InMemoryCourierRepository(new IdGenerator());
        clientRepository = new InMemoryClientRepository(new IdGenerator());

        roadMapsSerializer = new XmlRoadMapsSerializerImpl();
    }

    @Test
    void serialize() {
        List<RoadMap> initialRoadmaps =  roadMapRepository.getAll();

        assertFalse(initialRoadmaps.isEmpty());

        String xml = roadMapsSerializer.serialize(initialRoadmaps);

        List<RoadMap> roadMapsDeserialized =  roadMapsSerializer.deserialize(xml, mockMapService.getCurrentMap().getIntersections() );

        assertEquals(initialRoadmaps.size(), roadMapsDeserialized.size());

        for(int i = 0; i < initialRoadmaps.size(); i++){
            // Ids are not compared because deserialized roadmaps have new ids
            assertEquals(initialRoadmaps.get(i).getDeliveries(), roadMapsDeserialized.get(i).getDeliveries());
            assertEquals(initialRoadmaps.get(i).getLegs(), roadMapsDeserialized.get(i).getLegs());
        }
    }


    @Test
    void unknownDeliveryIntersectionThrows(){
        List<RoadMap> initialRoadmaps =  roadMapRepository.getAll();

        assertFalse(initialRoadmaps.isEmpty());

        // Chose an intersection that exists in the serialized roadmaps
        Intersection anIntersection = initialRoadmaps.getFirst().getDeliveries().getFirst().destination();

        // Remove it from the map
        Map<Long, Intersection> existingIntersections = new HashMap<>(mockMapService.getCurrentMap().getIntersections());
        existingIntersections.remove(anIntersection.getId());

        String xml = roadMapsSerializer.serialize(initialRoadmaps);

        assertThrows(RuntimeException.class, () -> roadMapsSerializer.deserialize(xml, existingIntersections));
    }


    @Test
    void unknownSegmentIntersectionThrows(){
        List<RoadMap> initialRoadmaps =  roadMapRepository.getAll();

        assertFalse(initialRoadmaps.isEmpty());

        // Chose an intersection that exists in the serialized roadmaps
        Intersection anIntersection = initialRoadmaps.getFirst().getLegs().getFirst().segments().getFirst().destination();

        // Remove it from the map
        Map<Long, Intersection> existingIntersections = new HashMap<>(mockMapService.getCurrentMap().getIntersections());
        existingIntersections.remove(anIntersection.getId());

        String xml = roadMapsSerializer.serialize(initialRoadmaps);

        assertThrows(RuntimeException.class, () -> roadMapsSerializer.deserialize(xml, existingIntersections));
    }

}