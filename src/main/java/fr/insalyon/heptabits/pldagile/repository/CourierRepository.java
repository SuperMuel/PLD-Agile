package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.Courier;

import java.util.List;

public interface CourierRepository {
    Courier create(String name);
    Courier findById(Long id);
    List<Courier> findAll();
    void update(Courier courier);
    void delete(Courier courier);
}
