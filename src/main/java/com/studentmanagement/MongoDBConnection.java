package com.studentmanagement;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {

    private static final String CONNECTION_STRING =
            "mongodb://localhost:27017";

    private static MongoClient mongoClient;

    public static MongoDatabase getDatabase() {

        if (mongoClient == null) {
            mongoClient = MongoClients.create(CONNECTION_STRING);
        }

        return mongoClient.getDatabase("StudentManagementDB");
    }

    public static void closeConnection() {

        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
        }
    }
}
