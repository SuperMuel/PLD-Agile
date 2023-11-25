package fr.insalyon.heptabits.pldagile.repository;


import fr.insalyon.heptabits.pldagile.model.Delivery;

import java.util.List;

public interface DeliveryRepository {
    Delivery findById(Long id);

    List<Delivery> findAll();

    void save(Delivery delivery);

    void delete(Delivery delivery);
}
