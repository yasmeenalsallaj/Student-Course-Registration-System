import exception.RegistrationException;
import model.Course;
import model.Schedule;
import model.Student;
import repository.CourseRepository;
import repository.StudentRepository;
import service.RegistrationService;
import ui.AdminMenu;
import ui.InputUtil;
import ui.StudentMenu;

public class Main {
    public static void main(String[] args) {
        StudentRepository students = new StudentRepository();
        CourseRepository courses = new CourseRepository();
        RegistrationService service = new RegistrationService(students, courses);

        try {
            loadSampleData(students, courses, service);
        } catch (RegistrationException e) {
            System.out.println("Could not load sample data: " + e.getMessage());
            return;
        }

        AdminMenu admin = new AdminMenu(students, courses, service);
        StudentMenu student = new StudentMenu(students, courses, service);

        while (true) {
            System.out.println("\n=== Student Course Registration System ===");
            System.out.println("1. Admin");
            System.out.println("2. Student");
            System.out.println("0. Exit");
            int choice = InputUtil.readInt("Choice: ");
            try {
                switch (choice) {
                    case 1: admin.run(); break;
                    case 2: student.run(); break;
                    case 0:
                        System.out.println("Goodbye.");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (RuntimeException e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }

    private static void loadSampleData(StudentRepository students, CourseRepository courses,
                                       RegistrationService service) throws RegistrationException {
        Student lina = new Student("S1", "Lina Haddad", "lina@example.com");
        Student omar = new Student("S2", "Omar Nasser", "omar@example.com");
        Student rana = new Student("S3", "Rana Khalil", "rana@example.com");
        Student sara = new Student("S4", "Sara Mansour", "sara@example.com");
        lina.addCompletedCourse("CS101");
        students.add(lina);
        students.add(omar);
        students.add(rana);
        students.add(sara);

        Course cs101 = new Course("CS101", "Intro to Programming", 3, 30, new Schedule("SUN", "08:00", "09:30"));
        Course cs201 = new Course("CS201", "Data Structures", 3, 30, new Schedule("MON", "08:00", "09:30"));
        Course ma101 = new Course("MA101", "Calculus I", 3, 30, new Schedule("SUN", "10:00", "11:30"));
        Course ph101 = new Course("PH101", "Physics I", 3, 30, new Schedule("TUE", "08:00", "09:30"));
        Course en101 = new Course("EN101", "English Writing", 3, 30, new Schedule("WED", "08:00", "09:30"));
        Course hs101 = new Course("HS101", "History", 3, 30, new Schedule("MON", "10:00", "11:30"));
        Course db301 = new Course("DB301", "Database Systems", 3, 1, new Schedule("THU", "10:00", "11:30"));
        Course se301 = new Course("SE301", "Software Engineering", 3, 30, new Schedule("SUN", "08:30", "10:00"));
        Course cs401 = new Course("CS401", "Capstone Project", 4, 30, new Schedule("THU", "12:00", "14:00"));
        cs201.addPrerequisite("CS101");

        courses.add(cs101);
        courses.add(cs201);
        courses.add(ma101);
        courses.add(ph101);
        courses.add(en101);
        courses.add(hs101);
        courses.add(db301);
        courses.add(se301);
        courses.add(cs401);

        service.register(omar, db301);
        service.register(rana, cs101);
        service.register(rana, ma101);
        service.register(rana, ph101);
        service.register(rana, en101);
        service.register(rana, hs101);
    }
}
