package com.example.pschm.klassenbuch.SqLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.pschm.klassenbuch.StartUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pschm on 16.12.2017.
 */

public class AttandanceDataSource {

    private String[] columns = {
            KlassenbuchDbHelper.UnitID,
            KlassenbuchDbHelper.StudentID,
            KlassenbuchDbHelper.AttendanceDate,
            KlassenbuchDbHelper.AttendanceDelay
    };

    private static final String LOG_TAG = Attendance.class.getSimpleName();

    private SQLiteDatabase database;
    private KlassenbuchDbHelper dbHelper;

    public AttandanceDataSource(Context context) {
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

    public Attendance createAttendance(long unitId, long studentID, long date, int delay) {

        ContentValues values = new ContentValues();
        values.put(KlassenbuchDbHelper.UnitID, unitId);
        values.put(KlassenbuchDbHelper.StudentID, studentID);
        values.put(KlassenbuchDbHelper.AttendanceDate, date);
        values.put(KlassenbuchDbHelper.AttendanceDelay, delay);

        database.insert(KlassenbuchDbHelper.TableAttendance, null, values);

        Cursor cursor = database.query(KlassenbuchDbHelper.TableAttendance,
                columns, KlassenbuchDbHelper.StudentID + "=? AND " + KlassenbuchDbHelper.UnitID + "=?",
                new String[]{String.valueOf(studentID), String.valueOf(unitId)}, null, null, null);

        cursor.moveToFirst();
        Attendance attendance = cursorToAttendance(cursor);
        cursor.close();

        return attendance;
    }

    public static void StaticCreateAttendance(Context c, String unitId, String studentID, long date, int delay){
        AttandanceDataSource ads = new AttandanceDataSource(c);

        ads.open();

        ads.createAttendance(Long.valueOf(unitId), Long.valueOf(studentID), date, delay);

        ads.close();
    }

    private Attendance cursorToAttendance(Cursor cursor) {
        int idUnitID = cursor.getColumnIndex(KlassenbuchDbHelper.UnitID);
        int idStudentID = cursor.getColumnIndex(KlassenbuchDbHelper.StudentID);
        int idDate = cursor.getColumnIndex(KlassenbuchDbHelper.AttendanceDate);
        int idDelay = cursor.getColumnIndex(KlassenbuchDbHelper.AttendanceDelay);

        long unitID = cursor.getLong(idUnitID);
        long studentID = cursor.getLong(idStudentID);
        int date = cursor.getInt(idDate);
        int delay = cursor.getInt(idDelay);

        Attendance attendance = new Attendance(unitID, studentID, date, delay);

        return attendance;
    }

    public List<Attendance> getAllAttendance() {
        Log.d(LOG_TAG, "gettAllAttendance");
        List<Attendance> AttendanceList = new ArrayList<>();

        Cursor cursor = database.query(KlassenbuchDbHelper.TableAttendance, columns, null, null, null, null, null);

        cursor.moveToFirst();
        Attendance attendance;

        while(!cursor.isAfterLast()) {
            attendance = cursorToAttendance(cursor);
            AttendanceList.add(attendance);
            Log.d(LOG_TAG,  "Inhalt: " + attendance.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return AttendanceList;
    }

    public static int CountDelay(Context c, String studentID, String unitID){
        AttandanceDataSource ads = new AttandanceDataSource(c);
        int count = 0;
        String select = "SELECT COUNT(*) FROM " + KlassenbuchDbHelper.TableAttendance +
                " WHERE " + KlassenbuchDbHelper.StudentID + "=?  AND " +
                KlassenbuchDbHelper.UnitID + "=?;";

        ads.open();
        Cursor cursor = null;

        try{
            cursor= ads.database.rawQuery(select, new String[]{studentID, unitID});

            cursor.moveToFirst();
            int countID = cursor.getColumnIndex("COUNT(*)");
            count = cursor.getInt(countID);

            cursor.close();
        }
        catch (Exception ex) { Log.e(LOG_TAG, "Fehler: " + ex.getMessage()); }

        ads.close();
        Log.e(LOG_TAG, "count: " + count);
        return count;

    }

    public static int SumDelays(Context c, String studentID, String unitID){
        AttandanceDataSource ads = new AttandanceDataSource(c);
        int sum = 0;
        String select = "SELECT COUNT(*) FROM " + KlassenbuchDbHelper.TableAttendance +
                " WHERE " + KlassenbuchDbHelper.StudentID + "=?  AND " +
                KlassenbuchDbHelper.UnitID + "=? AND NOT " +
                KlassenbuchDbHelper.AttendanceDelay + "=? AND NOT " +
                KlassenbuchDbHelper.AttendanceDelay + "=?;";

        ads.open();
        Cursor cursor = null;

        try{
            cursor= ads.database.rawQuery(select, new String[]{studentID, unitID, String.valueOf(KlassenbuchDbHelper.AttendanceAbsent), String.valueOf(KlassenbuchDbHelper.AttendancePresent)});

            cursor.moveToFirst();
            int countID = cursor.getColumnIndex("COUNT(*)");
            sum = cursor.getInt(countID);

            cursor.close();
        }
        catch (Exception ex) { Log.e(LOG_TAG, "Fehler: " + ex.getMessage()); }

        ads.close();
        Log.e(LOG_TAG, "sum: " + sum);
        return sum;
    }

    public static int CountAttendance(Context c, String studentID, String unitID, String condition){
        AttandanceDataSource ads = new AttandanceDataSource(c);
        int sum = 0;
        String select = "SELECT COUNT(*) FROM " + KlassenbuchDbHelper.TableAttendance +
                " WHERE " + KlassenbuchDbHelper.StudentID + "=?  AND " +
                KlassenbuchDbHelper.UnitID + "=? AND " +
                KlassenbuchDbHelper.AttendanceDelay + "=?;";

        ads.open();
        Cursor cursor = null;

        try{
            cursor= ads.database.rawQuery(select, new String[]{studentID, unitID, condition});

            cursor.moveToFirst();
            int countID = cursor.getColumnIndex("COUNT(*)");
            sum = cursor.getInt(countID);

            cursor.close();
        }
        catch (Exception ex) { Log.e(LOG_TAG, "Fehler: " + ex.getMessage()); }

        ads.close();
        Log.e(LOG_TAG, "sum: " + sum);
        return sum;
    }

    public static void updateAttendance(Context c, String dalay, String milli, String unitID, String studentID){
        AttandanceDataSource ads = new AttandanceDataSource(c);

        String select = "UPDATE " + KlassenbuchDbHelper.TableAttendance +
                " SET " + KlassenbuchDbHelper.AttendanceDelay + "=" + dalay +
                " WHERE " + KlassenbuchDbHelper.AttendanceDate +"=? AND " +
                KlassenbuchDbHelper.UnitID + "=? AND " +
                KlassenbuchDbHelper.StudentID + "=? AND " +
                KlassenbuchDbHelper.AttendanceDelay + "=" + KlassenbuchDbHelper.AttendanceAbsent + ";";

        ads.open();
        Cursor cursor = null;

        try{
            cursor= ads.database.rawQuery(select, new String[]{milli, unitID, studentID});
            Attendance attendance;

            while(!cursor.isAfterLast()) {
                attendance = ads.cursorToAttendance(cursor);
                Log.d(LOG_TAG,  "Inhalt: " + attendance.toString());
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception ex) { Log.e(LOG_TAG, "Fehler: " + ex.getMessage()); }

        ads.close();
    }
}
