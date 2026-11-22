package service;

import exception.RegistrationException;
import model.Course;
import model.Registration;
import model.Student;
import repository.CourseRepository;
import repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * All registration business rules live here (seats, duplicates, credit limit,
 * prerequisites, time conflicts). Menus only read input and print messages.
 */
public class RegistrationService {

    /** Maximum credit hours per student (value agreed in docs/api-contract.md). */
    public static final int MAX_CREDITS = 18;

    private final StudentRepository students;
    private final CourseRepository courses;
    private final List<Registration> registrations = new ArrayList<>();

    public RegistrationService(StudentRepository students, CourseRepository courses) {
        this.students = students;
        this.courses = courses;
    }

    /**
     * Throws RegistrationException with a clear message at the first failing check,
     * in this order: seats, already registered, credit limit, prerequisites, time conflict.
     */
    public void checkEligibility(Student student, Course course) throws RegistrationException {
        if (student == null || course == null) {
            throw new RegistrationException("Student and course are required.");
        }
        // a. seats
        if (!course.hasFreeSeat()) {
            throw new RegistrationException("Course is full: " + course.getCode() + ".");
        }
        // b. already registered
        if (isRegistered(student, course)) {
            throw new RegistrationException("Already registered in " + course.getCode() + ".");
        }
        // c. credit limit
        int total = totalCredits(student);
        if (total + course.getCredits() > MAX_CREDITS) {
            throw new RegistrationException("Credit limit exceeded: " + total + " + "
                    + course.getCredits() + " is more than the maximum of " + MAX_CREDITS + " credits.");
        }
        // d. prerequisites (the student must have completed them)
        for (String prerequisite : course.getPrerequisites()) {
            if (!student.hasCompleted(prerequisite)) {
                throw new RegistrationException("Missing prerequisite " + prerequisite
                        + " for " + course.getCode() + ".");
            }
        }
        // e. time conflict with a course the student is already registered in
        for (Course other : getCoursesOf(student)) {
            if (course.getSchedule().conflictsWith(other.getSchedule())) {
                throw new RegistrationException("Time conflict: " + course.getCode() + " ("
                        + course.getSchedule() + ") overlaps with " + other.getCode()
                        + " (" + other.getSchedule() + ").");
            }
        }
    }

    /** Registers the student. If a check fails nothing is saved and no seat is taken. */
    public void register(Student student, Course course) throws RegistrationException {
        checkEligibility(student, course);
        registrations.add(new Registration(student, course));
        course.increaseEnrolled();
    }

    /** Removes the registration and frees the seat. */
    public void drop(Student student, Course course) throws RegistrationException {
        Registration found = findRegistration(student, course);
        if (found == null) {
            throw new RegistrationException("The student is not registered in "
                    + (course == null ? "this course" : course.getCode()) + ".");
        }
        registrations.remove(found);
        course.decreaseEnrolled();
    }

    /** Courses the student is registered in. */
    public List<Course> getCoursesOf(Student student) {
        List<Course> result = new ArrayList<>();
        for (Registration r : registrations) {
            if (sameStudent(r.getStudent(), student)) {
                result.add(r.getCourse());
            }
        }
        return result;
    }

    /** Sum of credits of the courses the student is registered in. */
    public int totalCredits(Student student) {
        int total = 0;
        for (Course c : getCoursesOf(student)) {
            total += c.getCredits();
        }
        return total;
    }

    /** Students registered in the course. */
    public List<Student> getRoster(Course course) {
        List<Student> result = new ArrayList<>();
        for (Registration r : registrations) {
            if (sameCourse(r.getCourse(), course)) {
                result.add(r.getStudent());
            }
        }
        return result;
    }

    /** Used by the delete rule: a course with registrations cannot be deleted. */
    public boolean hasRegistrations(Course course) {
        for (Registration r : registrations) {
            if (sameCourse(r.getCourse(), course)) {
                return true;
            }
        }
        return false;
    }

    public boolean isRegistered(Student student, Course course) {
        return findRegistration(student, course) != null;
    }

    private Registration findRegistration(Student student, Course course) {
        for (Registration r : registrations) {
            if (sameStudent(r.getStudent(), student) && sameCourse(r.getCourse(), course)) {
                return r;
            }
        }
        return null;
    }

    private static boolean sameStudent(Student a, Student b) {
        return a != null && b != null && a.getId().equalsIgnoreCase(b.getId());
    }

    private static boolean sameCourse(Course a, Course b) {
        return a != null && b != null && a.getCode().equalsIgnoreCase(b.getCode());
    }
}
