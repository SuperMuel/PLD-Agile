package fr.insalyon.heptabits.pldagile.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Represents a time window.
 * <p>
 * Stores the start and end time.
 * <p>
 * All fields are final.
 */
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

    /**
     * Constructor
     *
     * @param start Start time of the time window
     * @param end   End time of the time window
     */
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


    /**
     * @return the start time of the time window
     */
    public LocalTime getStart() {
        return start;
    }

    /**
     * @return the end time of the time window
     */
    public LocalTime getEnd() {
        return end;
    }

    @Override
    public String toString() {
        // 9h à 10h
        return start.getHour() + "h à " + end.getHour() + "h";
    }


    /**
     * Compare the start time of two time windows
     *
     * @param other Time window to compare to
     * @return 0 if the start time of the two time windows are equal, a negative number if the start time of this time window is before the start time of the other time window, a positive number otherwise
     */
    public int compareStartTo(TimeWindow other) {
        return this.getStart().compareTo(other.getStart());
    }

    /**
     * Compare the end time of two time windows
     *
     * @param other Time window to compare to
     * @return 0 if the end time of the two time windows are equal, a negative number if the end time of this time window is before the end time of the other time window, a positive number otherwise
     */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeWindow that = (TimeWindow) o;
        return Objects.equals(start, that.start) && Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
