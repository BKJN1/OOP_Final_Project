package academic;

import enums.LessonType;
import java.io.Serializable;
import users.Teacher;

public class Lesson implements Serializable {
    private static final long serialVersionUID = 1L;

    private LessonType type;
    private String room;
    private String time;
    private Teacher instructor;

    public Lesson(LessonType type, String room, String time, Teacher instructor) {
        this.type = type;
        this.room = room;
        this.time = time;
        this.instructor = instructor;
    }

    public LessonType getType() {
        return type;
    }

    public Teacher getInstructor() {
        return instructor;
    }

    @Override
    public String toString() {
        return type + " at " + time + " in " + room + " by " + instructor.getFullName();
    }
}
