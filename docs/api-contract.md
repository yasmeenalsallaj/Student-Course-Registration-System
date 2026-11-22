# API Contract

This file records what the three members agreed on, so our code fits together.
Update it whenever something changes.

## 1. Decisions to agree on

| # | Decision | Agreed value |
|---|----------|--------------|
| 1 | MAX_CREDITS (maximum credit hours per student) | ___ |
| 2 | How prerequisites are checked | ___ (A: Student has completedCourseCodes, or B: student is currently registered in the prerequisite course) |
| 3 | When findById / findByCode finds nothing | ___ (return null, or throw RegistrationException) |
| 4 | Where student data is stored | In memory |

## 2. Proposed constructors (change if we agree on something else)

- Student(String id, String name, String email)
- Course(String code, String title, int credits, int capacity, Schedule schedule)
- Schedule(DayOfWeek day, LocalTime start, LocalTime end)
- StudentRepository()
- CourseRepository()
- RegistrationService(StudentRepository students, CourseRepository courses)
- AdminMenu(StudentRepository students, CourseRepository courses, RegistrationService service)
- StudentMenu(StudentRepository students, CourseRepository courses, RegistrationService service)

## 3. Rules

- Java version: 17 or higher.
- Business rules (seats, duplicates, credit limit, prerequisites, time conflicts) live only in RegistrationService.
- Menus only read input and print messages. They catch RegistrationException and print e.getMessage().
- All input is read through ui.InputUtil.
- Nobody edits a file owned by another member. Ask in an issue instead.

## 4. File owners

- Member 1: model/, exception/, repository/
- Member 2: service/RegistrationService.java, ui/AdminMenu.java
- Member 3: ui/StudentMenu.java, ui/InputUtil.java, Main.java, README.md
