package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.Delivery;
import fr.insalyon.heptabits.pldagile.model.Intersection;
import fr.insalyon.heptabits.pldagile.model.Map;
import fr.insalyon.heptabits.pldagile.model.Segment;
import fr.insalyon.heptabits.pldagile.repository.DeliveryRepository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XmlDeliveriesService implements IXmlDeliveriesService {
    private final DeliveryRepository deliveryRepository;
    private final MapService mapService;
    private final DocumentBuilder documentBuilder;

    public DeliveryRepository getDeliveryRepository() { return this.deliveryRepository; }

    public XmlDeliveriesService(DeliveryRepository deliveryRepository, MapService mapService, DocumentBuilder documentBuilder) {
        this.deliveryRepository = deliveryRepository;
        this.mapService = mapService;
        this.documentBuilder = documentBuilder;
    }

    @Override
    public void exportDeliveriesToXml(String filePath) {
        List<Delivery> deliveryList = deliveryRepository.findAll();
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<deliveries>\n");

            for (Delivery delivery : deliveryList) {
                writer.write("    <delivery startTime=\"" + delivery.getScheduledDateTime() + "\" ");
                writer.write("destinationId=\"" + delivery.getDestination().getId() + "\" ");
                writer.write("courierId=\"" + delivery.getCourierId() + "\"/>\n");
            }

            writer.write("</deliveries>\n");

            System.out.println("Fichier XML généré avec succès !");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importDeliveriesFromXml(String filePath) throws IOException, SAXException {

        Document document;
        Path file = Path.of(filePath);
        document = this.documentBuilder.parse(file.toFile());
        NodeList deliveryList = document.getElementsByTagName("delivery");
        for (int i = 0; i < deliveryList.getLength(); i++) {
            Node node = deliveryList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                LocalDateTime startTime = LocalDateTime.parse(element.getAttribute("startTime"));
                long destinationId = Long.parseLong(element.getAttribute("destinationId"));
                long courierId = Long.parseLong(element.getAttribute("courierId"));

                deliveryRepository.create(startTime, mapService.getCurrentMap().getIntersections().get(destinationId), courierId);
            }
        }
    }
}
