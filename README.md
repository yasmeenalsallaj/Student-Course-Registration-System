# Student Course Registration System

A console-based Java application that lets an administrator manage students and courses, and lets students browse, register in, and drop courses. Built as a group project for the Software Engineering course (Part 4: Implementation and Version Control).

## Team

- Tala Abuzaid
- Yasmeen Alsallaj
- Haya Abu Hazeem

## Features

**Admin**
- Add a student (unique ID, name, email)
- Add, edit, and delete courses (a course can be deleted only if no students are registered in it)
- View a course roster
- View all students and all courses

**Student**
- View available courses (only courses with free seats)
- Search courses by code or title
- Register in a course
- Drop a course
- View registered courses and total credit hours

**Registration rules** (all checked in `RegistrationService`, in this order):
1. The course has a free seat
2. The student is not already registered in it
3. The credit limit is not exceeded (maximum 18 credits)
4. The student has completed all prerequisites
5. There is no time conflict with the student's other courses

If any check fails, a clear error message is shown and nothing is saved.

## Requirements

- Java 17 or higher

## How to Compile and Run

From the project root folder.

**Linux / macOS**
```bash
javac -d out $(find src -name "*.java")
java -cp out Main
```

**Windows (Command Prompt)**
```bat
dir /s /b src\*.java > sources.txt
javac -d out @sources.txt
java -cp out Main
```

## Sample Data

The program starts with sample data so every feature can be tried immediately.

- Students: `S1` Lina
