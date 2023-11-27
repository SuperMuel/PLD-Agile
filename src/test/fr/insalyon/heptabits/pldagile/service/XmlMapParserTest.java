package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class XmlMapParserTest {
    final String XML_BODY = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<map>\n" +
            "    <warehouse address=\"1\"/>\n" +
            "    <intersection id=\"1\" latitude=\"1\" longitude=\"1\"/>\n" +
            "    <intersection id=\"2\" latitude=\"2\" longitude=\"2\"/>\n" +
            "    <intersection id=\"3\" latitude=\"3\" longitude=\"3\"/>\n" +
            "    <segment origin=\"1\" destination=\"2\" length=\"1\" name=\"s1\"/>\n" +
            "</map>";

    XmlMapParser parser;
    Document document;
    @BeforeEach
    void setUp() throws ParserConfigurationException, IOException, SAXException {
        parser = new XmlMapParser();

        // create document from string instead of file
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(XML_BODY));
        document = builder.parse(is);
    }


    @Test
    void parse() {
        Map map = parser.parse(document, 1L);
        System.out.println(map);
        assertEquals(1, map.getWarehouseId());
        assertEquals(3, map.getIntersections().size());
        assertEquals(1, map.getIntersections().get(1L).getId());
        assertEquals(2, map.getIntersections().get(2L).getId());
        assertEquals(3, map.getIntersections().get(3L).getId());
        assertEquals(1, map.getSegments().size());
        assertEquals("s1", map.getSegments().get(0).getName());


    }

    @Test
    void segmentIdsStartAt0() {
        Map map = parser.parse(document, 1L);
        assertEquals(0, map.getSegments().get(0).getId());
    }
}