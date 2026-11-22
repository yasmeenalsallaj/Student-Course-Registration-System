package model;

/** Links one Student to one Course. */
public class Registration {

    private final Student student;
    private final Course course;

    public Registration(Student student, Course course) {
        this.student = student;
        this.course = course;
    }

    public Student getStudent() { return student; }
    public Course getCourse() { return course; }
}
