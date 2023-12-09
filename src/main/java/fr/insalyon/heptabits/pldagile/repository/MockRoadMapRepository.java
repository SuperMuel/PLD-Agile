package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.*;
import fr.insalyon.heptabits.pldagile.service.MockMapService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class MockRoadMapRepository extends InMemoryRoadMapRepository {

    public MockRoadMapRepository(IdGenerator idGenerator, MockMapService mapService) {
        super(idGenerator);

        LocalDate date = LocalDate.now();

        long courier1Id = 1;
        TimeWindow timeWindow1 = new TimeWindow(LocalTime.of(8, 0), LocalTime.of(9, 0));

        long client1Id = 1;
        long client2Id = 2;

        Map map = mapService.getCurrentMap();

        Intersection i1 = map. getIntersections().get(1L);
        Intersection i2 = map. getIntersections().get(2L);
        Intersection i3 = map. getIntersections().get(3L);
        Intersection i4 = map. getIntersections().get(4L);
        Intersection i5 = map. getIntersections().get(5L);


        Segment s12 = new Segment( i1, i2, "Street 12", 5.385164807134504);
        Segment s23 = new Segment( i2, i3, "Street 23", 4.123105625617661);
        Segment s24 = new Segment( i2, i4, "Street 24", 3.0);
        Segment s25 = new Segment( i2, i5, "Street 25", 3.0);
        Segment s31 = new Segment( i3, i1, "Street 31", 7.211102550927978);
        Segment s35 = new Segment( i3, i5, "Street 35", 5.656854249492381);

        Leg leg1 = new Leg(List.of(i3, i5),List.of(s35), LocalTime.of(7,45));
        Delivery delivery1 = new Delivery( date.atTime(LocalTime.of(8,0)), i5, courier1Id, client1Id, timeWindow1 );
        Leg leg2 = new Leg(List.of(i5, i2, i1), List.of(s25, s12), LocalTime.of(8,2, 30));
        Delivery delivery2 = new Delivery( date.atTime(LocalTime.of(8,15)), i1, courier1Id, client2Id, timeWindow1 );

        Leg leg3 = new Leg(List.of(i1, i3), List.of(s31), LocalTime.of(8,17, 30));

        List<Delivery> deliveries = List.of(delivery1, delivery2);
        List<Leg> legs = List.of(leg1, leg2, leg3);

        create(deliveries, legs);
    }


}
