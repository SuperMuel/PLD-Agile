package fr.insalyon.heptabits.pldagile.model;

import java.time.LocalTime;

public class TimeWindow extends BaseEntity{
    private final LocalTime start;
    private final LocalTime end;

    public TimeWindow(long id, LocalTime start, LocalTime end) {
        super(id);
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
        return "TimeWindow{" +
                "id=" + getId() +
                ", start=" + start +
                ", end=" + end +
                '}';
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

}
