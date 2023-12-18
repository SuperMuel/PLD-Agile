package fr.insalyon.heptabits.pldagile.service;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class is used to serialize and deserialize road maps into xml files
 */
public interface IXmlRoadMapService {

    /**
     * Serializes a list of road maps into an xml file
     *
     * @param fileWriter the file to write the road maps to
     */
    void exportRoadMapsToXml(FileWriter fileWriter);

    /**
     * Deserializes a list of road maps from an xml file
     *
     * @param file the file to read the road maps from
     */
    void importRoadMapsFromXml(File file);
}
