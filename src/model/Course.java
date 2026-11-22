package model;

import exception.RegistrationException;

import java.util.ArrayList;
import java.util.List;

/** A course with a limited number of seats. */
public class Course {

    private final String code;
    private String title;
    private int credits;
    private int capacity;
    private int enrolledCount;
    private Schedule schedule;
    private final List<String> prerequisites = new ArrayList<>();

    public Course(String code, String title, int credits, int capacity, Schedule schedule) {
        this.code = code.trim().toUpperCase();
        this.title = title;
        this.credits = credits;
        this.capacity = capacity;
        this.schedule = schedule;
    }

    public int availableSeats() {
        return capacity - enrolledCount;
    }

    public boolean hasFreeSeat() {
        return availableSeats() > 0;
    }

    /** Takes one seat. The service checks hasFreeSeat() before calling this. */
    public void increaseEnrolled() {
        if (!hasFreeSeat()) {
            throw new IllegalStateException("Course " + code + " has no free seats.");
        }
        enrolledCount++;
    }

    /** Frees one seat. Never goes below zero. */
    public void decreaseEnrolled() {
        if (enrolledCount > 0) {
            enrolledCount--;
        }
    }

    /**
     * Edits title, credits, capacity and schedule.
     * Extra rule (not in the SRS): capacity cannot be less than the students already enrolled.
     */
    public void update(String title, int credits, int capacity, Schedule schedule) throws RegistrationException {
        if (title == null || title.trim().isEmpty()) {
            throw new RegistrationException("Title is required.");
        }
        if (credits <= 0) {
            throw new RegistrationException("Credits must be greater than 0.");
        }
        if (capacity <= 0) {
            throw new RegistrationException("Capacity must be greater than 0.");
        }
        if (capacity < enrolledCount) {
            throw new RegistrationException("Capacity cannot be less than the "
                    + enrolledCount + " students already enrolled.");
        }
        if (schedule == null) {
            throw new RegistrationException("Schedule is required.");
        }
        this.title = title.trim();
        this.credits = credits;
        this.capacity = capacity;
        this.schedule = schedule;
    }

    public void addPrerequisite(String courseCode) {
        prerequisites.add(courseCode.trim().toUpperCase());
    }

    public String getCode() { return code; }
    public String getTitle() { return title; }
    public int getCredits() { return credits; }
    public int getCapacity() { return capacity; }
    public int getEnrolledCount() { return enrolledCount; }
    public Schedule getSchedule() { return schedule; }
    public List<String> getPrerequisites() { return prerequisites; }

    @Override
    public String toString() {
        return String.format("%-8s %-26s %d cr  %2d/%-2d seats  %s",
                code, title, credits, enrolledCount, capacity, schedule);
    }
}
