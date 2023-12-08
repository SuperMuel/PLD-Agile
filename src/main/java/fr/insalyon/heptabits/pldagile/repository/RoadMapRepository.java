package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.Delivery;
import fr.insalyon.heptabits.pldagile.model.Leg;
import fr.insalyon.heptabits.pldagile.model.RoadMap;

import java.util.List;

public interface RoadMapRepository {
    List<RoadMap> getAll();

    RoadMap getById(long id);

    RoadMap create(List<Delivery> deliveries, List<Leg> legs, long idCourier);

    void update(RoadMap roadMap);

    RoadMap getByCourierID(long idCourier);

    void delete(long id); // TODO decide what happens to the deliveries

}
