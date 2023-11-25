package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.Map;

import java.nio.file.Path;

public class XmlMapService implements  MapService {

    Map currentMap;

    void loadMap(Path xmlPath) {
        if (xmlPath == null) {
            throw new IllegalArgumentException("xmlPath cannot be null");
        }

        // TODO
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
