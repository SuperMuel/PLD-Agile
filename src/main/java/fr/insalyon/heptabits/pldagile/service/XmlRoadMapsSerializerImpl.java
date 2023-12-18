package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.*;
import fr.insalyon.heptabits.pldagile.repository.ClientRepository;
import fr.insalyon.heptabits.pldagile.repository.CourierRepository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to serialize and deserialize road maps into xml files
 */
public class XmlRoadMapsSerializerImpl implements IXmlRoadMapsSerializer {

    /**
     * Serialize a list of roadmaps to XML
     * @param roadMaps the roadmaps to serialize
     * @return the XML string
     */
    @Override
    public String serialize(List<RoadMap> roadMaps) {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<roadmaps>\n");

        for (RoadMap roadMap : roadMaps) {
            xml.append("  <roadmap>\n");

            // Deliveries
            xml.append("      <deliveries>\n");
            List<Delivery> deliveries = roadMap.getDeliveries();
            for (Delivery delivery : deliveries) {
                xml.append("          <delivery scheduledDateTime=\"").append(delivery.scheduledDateTime()).append("\" ");
                xml.append("destinationId=\"").append(delivery.destination().getId()).append("\" ");
                xml.append("courierId=\"").append(delivery.courierId()).append("\" ");
                xml.append("clientId=\"").append(delivery.clientId()).append("\" ");
                xml.append("timeWindowStart=\"").append(delivery.timeWindow().getStart()).append("\" ");
                xml.append("timeWindowEnd=\"").append(delivery.timeWindow().getEnd()).append("\"/>\n");
            }
            xml.append("      </deliveries>\n");

            // Legs
            xml.append("      <legs>\n");
            List<Leg> legs = roadMap.getLegs();
            for (Leg leg : legs) {
                xml.append("          <leg departureTime=\"").append(leg.departureTime()).append("\">\n");

                // Intersections in leg
                xml.append("              <intersections>\n");
                List<Intersection> intersections = leg.intersections();
                for (Intersection intersection : intersections) {
                    xml.append("                  <intersection id=\"").append(intersection.getId()).append("\" ");
                    xml.append("latitude=\"").append(intersection.getLatitude()).append("\" ");
                    xml.append("longitude=\"").append(intersection.getLongitude()).append("\"/>\n");
                }
                xml.append("              </intersections>\n");

                // Segments in leg
                xml.append("              <segments>\n");
                List<Segment> segments = leg.segments();
                for (Segment segment : segments) {
                    xml.append("                  <segment originId=\"").append(segment.getOriginId()).append("\" ");
                    xml.append("destinationId=\"").append(segment.getDestinationId()).append("\" ");
                    xml.append("name=\"").append(segment.name()).append("\" ");
                    xml.append("length=\"").append(segment.length()).append("\"/>\n");
                }
                xml.append("              </segments>\n");

                xml.append("          </leg>\n");

            }
            xml.append("      </legs>\n");

            xml.append("  </roadmap>\n");
        }

        xml.append("</roadmaps>");

        return xml.toString();
    }

