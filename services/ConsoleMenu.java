package services;

import academic.Course;
import academic.Mark;
import enums.CourseType;
import enums.Format;
import enums.UrgencyLevel;
import exceptions.UnauthorizedActionException;
import interfaces.CanResearch;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import research.ResearchPaper;
import research.Researcher;
import storage.DataManager;
import storage.Database;
import system.Request;
import users.Admin;
import users.Dean;
import users.Employee;
import users.GraduateStudent;
import users.Manager;
import users.Student;
import users.Teacher;
import users.TechSupportSpecialist;
import users.User;

public class ConsoleMenu {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static Database database;
    private static AuthService authService;
    private static CourseService courseService;
    private static NewsService newsService;
    private static ResearchService researchService;

    public ConsoleMenu(Database database) {
        ConsoleMenu.database = database;
        authService = new AuthService(database);
        courseService = new CourseService(database);
        newsService = new NewsService(database);
        researchService = new ResearchService(database);
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== University System ===");
            System.out.println("1. Login");
            System.out.println("2. Search (regular expressions)");
            System.out.println("3. Exit");
            int choice = readInt("Choose: ");

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    regexSearch();
                    break;
                case 3:
                    saveBeforeExit();
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void login() {
        String username = readLine("Username: ");
        String password = readLine("Password: ");
        try {
            User user = authService.login(username, password);
            System.out.println("Logged in as " + user.getFullName() + " (" + user.getClass().getSimpleName() + ")");
            showRoleMenu(user);
        } catch (UnauthorizedActionException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void showRoleMenu(User user) {
        if (user instanceof Admin) {
            adminMenu((Admin) user);
        } else if (user instanceof Manager) {
            managerMenu((Manager) user);
        } else if (user instanceof Teacher) {
            teacherMenu((Teacher) user);
        } else if (user instanceof TechSupportSpecialist) {
            techSupportMenu((TechSupportSpecialist) user);
        } else if (user instanceof Dean) {
            deanMenu((Dean) user);
        } else if (user instanceof GraduateStudent) {
            studentMenu((Student) user);
        } else if (user instanceof Student) {
            studentMenu((Student) user);
        } else {
            System.out.println("No menu for this role.");
        }
        authService.logout();
    }

    private static void adminMenu(Admin admin) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. View users");
            System.out.println("2. Add sample student");
            System.out.println("3. Update user");
            System.out.println("4. Remove user");
            System.out.println("5. View logs");
            System.out.println("6. Logout");
            switch (readInt("Choose: ")) {
                case 1:
                    database.getUsers().forEach(System.out::println);
                    break;
                case 2:
                    Student student = new Student(readLine("Id: "), readLine("Username: "), readLine("Password: "),
                            readLine("Full name: "), readLine("Email: "), readLine("Major: "), readInt("Year: "));
                    admin.addUser(database, student);
                    System.out.println("Student added.");
                    break;
                case 3:
                    updateUser(admin);
                    break;
                case 4:
                    admin.removeUser(database, readLine("User id: "));
                    System.out.println("User removed if id existed.");
                    break;
                case 5:
                    database.getLogs().forEach(System.out::println);
                    break;
                case 6:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void managerMenu(Manager manager) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Manager Menu ===");
            System.out.println("1. View courses");
            System.out.println("2. Add course");
            System.out.println("3. Assign course to teacher");
            System.out.println("4. Create academic report");
            System.out.println("5. Manage news");
            System.out.println("6. View students by GPA");
            System.out.println("7. View students alphabetically");
            System.out.println("8. Logout");
            switch (readInt("Choose: ")) {
                case 1:
                    database.getCourses().forEach(System.out::println);
                    break;
                case 2:
                    addCourse(manager);
                    break;
                case 3:
                    assignCourseToTeacher(manager);
                    break;
                case 4:
                    System.out.println(manager.createAcademicReport(database));
                    break;
                case 5:
                    manager.publishNews(newsService, readLine("Topic: "), readLine("Content: "));
                    System.out.println("News published.");
                    break;
                case 6:
                    manager.viewStudentsSorted(database, Student.byGpa()).forEach(System.out::println);
                    break;
                case 7:
                    manager.viewStudentsSorted(database, Comparator.comparing(Student::getFullName)).forEach(System.out::println);
                    break;
                case 8:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void teacherMenu(Teacher teacher) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Teacher Menu ===");
            System.out.println("1. View my courses");
            System.out.println("2. View students of course");
            System.out.println("3. Put mark");
            System.out.println("4. Send message to employee");
            System.out.println("5. Send complaint to dean");
            System.out.println("6. Research menu");
            System.out.println("7. Logout");
            switch (readInt("Choose: ")) {
                case 1:
                    teacher.getCourses().forEach(System.out::println);
                    break;
                case 2:
                    Course course = chooseCourse();
                    if (course != null) {
                        teacher.viewStudents(course).forEach(System.out::println);
                    }
                    break;
                case 3:
                    putMark(teacher);
                    break;
                case 4:
                    sendEmployeeMessage(teacher);
                    break;
                case 5:
                    sendComplaint(teacher);
                    break;
                case 6:
                    researchMenu(teacher);
                    break;
                case 7:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void studentMenu(Student student) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Student Menu ===");
            System.out.println("1. View courses");
            System.out.println("2. Register for course");
            System.out.println("3. View teacher of course");
            System.out.println("4. View marks");
            System.out.println("5. View transcript");
            System.out.println("6. Rate teacher");
            System.out.println("7. View notifications");
            System.out.println("8. Research menu");
            System.out.println("9. Logout");
            switch (readInt("Choose: ")) {
                case 1:
                    database.getCourses().forEach(System.out::println);
                    break;
                case 2:
                    registerForCourse(student);
                    break;
                case 3:
                    Course course = chooseCourse();
                    if (course != null) {
                        course.getInstructors().forEach(System.out::println);
                    }
                    break;
                case 4:
                    student.getMarks().forEach((c, m) -> System.out.println(c.getCode() + ": " + m));
                    break;
                case 5:
                    System.out.println(student.getTranscript());
                    break;
                case 6:
                    Teacher teacher = chooseTeacher();
                    if (teacher != null) {
                        student.rateTeacher(teacher, readInt("Rating 1-5: "));
                        System.out.println("Rating sent.");
                    }
                    break;
                case 7:
                    student.getNotifications().forEach(System.out::println);
                    break;
                case 8:
                    researchMenu(student);
                    break;
                case 9:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void techSupportMenu(TechSupportSpecialist support) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Tech Support Menu ===");
            System.out.println("1. View new requests");
            System.out.println("2. Accept request");
            System.out.println("3. Reject request");
            System.out.println("4. Mark request done");
            System.out.println("5. View all requests");
            System.out.println("6. Logout");
            switch (readInt("Choose: ")) {
                case 1:
                    support.seeNewRequests(database).forEach(System.out::println);
                    break;
                case 2:
                    changeRequestStatus(support, "accept");
                    break;
                case 3:
                    changeRequestStatus(support, "reject");
                    break;
                case 4:
                    changeRequestStatus(support, "done");
                    break;
                case 5:
                    database.getRequests().forEach(System.out::println);
                    break;
                case 6:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void deanMenu(Dean dean) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Dean Menu ===");
            System.out.println("1. View requests");
            System.out.println("2. Sign request");
            System.out.println("3. Send message to employee");
            System.out.println("4. Logout");
            switch (readInt("Choose: ")) {
                case 1:
                    database.getRequests().forEach(System.out::println);
                    break;
                case 2:
                    Request request = chooseRequest();
                    if (request != null) {
                        dean.signRequest(request);
                        System.out.println("Request signed.");
                    }
                    break;
                case 3:
                    sendEmployeeMessage(dean);
                    break;
                case 4:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void researchMenu(User user) {
        if (!(user instanceof CanResearch) || !((CanResearch) user).isResearcher()) {
            System.out.println("This user is not a researcher.");
            return;
        }
        Researcher researcher = ((CanResearch) user).getResearchProfile();
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Research Menu ===");
            System.out.println("1. View my h-index");
            System.out.println("2. Print my papers by citations");
            System.out.println("3. Print all university papers by date");
            System.out.println("4. Print top cited researcher");
            System.out.println("5. Show citation for my first paper");
            System.out.println("6. Back");
            switch (readInt("Choose: ")) {
                case 1:
                    System.out.println("h-index: " + researcher.calculateHIndex());
                    break;
                case 2:
                    researcher.printPapers(ResearchPaper.byCitations());
                    break;
                case 3:
                    researchService.printAllPapers(ResearchPaper.byDate()).forEach(System.out::println);
                    break;
                case 4:
                    System.out.println(researchService.topCitedResearcher());
                    break;
                case 5:
                    if (researcher.getPapers().isEmpty()) {
                        System.out.println("No papers.");
                    } else {
                        ResearchPaper paper = researcher.getPapers().get(0);
                        System.out.println(paper.getCitation(Format.PLAIN_TEXT));
                        System.out.println(paper.getCitation(Format.BIBTEX));
                    }
                    break;
                case 6:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void regexSearch() {
        String regex = readLine("Enter regex: ");
        Pattern pattern;
        try {
            pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        } catch (PatternSyntaxException e) {
            System.out.println("Invalid regex: " + e.getMessage());
            return;
        }

        System.out.println("\nUsers:");
        database.getUsers().stream()
                .filter(u -> pattern.matcher(u.getId() + " " + u.getUsername() + " " + u.getFullName() + " " + u.getEmail()).find())
                .forEach(System.out::println);

        System.out.println("\nCourses:");
        database.getCourses().stream()
                .filter(c -> pattern.matcher(c.toString()).find())
                .forEach(System.out::println);

        System.out.println("\nNews:");
        database.getNews().stream()
                .filter(n -> pattern.matcher(n.toString()).find())
                .forEach(System.out::println);

        System.out.println("\nResearch papers:");
        researchService.printAllPapers(ResearchPaper.byDate()).stream()
                .filter(p -> pattern.matcher(p.toString()).find())
                .forEach(System.out::println);
    }

    private static void addCourse(Manager manager) {
        String code = readLine("Code: ");
        String title = readLine("Title: ");
        int credits = readInt("Credits: ");
        CourseType type = readCourseType();
        String major = readLine("Intended major: ");
        int year = readInt("Intended year: ");
        manager.addCourse(database, code, title, credits, type, major, year);
        System.out.println("Course added.");
    }

    private static void updateUser(Admin admin) {
        User user = chooseUser();
        if (user == null) {
            System.out.println("Invalid user.");
            return;
        }
        System.out.println("Leave field empty to keep current value.");
        String username = readLine("New username [" + user.getUsername() + "]: ");
        String password = readLine("New password: ");
        String fullName = readLine("New full name [" + user.getFullName() + "]: ");
        String email = readLine("New email [" + user.getEmail() + "]: ");
        admin.updateUser(database, user, username, password, fullName, email);
        System.out.println("User updated: " + user);
    }

    private static void assignCourseToTeacher(Manager manager) {
        Course course = chooseCourse();
        Teacher teacher = chooseTeacher();
        if (course != null && teacher != null) {
            manager.assignCourseToTeacher(course, teacher);
            System.out.println("Assigned.");
        }
    }

    private static void putMark(Teacher teacher) {
        Student student = chooseStudent();
        Course course = chooseCourse();
        if (student != null && course != null) {
            Mark mark = new Mark(readInt("1st attestation: "), readInt("2nd attestation: "), readInt("Final: "));
            teacher.putMark(student, course, mark);
            System.out.println("Mark saved.");
        }
    }

    private static void registerForCourse(Student student) {
        Course course = chooseCourse();
        if (course == null) {
            return;
        }
        try {
            courseService.register(student, course);
            System.out.println("Registered.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendEmployeeMessage(Employee sender) {
        Employee receiver = chooseEmployee();
        if (receiver != null) {
            sender.sendMessage(receiver, readLine("Message: "));
            System.out.println("Message sent.");
        }
    }

    private static void sendComplaint(Teacher teacher) {
        Student student = chooseStudent();
        Dean dean = chooseDean();
        if (student != null && dean != null) {
            Request request = teacher.sendComplaint(student, dean, UrgencyLevel.HIGH, readLine("Complaint text: "));
            database.addRequest(request);
            System.out.println("Complaint sent.");
        }
    }

    private static void changeRequestStatus(TechSupportSpecialist support, String action) {
        Request request = chooseRequest();
        if (request == null) {
            return;
        }
        if ("accept".equals(action)) {
            support.accept(request);
        } else if ("reject".equals(action)) {
            support.reject(request);
        } else {
            support.done(request);
        }
        System.out.println("Request updated.");
    }

    private static Course chooseCourse() {
        List<Course> courses = database.getCourses();
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i));
        }
        int index = readInt("Course number: ") - 1;
        return index >= 0 && index < courses.size() ? courses.get(index) : null;
    }

    private static User chooseUser() {
        List<User> users = database.getUsers();
        for (int i = 0; i < users.size(); i++) {
            System.out.println((i + 1) + ". " + users.get(i) + " username=" + users.get(i).getUsername());
        }
        int index = readInt("User number: ") - 1;
        return index >= 0 && index < users.size() ? users.get(index) : null;
    }

    private static Student chooseStudent() {
        List<Student> students = database.getStudents();
        for (int i = 0; i < students.size(); i++) {
            System.out.println((i + 1) + ". " + students.get(i));
        }
        int index = readInt("Student number: ") - 1;
        return index >= 0 && index < students.size() ? students.get(index) : null;
    }

    private static Teacher chooseTeacher() {
        List<Teacher> teachers = database.getUsers().stream()
                .filter(Teacher.class::isInstance)
                .map(Teacher.class::cast)
                .toList();
        for (int i = 0; i < teachers.size(); i++) {
            System.out.println((i + 1) + ". " + teachers.get(i));
        }
        int index = readInt("Teacher number: ") - 1;
        return index >= 0 && index < teachers.size() ? teachers.get(index) : null;
    }

    private static Employee chooseEmployee() {
        List<Employee> employees = database.getUsers().stream()
                .filter(Employee.class::isInstance)
                .map(Employee.class::cast)
                .toList();
        for (int i = 0; i < employees.size(); i++) {
            System.out.println((i + 1) + ". " + employees.get(i));
        }
        int index = readInt("Employee number: ") - 1;
        return index >= 0 && index < employees.size() ? employees.get(index) : null;
    }

    private static Dean chooseDean() {
        List<Dean> deans = database.getUsers().stream()
                .filter(Dean.class::isInstance)
                .map(Dean.class::cast)
                .toList();
        for (int i = 0; i < deans.size(); i++) {
            System.out.println((i + 1) + ". " + deans.get(i));
        }
        int index = readInt("Dean number: ") - 1;
        return index >= 0 && index < deans.size() ? deans.get(index) : null;
    }

    private static Request chooseRequest() {
        List<Request> requests = database.getRequests();
        for (int i = 0; i < requests.size(); i++) {
            System.out.println((i + 1) + ". " + requests.get(i));
        }
        int index = readInt("Request number: ") - 1;
        return index >= 0 && index < requests.size() ? requests.get(index) : null;
    }

    private static CourseType readCourseType() {
        System.out.println("1. MAJOR");
        System.out.println("2. MINOR");
        System.out.println("3. FREE_ELECTIVE");
        int choice = readInt("Course type: ");
        if (choice == 2) {
            return CourseType.MINOR;
        }
        if (choice == 3) {
            return CourseType.FREE_ELECTIVE;
        }
        return CourseType.MAJOR;
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = SCANNER.nextLine().trim();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.println("Enter a number.");
            }
        }
    }

    private void saveBeforeExit() {
        try {
            DataManager.getInstance("university.dat").save(database);
            System.out.println("Data saved to university.dat");
        } catch (Exception e) {
            System.out.println("Could not save data: " + e.getMessage());
        }
    }
}
