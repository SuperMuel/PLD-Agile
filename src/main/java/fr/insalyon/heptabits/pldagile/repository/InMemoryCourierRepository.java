package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.IdGenerator;
import fr.insalyon.heptabits.pldagile.model.Courier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryCourierRepository implements CourierRepository {

    private final HashMap<Long, Courier> couriers;
    private final IdGenerator idGenerator;
    public InMemoryCourierRepository(IdGenerator idGenerator) {
        this.couriers = new HashMap<>();
        this.idGenerator = idGenerator;
    }

    @Override
    public Courier create(String firstName, String lastName, String email, String phoneNumber) {
        Courier courier = new Courier(idGenerator.getNextId(), firstName, lastName, email, phoneNumber);
        couriers.put(courier.getId(), courier);
        return courier;
    }

    @Override
    public Courier findById(Long id) {
        return  couriers.get(id);
    }

    @Override
    public List<Courier> findAll() {
        return new ArrayList<>(this.couriers.values());
    }

    @Override
    public Courier update(Courier courier) {
        couriers.put(courier.getId(), courier);
        return courier;
    }

    @Override
    public void deleteById(long id) {
        couriers.remove(id);
    }
}
