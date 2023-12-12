package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.Delivery;
import fr.insalyon.heptabits.pldagile.model.Leg;
import fr.insalyon.heptabits.pldagile.model.RoadMap;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for road maps.
 *
 * This interface is used to abstract the storage of road maps.
 * It is used to create, find, update and delete road maps.
 */
public interface RoadMapRepository {

/**
     * Finds all road maps.
     *
     * @return a list of all road maps
     */
    List<RoadMap> getAll();


    /**
     * Finds a road map by its id.
     *
     * @param id the id of the road map
     * @return the road map if found, null otherwise
     */
    RoadMap getById(long id);

    /**
     * Creates a road map.
     *
     * @param deliveries the deliveries of the road map
     * @param legs the legs of the road map
     * @return the created road map
     */
    RoadMap create(List<Delivery> deliveries, List<Leg> legs);

    /**
     * Updates a road map.
     *
     * @param id the id of the road map
     * @param deliveries the deliveries of the road map
     * @param legs the legs of the road map
     */
    void updateById(long id, List<Delivery> deliveries, List<Leg> legs);

    /**
     * Finds a road map by its courier and date.
     *
     * @param idCourier the id of the courier
     * @param date the date of the road map
     * @return the road map if found, null otherwise
     */
    RoadMap getByCourierAndDate(long idCourier, LocalDate date);

    /**
     * Finds all road maps by date.
     *
     * @param date the date of the road maps
     * @return a list of all road maps for the given date
     */
    List<RoadMap> getByDate(LocalDate date);

    /**
     * Deletes a road map.
     *
     * @param id the id of the road map to delete
     */
    void delete(long id);

}
