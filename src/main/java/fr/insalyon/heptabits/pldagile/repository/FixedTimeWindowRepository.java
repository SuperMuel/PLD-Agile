package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.TimeWindow;

public class FixedTimeWindowRepository {
    public FixedTimeWindowRepository() {
    }

    public java.util.List<TimeWindow> getAll() {
        //create 4 1hour time windows from 8am to 12am using a loop
        java.util.List<TimeWindow> timeWindows = new java.util.ArrayList<>();
        for (int i = 0; i < 4; i++) {
            timeWindows.add(new TimeWindow(i, java.time.LocalTime.of(8 + i, 0), java.time.LocalTime.of(9 + i, 0)));
        }

        return timeWindows;
    }


}
