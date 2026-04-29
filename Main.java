import academic.Course;
import academic.Lesson;
import academic.Mark;
import enums.CourseType;
import enums.LessonType;
import enums.UrgencyLevel;
import java.io.File;
import java.time.LocalDate;
import research.Journal;
import research.ResearchPaper;
import research.ResearchProject;
import services.ConsoleMenu;
import services.NewsService;
import services.UserFactory;
import storage.DataManager;
import storage.Database;
import system.Request;
import users.Admin;
import users.Dean;
import users.GraduateStudent;
import users.Manager;
import users.ResearchEmployee;
import users.Student;
import users.Teacher;
import users.TechSupportSpecialist;

public class Main {
    private static final String DATA_FILE = "university.dat";

    public static void main(String[] args) {
        new ConsoleMenu(loadOrSeedDatabase()).start();
    }

    private static Database loadOrSeedDatabase() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try {
                return DataManager.getInstance(DATA_FILE).load();
            } catch (Exception e) {
                System.out.println("Could not load saved data, using seed data: " + e.getMessage());
            }
        }
        return seedDatabase();
    }

    private static Database seedDatabase() {
        try {
            Database db = new Database();
            NewsService seedNews = new NewsService(db);
            Manager manager = (Manager) UserFactory.create("MANAGER", "M1", "manager", "1234",
                    "Aruzhan Manager", "manager@uni.kz", "OR");
            Admin admin = (Admin) UserFactory.create("ADMIN", "A1", "admin", "1234",
                    "Dana Admin", "admin@uni.kz", "Administration");
            Dean dean = (Dean) UserFactory.create("DEAN", "D1", "dean", "1234",
                    "Askar Dean", "dean@uni.kz", "SITE");
            Teacher professor = (Teacher) UserFactory.create("PROFESSOR", "T1", "prof", "1234",
                    "Professor Kim", "kim@uni.kz", "SITE");
            Teacher lector = (Teacher) UserFactory.create("LECTOR", "T2", "lector", "1234",
                    "Lector Lee", "lee@uni.kz", "SITE");
            Student bachelor = (Student) UserFactory.create("STUDENT", "S1", "student", "1234",
                    "Nursultan Student", "student@uni.kz", "SITE");
            GraduateStudent master = (GraduateStudent) UserFactory.create("GRADUATE_STUDENT", "G1", "grad", "1234",
                    "Aigerim Graduate", "grad@uni.kz", "SITE");
            TechSupportSpecialist support = (TechSupportSpecialist) UserFactory.create("SUPPORT", "TS1", "support", "1234",
                    "Timur Support", "support@uni.kz", "IT");
            ResearchEmployee labResearcher = (ResearchEmployee) UserFactory.create("RESEARCH_EMPLOYEE", "R1", "lab", "1234",
                    "Madina Lab Researcher", "lab@uni.kz", "Research Center");

            db.addUser(admin);
            db.addUser(manager);
            db.addUser(dean);
            db.addUser(professor);
            db.addUser(lector);
            db.addUser(bachelor);
            db.addUser(master);
            db.addUser(support);
            db.addUser(labResearcher);

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
            ResearchPaper p4 = new ResearchPaper("Laboratory Data Mining", journal, 9,
                    LocalDate.of(2025, 5, 5), "10.1000/lab.1", 7);
            professor.getResearchProfile().addPaper(p1);
            professor.getResearchProfile().addPaper(p2);
            professor.getResearchProfile().addPaper(p3);
            master.getResearchProfile().addPaper(p2);
            master.addDiplomaPaper(p2);
            labResearcher.getResearchProfile().addPaper(p4);
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