    Document documentFromString(String input) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(input));
        return builder.parse(is);
    }

    /**
     * Deserialize a list of roadmaps from XML
     * @param xml the XML string
     * @param existingIntersections the intersections that already exist in the map
     * @return the list of roadmaps. The roadmaps will have -1 ids. The intersections and segments will have the ids from the XML file.
     *
     * @throws RuntimeException if the XML is invalid, or if an intersection id is invalid
     */
    @Override
    public List<RoadMap> deserialize(String xml, java.util.Map<Long, Intersection> existingIntersections) {
        Document document;
        try {
            document = documentFromString(xml);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }

        List<RoadMap> roadMaps = new ArrayList<>();

        NodeList roadmapList = document.getElementsByTagName("roadmap");
        for (int i = 0; i < roadmapList.getLength(); i++) {
            Node roadMapNode = roadmapList.item(i);
            if (roadMapNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Element roadMapElement = (Element) roadMapNode;

            // Deliveries
            NodeList deliveryList = roadMapElement.getElementsByTagName("delivery");
            List<Delivery> deliveries = new ArrayList<>();
            for (int j = 0; j < deliveryList.getLength(); j++) {
                Node deliveryNode = deliveryList.item(j);
                if (deliveryNode.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                Element deliveryElement = (Element) deliveryNode;

                LocalDateTime scheduledDateTime = LocalDateTime.parse(deliveryElement.getAttribute("scheduledDateTime"));
                long destinationId = Long.parseLong(deliveryElement.getAttribute("destinationId"));
                long courierId = Long.parseLong(deliveryElement.getAttribute("courierId"));
                long clientId = Long.parseLong(deliveryElement.getAttribute("clientId"));

                TimeWindow timeWindow = new TimeWindow(LocalTime.parse(deliveryElement.getAttribute("timeWindowStart")), LocalTime.parse(deliveryElement.getAttribute("timeWindowEnd")));

                Intersection intersection = existingIntersections.get(destinationId);
                if (intersection == null) {
                    throw new RuntimeException("Invalid intersection id in XML file. Did you load the correct map?");
                }
                Delivery delivery = new Delivery(scheduledDateTime, intersection, courierId, clientId, timeWindow);
                deliveries.add(delivery);

            }

            // Legs
            NodeList legList = roadMapElement.getElementsByTagName("leg");
            List<Leg> legs = new ArrayList<>();
            for (int j = 0; j < legList.getLength(); j++) {
                Node legNode = legList.item(j);
                if (legNode.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                Element legElement = (Element) legNode;

                LocalTime departureTime = LocalTime.parse(legElement.getAttribute("departureTime"));

                // Intersections in leg
                NodeList intersectionList = legElement.getElementsByTagName("intersection");
                List<Intersection> intersections = new ArrayList<>();
                for (int k = 0; k < intersectionList.getLength(); k++) {
                    Node intersectionNode = intersectionList.item(k);
                    if (intersectionNode.getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }
                    Element intersectionElement = (Element) intersectionNode;

                    long intersectionId = Long.parseLong(intersectionElement.getAttribute("id"));
                    Intersection intersection = existingIntersections.get(intersectionId);

                    if (intersection == null) {
                        throw new RuntimeException("Invalid intersection id in XML file. Did you load the correct map?");
                    }
                    intersections.add(intersection);

                }

                // Segments in leg
                NodeList segmentList = legElement.getElementsByTagName("segment");
                List<Segment> segments = new ArrayList<>();
                for (int k = 0; k < segmentList.getLength(); k++) {
                    Node segmentNode = segmentList.item(k);
                    if (segmentNode.getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }
                    Element segmentElement = (Element) segmentNode;

                    long originId = Long.parseLong(segmentElement.getAttribute("originId"));
                    long destinationId = Long.parseLong(segmentElement.getAttribute("destinationId"));
                    String name = (segmentElement.getAttribute("name"));
                    double length = Double.parseDouble(segmentElement.getAttribute("length"));

                    Intersection originIntersection = existingIntersections.get(originId);
                    Intersection destinationIntersection = existingIntersections.get(destinationId);

                    if (originIntersection == null || destinationIntersection == null) {
                        throw new RuntimeException("Invalid intersection id in XML file. Did you load the correct map?");
                    }
                    Segment segment = new Segment(originIntersection, destinationIntersection, name, length);
                    segments.add(segment);

                }

                if (intersections.isEmpty() || segments.isEmpty()) {
                    throw new RuntimeException("No intersections or segments in leg.");
                }
                Leg leg = new Leg(intersections, segments, departureTime);
                legs.add(leg);
            }

            roadMaps.add(new RoadMap(-1, deliveries, legs));
        }
        return  roadMaps;
    }
}
