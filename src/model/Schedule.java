package model;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/** When a course meets: one day and a time range, for example SUN 10:00-11:30. */
public class Schedule {

    private final String day;
    private final LocalTime start;
    private final LocalTime end;

    /**
     * @param day   day name, for example "SUN"
     * @param start start time in HH:MM format, for example "10:00"
     * @param end   end time in HH:MM format, for example "11:30"
     * @throws IllegalArgumentException if a time is not valid or end is not after start
     */
    public Schedule(String day, String start, String end) {
        if (day == null || day.trim().isEmpty()) {
            throw new IllegalArgumentException("Day is required.");
        }
        try {
            this.start = LocalTime.parse(start.trim());
            this.end = LocalTime.parse(end.trim());
        } catch (DateTimeParseException | NullPointerException e) {
            throw new IllegalArgumentException("Time must be in HH:MM format, for example 09:30.");
        }
        if (!this.end.isAfter(this.start)) {
            throw new IllegalArgumentException("End time must be after start time.");
        }
        this.day = day.trim().toUpperCase();
    }

    /** True if both are on the same day and each one starts before the other ends. */
    public boolean conflictsWith(Schedule other) {
        if (other == null || !day.equals(other.day)) {
            return false;
        }
        return start.isBefore(other.end) && other.start.isBefore(end);
    }

    public String getDay() { return day; }
    public LocalTime getStart() { return start; }
    public LocalTime getEnd() { return end; }

    @Override
    public String toString() {
        return day + " " + start + "-" + end;
    }
}
