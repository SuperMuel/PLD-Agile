package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.Delivery;
import fr.insalyon.heptabits.pldagile.model.IdGenerator;
import fr.insalyon.heptabits.pldagile.model.Leg;
import fr.insalyon.heptabits.pldagile.model.RoadMap;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

/**
 * Repository for road maps to create, find, update and delete road maps.
 *
 * This implementation stores road maps in memory, so this it not persistent.
 */
public class InMemoryRoadMapRepository implements RoadMapRepository {
    private final HashMap<Long, RoadMap> roadMaps;

    private final IdGenerator idGenerator;

    /**
     * Creates a new in memory road map repository.
     *
     * @param idGenerator the id generator to use
     */
    public InMemoryRoadMapRepository(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
        roadMaps = new HashMap<>();
    }


    /**
     * Finds all road maps.
     *
     * @return a list of all road maps
     */
    @Override
    public List<RoadMap> getAll() {
        return List.copyOf(roadMaps.values());
    }

    /**
     * Finds a road map by its id.
     *
     * @param id the id of the road map
     * @return the road map if found, null otherwise
     */
    @Override
    public RoadMap getById(long id) {
        return roadMaps.get(id);
    }

    /**
     * Creates a road map.
     *
     * @param deliveries the deliveries of the road map
     * @param legs the legs of the road map
     * @return the created road map
     */
    @Override
    public RoadMap create(List<Delivery> deliveries, List<Leg> legs) {
        if(deliveries == null || legs == null) {
            throw new NullPointerException("RoadMapRepository.create: deliveries and legs must not be null");
        }
        final long id = idGenerator.getNextId();
        final RoadMap roadMap = new RoadMap(id, deliveries, legs);
        roadMaps.put(id, roadMap);
        return roadMap;
    }

    /**
     * Updates a road map.
     *
     * @param id the id of the road map to update
     * @param deliveries the deliveries of the road map
     * @param legs the legs of the road map
     */
    @Override
    public void updateById(long id, List<Delivery> deliveries, List<Leg> legs) {
        if(deliveries == null || legs == null) {
            throw new NullPointerException("RoadMapRepository.updateById: deliveries and legs must not be null");
        }

        if (!roadMaps.containsKey(id)) {
            throw new IllegalArgumentException("RoadMapRepository.updateById: no road map with id " + id);
        }
        roadMaps.put(id, new RoadMap(id, deliveries, legs));
    }


    /**
     * Finds a road map by its courier and date.
     *
     * @param idCourier the id of the courier
     * @param date the date of the road map
     * @return the road map if found, null otherwise
     */
    @Override
    public RoadMap getByCourierAndDate(long idCourier, LocalDate date) {
        if(date == null) {
            throw new NullPointerException("RoadMapRepository.getByCourierAndDate: date must not be null");
        }
        return roadMaps.values().stream()
                .filter(roadMap -> roadMap.getCourierId() == idCourier && roadMap.getDate().equals(date))
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds all road maps by date.
     *
     * @param date the date of the road maps
     * @return a list of road maps with the given date
     */
    @Override
    public List<RoadMap> getByDate(LocalDate date) {
        if(date == null) {
            throw new NullPointerException("RoadMapRepository.getByDate: date must not be null");
        }
        return roadMaps.values().stream()
                .filter(roadMap -> roadMap.getDate().equals(date))
                .toList();
    }

    /**
     * Deletes a road map.
     *
     * @param id the id of the road map to delete
     */
    @Override
    public void delete(long id) {
        roadMaps.remove(id);
    }
}
