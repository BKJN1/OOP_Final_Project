package academic;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import users.Student;

public class Transcript implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Student student;
    private final Map<Course, Mark> marks;

    public Transcript(Student student, Map<Course, Mark> marks) {
        this.student = student;
        this.marks = new HashMap<>(marks);
    }

    public double getGpa() {
        if (marks.isEmpty()) {
            return 0.0;
        }
        double weighted = 0;
        int credits = 0;
        for (Map.Entry<Course, Mark> entry : marks.entrySet()) {
            weighted += entry.getValue().getGpaPoint() * entry.getKey().getCredits();
            credits += entry.getKey().getCredits();
        }
        return credits == 0 ? 0.0 : weighted / credits;
    }

    @Override
    public String toString() {
        return "Transcript of " + student.getFullName() + ", GPA=" + String.format("%.2f", getGpa()) + ", marks=" + marks;
    }
}
