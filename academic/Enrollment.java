package academic;

import java.io.Serializable;
import java.time.LocalDate;
import users.Student;

public class Enrollment implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Student student;
    private final Course course;
    private boolean approved;
    private final LocalDate date = LocalDate.now();

    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public void approve() {
        approved = true;
    }

    public boolean isApproved() {
        return approved;
    }

    @Override
    public String toString() {
        return student.getFullName() + " -> " + course.getCode() + " approved=" + approved + " date=" + date;
    }
}
