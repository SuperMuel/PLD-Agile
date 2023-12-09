package fr.insalyon.heptabits.pldagile.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class TimeWindow {
    private final LocalTime start;
    private final LocalTime end;


    /**
     * Constructor
     *
     * @param startHour Start hour of the time window
     * @param endHour   End hour of the time window
     */
    public TimeWindow(int startHour, int endHour) {
        this(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0));
    }

    public TimeWindow(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;

        if (start.equals(end)) {
            throw new IllegalArgumentException("TimeWindow constructor: empty time window");
        }

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("TimeWindow constructor: negative duration");
        }
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    @Override
    public String toString() {
        // 9h à 10h
        return start.getHour() + "h à " + end.getHour() + "h";
    }


    // help functions

    public int compareStartTo(TimeWindow other) {
        return this.getStart().compareTo(other.getStart());
    }

    public int compareEndTo(TimeWindow other) {
        return this.getEnd().compareTo(other.getEnd());
    }


    /**
     * Check if a time is in the time window, or equals to the start or end of the time window
     *
     * @param time Time to check
     * @return true if the time is in the time window, false otherwise
     */
    public boolean contains(LocalTime time) {
        return time.equals(start) || time.equals(end) || (time.isAfter(start) && time.isBefore(end));
    }

    /**
     * Check if a time is in the time window, or equals to the start or end of the time window
     *
     * @param time Time to check
     * @return true if the time is in the time window, false otherwise
     */
    public boolean contains(LocalDateTime time) {
        return contains(time.toLocalTime());
    }

}
