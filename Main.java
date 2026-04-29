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
            Manager manager = (Manager) UserFactory.create("MANAGER", "M1", "manager1", "1234",
                    "Aruzhan Manager", "manager@uni.kz", "OR");
            Manager departmentManager = (Manager) UserFactory.create("MANAGER", "M2", "manager2", "1234",
                    "Miras Department Manager", "manager2@uni.kz", "SITE");
            Admin admin = (Admin) UserFactory.create("ADMIN", "A1", "admin1", "1234",
                    "Dana Admin", "admin@uni.kz", "Administration");
            Dean dean = (Dean) UserFactory.create("DEAN", "D1", "dean1", "1234",
                    "Askar Dean", "dean@uni.kz", "SITE");
            Teacher professor = (Teacher) UserFactory.create("PROFESSOR", "T1", "teacher1", "1234",
                    "Professor Kim", "kim@uni.kz", "SITE");
            Teacher lector = (Teacher) UserFactory.create("LECTOR", "T2", "teacher2", "1234",
                    "Lector Lee", "lee@uni.kz", "SITE");
            Teacher oilProfessor = (Teacher) UserFactory.create("PROFESSOR", "T3", "teacher3", "1234",
                    "Professor Omar", "omar@uni.kz", "Oil and Gas");
            Student bachelor = (Student) UserFactory.create("STUDENT", "S1", "student1", "1234",
                    "Nursultan Student", "student@uni.kz", "SITE");
            Student secondBachelor = (Student) UserFactory.create("STUDENT", "S2", "student2", "1234",
                    "Diana Student", "student2@uni.kz", "SITE");
            Student oilStudent = (Student) UserFactory.create("STUDENT", "S3", "student3", "1234",
                    "Erlan Oil Student", "student3@uni.kz", "Oil and Gas");
            GraduateStudent master = (GraduateStudent) UserFactory.create("GRADUATE_STUDENT", "G1", "grad1", "1234",
                    "Aigerim Graduate", "grad@uni.kz", "SITE");
            GraduateStudent phd = (GraduateStudent) UserFactory.create("GRADUATE_STUDENT", "G2", "grad2", "1234",
                    "Serik PhD Student", "grad2@uni.kz", "SITE");
            TechSupportSpecialist support = (TechSupportSpecialist) UserFactory.create("SUPPORT", "TS1", "support1", "1234",
                    "Timur Support", "support@uni.kz", "IT");
            TechSupportSpecialist support2 = (TechSupportSpecialist) UserFactory.create("SUPPORT", "TS2", "support2", "1234",
                    "Ayan Support", "support2@uni.kz", "IT");
            ResearchEmployee labResearcher = (ResearchEmployee) UserFactory.create("RESEARCH_EMPLOYEE", "R1", "researcher1", "1234",
                    "Madina Lab Researcher", "lab@uni.kz", "Research Center");

            db.addUser(admin);
            db.addUser(manager);
            db.addUser(departmentManager);
            db.addUser(dean);
            db.addUser(professor);
            db.addUser(lector);
            db.addUser(oilProfessor);
            db.addUser(bachelor);
            db.addUser(secondBachelor);
            db.addUser(oilStudent);
            db.addUser(master);
            db.addUser(phd);
            db.addUser(support);
            db.addUser(support2);
            db.addUser(labResearcher);

            Course oop = manager.addCourse(db, "CSCI2106", "Object-Oriented Programming", 5,
                    CourseType.MAJOR, "SITE", 2);
            Course research = manager.addCourse(db, "RES5001", "Research Methods", 6,
                    CourseType.MAJOR, "SITE", 1);
            Course databases = manager.addCourse(db, "CSCI2203", "Databases", 5,
                    CourseType.MAJOR, "SITE", 2);
            Course geology = manager.addCourse(db, "OG101", "Introduction to Oil and Gas", 5,
                    CourseType.FREE_ELECTIVE, "Oil and Gas", 1);
            Course entrepreneurship = manager.addCourse(db, "BUS2101", "Technology Entrepreneurship", 3,
                    CourseType.MINOR, "Business", 2);
            manager.assignCourseToTeacher(oop, professor);
            manager.assignCourseToTeacher(oop, lector);
            manager.assignCourseToTeacher(research, professor);
            manager.assignCourseToTeacher(databases, lector);
            manager.assignCourseToTeacher(geology, oilProfessor);
            manager.assignCourseToTeacher(entrepreneurship, professor);
            oop.addLesson(new Lesson(LessonType.LECTURE, "A-101", "Monday 10:00", professor));
            oop.addLesson(new Lesson(LessonType.PRACTICE, "B-204", "Wednesday 15:00", lector));
            databases.addLesson(new Lesson(LessonType.LECTURE, "A-202", "Tuesday 12:00", lector));
            geology.addLesson(new Lesson(LessonType.LECTURE, "OG-1", "Thursday 09:00", oilProfessor));

            bachelor.registerForCourse(oop);
            bachelor.registerForCourse(geology);
            secondBachelor.registerForCourse(oop);
            secondBachelor.registerForCourse(databases);
            oilStudent.registerForCourse(geology);
            master.registerForCourse(research);
            phd.registerForCourse(research);
            professor.putMark(bachelor, oop, new Mark(28, 27, 35));
            oilProfessor.putMark(bachelor, geology, new Mark(25, 25, 30));
            professor.putMark(secondBachelor, oop, new Mark(30, 28, 34));
            lector.putMark(secondBachelor, databases, new Mark(27, 26, 32));
            oilProfessor.putMark(oilStudent, geology, new Mark(29, 29, 36));
            professor.putMark(master, research, new Mark(30, 30, 35));
            professor.putMark(phd, research, new Mark(30, 29, 38));

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
            phd.getResearchProfile().addPaper(p3);
            phd.addDiplomaPaper(p3);
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
            Request projectorRequest = new Request(lector, "Please check projector in B-204", UrgencyLevel.MEDIUM);
            dean.signRequest(projectorRequest);
            db.addRequest(projectorRequest);
            return db;
        } catch (Exception e) {
            throw new IllegalStateException("Could not seed database: " + e.getMessage(), e);
        }
    }
}
