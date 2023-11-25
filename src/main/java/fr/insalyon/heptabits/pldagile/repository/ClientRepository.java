package fr.insalyon.heptabits.pldagile.repository;


import fr.insalyon.heptabits.pldagile.model.Client;

import java.util.List;

public interface ClientRepository {
    Client create(String name, String phoneNumber);
    Client findById(Long id);
    List<Client> findAll();
    void update(Client client);
    void delete(Client client);
}
