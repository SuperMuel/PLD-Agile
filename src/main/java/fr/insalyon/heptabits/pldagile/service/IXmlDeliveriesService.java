package fr.insalyon.heptabits.pldagile.service;

import org.xml.sax.SAXException;

import java.io.IOException;

public interface IXmlDeliveriesService {

    void exportDeliveriesToXml(String filePath);

    void importDeliveriesFromXml(String filePath) throws IOException, SAXException;
}
