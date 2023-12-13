package fr.insalyon.heptabits.pldagile.service;

import org.xml.sax.SAXException;

import java.io.IOException;

public interface IXmlRoadMapService {

    void exportRoadMapsToXml(String filePath);

    void importRoadMapsFromXml(String filePath) throws IOException, SAXException;
}
