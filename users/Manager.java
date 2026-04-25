package users;

import academic.Course;
import enums.CourseType;
import enums.ManagerType;
import java.util.Comparator;
import java.util.List;
import services.NewsService;
import services.ReportService;
import storage.Database;
import system.News;

public class Manager extends Employee {
    private static final long serialVersionUID = 1L;

    private ManagerType managerType;

    public Manager(String id, String username, String password, String fullName, String email,
                   String department, ManagerType managerType) {
        super(id, username, password, fullName, email, department);
        this.managerType = managerType;
    }

    public ManagerType getManagerType() {
        return managerType;
    }

    public void assignCourseToTeacher(Course course, Teacher teacher) {
        course.addInstructor(teacher);
        teacher.assignCourse(course);
    }

    public Course addCourse(Database database, String code, String title, int credits, CourseType type,
                            String major, int year) {
        Course course = new Course(code, title, credits, type, major, year);
        database.addCourse(course);
        return course;
    }

    public String createAcademicReport(Database database) {
        return new ReportService().generateMarksReport(database.getStudents());
    }

    public News publishNews(NewsService newsService, String topic, String content) {
        return newsService.publishNews(topic, content);
    }

    public List<Student> viewStudentsSorted(Database database, Comparator<Student> comparator) {
        return database.getStudents().stream().sorted(comparator).toList();
    }
}
