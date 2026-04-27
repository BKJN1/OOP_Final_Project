import academic.Course;
import academic.Lesson;
import academic.Mark;
import enums.CourseType;
import enums.LessonType;
import enums.ManagerType;
import enums.TeacherPosition;
import enums.UrgencyLevel;
import java.time.LocalDate;
import research.Journal;
import research.ResearchPaper;
import research.ResearchProject;
import services.ConsoleMenu;
import services.NewsService;
import storage.Database;
import system.Request;
import users.Admin;
import users.Dean;
import users.GraduateStudent;
import users.Manager;
import users.Student;
import users.Teacher;
import users.TechSupportSpecialist;

public class Main {
    public static void main(String[] args) {
        new ConsoleMenu(seedDatabase()).start();
    }

    private static Database seedDatabase() {
        try {
            Database db = new Database();
            NewsService seedNews = new NewsService(db);
            Manager manager = new Manager("M1", "manager", "1234", "Aruzhan Manager", "manager@uni.kz",
                    "OR", ManagerType.OR);
            Admin admin = new Admin("A1", "admin", "1234", "Dana Admin", "admin@uni.kz");
            Dean dean = new Dean("D1", "dean", "1234", "Askar Dean", "dean@uni.kz", "SITE");
            Teacher professor = new Teacher("T1", "prof", "1234", "Professor Kim", "kim@uni.kz",
                    "SITE", TeacherPosition.PROFESSOR);
            Teacher lector = new Teacher("T2", "lector", "1234", "Lector Lee", "lee@uni.kz",
                    "SITE", TeacherPosition.LECTOR);
            Student bachelor = new Student("S1", "student", "1234", "Nursultan Student", "student@uni.kz",
                    "SITE", 2);
            GraduateStudent master = new GraduateStudent("G1", "grad", "1234", "Aigerim Graduate", "grad@uni.kz",
                    "SITE", 1);
            TechSupportSpecialist support = new TechSupportSpecialist("TS1", "support", "1234",
                    "Timur Support", "support@uni.kz", "IT");

            db.addUser(admin);
            db.addUser(manager);
            db.addUser(dean);
            db.addUser(professor);
            db.addUser(lector);
            db.addUser(bachelor);
            db.addUser(master);
            db.addUser(support);

            Course oop = manager.addCourse(db, "CSCI2106", "Object-Oriented Programming", 5,
                    CourseType.MAJOR, "SITE", 2);
            Course research = manager.addCourse(db, "RES5001", "Research Methods", 6,
                    CourseType.MAJOR, "SITE", 1);
            manager.assignCourseToTeacher(oop, professor);
            manager.assignCourseToTeacher(oop, lector);
            manager.assignCourseToTeacher(research, professor);
            oop.addLesson(new Lesson(LessonType.LECTURE, "A-101", "Monday 10:00", professor));
            oop.addLesson(new Lesson(LessonType.PRACTICE, "B-204", "Wednesday 15:00", lector));

            bachelor.registerForCourse(oop);
            master.registerForCourse(research);
            professor.putMark(bachelor, oop, new Mark(28, 27, 35));
            professor.putMark(master, research, new Mark(30, 30, 35));

            Journal journal = new Journal("KBTU Research Journal");
            db.addJournal(journal);
            journal.subscribe(bachelor);
            journal.subscribe(master);

            ResearchPaper p1 = new ResearchPaper("LMS Logs and Student Performance", journal, 12,
                    LocalDate.of(2024, 4, 12), "10.1000/lms.1", 10);
            ResearchPaper p2 = new ResearchPaper("Course Retake Prediction", journal, 8,
                    LocalDate.of(2024, 10, 2), "10.1000/lms.2", 6);
            ResearchPaper p3 = new ResearchPaper("Academic Analytics", journal, 10,
                    LocalDate.of(2025, 3, 20), "10.1000/lms.3", 4);
            professor.getResearchProfile().addPaper(p1);
            professor.getResearchProfile().addPaper(p2);
            professor.getResearchProfile().addPaper(p3);
            journal.publish(p1);
            seedNews.announcePaper(p1.getTitle());
            seedNews.announceTopCitedResearcher();
            master.assignSupervisor(professor.getResearchProfile());

            ResearchProject project = new ResearchProject("Student Performance Analytics");
            project.join(professor);
            project.join(master);
            project.publishPaper(p1);

            Request request = professor.sendComplaint(bachelor, dean, UrgencyLevel.HIGH, "Repeated late submissions");
            dean.signRequest(request);
            db.addRequest(request);
            return db;
        } catch (Exception e) {
            throw new IllegalStateException("Could not seed database: " + e.getMessage(), e);
        }
    }
}
