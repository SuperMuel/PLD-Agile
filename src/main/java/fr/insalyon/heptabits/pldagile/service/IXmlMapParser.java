package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.Map;
import org.w3c.dom.Document;

public interface IXmlMapParser {
    Map parse(Document document, long mapId);
}
