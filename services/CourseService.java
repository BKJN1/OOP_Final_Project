package services;

import academic.Course;
import academic.Enrollment;
import exceptions.CreditLimitExceededException;
import exceptions.TooManyFailsException;
import storage.Database;
import system.Log;
import users.Student;

public class CourseService {
    private final Database database;

    public CourseService(Database database) {
        this.database = database;
    }

    public Enrollment register(Student student, Course course) throws CreditLimitExceededException, TooManyFailsException {
        student.registerForCourse(course);
        Enrollment enrollment = new Enrollment(student, course);
        enrollment.approve();
        database.addLog(new Log(student, "Registered for " + course.getCode()));
        return enrollment;
    }
}
