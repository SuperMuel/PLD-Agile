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
    final String XML_BODY = """
            <?xml version="1.0" encoding="UTF-8"?>
            <map>
                <warehouse address="1"/>
                <intersection id="1" latitude="1" longitude="1"/>
                <intersection id="2" latitude="2" longitude="2"/>
                <intersection id="3" latitude="3" longitude="3"/>
                <segment origin="1" destination="2" length="1" name="s1"/>
            </map>""";

    XmlMapParser parser;
    Document document;

    Document documentFromString(String input) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(input));
        return builder.parse(is);
    }

    @BeforeEach
    void setUp() throws ParserConfigurationException, IOException, SAXException {
        parser = new XmlMapParser();

        // create document from string instead of file
        document = documentFromString(XML_BODY);
    }


    @Test
    void parse() {
        Map map = parser.parse(document, 1L);
        assertEquals(1, map.getWarehouseId());
        assertEquals(3, map.getIntersections().size());
        assertEquals(1, map.getIntersections().get(1L).getId());
        assertEquals(2, map.getIntersections().get(2L).getId());
        assertEquals(3, map.getIntersections().get(3L).getId());
        assertEquals(1, map.getSegments().size());
        assertEquals("s1", map.getSegments().getFirst().name());
    }



    final String INVALID_XML_BODY = """
            <?xml version="1.0" encoding="UTF-8"?>
            <map>
                <warehouse address="1"/>
                <intersection id="1" latitude="1" longitude="1"/>
                <intersection id="2" latitude="2" longitude="2"/>
                <intersection id="3" latitude="3" longitude="3"/>
                <segment origin="1" destination="23423" length="1" name="s1"/>
            </map>""";

    @Test
    void inexistingIntersectionInSegmentThrows() throws ParserConfigurationException, IOException, SAXException {
        Document invalidDocument = documentFromString(INVALID_XML_BODY);

        assertThrows(RuntimeException.class, () -> parser.parse(invalidDocument, 1L));

    }


}