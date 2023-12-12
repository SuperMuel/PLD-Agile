package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.Map;
import org.w3c.dom.Document;

/**
 * An interface for the XML map parser.
 */
public interface IXmlMapParser {
    /**
     * Parses a XML document to a map.
     *
     * @param document the XML document to parse
     * @param mapId the id of the map
     * @return the parsed map
     */
    Map parse(Document document, long mapId);
}
