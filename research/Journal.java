package research;

import interfaces.Observer;
import interfaces.Subject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import system.Notification;

public class Journal implements Subject, Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final List<ResearchPaper> papers = new ArrayList<>();
    private final List<Observer> subscribers = new ArrayList<>();

    public Journal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " papers=" + papers.size() + " subscribers=" + subscribers.size();
    }

    public void publish(ResearchPaper paper) {
        papers.add(paper);
        notifyObservers("New paper in " + name + ": " + paper.getTitle());
    }

    @Override
    public void subscribe(Observer observer) {
        if (!subscribers.contains(observer)) {
            subscribers.add(observer);
        }
    }

    @Override
    public void unsubscribe(Observer observer) {
        subscribers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        Notification notification = new Notification(message);
        subscribers.forEach(s -> s.update(notification));
    }
}
