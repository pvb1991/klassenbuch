package com.example.pschm.klassenbuch.SqLite;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by pschm on 17.12.2017.
 */

public class MarkDataSource {
    private String[] columns = {
            KlassenbuchDbHelper.StudentID,
            KlassenbuchDbHelper.TeacherID,
            KlassenbuchDbHelper.UnitID,
            KlassenbuchDbHelper.MarkType,
            KlassenbuchDbHelper.MarkMark,
            KlassenbuchDbHelper.MarkNote
    };

    private static final String LOG_TAG = Mark.class.getSimpleName();

    private SQLiteDatabase database;
    private KlassenbuchDbHelper dbHelper;

    public MarkDataSource(Context context) {
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

    public Mark createMark(long studentID, long teacherID, long unitID, String type, int mark, String note) {
        ContentValues values = new ContentValues();
        values.put(KlassenbuchDbHelper.StudentID, studentID);
        values.put(KlassenbuchDbHelper.TeacherID, teacherID);
        values.put(KlassenbuchDbHelper.UnitID, unitID);
        values.put(KlassenbuchDbHelper.MarkType, type);
        values.put(KlassenbuchDbHelper.MarkMark, mark);
        values.put(KlassenbuchDbHelper.MarkNote, note);

        database.insert(KlassenbuchDbHelper.TableMark, null, values);

        Cursor cursor = database.query(KlassenbuchDbHelper.TableMark,
                columns, KlassenbuchDbHelper.StudentID + "=? AND " + KlassenbuchDbHelper.TeacherID + "=? AND "+ KlassenbuchDbHelper.UnitID + "=?",
                new String[]{String.valueOf(studentID), String.valueOf(teacherID), String.valueOf(unitID)}, null, null, null);

        cursor.moveToFirst();
        Mark mark1 = cursorToMark(cursor);
        cursor.close();

        return mark1;
    }

    public static void staticCreatMark(Context c, String studentID, String teacherID, String unitID, String type, int mark, String note){
        MarkDataSource mds = new MarkDataSource(c);
        mds.open();
        mds.createMark(Long.valueOf(studentID), Long.valueOf(teacherID), Long.valueOf(unitID), type, mark, note);
        mds.close();
    }

    private Mark cursorToMark(Cursor cursor) {
        int idStundentID = cursor.getColumnIndex(KlassenbuchDbHelper.StudentID);
        int idTeacherID = cursor.getColumnIndex(KlassenbuchDbHelper.TeacherID);
        int idUnit = cursor.getColumnIndex(KlassenbuchDbHelper.UnitID);
        int idType = cursor.getColumnIndex(KlassenbuchDbHelper.MarkType);
        int idMark = cursor.getColumnIndex(KlassenbuchDbHelper.MarkMark);
        int idNote = cursor.getColumnIndex(KlassenbuchDbHelper.MarkNote);

        long studentID = cursor.getLong(idStundentID);
        long teacherID = cursor.getLong(idTeacherID);
        long unitID = cursor.getLong(idUnit);
        String type = cursor.getString(idType);
        int mark = cursor.getInt(idMark);
        String note = cursor.getString(idNote);

        Mark markl = new Mark(studentID, teacherID, unitID, type, mark, note);

        return markl;
    }

    public List<Mark> getAllMark() {
        Log.d(LOG_TAG, "gettAllShoppingMemos");
        List<Mark> MarkList = new ArrayList<>();

        Cursor cursor = database.query(KlassenbuchDbHelper.TableMark, columns, null, null, null, null, null);

        cursor.moveToFirst();
        Mark mark;

        while(!cursor.isAfterLast()) {
            mark = cursorToMark(cursor);
            MarkList.add(mark);
            Log.d(LOG_TAG,", Inhalt: " + mark.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return MarkList;
    }

    public static int CountMarkType(Context c, String studentID, String unitID, String teacherID, String markTyp, String mark){
        MarkDataSource mds = new MarkDataSource(c);
        int count = 0;
        String select = "SELECT COUNT(*) FROM " + KlassenbuchDbHelper.TableMark +
                " WHERE " + KlassenbuchDbHelper.StudentID + "=?  AND " +
                KlassenbuchDbHelper.UnitID + "=? AND " +
                KlassenbuchDbHelper.TeacherID + "=? AND " +
                KlassenbuchDbHelper.MarkType + "=? AND " +
                KlassenbuchDbHelper.MarkMark + "=?;";

        mds.open();
        Cursor cursor = null;

        try{
            cursor= mds.database.rawQuery(select, new String[]{studentID, unitID, teacherID, markTyp, mark});

            cursor.moveToFirst();
            int countID = cursor.getColumnIndex("COUNT(*)");
            count = cursor.getInt(countID);

            cursor.close();
        }
        catch (Exception ex) { Log.e(LOG_TAG, "Fehler: " + ex.getMessage()); }

        mds.close();
        Log.e(LOG_TAG, "count: " + count);
        return count;

    }

    public static List<Mark> getExams(Context c, String studentID, String unitID, String teacherID, String markTyp){
        MarkDataSource mds = new MarkDataSource(c);
        List<Mark> MarkList = new ArrayList<>();
        String select = "SELECT * FROM " + KlassenbuchDbHelper.TableMark +
                " WHERE " + KlassenbuchDbHelper.StudentID + "=?  AND " +
                KlassenbuchDbHelper.UnitID + "=? AND " +
                KlassenbuchDbHelper.TeacherID + "=? AND " +
                KlassenbuchDbHelper.MarkType + "=?;";

        mds.open();
        Cursor cursor = null;

        try{
            cursor= mds.database.rawQuery(select, new String[]{studentID, unitID, teacherID, markTyp});

            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                Mark mark = mds.cursorToMark(cursor);
                MarkList.add(mark);
                Log.d(LOG_TAG,", Inhalt: " + mark.toString());
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception ex) { Log.e(LOG_TAG, "Fehler: " + ex.getMessage()); }

        mds.close();

        return MarkList;

    }
}
