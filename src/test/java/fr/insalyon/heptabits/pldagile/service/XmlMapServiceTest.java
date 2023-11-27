package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.Intersection;
import fr.insalyon.heptabits.pldagile.model.Map;
import fr.insalyon.heptabits.pldagile.model.Segment;
import fr.insalyon.heptabits.pldagile.service.IXmlMapParser;
import fr.insalyon.heptabits.pldagile.service.XmlMapService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class XmlMapServiceTest {
    IXmlMapParser mockParser;
    DocumentBuilder mockDocumentBuilder;
    XmlMapService service;

    Map map;

    private Map createExampleMap() {
        Intersection i1 = new Intersection(1, 1, 1);
        Intersection i2 = new Intersection(2, 2, 2);
        Intersection i3 = new Intersection(3, 3, 3);

        HashMap<Long, Intersection> intersections = new HashMap<>();
        intersections.put(i1.getId(), i1);
        intersections.put(i2.getId(), i2);
        intersections.put(i3.getId(), i3);

        Segment s1 = new Segment(1, i1.getId(), i2.getId(), "s1", 1);
        List<Segment> segments = new ArrayList<>();
        segments.add(s1);

        return new Map(42, intersections, segments, i1.getId());
    }

    @BeforeEach
    public void setUp() {
        map = createExampleMap();

        mockParser = Mockito.mock(IXmlMapParser.class);
        mockDocumentBuilder = Mockito.mock(DocumentBuilder.class);
        service = new XmlMapService(mockParser, mockDocumentBuilder);
    }


    @Test
    void loadMap() throws Exception{
        Document mockDocument = Mockito.mock(Document.class);

        when(mockParser.parse(any(Document.class), anyLong())).thenReturn(map);
        when(mockDocumentBuilder.parse(any(File.class))).thenReturn(mockDocument);

        service.loadMap(Path.of("test.xml"));

        verify(mockParser).parse(any(Document.class), anyLong());
        assertEquals(map.getId(), service.getCurrentMap().getId());
    }


    @Test
    void loadVoidMap() {
        assertThrows(IllegalArgumentException.class, () -> service.loadMap(null));
    }

    @Test
    void getCurrentMap() throws Exception {
        Document mockDocument = Mockito.mock(Document.class);

        when(mockParser.parse(any(Document.class), anyLong())).thenReturn(map);
        when(mockDocumentBuilder.parse(any(File.class))).thenReturn(mockDocument);

        service.loadMap(Path.of("test.xml"));

        verify(mockParser).parse(any(Document.class), anyLong());
        assertEquals(map.getId(), service.getCurrentMap().getId());
    }

    @Test
    void getCurrentMapOnNotLoadedMap() {
        assertThrows(IllegalStateException.class, service::getCurrentMap);
    }

}