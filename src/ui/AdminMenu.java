package ui;

import exception.RegistrationException;
import model.Course;
import model.Schedule;
import model.Student;
import repository.CourseRepository;
import repository.StudentRepository;
import service.RegistrationService;

import java.util.List;

/** Admin console: manage students and courses, view rosters. Reads input and prints only. */
public class AdminMenu {

    private final StudentRepository students;
    private final CourseRepository courses;
    private final RegistrationService service;

    public AdminMenu(StudentRepository students, CourseRepository courses, RegistrationService service) {
        this.students = students;
        this.courses = courses;
        this.service = service;
    }

    public void run() {
        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. Add student");
            System.out.println("2. Add course");
            System.out.println("3. Edit course");
            System.out.println("4. Delete course");
            System.out.println("5. View course roster");
            System.out.println("6. View all students");
            System.out.println("7. View all courses");
            System.out.println("0. Back");
            int choice = InputUtil.readInt("Choice: ");
            try {
                switch (choice) {
                    case 1: addStudent(); break;
                    case 2: addCourse(); break;
                    case 3: editCourse(); break;
                    case 4: deleteCourse(); break;
                    case 5: showRoster(); break;
                    case 6: showStudents(); break;
                    case 7: showCourses(); break;
                    case 0: return;
                    default: System.out.println("Invalid choice. Please try again.");
                }
            } catch (RegistrationException | IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void addStudent() throws RegistrationException {
        String id = InputUtil.readLine("Student ID: ");
        String name = InputUtil.readLine("Name: ");
        String email = InputUtil.readLine("Email: ");
        students.add(new Student(id, name, email)); // the repository rejects duplicate IDs
        System.out.println("Student added.");
    }

    private void addCourse() throws RegistrationException {
        String code = InputUtil.readLine("Course code: ");
        String title = InputUtil.readLine("Title: ");
        int credits = InputUtil.readInt("Credits: ");
        int capacity = InputUtil.readInt("Capacity: ");
        Schedule schedule = readSchedule();
        courses.add(new Course(code, title, credits, capacity, schedule));
        System.out.println("Course added.");
    }

    private void editCourse() throws RegistrationException {
        Course course = findCourse();
        if (course == null) {
            return;
        }
        System.out.println("Editing: " + course);
        String title = InputUtil.readLine("New title: ");
        int credits = InputUtil.readInt("New credits: ");
        int capacity = InputUtil.readInt("New capacity: ");
        Schedule schedule = readSchedule();
        courses.update(course.getCode(), title, credits, capacity, schedule);
        System.out.println("Course updated.");
    }

    private void deleteCourse() {
        Course course = findCourse();
        if (course == null) {
            return;
        }
        // Rule: a course with registered students cannot be deleted (checked by the service)
        if (service.hasRegistrations(course)) {
            System.out.println("Cannot delete " + course.getCode()
                    + ": students are registered in it.");
            return;
        }
        courses.delete(course.getCode());
        System.out.println("Course " + course.getCode() + " deleted.");
    }

    private void showRoster() {
        Course course = findCourse();
        if (course == null) {
            return;
        }
        List<Student> roster = service.getRoster(course);
        if (roster.isEmpty()) {
            System.out.println("No students are registered in " + course.getCode() + ".");
            return;
        }
        System.out.println("Roster of " + course.getCode() + " - " + course.getTitle() + ":");
        for (Student s : roster) {
            System.out.println("  " + s);
        }
    }

    private void showStudents() {
        List<Student> all = students.findAll();
        if (all.isEmpty()) {
            System.out.println("No students yet.");
            return;
        }
        for (Student s : all) {
            System.out.println(s);
        }
    }

    private void showCourses() {
        List<Course> all = courses.findAll();
        if (all.isEmpty()) {
            System.out.println("No courses yet.");
            return;
        }
        for (Course c : all) {
            System.out.println(c);
        }
    }

    /** Asks for a course code and returns the course, or null (with a message) if not found. */
    private Course findCourse() {
        String code = InputUtil.readLine("Course code: ");
        Course course = courses.findByCode(code);
        if (course == null) {
            System.out.println("Course " + code + " was not found.");
        }
        return course;
    }

    /** Asks for day and times until the schedule is valid. */
    private Schedule readSchedule() {
        while (true) {
            String day = InputUtil.readLine("Day (for example SUN): ");
            String start = InputUtil.readLine("Start time (HH:MM): ");
            String end = InputUtil.readLine("End time (HH:MM): ");
            try {
                return new Schedule(day, start, end);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage() + " Please enter the schedule again.");
            }
        }
    }
}
