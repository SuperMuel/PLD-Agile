package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.IdGenerator;
import fr.insalyon.heptabits.pldagile.model.Delivery;
import fr.insalyon.heptabits.pldagile.model.Intersection;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryDeliveryRepository implements DeliveryRepository {

    final private HashMap<Long, Delivery> hashMapDeliveries;

    final private IdGenerator idGenerator;
    public InMemoryDeliveryRepository(IdGenerator idGenerator){
        hashMapDeliveries = new HashMap<>();
        this.idGenerator = idGenerator;
    }
    @Override
    public Delivery findById(long id) {
        return hashMapDeliveries.get(id);
    }

    @Override
    public List<Delivery> findAll() {
        List<Delivery> res = new ArrayList<>();
        for (Map.Entry<Long, Delivery> mapEntry : hashMapDeliveries.entrySet()) {
            res.add(mapEntry.getValue());
        }

        Comparator<Delivery> byScheduledDateTime = Comparator.comparing(Delivery::getScheduledDateTime);
        res.sort(byScheduledDateTime);

        return res;
    }

    @Override
    public Delivery create(LocalDateTime scheduledDateTime, Intersection destination, long courierId, long clientId) {
        Delivery delivery = new Delivery(idGenerator.getNextId(), scheduledDateTime, destination, courierId, clientId);
        hashMapDeliveries.put(delivery.getId(), delivery);
        return delivery;
    }

    @Override
    public Delivery update(Delivery delivery) {
        hashMapDeliveries.put(delivery.getId(), delivery);
        return hashMapDeliveries.get(delivery.getId());
    }

    @Override
    public void delete(Delivery delivery) {
        hashMapDeliveries.remove(delivery.getId());
    }



}
