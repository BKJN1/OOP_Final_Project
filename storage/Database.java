package storage;

import academic.Course;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import research.Journal;
import research.Researcher;
import system.Log;
import system.News;
import system.Request;
import users.Student;
import users.User;

public class Database implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<User> users = new ArrayList<>();
    private final List<Course> courses = new ArrayList<>();
    private final List<News> news = new ArrayList<>();
    private final List<Request> requests = new ArrayList<>();
    private final List<Log> logs = new ArrayList<>();
    private final List<Journal> journals = new ArrayList<>();

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(String id) {
        users.removeIf(u -> u.getId().equals(id));
    }

    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    public User findByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public List<Course> getCourses() {
        return courses.stream().sorted().toList();
    }

    public List<Student> getStudents() {
        return users.stream()
                .filter(Student.class::isInstance)
                .map(Student.class::cast)
                .toList();
    }

    public List<Researcher> getResearchers() {
        return users.stream()
                .filter(u -> u instanceof interfaces.CanResearch)
                .map(u -> ((interfaces.CanResearch) u).getResearchProfile())
                .filter(r -> r != null)
                .toList();
    }

    public Researcher getTopCitedResearcher() {
        return getResearchers().stream()
                .max(Comparator.comparingInt(r -> r.getPapers().stream().mapToInt(p -> p.getCitations()).sum()))
                .orElse(null);
    }

    public void addNews(News item) {
        news.add(item);
    }

    public List<News> getNews() {
        return news.stream().sorted().toList();
    }

    public void addRequest(Request request) {
        requests.add(request);
    }

    public List<Request> getRequests() {
        return new ArrayList<>(requests);
    }

    public void addLog(Log log) {
        logs.add(log);
    }

    public List<Log> getLogs() {
        return new ArrayList<>(logs);
    }

    public void addJournal(Journal journal) {
        journals.add(journal);
    }

    public List<Journal> getJournals() {
        return new ArrayList<>(journals);
    }
}
