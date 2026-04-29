# OOP Final Project

Research-oriented university system implemented in Java for the OOP final project.

## What is implemented

- Authentication through `AuthService`, including regex search authentication.
- User hierarchy: `User`, `Employee`, `Admin`, `Manager`, `Teacher`, `Dean`, `TechSupportSpecialist`, `Student`, `GraduateStudent`, `ResearchEmployee`.
- Academic models: `Course`, `Lesson`, `Mark`, `Transcript`, `Schedule`, `Enrollment`.
- Course registration with 21-credit limit and fail-count validation.
- Teacher workflow: view courses/students, put marks, send complaints to dean.
- Manager workflow: add courses, assign teachers, publish news, generate academic reports.
- Admin workflow: add/remove users and view logs.
- Tech support workflow: view new requests and change request statuses.
- Research workflow: `Researcher`, `ResearchPaper`, `ResearchProject`, `Journal`, h-index calculation, sorted paper printing, citation generation in plain text and BibTeX.
- Graduate student supervisor validation with custom exception when supervisor h-index is below 3.
- News with comments and pinned Research news.
- Journal subscription notifications using Observer pattern; all users can subscribe.
- Serialization through `DataManager`; saved data is loaded on startup when `university.dat` exists.
- Console localization between `KZ`, `EN`, and `RU` for menus, prompts, and main system messages.
- Console demo in `Main.java`.

## Design patterns

- Singleton: `DataManager`.
- Observer: `Journal`, `Subject`, `Observer`, `Notification`.
- Factory: `UserFactory`.
- Service layer: `AuthService`, `CourseService`, `NewsService`, `ReportService`, `ResearchService`.
- Strategy-style sorting: `Comparator<ResearchPaper>` for sorting by date, citations, and length.

## Requirements coverage

The project uses inheritance, abstract classes, interfaces, enumerations, collections, custom exceptions, serialization, `Comparable`, `Comparator`, `equals`, `hashCode`, and `toString`.

Important required classes are present:

- `User`, `Employee`, `Teacher`, `Manager`, `Student`, `GraduateStudent`, `Admin`
- `Course`, `Mark`, `Lesson`
- `TechSupportSpecialist`
- `Researcher`, `ResearchPaper`, `ResearchProject`
- `News`, `Message`

## How to run

Compile:

```bash
javac Main.java users/*.java academic/*.java enums/*.java exceptions/*.java interfaces/*.java research/*.java services/*.java storage/*.java system/*.java
```

Run:

```bash
java Main
```

The program starts with the main menu:

```text
1. Login
2. Search (regular expressions)
3. Exit
4. Switch language
```

After login, the program opens a role-specific menu. For example, manager users see manager actions, students see student actions, teachers see teacher actions, and so on.

Recent menu features:

- Teachers can use `Manage course` to view course details, update title/credits/type, add lessons, and view lessons.
- Students can use `Student organizations` to join an organization as a member or become its head.
- Managers can use `Approve students registration` to approve pending course registration requests.
- Managers can view employee requests and teachers sorted alphabetically.
- Users can subscribe/unsubscribe from university journals.
- Users can add and view news comments.
- Researchers can view top cited researchers by university, school, and year.

Sample accounts:

| Role | Username | Password |
| --- | --- | --- |
| Admin | `admin` | `1234` |
| Manager | `manager` | `1234` |
| Dean | `dean` | `1234` |
| Professor | `prof` | `1234` |
| Teacher | `lector` | `1234` |
| Student | `student` | `1234` |
| Graduate student | `grad` | `1234` |
| Tech support | `support` | `1234` |
| Research employee | `lab` | `1234` |

The regex search asks for authentication and checks users, courses, news, and research papers. Example regex queries:

```text
Kim
CSCI.*
Research|Analytics
student
```

On exit, the application saves current data to `university.dat`.

## Notes for defense

This implementation is a compact but complete console demo. It is designed to show the required OOP structure and core functionality, not a full production LMS.
