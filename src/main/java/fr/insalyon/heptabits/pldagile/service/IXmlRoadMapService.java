package fr.insalyon.heptabits.pldagile.service;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public interface IXmlRoadMapService {

    void exportRoadMapsToXml(FileWriter fileWriter);

    void importRoadMapsFromXml(File file);
}
