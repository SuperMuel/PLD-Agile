package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.Client;

import java.util.List;

public interface ClientRepository {
    Client create(String name, String surname, String phoneNumber);
    Client findById(long id);
    List<Client> findByName(String name, String lastName);
    List<Client> findAll();
    Client update(Client client);
    void delete(Client client);
}
