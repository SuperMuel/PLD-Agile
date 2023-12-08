package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.Delivery;
import fr.insalyon.heptabits.pldagile.model.IdGenerator;
import fr.insalyon.heptabits.pldagile.model.Leg;
import fr.insalyon.heptabits.pldagile.model.RoadMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryRoadMapRepository implements RoadMapRepository {
    private final HashMap<Long, RoadMap> roadMaps;

    private final IdGenerator idGenerator;

    public InMemoryRoadMapRepository(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
        roadMaps = new HashMap<>();
    }

    @Override
    public List<RoadMap> getAll() {
        return List.copyOf(roadMaps.values());
    }

    @Override
    public RoadMap getById(long id) {
        return roadMaps.get(id);
    }

    @Override
    public RoadMap getByCourierID(long idCourier){
        for (Map.Entry<Long, RoadMap> mapEntry : roadMaps.entrySet()) {
            if(idCourier == mapEntry.getValue().getIdCourier()) {
                return mapEntry.getValue();
            }
        }
        return null;
    }

    @Override
    public RoadMap create(List<Delivery> deliveries, List<Leg> legs, long idCourier) {
        final long id = idGenerator.getNextId();
        final RoadMap roadMap = new RoadMap(id, deliveries, legs, idCourier);
        roadMaps.put(id, roadMap);
        return roadMap;
    }

    @Override
    public void update(RoadMap roadMap) {
        roadMaps.put(roadMap.getId(), roadMap);
    }

    @Override
    public void delete(long id) {
        roadMaps.remove(id);
    }
}
