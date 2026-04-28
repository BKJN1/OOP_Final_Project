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
        Enrollment enrollment = new Enrollment(student, course);
        database.addEnrollment(enrollment);
        database.addLog(new Log(student, "Requested registration for " + course.getCode()));
        return enrollment;
    }

    public void approveEnrollment(Enrollment enrollment) throws CreditLimitExceededException, TooManyFailsException {
        enrollment.getStudent().registerForCourse(enrollment.getCourse());
        enrollment.approve();
        database.addLog(new Log(enrollment.getStudent(), "Registration approved for " + enrollment.getCourse().getCode()));
    }
}
