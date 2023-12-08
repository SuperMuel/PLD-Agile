package fr.insalyon.heptabits.pldagile.model;

import java.time.LocalTime;

public class TimeWindow {
    private final LocalTime start;
    private final LocalTime end;

    public TimeWindow(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
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

    public boolean isBefore(TimeWindow other) {
        return this.getEnd().isBefore(other.getStart());
    }

    public boolean isAfter(TimeWindow other) {
        return this.getStart().isAfter(other.getEnd());
    }

    public boolean isDuring(TimeWindow other) {
        return !this.isBefore(other) && !this.isAfter(other);
    }

    public boolean isDuring(LocalTime time) {
        return !time.isBefore(this.getStart()) && !time.isAfter(this.getEnd());
    }

    public int compareStartTo(TimeWindow other) {
        return this.getStart().compareTo(other.getStart());
    }

    public int compareEndTo(TimeWindow other) {
        return this.getEnd().compareTo(other.getEnd());
    }


}
