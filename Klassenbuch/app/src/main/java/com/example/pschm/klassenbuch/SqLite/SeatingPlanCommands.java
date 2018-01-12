package com.example.pschm.klassenbuch.SqLite;

import android.content.Context;
import android.util.Log;

import com.example.pschm.klassenbuch.NewClass;

/**
 * Created by pschm on 26.12.2017.
 */

public class SeatingPlanCommands {
    public static final String LOG_TAG_SeatingPlan = SeatingPlan.class.getSimpleName();
    private static SeatingPlanDataSource seatingPlanDataSource;
    private static SeatingPlanCommands spc = new SeatingPlanCommands();

    public static void InsertSeatingPlan(Context c, long roomNumber, long studentID, long unitID, long teacherID, int posX, int posY) {
        spc.seatingPlanDataSource = new SeatingPlanDataSource(c);

        Log.d(LOG_TAG_SeatingPlan, "Die Datenquelle wird geöffnet.");
        spc.seatingPlanDataSource.open();

        SeatingPlan seatingPlan = spc.seatingPlanDataSource.createSeatingPlan(roomNumber, studentID, unitID, teacherID, posY, posX);

        Log.d(LOG_TAG_SeatingPlan, "Es wurde der folgende Eintrag in die Datenbank geschrieben:");
        Log.d(LOG_TAG_SeatingPlan, "Inhalt: " + seatingPlan.toString());

        Log.d(NewClass.class.getSimpleName(), "roomnumber: " + roomNumber + " StudentID:"+ studentID+ " unitID:" +unitID+ " TeacherID:" + teacherID + " X: " + posX + " Y:" + posY + " B:");

        Log.d(LOG_TAG_SeatingPlan, "Die Datenquelle wird geschlossen.");
        spc.seatingPlanDataSource.close();
    }
}
