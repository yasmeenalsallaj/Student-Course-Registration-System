package ui;

import exception.RegistrationException;
import model.Course;
import model.Student;
import repository.CourseRepository;
import repository.StudentRepository;
import service.RegistrationService;
import java.util.List;

public class StudentMenu {
    private final StudentRepository students;
    private final CourseRepository courses;
    private final RegistrationService service;

    public StudentMenu(StudentRepository students, CourseRepository courses, RegistrationService service) {
        this.students = students;
        this.courses = courses;
        this.service = service;
    }

    public void run() {
        String id = InputUtil.readLine("Student ID (for example S1): ");
        Student student = students.findById(id);
        if (student == null) {
            System.out.println("No student with ID " + id + ".");
            return;
        }
        System.out.println("\nWelcome, " + student.getName() + ".");
        while (true) {
            System.out.println("\n=== Student Menu ===");
            System.out.println("1. View available courses");
            System.out.println("2. Search courses");
            System.out.println("3. Register in a course");
            System.out.println("4. Drop a course");
            System.out.println("5. My courses and total credits");
            System.out.println("0. Back");
            int choice = InputUtil.readInt("Choice: ");
            try {
                switch (choice) {
                    case 1: showAvailable(); break;
                    case 2: search(); break;
                    case 3: register(student); break;
                    case 4: drop(student); break;
                    case 5: showMine(student); break;
                    case 0: return;
                    default: System.out.println("Invalid choice. Please try again.");
                }
            } catch (RegistrationException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void showAvailable() {
        System.out.println("Available courses:");
        printCourses(courses.findAvailable());
    }

    private void search() {
        String keyword = InputUtil.readLine("Search by code or title: ");
        printCourses(courses.search(keyword));
    }

    private void register(Student student) throws RegistrationException {
        System.out.println("Courses with free seats:");
        printCourses(courses.findAvailable());
        Course course = askCourse();
        if (course == null) {
            return;
        }
        service.register(student, course);
        System.out.println("Registered in " + course.getCode() + " - " + course.getTitle() + ".");
        showMine(student);
    }

    private void drop(Student student) throws RegistrationException {
        if (service.getCoursesOf(student).isEmpty()) {
            System.out.println("You are not registered in any course.");
            return;
        }
        showMine(student);
        Course course = askCourse();
        if (course == null) {
            return;
        }
        service.drop(student, course);
        System.out.println("Dropped " + course.getCode() + " - " + course.getTitle() + ".");
        showMine(student);
    }

    private void showMine(Student student) {
        List<Course> mine = service.getCoursesOf(student);
        if (mine.isEmpty()) {
            System.out.println("You are not registered in any course.");
            return;
        }
        System.out.println("Your courses:");
        for (Course c : mine) {
            System.out.println(c);
        }
        System.out.println("Total credits: " + service.totalCredits(student)
                + " / " + RegistrationService.MAX_CREDITS);
    }

    private Course askCourse() {
        String code = InputUtil.readLine("Course code: ");
        Course course = courses.findByCode(code);
        if (course == null) {
            System.out.println("Course " + code + " was not found.");
        }
        return course;
    }

    private void printCourses(List<Course> list) {
        if (list.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        for (Course c : list) {
            System.out.println(c);
        }
    }
}
