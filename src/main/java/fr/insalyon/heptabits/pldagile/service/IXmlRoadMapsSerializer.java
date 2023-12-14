package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.Intersection;
import fr.insalyon.heptabits.pldagile.model.RoadMap;
import fr.insalyon.heptabits.pldagile.repository.ClientRepository;
import fr.insalyon.heptabits.pldagile.repository.CourierRepository;

import java.util.List;

public interface IXmlRoadMapsSerializer {
    public String serialize(List<RoadMap> roadMaps);

    public List<RoadMap> deserialize(String xml, java.util.Map<Long, Intersection> existingIntersections) ;
}
