package academic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Schedule implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Lesson> lessons = new ArrayList<>();

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    public List<Lesson> getLessons() {
        return new ArrayList<>(lessons);
    }

    @Override
    public String toString() {
        return lessons.toString();
    }
}
