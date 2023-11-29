package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.Courier;

import java.util.List;

public class MockCourierRepository implements CourierRepository {

    private final List<Courier> couriers;

    public MockCourierRepository() {
        this.couriers = List.of(
                new Courier(1L, "John", "Doe", "johndoe.email.com", "0123456789"),
                new Courier(2L, "Jane", "Doe", "janedoe.email.com", "0123456789"),
                new Courier(3L, "Jack", "Doe", "jackdoe.email.com", "0123456789"),
                new Courier(4L, "Jill", "Doe", "jilldoe.email.com", "0123456789"),
                new Courier(5L, "James", "Doe", "jamesdoe.email.com", "0123456789"),
                new Courier(6L, "Judy", "Doe", "judydoe.email.com", "0123456789")
        );
    }


    @Override
    public Courier create(String firstName, String lastName, String email, String phoneNumber) {
        // thrown "unimplemented" error
        throw new UnsupportedOperationException("This is a mock repository. Only get and find methods are implemented.");

    }

    @Override
    public Courier findById(Long id) {
        return couriers.stream()
                .filter(courier -> courier.getId() == id)
                .findFirst()
                .orElseThrow();

    }

    @Override
    public List<Courier> findAll() {
        return couriers;
    }

    @Override
    public Courier update(Courier courier) {
        // thrown "unimplemented" error
        throw new UnsupportedOperationException("This is a mock repository. Only get and find methods are implemented.");
    }

    @Override
    public void deleteById(long id) {
        // thrown "unimplemented" error
        throw new UnsupportedOperationException("This is a mock repository. Only get and find methods are implemented.");
    }
}
