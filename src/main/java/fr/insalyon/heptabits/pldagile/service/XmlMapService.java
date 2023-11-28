package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.Map;

import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;

import java.io.IOException;

import org.xml.sax.SAXException;


public class XmlMapService implements MapService {

    private Map currentMap;
    private final IXmlMapParser parser;
    private final DocumentBuilder documentBuilder;


    public XmlMapService(IXmlMapParser parser, DocumentBuilder documentBuilder) {
        this.parser = parser;
        this.documentBuilder = documentBuilder;
    }

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
