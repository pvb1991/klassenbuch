package com.example.pschm.klassenbuch.SqLite;


import android.content.Context;
import android.util.Log;


/**
 * Created by pschm on 16.12.2017.
 */

public class StudentCommands {
    public static final String LOG_TAG = Student.class.getSimpleName();
    private StudentDataSource studentDataSource;

    public void InsertStudent(Context c, String vorname, String name, String street, String housenumber, int zipcode, String locus) {
        studentDataSource = new StudentDataSource(c);

        Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
        studentDataSource.open();

        Student student = studentDataSource.createStudent(vorname, name, street, housenumber, zipcode, locus);
        Log.d(LOG_TAG, "Es wurde der folgende Eintrag in die Datenbank geschrieben:");
        Log.d(LOG_TAG, "ID: " + student.getID() + ", Inhalt: " + student.toString());

                Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
        studentDataSource.close();
    }
}
