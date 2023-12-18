package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.Intersection;
import fr.insalyon.heptabits.pldagile.model.RoadMap;

import java.util.List;

/**
 * This class is used to serialize and deserialize road maps into xml strings
 */
public interface IXmlRoadMapsSerializer {
    /**
     * Serializes a list of road maps into an xml string
     *
     * @param roadMaps the road maps to serialize
     * @return the xml representation of the road maps
     */
    public String serialize(List<RoadMap> roadMaps);

    /**
     * Deserializes a list of road maps from an xml string
     *
     * @param xml the xml representation of the road maps
     * @param existingIntersections the intersections that already exist in the database
     * @return the deserialized road maps
     */
    public List<RoadMap> deserialize(String xml, java.util.Map<Long, Intersection> existingIntersections) ;
}
