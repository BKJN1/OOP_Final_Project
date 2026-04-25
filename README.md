# OOP Final Project

Research-oriented university system implemented in Java for the OOP final project.

## What is implemented

- Authentication through `AuthService`.
- User hierarchy: `User`, `Employee`, `Admin`, `Manager`, `Teacher`, `Dean`, `TechSupportSpecialist`, `Student`, `GraduateStudent`.
- Academic models: `Course`, `Lesson`, `Mark`, `Transcript`, `Schedule`, `Enrollment`.
- Course registration with 21-credit limit and fail-count validation.
- Teacher workflow: view courses/students, put marks, send complaints to dean.
- Manager workflow: add courses, assign teachers, publish news, generate academic reports.
- Admin workflow: add/remove users and view logs.
- Tech support workflow: view new requests and change request statuses.
- Research workflow: `Researcher`, `ResearchPaper`, `ResearchProject`, `Journal`, h-index calculation, sorted paper printing, citation generation in plain text and BibTeX.
- Graduate student supervisor validation with custom exception when supervisor h-index is below 3.
- News with comments and pinned Research news.
- Journal subscription notifications using Observer pattern.
- Serialization through `DataManager`.
- Console demo in `Main.java`.

## Design patterns

- Singleton: `DataManager`.
- Observer: `Journal`, `Subject`, `Observer`, `Notification`.
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

The demo creates sample users, courses, marks, research papers, news, requests, reports, and checks serialization by writing and reading `university.dat`.

## Notes for defense

This implementation is a compact but complete console demo. It is designed to show the required OOP structure and core functionality, not a full production LMS.
