package academic;

import enums.CourseType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import users.Student;
import users.Teacher;

public class Course implements Serializable, Comparable<Course> {
    private static final long serialVersionUID = 1L;

    private final String code;
    private String title;
    private int credits;
    private CourseType type;
    private String intendedMajor;
    private int intendedYear;
    private final List<Teacher> instructors = new ArrayList<>();
    private final List<Student> students = new ArrayList<>();
    private final List<Lesson> lessons = new ArrayList<>();

    public Course(String code, String title, int credits, CourseType type, String intendedMajor, int intendedYear) {
        this.code = code;
        this.title = title;
        this.credits = credits;
        this.type = type;
        this.intendedMajor = intendedMajor;
        this.intendedYear = intendedYear;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public CourseType getType() {
        return type;
    }

    public void setType(CourseType type) {
        this.type = type;
    }

    public String getIntendedMajor() {
        return intendedMajor;
    }

    public void setIntendedMajor(String intendedMajor) {
        this.intendedMajor = intendedMajor;
    }

    public int getIntendedYear() {
        return intendedYear;
    }

    public void setIntendedYear(int intendedYear) {
        this.intendedYear = intendedYear;
    }

    public void addInstructor(Teacher teacher) {
        if (!instructors.contains(teacher)) {
            instructors.add(teacher);
        }
    }

    public List<Teacher> getInstructors() {
        return new ArrayList<>(instructors);
    }

    public void addStudent(Student student) {
        if (!students.contains(student)) {
            students.add(student);
        }
    }

    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    public List<Lesson> getLessons() {
        return new ArrayList<>(lessons);
    }

    @Override
    public int compareTo(Course other) {
        return code.compareToIgnoreCase(other.code);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return Objects.equals(code, course.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return code + " " + title + " (" + credits + " credits, " + type + ")";
    }
}
