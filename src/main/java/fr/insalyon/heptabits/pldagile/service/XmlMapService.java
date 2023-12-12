package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.Map;

import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;

import java.io.IOException;

import org.xml.sax.SAXException;


/**
 * An implementation of the map service that loads a map from a XML file.
 */
public class XmlMapService implements MapService {

    private Map currentMap;
    private final IXmlMapParser parser;
    private final DocumentBuilder documentBuilder;

    /**
     * Creates a new XML map service.
     *
     * @param parser the XML map parser
     * @param documentBuilder the document builder
     */
    public XmlMapService(IXmlMapParser parser, DocumentBuilder documentBuilder) {
        this.parser = parser;
        this.documentBuilder = documentBuilder;
    }

    /**
     * Loads a map from a XML file.
     *
     * @param xmlPath the path to the XML file
     * @throws IllegalArgumentException if xmlPath is null
     * @throws RuntimeException if the XML file is invalid
     */
    public void loadMap(Path xmlPath) {
        if (xmlPath == null) {
            throw new IllegalArgumentException("xmlPath cannot be null");
        }
        try {
            Document doc = documentBuilder.parse(xmlPath.toFile());
            currentMap = parser.parse(doc, 0);

        } catch (IOException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the current map.
     *
     * You must call loadMap before calling this method.
     *
     * @throws IllegalStateException if no map is loaded
     */
    @Override
    public Map getCurrentMap() {
        if (currentMap == null) {
            throw new IllegalStateException("Map not loaded");
        }
        return currentMap;
    }
}
