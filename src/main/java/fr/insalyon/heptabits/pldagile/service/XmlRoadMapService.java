package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.*;
import fr.insalyon.heptabits.pldagile.repository.ClientRepository;
import fr.insalyon.heptabits.pldagile.repository.CourierRepository;
import fr.insalyon.heptabits.pldagile.repository.RoadMapRepository;

import javax.xml.parsers.DocumentBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class XmlRoadMapService implements IXmlRoadMapService {
    private final RoadMapRepository roadMapRepository;
    private final MapService mapService;
    private final CourierRepository courierRepository;
    private final ClientRepository clientRepository;

    private final IXmlRoadMapsSerializer xmlRoadMapsSerializer;

    public XmlRoadMapService(RoadMapRepository roadMapRepository, MapService mapService, CourierRepository courierRepository, ClientRepository clientRepository, IXmlRoadMapsSerializer xmlRoadMapsSerializer) {
        this.roadMapRepository = roadMapRepository;
        this.mapService = mapService;
        this.courierRepository = courierRepository;
        this.clientRepository = clientRepository;
        this.xmlRoadMapsSerializer = xmlRoadMapsSerializer;
    }

    /**
     * Export roadmaps to XML file
     *
     * @param fileWriter FileWriter
     * @throws RuntimeException RuntimeException if error while writing to file
     */
    @Override
    public void exportRoadMapsToXml(FileWriter fileWriter) {
        List<RoadMap> roadMaps = roadMapRepository.getAll();
        String xml = xmlRoadMapsSerializer.serialize(roadMaps);

        try {
            fileWriter.write(xml);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("Error while writing to file");
        }
    }

    /**
     * Import roadmaps from XML file
     *
     * @param file XML file
     * @throws RuntimeException RuntimeException if error while reading file
     */
    @Override
    public void importRoadMapsFromXml(File file) {
        String xml;
        try {
            xml = Files.readString(file.toPath());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid XML");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<RoadMap> roadMaps = xmlRoadMapsSerializer.deserialize(xml, mapService.getCurrentMap().getIntersections());

        for (RoadMap roadMap : roadMaps) {
            if (courierRepository.findById(roadMap.getCourierId()) == null) {
                throw new RuntimeException("Courier not found");
            }
            roadMap.getDeliveries().forEach(delivery -> {
                if (clientRepository.findById(delivery.clientId()) == null) {
                    throw new RuntimeException("Client not found");
                }
            });

            roadMapRepository.create(roadMap.getDeliveries(), roadMap.getLegs());
        }


    }

}