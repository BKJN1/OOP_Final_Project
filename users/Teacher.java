package users;

import academic.Course;
import academic.Mark;
import enums.TeacherPosition;
import enums.UrgencyLevel;
import interfaces.CanResearch;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import research.Researcher;
import system.Request;

public class Teacher extends Employee implements CanResearch {
    private static final long serialVersionUID = 1L;

    private TeacherPosition position;
    private Researcher researchProfile;
    private final List<Course> courses = new ArrayList<>();

    public Teacher(String id, String username, String password, String fullName, String email,
                   String department, TeacherPosition position) {
        super(id, username, password, fullName, email, department);
        this.position = position;
        if (position == TeacherPosition.PROFESSOR) {
            this.researchProfile = new Researcher(this);
        }
    }

    public TeacherPosition getPosition() {
        return position;
    }

    public void setResearchProfile(Researcher researchProfile) {
        this.researchProfile = researchProfile;
    }

    @Override
    public Researcher getResearchProfile() {
        return researchProfile;
    }

    public List<Course> getCourses() {
        return new ArrayList<>(courses);
    }

    public void assignCourse(Course course) {
        if (!courses.contains(course)) {
            courses.add(course);
        }
    }

    public void putMark(Student student, Course course, Mark mark) {
        student.addMark(course, mark);
    }

    public List<Student> viewStudents(Course course) {
        return course.getStudents();
    }

    public Request sendComplaint(Student student, Dean dean, UrgencyLevel urgency, String text) {
        Request request = new Request(this, "Complaint about " + student.getFullName() + ": " + text, urgency);
        dean.update(request.toNotification());
        return request;
    }

    public static Comparator<Teacher> byName() {
        return Comparator.comparing(Teacher::getFullName, String.CASE_INSENSITIVE_ORDER);
    }
}
