package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.Delivery;
import fr.insalyon.heptabits.pldagile.model.DeliveryRequest;
import fr.insalyon.heptabits.pldagile.model.Map;
import fr.insalyon.heptabits.pldagile.model.RoadMap;
import fr.insalyon.heptabits.pldagile.repository.DeliveryRepository;
import fr.insalyon.heptabits.pldagile.repository.InMemoryRoadMapRepository;
import fr.insalyon.heptabits.pldagile.repository.RoadMapRepository;

import javax.xml.parsers.DocumentBuilder;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RoadMapService implements IRoadMapService {

    private final RoadMapRepository roadMapRepository;

    private final RoadMapOptimizer roadMapOptimizer;

    private final MapService mapService;

    public RoadMapService(RoadMapRepository roadMapRepository, RoadMapOptimizer roadMapOptimizer, MapService mapService) {
        this.roadMapRepository = roadMapRepository;
        this.roadMapOptimizer = roadMapOptimizer;
        this.mapService = mapService;
    }

    @Override
    public void addRequest(DeliveryRequest newRequest) throws ImpossibleRoadMapException {
        LocalTime WAREHOUSE_DEPARTURE_TIME = LocalTime.of(7, 45);

        List<DeliveryRequest> requests = new ArrayList<>();

        RoadMap previousRoadMap = roadMapRepository.getByCourierAndDate(newRequest.getCourierId(), newRequest.getDate());
        if (previousRoadMap != null) {
            requests.addAll(previousRoadMap.getDeliveries().stream().map(DeliveryRequest::new).toList());
        }

        requests.add(newRequest);

        RoadMap newRoadMap = roadMapOptimizer.optimize(requests, mapService.getCurrentMap(), WAREHOUSE_DEPARTURE_TIME);

        if (previousRoadMap == null) {
            roadMapRepository.create(newRoadMap.getDeliveries(), newRoadMap.getLegs());
            System.out.println("Roadmap created");
        } else {
            roadMapRepository.update(newRoadMap);
            System.out.println("Roadmap updated");
        }
    }


}
