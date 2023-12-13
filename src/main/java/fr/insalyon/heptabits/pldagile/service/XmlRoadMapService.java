package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.*;
import fr.insalyon.heptabits.pldagile.repository.ClientRepository;
import fr.insalyon.heptabits.pldagile.repository.CourierRepository;
import fr.insalyon.heptabits.pldagile.repository.RoadMapRepository;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class XmlRoadMapService implements IXmlRoadMapService {
    private final RoadMapRepository roadMapRepository;
    private final MapService mapService;
    private final DocumentBuilder documentBuilder;
    private final CourierRepository courierRepository;
    private final ClientRepository clientRepository;
    public RoadMapRepository getRoadMapRepository() { return this.roadMapRepository; }

    public XmlRoadMapService(RoadMapRepository roadMapRepository, MapService mapService, DocumentBuilder documentBuilder, CourierRepository courierRepository, ClientRepository clientRepository) {
        this.roadMapRepository = roadMapRepository;
        this.mapService = mapService;
        this.documentBuilder = documentBuilder;
        this.courierRepository = courierRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public void exportRoadMapsToXml(String filePath) {

        List<RoadMap> roadmapList = roadMapRepository.getAll();
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<roadmaps>\n");

            for (RoadMap roadMap : roadmapList) {
                writer.write("  <roadmap>\n");

                // Deliveries
                writer.write("      <deliveries>\n");
                List<Delivery> deliveries = roadMap.getDeliveries();
                for (Delivery delivery : deliveries) {
                    writer.write("          <delivery scheduledDateTime=\"" + delivery.scheduledDateTime() + "\" ");
                    writer.write("destinationId=\"" + delivery.destination().getId() + "\" ");
                    writer.write("courierId=\"" + delivery.courierId() + "\" ");
                    writer.write("clientId=\"" + delivery.clientId() + "\" ");
                    writer.write("timeWindowStart=\"" + delivery.timeWindow().getStart() + "\" ");
                    writer.write("timeWindowEnd=\"" + delivery.timeWindow().getEnd() + "\"/>\n");
                }
                writer.write("      </deliveries>\n");

                // Legs
                writer.write("      <legs>\n");
                List<Leg> legs = roadMap.getLegs();
                for (Leg leg : legs) {
                    writer.write("          <leg departureTime=\"" + leg.departureTime() + "\">\n");

                    // Intersections in leg
                    writer.write("              <intersections>\n");
                    List<Intersection> intersections = leg.intersections();
                    for (Intersection intersection : intersections) {
                        writer.write("                  <intersection id=\"" + intersection.getId() + "\" ");
                        writer.write("latitude=\"" + intersection.getLatitude() + "\" ");
                        writer.write("longitude=\"" + intersection.getLongitude() + "\"/>\n");
                    }
                    writer.write("              </intersections>\n");

                    // Segments in leg
                    writer.write("              <segments>\n");
                    List<Segment> segments = leg.segments();
                    for (Segment segment : segments) {
                        writer.write("                  <segment originId=\"" + segment.getOriginId() + "\" ");
                        writer.write("destinationId=\"" + segment.getDestinationId() + "\" ");
                        writer.write("name=\"" + segment.name() + "\" ");
                        writer.write("length=\"" + segment.length() + "\"/>\n");
                    }
                    writer.write("              </segments>\n");

                    writer.write("          </leg>\n");

                }
                writer.write("      </legs>\n");

                writer.write("  </roadmap>\n");
            }

            writer.write("</roadmaps>");

            System.out.println("Fichier XML généré avec succès !");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void importRoadMapsFromXml(String filePath) throws IOException, SAXException {
        Document document;
        Path file = Path.of(filePath);
        document = this.documentBuilder.parse(file.toFile());
        NodeList roadmapList = document.getElementsByTagName("roadmap");
        for (int i = 0; i < roadmapList.getLength(); i++) {
            Node roadMapNode = roadmapList.item(i);
            if (roadMapNode.getNodeType() == Node.ELEMENT_NODE) {
                Element roadMapElement = (Element) roadMapNode;

                // Deliveries
                NodeList deliveryList = roadMapElement.getElementsByTagName("delivery");
                List<Delivery> deliveries = new ArrayList<>();
                for (int j = 0; j < deliveryList.getLength(); j++) {
                    Node deliveryNode = deliveryList.item(j);
                    if (deliveryNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element deliveryElement = (Element) deliveryNode;

                        LocalDateTime scheduledDateTime = LocalDateTime.parse(deliveryElement.getAttribute("scheduledDateTime"));
                        long destinationId = Long.parseLong(deliveryElement.getAttribute("destinationId"));
                        long courierId = Long.parseLong(deliveryElement.getAttribute("courierId"));
                        long clientId = Long.parseLong(deliveryElement.getAttribute("clientId"));

                        TimeWindow timeWindow = new TimeWindow(LocalTime.parse(deliveryElement.getAttribute("timeWindowStart")), LocalTime.parse(deliveryElement.getAttribute("timeWindowEnd")));

                        Courier courier = courierRepository.findById(courierId);
                        Client client = clientRepository.findById(clientId);
                        Intersection intersection = mapService.getCurrentMap().getIntersections().get(destinationId);
                        if (courier == null || client == null || intersection == null) {
                            System.out.println("Livreur et/ou Client et/ou Intersection dans les <deliveries> du fichier XML introuvable(s).");
                        } else {
                            Delivery delivery = new Delivery(scheduledDateTime, intersection, courierId, clientId, timeWindow);
                            deliveries.add(delivery);
                        }
                    }
                }

                // Legs
                NodeList legList = roadMapElement.getElementsByTagName("leg");
                List<Leg> legs = new ArrayList<>();
                for (int j = 0; j < legList.getLength(); j++) {
                    Node legNode = legList.item(j);
                    if (legNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element legElement = (Element) legNode;

                        LocalTime departureTime = LocalTime.parse(legElement.getAttribute("departureTime"));

                        // Intersections in leg
                        NodeList intersectionList = legElement.getElementsByTagName("intersection");
                        List<Intersection> intersections = new ArrayList<>();
                        for (int k = 0; k < intersectionList.getLength(); k++) {
                            Node intersectionNode = intersectionList.item(k);
                            if (intersectionNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element intersectionElement = (Element) intersectionNode;

                                long intersectionId = Long.parseLong(intersectionElement.getAttribute("id"));
                                Intersection intersection = mapService.getCurrentMap().getIntersections().get(intersectionId);

                                if (intersection == null) {
                                    System.out.println("Intersection id=" + intersectionId + " dans les <intersections> des <legs> du fichier XML introuvable.");
                                } else {
                                    intersections.add(intersection);
                                }
                            }
                        }

                        // Segments in leg
                        NodeList segmentList = legElement.getElementsByTagName("segment");
                        List<Segment> segments = new ArrayList<>();
                        for (int k = 0; k < segmentList.getLength(); k++) {
                            Node segmentNode = segmentList.item(k);
                            if (segmentNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element segmentElement = (Element) segmentNode;

                                long originId = Long.parseLong(segmentElement.getAttribute("originId"));
                                long destinationId = Long.parseLong(segmentElement.getAttribute("destinationId"));
                                String name = (segmentElement.getAttribute("name"));
                                double length = Double.parseDouble(segmentElement.getAttribute("length"));

                                Intersection originIntersection = mapService.getCurrentMap().getIntersections().get(originId);
                                Intersection destinationIntersection = mapService.getCurrentMap().getIntersections().get(destinationId);

                                if (originIntersection == null || destinationIntersection == null) {
                                    System.out.println("Intersection d'origine et/ou de destination dans les <segments> des <legs> du fichier XML introuvable(s).");
                                } else {
                                    Segment segment = new Segment(originIntersection, destinationIntersection, name, length);
                                    segments.add(segment);
                                }
                            }
                        }

                        if (intersections.isEmpty() || segments.isEmpty()) {
                            System.out.println("Pas d'intersections et/ou segments valides pour créer une leg.");
                        } else {
                            Leg leg = new Leg(intersections, segments, departureTime);
                            legs.add(leg);
                        }

                    }
                }

                roadMapRepository.create(deliveries, legs);

            }
        }

    }


}