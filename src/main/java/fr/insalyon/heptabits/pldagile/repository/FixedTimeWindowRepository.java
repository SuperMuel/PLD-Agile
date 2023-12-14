package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.TimeWindow;

/**
 * Repository for time windows.
 * <p>
 * These time windows are hard-coded.
 * <p>
 * Returns 1h time windows from 8am to 12am.
 */
public class FixedTimeWindowRepository implements TimeWindowRepository {
    public java.util.List<TimeWindow> getAll() {
        //create 4 1hour time windows from 8am to 12am using a loop
        java.util.List<TimeWindow> timeWindows = new java.util.ArrayList<>();
        for (int i = 0; i < 4; i++) {
            timeWindows.add(new TimeWindow(java.time.LocalTime.of(8 + i, 0), java.time.LocalTime.of(9 + i, 0)));
        }

        return timeWindows;
    }




}
