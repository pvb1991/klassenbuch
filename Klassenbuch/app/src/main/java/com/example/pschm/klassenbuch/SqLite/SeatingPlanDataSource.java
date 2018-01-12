package com.example.pschm.klassenbuch.SqLite;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pschm on 18.12.2017.
 */

public class SeatingPlanDataSource {
    private long roomnumber;
    private long studentID;
    private long unitId;
    private long teacherID;
    private int posY;
    private int posX;
    private String[] columns = {
            KlassenbuchDbHelper.RoomRoomnumber,
            KlassenbuchDbHelper.StudentID,
            KlassenbuchDbHelper.UnitID,
            KlassenbuchDbHelper.TeacherID,
            KlassenbuchDbHelper.SeatingPlanPosY,
            KlassenbuchDbHelper.SeatingPlanPosX
    };

    private static final String LOG_TAG = SeatingPlanDataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private KlassenbuchDbHelper dbHelper;

    public SeatingPlanDataSource(Context context) {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new KlassenbuchDbHelper(context);
    }

    public void open() {
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    public void close() {
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }

    public SeatingPlan createSeatingPlan(long roomnumber, long studentID, long unitId, long teacherID, int posY, int posX) {

        ContentValues values = new ContentValues();
        values.put(KlassenbuchDbHelper.RoomRoomnumber, roomnumber);
        values.put(KlassenbuchDbHelper.StudentID, studentID);
        values.put(KlassenbuchDbHelper.UnitID, unitId);
        values.put(KlassenbuchDbHelper.TeacherID, teacherID);
        values.put(KlassenbuchDbHelper.SeatingPlanPosY, posY);
        values.put(KlassenbuchDbHelper.SeatingPlanPosX, posX);

        database.insert(KlassenbuchDbHelper.TableSeatingPlan, null, values);

        Cursor cursor = database.query(KlassenbuchDbHelper.TableSeatingPlan,
                columns, KlassenbuchDbHelper.RoomRoomnumber + "=? AND " + KlassenbuchDbHelper.StudentID + "=? AND " + KlassenbuchDbHelper.TeacherID + "=?",
                new String[]{String.valueOf(roomnumber), String.valueOf(studentID), String.valueOf(teacherID)}, null, null, null);

        cursor.moveToFirst();
        SeatingPlan seatingPlan = cursorToSeatingPlan(cursor);
        cursor.close();

        return seatingPlan;
    }

    private SeatingPlan cursorToSeatingPlan(Cursor cursor) {
        int idRoomnumber = cursor.getColumnIndex(KlassenbuchDbHelper.RoomRoomnumber);
        int idStudent = cursor.getColumnIndex(KlassenbuchDbHelper.StudentID);
        int idUnitID = cursor.getColumnIndex(KlassenbuchDbHelper.UnitID);
        int idTeacherID = cursor.getColumnIndex(KlassenbuchDbHelper.TeacherID);
        int idPosY = cursor.getColumnIndex(KlassenbuchDbHelper.SeatingPlanPosY);
        int idPosX = cursor.getColumnIndex(KlassenbuchDbHelper.SeatingPlanPosX);

        long roomnumber = cursor.getLong(idRoomnumber);
        long student = cursor.getLong(idStudent);
        long unitID = cursor.getLong(idUnitID);
        long teacherID = cursor.getLong(idTeacherID);
        int posY = cursor.getInt(idPosY);
        int posX = cursor.getInt(idPosX);

        SeatingPlan seatingPlan = new SeatingPlan(roomnumber, student, unitID, teacherID, posY, posX);

        return seatingPlan;
    }

    public List<SeatingPlan> getAllSeatingPlan() {
        Log.d(LOG_TAG, "gettAllSeatingPlan");
        List<SeatingPlan> SeatingPlanList = new ArrayList<>();

        Cursor cursor = database.query(KlassenbuchDbHelper.TableSeatingPlan, columns, null, null, null, null, null);

        cursor.moveToFirst();
        SeatingPlan seatingPlan;

        while(!cursor.isAfterLast()) {
            seatingPlan = cursorToSeatingPlan(cursor);
            SeatingPlanList.add(seatingPlan);
            Log.d(LOG_TAG,  "Inhalt: " + seatingPlan.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return SeatingPlanList;
    }

    public static List<SeatingPlan> getStaticCheckSeatingPlan(Context c, String roomnumber, String unitID, String teacherID) {
        SeatingPlanDataSource obj = new SeatingPlanDataSource(c);
        obj.open();
        Log.d(LOG_TAG, "gettAllRoomsInFloor");
        List<SeatingPlan> seatingPlanList = new ArrayList<>();

        Cursor cursor = obj.database.query(KlassenbuchDbHelper.TableSeatingPlan, obj.columns,
                KlassenbuchDbHelper.RoomRoomnumber + "=? AND " + KlassenbuchDbHelper.UnitID + "=? AND " + KlassenbuchDbHelper.TeacherID + "=?"
                , new String[]{roomnumber, unitID, teacherID}, null, null, null);

        cursor.moveToFirst();
        SeatingPlan seatingPlan;

        while(!cursor.isAfterLast()) {
            seatingPlan = obj.cursorToSeatingPlan(cursor);
            seatingPlanList.add(seatingPlan);
            Log.d(LOG_TAG, "Roomnumber: " + seatingPlan.getRoomnumber());
            cursor.moveToNext();
        }
        cursor.close();


        obj.close();

        return seatingPlanList;
    }

    public static List<SeatingPlan> getStaticSeatingPlanList(Context c, String unitID, String teacherID) {
        SeatingPlanDataSource obj = new SeatingPlanDataSource(c);
        obj.open();
        Log.d(LOG_TAG, "gettAllRoomsInFloor");
        List<SeatingPlan> seatingPlanList = new ArrayList<>();

        Cursor cursor = obj.database.query(KlassenbuchDbHelper.TableSeatingPlan, obj.columns,
                KlassenbuchDbHelper.UnitID + "=? AND " + KlassenbuchDbHelper.TeacherID + "=?"
                , new String[]{unitID, teacherID}, null, null, null);

        cursor.moveToFirst();
        SeatingPlan seatingPlan;

        while(!cursor.isAfterLast()) {
            seatingPlan = obj.cursorToSeatingPlan(cursor);
            seatingPlanList.add(seatingPlan);
            Log.d(LOG_TAG, "Roomnumber: " + seatingPlan.getRoomnumber());
            cursor.moveToNext();
        }
        cursor.close();


        obj.close();

        return seatingPlanList;
    }

    public static void deleteSeatingPlan(Context c, String roomNumber, String unitID, String teacherID)
    {
        SeatingPlanDataSource obj = new SeatingPlanDataSource(c);
        obj.open();
        Log.d(LOG_TAG, "Delete All Seatingplan: " );
        obj.database.delete(KlassenbuchDbHelper.TableSeatingPlan, KlassenbuchDbHelper.RoomRoomnumber + "=? AND " + KlassenbuchDbHelper.UnitID + "=? AND " + KlassenbuchDbHelper.TeacherID + "=?",
                new String[]{ roomNumber, unitID, teacherID});
        obj.close();
    }

    public static void createbaar(Context c){
        StudentDataSource tds = new StudentDataSource(c);
        tds.open();
        tds.createStudent("Donald", "Duck", "Wilhelminenhofstraße", "75", 12459, "Berlin");
        tds.createStudent("Micky", "Mouse", "Wilhelminenhofstraße", "75", 12459, "Berlin");
        tds.createStudent("Mini", "Mouse", "Wilhelminenhofstraße", "75", 12459, "Berlin");
        tds.createStudent("Gustav", "Gans", "Wilhelminenhofstraße", "75", 12459, "Berlin");
        tds.createStudent("Dagobert", "Duck", "Wilhelminenhofstraße", "75", 12459, "Berlin");
        tds.createStudent("Tick", "Duck", "Wilhelminenhofstraße", "75", 12459, "Berlin");
        tds.createStudent("Trick", "Duck", "Wilhelminenhofstraße", "75", 12459, "Berlin");
        tds.createStudent("Track", "Duck", "Wilhelminenhofstraße", "75", 12459, "Berlin");
        tds.createStudent("Gustav", "Gans", "Wilhelminenhofstraße", "75", 12459, "Berlin");
        tds.createStudent("Franz", "Gans", "Wilhelminenhofstraße", "75", 12459, "Berlin");
        tds.close();
    }

}
