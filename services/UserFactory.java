package services;

import enums.ManagerType;
import enums.TeacherPosition;
import users.Admin;
import users.Dean;
import users.GraduateStudent;
import users.Manager;
import users.ResearchEmployee;
import users.Student;
import users.Teacher;
import users.TechSupportSpecialist;
import users.User;

public final class UserFactory {
    private UserFactory() {
    }

    public static User create(String role, String id, String username, String password,
                              String fullName, String email, String departmentOrMajor) {
        switch (role.toUpperCase()) {
            case "ADMIN":
                return new Admin(id, username, password, fullName, email);
            case "MANAGER":
                return new Manager(id, username, password, fullName, email, departmentOrMajor, ManagerType.OR);
            case "DEAN":
                return new Dean(id, username, password, fullName, email, departmentOrMajor);
            case "PROFESSOR":
                return new Teacher(id, username, password, fullName, email, departmentOrMajor, TeacherPosition.PROFESSOR);
            case "LECTOR":
                return new Teacher(id, username, password, fullName, email, departmentOrMajor, TeacherPosition.LECTOR);
            case "GRADUATE_STUDENT":
                return new GraduateStudent(id, username, password, fullName, email, departmentOrMajor, 1);
            case "STUDENT":
                return new Student(id, username, password, fullName, email, departmentOrMajor, 1);
            case "SUPPORT":
                return new TechSupportSpecialist(id, username, password, fullName, email, departmentOrMajor);
            case "RESEARCH_EMPLOYEE":
                return new ResearchEmployee(id, username, password, fullName, email, departmentOrMajor);
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
}
