package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.Intersection;
import fr.insalyon.heptabits.pldagile.model.Map;
import fr.insalyon.heptabits.pldagile.model.Segment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XmlMapParser implements IXmlMapParser {
    public Map parse(Document document, long mapId) {
        document.getDocumentElement().normalize();

        NodeList intersectionList = document.getElementsByTagName("intersection");
        NodeList segmentList = document.getElementsByTagName("segment");
        long warehouseId = Long.parseLong(document.getElementsByTagName("warehouse").item(0).getAttributes().getNamedItem("address").getNodeValue());


        HashMap<Long, Intersection> intersections = new HashMap<>();
        List<Segment> segments = new ArrayList<>();

        for (int i = 0; i < intersectionList.getLength(); i++) {
            Node node = intersectionList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                long id = Long.parseLong(element.getAttribute("id"));
                float latitude = Float.parseFloat(element.getAttribute("latitude"));
                float longitude = Float.parseFloat(element.getAttribute("longitude"));
                intersections.put(id, new Intersection(id, latitude, longitude));
            }
        }

        for (int i = 0; i < segmentList.getLength(); i++) {
            Node node = segmentList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                long originId = Long.parseLong(element.getAttribute("origin"));
                long destinationId = Long.parseLong(element.getAttribute("destination"));
                float length = Float.parseFloat(element.getAttribute("length"));
                String name = element.getAttribute("name");

                Intersection origin = intersections.get(originId);
                Intersection destination = intersections.get(destinationId);

                if(origin == null || destination == null){
                    throw new RuntimeException("XML file is not valid : segment with unknown intersection");
                }

                segments.add(new Segment(origin, destination, name, length));
            }
        }

        return new Map(mapId, intersections, segments, warehouseId);
    }

}
