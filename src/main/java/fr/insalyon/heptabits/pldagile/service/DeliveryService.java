package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.Delivery;
import fr.insalyon.heptabits.pldagile.model.RoadMap;
import fr.insalyon.heptabits.pldagile.repository.RoadMapRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeliveryService {
    private final RoadMapRepository roadMapRepository;

    public DeliveryService(RoadMapRepository roadMapRepository) {
        this.roadMapRepository = roadMapRepository;
    }

    public List<Delivery> getDeliveriesOnDate(LocalDate date){
        List<Delivery> deliveries = new ArrayList<>();
        for(RoadMap roadMap : roadMapRepository.getByDate(date)){
            deliveries.addAll(roadMap.getDeliveries());
        }
        return Collections.unmodifiableList(deliveries);
    }

    public List<Delivery> getAll(){
        List<Delivery> deliveries = new ArrayList<>();
        for(RoadMap roadMap : roadMapRepository.getAll()){
            deliveries.addAll(roadMap.getDeliveries());
        }
        return Collections.unmodifiableList(deliveries);
    }

}
