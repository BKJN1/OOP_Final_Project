import academic.Course;
import academic.Lesson;
import academic.Mark;
import enums.CourseType;
import enums.Format;
import enums.LessonType;
import enums.ManagerType;
import enums.TeacherPosition;
import enums.UrgencyLevel;
import java.time.LocalDate;
import research.Journal;
import research.ResearchPaper;
import research.ResearchProject;
import services.AuthService;
import services.CourseService;
import services.NewsService;
import services.ResearchService;
import storage.DataManager;
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
        try {
            Database database = new Database();
            AuthService authService = new AuthService(database);
            CourseService courseService = new CourseService(database);
            NewsService newsService = new NewsService(database);
            ResearchService researchService = new ResearchService(database);

            Admin admin = new Admin("A1", "admin", "1234", "Dana Admin", "admin@uni.kz");
            Manager manager = new Manager("M1", "manager", "1234", "Aruzhan Manager", "manager@uni.kz",
                    "OR", ManagerType.OR);
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

            admin.addUser(database, admin);
            admin.addUser(database, manager);
            admin.addUser(database, dean);
            admin.addUser(database, professor);
            admin.addUser(database, lector);
            admin.addUser(database, bachelor);
            admin.addUser(database, master);
            admin.addUser(database, support);

            Course oop = manager.addCourse(database, "CSCI2106", "Object-Oriented Programming", 5,
                    CourseType.MAJOR, "SITE", 2);
            Course research = manager.addCourse(database, "RES5001", "Research Methods", 6,
                    CourseType.MAJOR, "SITE", 1);
            manager.assignCourseToTeacher(oop, professor);
            manager.assignCourseToTeacher(oop, lector);
            oop.addLesson(new Lesson(LessonType.LECTURE, "A-101", "Monday 10:00", professor));
            oop.addLesson(new Lesson(LessonType.PRACTICE, "B-204", "Wednesday 15:00", lector));

            authService.login("student", "1234");
            courseService.register(bachelor, oop);
            courseService.register(master, research);
            professor.putMark(bachelor, oop, new Mark(28, 27, 35));
            professor.putMark(master, research, new Mark(30, 30, 35));
            bachelor.rateTeacher(professor, 5);
            authService.logout();

            Journal journal = new Journal("KBTU Research Journal");
            database.addJournal(journal);
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
            newsService.announcePaper(p1.getTitle());
            newsService.announceTopCitedResearcher();
            master.assignSupervisor(professor.getResearchProfile());

            ResearchProject project = new ResearchProject("Student Performance Analytics");
            project.join(professor);
            project.join(master);
            project.publishPaper(p1);

            Request complaint = professor.sendComplaint(bachelor, dean, UrgencyLevel.HIGH, "Repeated late submissions");
            dean.signRequest(complaint);
            database.addRequest(complaint);
            support.seeNewRequests(database);
            support.accept(complaint);

            System.out.println("=== Authentication user ===");
            System.out.println(authService.login("admin", "1234"));
            authService.logout();

            System.out.println("\n=== Courses ===");
            database.getCourses().forEach(System.out::println);

            System.out.println("\n=== Transcript ===");
            System.out.println(bachelor.getTranscript());

            System.out.println("\n=== Research papers by citations ===");
            researchService.printAllPapers(ResearchPaper.byCitations()).forEach(System.out::println);
            System.out.println("Citation plain: " + p1.getCitation(Format.PLAIN_TEXT));
            System.out.println("Citation bibtex: " + p1.getCitation(Format.BIBTEX));

            System.out.println("\n=== News ===");
            database.getNews().forEach(System.out::println);

            System.out.println("\n=== Requests ===");
            database.getRequests().forEach(System.out::println);

            System.out.println("\n=== Report ===");
            System.out.println(manager.createAcademicReport(database));

            DataManager.getInstance("university.dat").save(database);
            Database loaded = DataManager.getInstance("university.dat").load();
            System.out.println("\nSerialization works, loaded users: " + loaded.getUsers().size());
        } catch (Exception e) {
            System.err.println("Demo failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
