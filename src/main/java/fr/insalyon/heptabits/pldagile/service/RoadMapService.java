package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.Delivery;
import fr.insalyon.heptabits.pldagile.model.DeliveryRequest;
import fr.insalyon.heptabits.pldagile.model.Map;
import fr.insalyon.heptabits.pldagile.model.RoadMap;
import fr.insalyon.heptabits.pldagile.repository.DeliveryRepository;
import fr.insalyon.heptabits.pldagile.repository.InMemoryRoadMapRepository;
import fr.insalyon.heptabits.pldagile.repository.RoadMapRepository;

import javax.xml.parsers.DocumentBuilder;

public class RoadMapService implements IRoadMapService {

    private RoadMapRepository roadMapRepository;

    private DeliveryRepository deliveryRepository;

    public RoadMapService(RoadMapRepository roadMapRepository, DeliveryRepository deliveryRepository) {
        this.roadMapRepository = roadMapRepository;
        this.deliveryRepository = deliveryRepository;
    }
    @Override
    public Delivery addRequest(DeliveryRequest request, long idCourier) throws ImpossibleRoadMapException {
        // appeler optimizer

        RoadMap roadMap = roadMapRepository.getByCourierID(idCourier);
        // if true - on crée la livraison + update road map
        Delivery delivery = deliveryRepository.create(request.getDate().atTime(request.getTimeWindow().getStart()), request.getDestination(), idCourier, request.getClientId(), request.getTimeWindow());
        roadMap.addDelivery(delivery);
        roadMapRepository.update(roadMap);

        System.out.println(roadMap);

        // if false - exception

    return null;
    }

    public RoadMapRepository getRoadMapRepository() {
        return roadMapRepository;
    }
}
