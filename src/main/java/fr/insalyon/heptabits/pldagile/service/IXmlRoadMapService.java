package fr.insalyon.heptabits.pldagile.service;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

public interface IXmlRoadMapService {

    void exportRoadMapsToXml(File file);

    void importRoadMapsFromXml(File file) throws IOException, SAXException;
}
