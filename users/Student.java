package users;

import academic.Course;
import academic.Mark;
import academic.Transcript;
import exceptions.CreditLimitExceededException;
import exceptions.TooManyFailsException;
import interfaces.CanResearch;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import research.Researcher;

public class Student extends User implements CanResearch {
    private static final long serialVersionUID = 1L;
    public static final int MAX_CREDITS = 21;
    public static final int MAX_FAILS = 3;

    private String major;
    private int yearOfStudy;
    private int failedCourses;
    private Researcher researchProfile;
    private final List<Course> courses = new ArrayList<>();
    private final Map<Course, Mark> marks = new HashMap<>();
    private final List<String> organizations = new ArrayList<>();

    public Student(String id, String username, String password, String fullName, String email,
                   String major, int yearOfStudy) {
        super(id, username, password, fullName, email);
        this.major = major;
        this.yearOfStudy = yearOfStudy;
    }

    public String getMajor() {
        return major;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public List<Course> getCourses() {
        return new ArrayList<>(courses);
    }

    public void registerForCourse(Course course) throws CreditLimitExceededException, TooManyFailsException {
        if (failedCourses > MAX_FAILS) {
            throw new TooManyFailsException("Student cannot fail more than 3 times.");
        }
        if (getTotalCredits() + course.getCredits() > MAX_CREDITS) {
            throw new CreditLimitExceededException("Student cannot register for more than 21 credits.");
        }
        if (!courses.contains(course)) {
            courses.add(course);
            course.addStudent(this);
        }
    }

    public int getTotalCredits() {
        return courses.stream().mapToInt(Course::getCredits).sum();
    }

    public void addMark(Course course, Mark mark) {
        marks.put(course, mark);
        if (mark.isFailed()) {
            failedCourses++;
        }
    }

    public Map<Course, Mark> getMarks() {
        return new HashMap<>(marks);
    }

    public Transcript getTranscript() {
        return new Transcript(this, marks);
    }

    public void rateTeacher(Teacher teacher, int rating) {
        teacher.update(new system.Notification("Rating from " + getFullName() + ": " + rating + "/5"));
    }

    public void joinOrganization(String organization) {
        organizations.add(organization);
    }

    public void setResearchProfile(Researcher researchProfile) {
        this.researchProfile = researchProfile;
    }

    @Override
    public Researcher getResearchProfile() {
        return researchProfile;
    }

    public static Comparator<Student> byGpa() {
        return Comparator.comparingDouble((Student s) -> s.getTranscript().getGpa()).reversed();
    }
}
