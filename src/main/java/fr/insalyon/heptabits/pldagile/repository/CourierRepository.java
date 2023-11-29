package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.Courier;

import java.util.List;

public interface CourierRepository {
    Courier create(String firstName, String lastName, String email, String phoneNumber);
    Courier findById(Long id);
    List<Courier> findAll();
    Courier update(Courier courier);
    void deleteById(long id);
}
