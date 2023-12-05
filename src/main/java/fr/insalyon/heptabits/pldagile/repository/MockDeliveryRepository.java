package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.Delivery;
import fr.insalyon.heptabits.pldagile.model.Intersection;
import fr.insalyon.heptabits.pldagile.model.TimeWindow;

import java.time.LocalDateTime;
import java.util.List;

public class MockDeliveryRepository implements DeliveryRepository {
    final private List<Delivery> deliveries;


    public MockDeliveryRepository(){
        TimeWindow timeWindow = new TimeWindow(LocalDateTime.now().toLocalTime(), LocalDateTime.now().plusHours(1).toLocalTime());
        this.deliveries = List.of(
                new Delivery(1L, LocalDateTime.of(2021, 1, 1, 12, 0), new Intersection(1L, 1, 1), 1L, 1, timeWindow),
                new Delivery(2L, LocalDateTime.of(2021, 1, 1, 12, 1), new Intersection(1L, 1, 1), 1L, 1, timeWindow),
                new Delivery(3L, LocalDateTime.of(2021, 1, 1, 12, 2), new Intersection(1L, 1, 1), 1L, 1, timeWindow),
                new Delivery(4L, LocalDateTime.of(2021, 1, 1, 12, 3), new Intersection(1L, 1, 1), 1L, 1, timeWindow),
                new Delivery(5L, LocalDateTime.of(2021, 1, 1, 12, 4), new Intersection(1L, 1, 1), 1L, 1, timeWindow),
                new Delivery(6L, LocalDateTime.of(2021, 1, 1, 12, 5), new Intersection(1L, 1, 1), 1L, 1, timeWindow)
        );
    }


    @Override
    public Delivery findById(long id) {
        return deliveries.stream()
                .filter(delivery -> delivery.getId() == id)
                .findFirst().orElse(null);
    }

    @Override
    public List<Delivery> findAll() {
        return deliveries;
    }

    @Override
    public Delivery create(LocalDateTime scheduledDateTime, Intersection destination, long courierId, long clientId, TimeWindow timeWindow) {
        // thrown "unimplemented" error
        throw new UnsupportedOperationException("This is a mock repository. Only get and find methods are implemented.");
    }

    @Override
    public Delivery update(Delivery delivery) {
        // thrown "unimplemented" error
        throw new UnsupportedOperationException("This is a mock repository. Only get and find methods are implemented.");
    }

    @Override
    public void delete(Delivery delivery) {
        // thrown "unimplemented" error
        throw new UnsupportedOperationException("This is a mock repository. Only get and find methods are implemented.");
    }
}
