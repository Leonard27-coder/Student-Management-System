package com.studentmanagement;

public class Student {

    private String studentId;
    private String firstName;
    private String lastName;
    private String programme;
    private int year;
    private String email;

    // Constructor
    public Student(String studentId, String firstName,
                   String lastName, String programme,
                   int year, String email) {

        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.programme = programme;
        this.year = year;
        this.email = email;
    }

    // Getters
    public String getStudentId() {
        return studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProgramme() {
        return programme;
    }

    public int getYear() {
        return year;
    }

    public String getEmail() {
        return email;
    }
}
