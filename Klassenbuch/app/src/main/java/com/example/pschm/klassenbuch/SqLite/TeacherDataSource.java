package com.example.pschm.klassenbuch.SqLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.pschm.klassenbuch.admin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pschm on 18.12.2017.
 */

public class TeacherDataSource {
    private String[] columns = {
            KlassenbuchDbHelper.TeacherID,
            KlassenbuchDbHelper.TeacherVorname,
            KlassenbuchDbHelper.TeacherName,
            KlassenbuchDbHelper.TeacherStreet,
            KlassenbuchDbHelper.TeacherHousenumber,
            KlassenbuchDbHelper.TeacherZIP,
            KlassenbuchDbHelper.TeacherLocus,
            KlassenbuchDbHelper.TeacherEmail,
            KlassenbuchDbHelper.TeacherAdmin,
            KlassenbuchDbHelper.TeacherPassword
    };

    private static final String LOG_TAG = Teacher.class.getSimpleName();

    private SQLiteDatabase database;
    private KlassenbuchDbHelper dbHelper;

    public TeacherDataSource(Context context) {
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

    public Teacher createTeacher(String vorname, String name, String street, String housenumber, int zipcode, String locus, String email, String password, int admin) {
        ContentValues values = new ContentValues();
        values.put(KlassenbuchDbHelper.TeacherVorname, vorname);
        values.put(KlassenbuchDbHelper.TeacherName, name);
        values.put(KlassenbuchDbHelper.TeacherStreet,street);
        values.put(KlassenbuchDbHelper.TeacherHousenumber, housenumber);
        values.put(KlassenbuchDbHelper.TeacherZIP, zipcode);
        values.put(KlassenbuchDbHelper.TeacherLocus,locus);
        values.put(KlassenbuchDbHelper.TeacherEmail, email);
        values.put(KlassenbuchDbHelper.TeacherPassword, password);
        values.put(KlassenbuchDbHelper.TeacherAdmin, admin);

        long insertId = database.insert(KlassenbuchDbHelper.TableTeacher, null, values);

        Cursor cursor = database.query(KlassenbuchDbHelper.TableTeacher,
                columns, KlassenbuchDbHelper.TeacherID + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Teacher teacher = cursorToTeacher(cursor);
        cursor.close();

        return teacher;
    }



    private Teacher cursorToTeacher(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(KlassenbuchDbHelper.TeacherID);
        int idVorname = cursor.getColumnIndex(KlassenbuchDbHelper.TeacherVorname);
        int idName = cursor.getColumnIndex(KlassenbuchDbHelper.TeacherName);
        int idStreet = cursor.getColumnIndex(KlassenbuchDbHelper.TeacherStreet);
        int idHousenumber = cursor.getColumnIndex(KlassenbuchDbHelper.TeacherHousenumber);
        int idZip = cursor.getColumnIndex(KlassenbuchDbHelper.TeacherZIP);
        int idLocus = cursor.getColumnIndex(KlassenbuchDbHelper.TeacherLocus);
        int idEmail = cursor.getColumnIndex(KlassenbuchDbHelper.TeacherEmail);
        int idPasswort = cursor.getColumnIndex(KlassenbuchDbHelper.TeacherPassword);
        int idAdmin = cursor.getColumnIndex(KlassenbuchDbHelper.TeacherAdmin);

        String vorname = cursor.getString(idVorname);
        String name = cursor.getString(idName);
        String street = cursor.getString(idStreet);
        String housenumber = cursor.getString(idHousenumber);
        int zipcode = cursor.getInt(idZip);
        String locus = cursor.getString(idLocus);
        long id = cursor.getLong(idIndex);
        String email = cursor.getString(idEmail);
        String password = cursor.getString(idPasswort);
        int admin = cursor.getInt(idAdmin);

        Teacher teacher = new Teacher(vorname, name, street, housenumber, zipcode, locus, email, password, admin, id);

        return teacher;
    }

    public List<Teacher> getAllTeacher() {
        Log.d(LOG_TAG, "gettAllTeacher");
        List<Teacher> TeacherList = new ArrayList<>();

        Cursor cursor = database.query(KlassenbuchDbHelper.TableTeacher, columns, null, null, null, null, null);

        cursor.moveToFirst();
        Teacher teacher;

        while(!cursor.isAfterLast()) {
            teacher = cursorToTeacher(cursor);
            TeacherList.add(teacher);
            Log.d(LOG_TAG, "ID: " + teacher.getID() + ", Inhalt: " + teacher.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return TeacherList;
    }

    public static List<Teacher> getStaticAllTeacherList(Context c){
        TeacherDataSource obj = new TeacherDataSource(c);
        obj.open();
        List<Teacher> ListTeacher = obj.getAllTeacher();
        obj.close();

        return ListTeacher;
    }

    public static String[][] getAllTeacherNamesPlusID(Context c){
        TeacherDataSource obj = new TeacherDataSource(c);
        obj.open();
        List<Teacher> ListTeacher = obj.getAllTeacher();
        obj.close();
        String[][] strTeacherList = new String[2][ListTeacher.size()];

        for(int i=0; i != ListTeacher.size(); i++){
            Teacher teacher = ListTeacher.get(i);
            strTeacherList[0][i] = Long.toString(teacher.getID());
            strTeacherList[1][i] = teacher.getName() + ", " + teacher.getVorname();
        }

        return strTeacherList;
    }

    public static String getTeacherWithID(Context c, String ID){
        TeacherDataSource obj = new TeacherDataSource(c);
        obj.open();

        Log.d(obj.LOG_TAG, "getAllTeachers");

        Cursor cursor = obj.database.query(KlassenbuchDbHelper.TableTeacher, obj.columns, KlassenbuchDbHelper.TeacherID + "=?", new String[]{ID}, null, null, KlassenbuchDbHelper.TeacherName + " ASC");

        cursor.moveToFirst();
        Teacher teacher;

        teacher = obj.cursorToTeacher(cursor);
        Log.d(obj.LOG_TAG, "ID: " + teacher.getID() + ", Inhalt: " + teacher.toString());
        cursor.close();
        String output = teacher.getName() + ", " + teacher.getVorname();
        Log.d(obj.LOG_TAG, output);
        obj.close();

        return output;
    }


}
