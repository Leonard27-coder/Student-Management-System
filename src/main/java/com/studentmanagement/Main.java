package com.studentmanagement;

import org.bson.Document;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        StudentService studentService = new StudentService();

        try {
            System.out.println("====================================");
            System.out.println("   STUDENT MANAGEMENT SYSTEM");
            System.out.println("====================================");

            boolean running = true;

            while (running) {

                System.out.println("\n========== MAIN MENU ==========");
                System.out.println("1. Register Student");
                System.out.println("2. View All Students");
                System.out.println("3. Search Student");
                System.out.println("4. Update Student Year");
                System.out.println("5. Delete Student");
                System.out.println("6. Exit");
                System.out.print("Choose an option: ");

                String choice = scanner.nextLine();

                switch (choice) {

                    case "1":
                        registerStudent(studentService);
                        break;

                    case "2":
                        viewStudents(studentService);
                        break;

                    case "3":
                        searchStudent(studentService);
                        break;

                    case "4":
                        updateStudent(studentService);
                        break;

                    case "5":
                        deleteStudent(studentService);
                        break;

                    case "6":
                        running = false;
                        System.out.println("Thank you for using the system!");
                        break;

                    default:
                        System.out.println("Invalid option. Try again.");
                }
            }

        } catch (Exception e) {
            System.out.println("Application error: " + e.getMessage());

        } finally {
            MongoDBConnection.closeConnection();
            scanner.close();
        }
    }

    // Register a student
    private static void registerStudent(StudentService service) {

        System.out.println("\n--- REGISTER STUDENT ---");

        System.out.print("Student ID: ");
        String id = scanner.nextLine();

        System.out.print("First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Programme: ");
        String programme = scanner.nextLine();

        System.out.print("Year of Study: ");
        int year = Integer.parseInt(scanner.nextLine());

        System.out.print("Email: ");
        String email = scanner.nextLine();

        Student student = new Student(
                id, firstName, lastName,
                programme, year, email
        );

        service.addStudent(student);
    }

    // View all students
    private static void viewStudents(StudentService service) {

        System.out.println("\n--- ALL STUDENTS ---");

        List<Document> students = service.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        for (Document student : students) {
            System.out.println("----------------------------");
            System.out.println("Student ID: " + student.getString("studentId"));
            System.out.println("Name: " + student.getString("firstName")
                    + " " + student.getString("lastName"));
            System.out.println("Programme: " + student.getString("programme"));
            System.out.println("Year: " + student.getInteger("year"));
            System.out.println("Email: " + student.getString("email"));
        }
    }

    // Search for a student
    private static void searchStudent(StudentService service) {

        System.out.print("\nEnter Student ID: ");
        String id = scanner.nextLine();

        Document student = service.findStudent(id);

        if (student == null) {
            System.out.println("Student not found.");
        } else {
            System.out.println("Student found:");
            System.out.println(student.toJson());
        }
    }

    // Update student year
    private static void updateStudent(StudentService service) {

        System.out.print("\nEnter Student ID: ");
        String id = scanner.nextLine();

        Document student = service.findStudent(id);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.print("Enter new academic year: ");
        int year = Integer.parseInt(scanner.nextLine());

        service.updateStudentYear(id, year);
    }

    // Delete student
    private static void deleteStudent(StudentService service) {

        System.out.print("\nEnter Student ID: ");
        String id = scanner.nextLine();

        Document student = service.findStudent(id);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        service.deleteStudent(id);
    }
}
