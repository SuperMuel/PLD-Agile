package fr.insalyon.heptabits.pldagile.repository;


import fr.insalyon.heptabits.pldagile.model.Delivery;
import fr.insalyon.heptabits.pldagile.model.Intersection;
import fr.insalyon.heptabits.pldagile.model.TimeWindow;

import java.time.LocalDateTime;
import java.util.List;

public interface DeliveryRepository {
    Delivery findById(long id);

    List<Delivery> findAll();

    Delivery create(LocalDateTime scheduledDateTime, Intersection destination, long courierId, long clientId, TimeWindow timeWindow);

    Delivery update(Delivery delivery);

    void delete(Delivery delivery);
}
