package repository;

import exception.RegistrationException;
import model.Course;
import model.Schedule;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Stores courses in memory, keyed by course code. */
public class CourseRepository {

    // LinkedHashMap is a HashMap that keeps insertion order, so lists print in a stable order
    private final Map<String, Course> courses = new LinkedHashMap<>();

    /** Adds a new course. Duplicate codes are rejected (FR2: unique code). */
    public void add(Course course) throws RegistrationException {
        if (course == null || isBlank(course.getCode()) || isBlank(course.getTitle())) {
            throw new RegistrationException("Code and title are required.");
        }
        if (course.getCredits() <= 0 || course.getCapacity() <= 0) {
            throw new RegistrationException("Credits and capacity must be greater than 0.");
        }
        if (course.getSchedule() == null) {
            throw new RegistrationException("Schedule is required.");
        }
        String key = key(course.getCode());
        if (courses.containsKey(key)) {
            throw new RegistrationException("A course with code " + course.getCode() + " already exists.");
        }
        courses.put(key, course);
    }

    /** Returns the course, or null if the code does not exist. */
    public Course findByCode(String code) {
        return code == null ? null : courses.get(key(code));
    }

    public List<Course> findAll() {
        return new ArrayList<>(courses.values());
    }

    /** FR8: edits title, credits, capacity and schedule of an existing course. */
    public void update(String code, String title, int credits, int capacity, Schedule schedule)
            throws RegistrationException {
        Course course = findByCode(code);
        if (course == null) {
            throw new RegistrationException("Course " + code + " was not found.");
        }
        course.update(title, credits, capacity, schedule);
    }

    /**
     * Removes the course. Returns false if the code does not exist.
     * The "no students registered" rule (FR9) is checked by the service, not here.
     */
    public boolean delete(String code) {
        return code != null && courses.remove(key(code)) != null;
    }

    /** FR7: courses whose code or title contains the keyword, ignoring case. */
    public List<Course> search(String keyword) {
        List<Course> result = new ArrayList<>();
        if (isBlank(keyword)) {
            return result;
        }
        String k = keyword.trim().toLowerCase();
        for (Course c : courses.values()) {
            if (c.getCode().toLowerCase().contains(k) || c.getTitle().toLowerCase().contains(k)) {
                result.add(c);
            }
        }
        return result;
    }

    /** FR3: only courses that still have free seats. */
    public List<Course> findAvailable() {
        List<Course> result = new ArrayList<>();
        for (Course c : courses.values()) {
            if (c.hasFreeSeat()) {
                result.add(c);
            }
        }
        return result;
    }

    private static String key(String code) {
        return code.trim().toUpperCase();
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
