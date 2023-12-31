package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.Delivery;
import fr.insalyon.heptabits.pldagile.model.RoadMap;
import fr.insalyon.heptabits.pldagile.repository.RoadMapRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Service that provide a view of deliveries.
 * <p>
 * It fetches the deliveries from the road map repository.
 * <p>
 * It can't create, update or delete deliveries. You should use the road map service for that.
 *
 */
public class DeliveryService {
    private final RoadMapRepository roadMapRepository;

    /**
     * Creates a new delivery service.
     *
     * @param roadMapRepository the road map repository to use
     */
    public DeliveryService(RoadMapRepository roadMapRepository) {
        this.roadMapRepository = roadMapRepository;
    }

    /**
     * Gets all deliveries on a given date.
     *
     * @param date the date to get the deliveries from
     * @return a list of deliveries on the given date
     */
    public List<Delivery> getDeliveriesOnDate(LocalDate date){
        List<Delivery> deliveries = new ArrayList<>();
        for(RoadMap roadMap : roadMapRepository.getByDate(date)){
            deliveries.addAll(roadMap.getDeliveries());
        }
        return Collections.unmodifiableList(deliveries);
    }

    /**
     * Gets all deliveries.
     *
     * @return a list of all deliveries
     */
    public List<Delivery> getAll(){
        List<Delivery> deliveries = new ArrayList<>();
        for(RoadMap roadMap : roadMapRepository.getAll()){
            deliveries.addAll(roadMap.getDeliveries());
        }
        return Collections.unmodifiableList(deliveries);
    }

}
