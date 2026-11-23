package com.studentmanagement;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class StudentService {

    private final MongoCollection<Document> collection;

    // Constructor: connects to the students collection
    public StudentService() {
        MongoDatabase database =
                MongoDBConnection.getDatabase();

        collection = database.getCollection("students");
    }

    // CREATE: Register a new student
    public void addStudent(Student student) {

        Document document = new Document("studentId",
                student.getStudentId())
                .append("firstName", student.getFirstName())
                .append("lastName", student.getLastName())
                .append("programme", student.getProgramme())
                .append("year", student.getYear())
                .append("email", student.getEmail());

        collection.insertOne(document);

        System.out.println("Student registered successfully!");
    }

    // READ: Display all students
    public List<Document> getAllStudents() {

        List<Document> students = new ArrayList<>();

        for (Document student : collection.find()) {
            students.add(student);
        }

        return students;
    }

    // SEARCH: Find one student by student ID
    public Document findStudent(String studentId) {

        return collection.find(
                Filters.eq("studentId", studentId)
        ).first();
    }

    // UPDATE: Change a student's academic year
    public void updateStudentYear(String studentId, int newYear) {

        collection.updateOne(
                Filters.eq("studentId", studentId),
                Updates.set("year", newYear)
        );

        System.out.println("Student update operation completed.");
    }

    // DELETE: Remove a student
    public void deleteStudent(String studentId) {

        collection.deleteOne(
                Filters.eq("studentId", studentId)
        );

        System.out.println("Student deletion operation completed.");
    }
}
