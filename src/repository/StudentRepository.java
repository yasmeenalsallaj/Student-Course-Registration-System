package repository;

import exception.RegistrationException;
import model.Student;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Stores students in memory, keyed by student ID. */
public class StudentRepository {

    // LinkedHashMap is a HashMap that keeps insertion order, so lists print in a stable order
    private final Map<String, Student> students = new LinkedHashMap<>();

    /** Adds a new student. Duplicate IDs are rejected (FR1). */
    public void add(Student student) throws RegistrationException {
        if (student == null || isBlank(student.getId()) || isBlank(student.getName())
                || isBlank(student.getEmail())) {
            throw new RegistrationException("ID, name and email are required.");
        }
        if (!student.getEmail().contains("@")) {
            throw new RegistrationException("Email is not valid.");
        }
        String key = key(student.getId());
        if (students.containsKey(key)) {
            throw new RegistrationException("A student with ID " + student.getId() + " already exists.");
        }
        students.put(key, student);
    }

    /** Returns the student, or null if the ID does not exist. */
    public Student findById(String id) {
        return id == null ? null : students.get(key(id));
    }

    public List<Student> findAll() {
        return new ArrayList<>(students.values());
    }

    private static String key(String id) {
        return id.trim().toUpperCase();
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
