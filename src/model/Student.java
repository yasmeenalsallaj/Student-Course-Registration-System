package model;

import java.util.HashSet;
import java.util.Set;

/** A student. Completed course codes are used to check prerequisites. */
public class Student {

    private final String id;
    private final String name;
    private final String email;
    private final Set<String> completedCourseCodes = new HashSet<>();

    public Student(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /** Marks a course as passed in an earlier semester. */
    public void addCompletedCourse(String courseCode) {
        completedCourseCodes.add(courseCode.trim().toUpperCase());
    }

    /** True if the student already passed the course with this code. */
    public boolean hasCompleted(String courseCode) {
        return courseCode != null && completedCourseCodes.contains(courseCode.trim().toUpperCase());
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Set<String> getCompletedCourseCodes() { return completedCourseCodes; }

    @Override
    public String toString() {
        return String.format("%-10s %-20s %s", id, name, email);
    }
}
