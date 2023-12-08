package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.Delivery;
import fr.insalyon.heptabits.pldagile.model.IdGenerator;
import fr.insalyon.heptabits.pldagile.model.Leg;
import fr.insalyon.heptabits.pldagile.model.RoadMap;

import java.util.HashMap;
import java.util.List;

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
    public RoadMap create(List<Delivery> deliveries, List<Leg> legs) {
        final long id = idGenerator.getNextId();
        final RoadMap roadMap = new RoadMap(id, deliveries, legs);
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
