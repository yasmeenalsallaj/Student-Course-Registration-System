# API Contract

This file records what the three members agreed on, so our code fits together.
Update it whenever something changes.

## 1. Decisions to agree on

| # | Decision | Agreed value |
|---|----------|--------------|
| 1 | MAX_CREDITS (maximum credit hours per student) | 18 |
| 2 | How prerequisites are checked | A: the student must have completed the prerequisite (Student.completedCourseCodes) |
| 3 | When findById / findByCode finds nothing | return null (the menu prints a "not found" message) |
| 4 | Where student data is stored | In memory |

## 2. Proposed constructors (change if we agree on something else)

- Student(String id, String name, String email)
- Course(String code, String title, int credits, int capacity, Schedule schedule)
- Schedule(String day, String start, String end)  // example: "SUN", "10:00", "11:30"
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

## 5. Eligibility check order

RegistrationService.checkEligibility runs the checks in this order and stops at the first failure:

1. Free seats
2. Already registered
3. Credit limit (MAX_CREDITS = 18)
4. Prerequisites
5. Time conflict
