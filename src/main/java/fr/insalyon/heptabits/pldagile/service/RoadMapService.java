package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.DeliveryRequest;
import fr.insalyon.heptabits.pldagile.model.RoadMap;
import fr.insalyon.heptabits.pldagile.repository.RoadMapRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RoadMapService implements IRoadMapService {

    private final LocalTime warehouseDepartureTime = LocalTime.of(7, 45);

    private final RoadMapRepository roadMapRepository;

    private final RoadMapOptimizer roadMapOptimizer;

    private final MapService mapService;

    public RoadMapService(RoadMapRepository roadMapRepository, RoadMapOptimizer roadMapOptimizer, MapService mapService) {
        this.roadMapRepository = roadMapRepository;
        this.roadMapOptimizer = roadMapOptimizer;
        this.mapService = mapService;
    }

    public List<DeliveryRequest> getDeliveryRequestsForCourierOnDate(long courierId, LocalDate date) {
        RoadMap roadMap = roadMapRepository.getByCourierAndDate(courierId, date);

        if (roadMap == null) {
            return new ArrayList<>();
        }

        return roadMap.getDeliveries().stream().map(DeliveryRequest::new).toList();
    }

    public boolean aRoadMapAlreadyExists(long courierId, LocalDate date) {
        return roadMapRepository.getByCourierAndDate(courierId, date) != null;
    }

    @Override
    public void addRequest(DeliveryRequest newRequest) throws ImpossibleRoadMapException {
        LocalDate date = newRequest.getDate();
        long courierId = newRequest.getCourierId();

        if (newRequest.getDestination().equals(mapService.getCurrentMap().getWarehouse())) {
            // TODO : Make the warehouse not clickable
            throw new ImpossibleRoadMapException("A request cannot be made at the warehouse");
        }

        final RoadMap existingRoadMap = roadMapRepository.getByCourierAndDate(courierId, date);

        List<DeliveryRequest> requests = new ArrayList<>(getDeliveryRequestsForCourierOnDate(courierId, date));

        // Check that the new request isn't at the same place and at timeWindow as an existing request
        for (DeliveryRequest request : requests) {
            if (request.getDestination().equals(newRequest.getDestination()) && request.getTimeWindow().equals(newRequest.getTimeWindow())) {
                // This is not the ideal behavior. We would like to be able to have multiple packages delivered at the same place
                // during the same time window. However, the current implementation of the optimizer doesn't allow it.
                throw new ImpossibleRoadMapException("A request already exists at this place and time");
                // TODO : Implement a new Exception type for this case and add a custom message on the UI.
            }
        }

        requests.add(newRequest);

        RoadMap newRoadMap = roadMapOptimizer.optimize(requests, mapService.getCurrentMap(), warehouseDepartureTime);

        if (aRoadMapAlreadyExists(courierId, date)) {
            roadMapRepository.updateById(existingRoadMap.getId(), newRoadMap.getDeliveries(), newRoadMap.getLegs());
        } else {
            roadMapRepository.create(newRoadMap.getDeliveries(), newRoadMap.getLegs());
        }

    }

    public LocalTime getWarehouseDepartureTime() {
        return warehouseDepartureTime;
    }
}
