package com.example.pschm.klassenbuch.SqLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.pschm.klassenbuch.NewClass;
import com.example.pschm.klassenbuch.admin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pschm on 17.12.2017.
 */

public class InClassDataSource {
    private String[] columns = {
            KlassenbuchDbHelper.ClassID,
            KlassenbuchDbHelper.StudentID
    };

    private static final String LOG_TAG = InClass.class.getSimpleName();

    private SQLiteDatabase database;
    private KlassenbuchDbHelper dbHelper;

    public InClassDataSource(Context context) {
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

    public InClass createInClass(long classID, long studentID) {

        ContentValues values = new ContentValues();
        values.put(KlassenbuchDbHelper.ClassID, classID);
        values.put(KlassenbuchDbHelper.StudentID, studentID);

        database.insert(KlassenbuchDbHelper.TableInClass, null, values);

        Cursor cursor = database.query(KlassenbuchDbHelper.TableInClass,
                columns, KlassenbuchDbHelper.StudentID + "=" + studentID + " AND " + KlassenbuchDbHelper.ClassID + "=" + classID,
                null, null, null, null);

        cursor.moveToFirst();
        InClass inClass = cursorToInClass(cursor);
        cursor.close();

        return inClass;
    }

    private InClass cursorToInClass(Cursor cursor) {
        int idClassID = cursor.getColumnIndex(KlassenbuchDbHelper.ClassID);
        int idStudentID = cursor.getColumnIndex(KlassenbuchDbHelper.StudentID);

        long classID = cursor.getLong(idClassID);
        long studentID = cursor.getLong(idStudentID);

        InClass inClass = new InClass(classID, studentID);

        return inClass;
    }

    public List<InClass> getAllInClass() {
        Log.d(LOG_TAG, "gettAllInClass");
        List<InClass> InClassList = new ArrayList<>();

        Cursor cursor = database.query(KlassenbuchDbHelper.TableInClass, columns, null, null, null, null, null);

        cursor.moveToFirst();
        InClass inClass;

        while(!cursor.isAfterLast()) {
            inClass = cursorToInClass(cursor);
            InClassList.add(inClass);
            Log.d(LOG_TAG,  "Inhalt: " + inClass.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return InClassList;
    }

    public static List<InClass> getStaticAllInClass(Context c, String classID) {
        InClassDataSource icds = new InClassDataSource(c);
        Log.d(LOG_TAG, "gettAllInClass");
        List<InClass> InClassList = new ArrayList<>();

        icds.open();
        Cursor cursor = icds.database.query(KlassenbuchDbHelper.TableInClass, icds.columns, KlassenbuchDbHelper.ClassID + "=?", new String[]{classID}, null, null, null);

        cursor.moveToFirst();
        InClass inClass;

        while(!cursor.isAfterLast()) {
            inClass = icds.cursorToInClass(cursor);
            InClassList.add(inClass);
            Log.d(LOG_TAG,  "Inhalt: " + inClass.toString());
            cursor.moveToNext();
        }

        cursor.close();
        icds.close();

        return InClassList;
    }

    public static List<InClass> getStaticAllinClass(Context c) {
        InClassDataSource icds = new InClassDataSource(c);
        Log.d(LOG_TAG, "gettAllInClass");
        List<InClass> InClassList = new ArrayList<>();

        icds.open();
        Cursor cursor = icds.database.query(KlassenbuchDbHelper.TableInClass, icds.columns, null, null, null, null, null);

        cursor.moveToFirst();
        InClass inClass;

        while(!cursor.isAfterLast()) {
            inClass = icds.cursorToInClass(cursor);
            InClassList.add(inClass);
            Log.d(LOG_TAG,  "Inhalt: " + inClass.toString());
            cursor.moveToNext();
        }

        cursor.close();
        icds.close();

        return InClassList;
    }

    public static String[] getStaticAllInClass(Context c){
        InClassDataSource icds = new InClassDataSource(c);

        icds.open();
        Log.d(LOG_TAG, "gettAllInClass");
        List<InClass> InClassList = new ArrayList<>();

        Cursor cursor = icds.database.query(KlassenbuchDbHelper.TableInClass, icds.columns, null, null, null, null, null);

        String[] output = new String[cursor.getCount()];
        int i = 0;
        cursor.moveToFirst();
        InClass inClass;

        while(!cursor.isAfterLast()) {
            inClass = icds.cursorToInClass(cursor);
            InClassList.add(inClass);

            output[i] =  ClassDataSource.getClassWithID(c, String.valueOf(inClass.getClassID())) + " - " + StudentDataSource.getStudentWithID(c, String.valueOf(inClass.getStudentID()));

            Log.d(LOG_TAG,  output[i]);
            cursor.moveToNext();
            i++;
        }

        cursor.close();
        icds.close();

        return output;
    }

    public static String[] getStudentsIdInClass(Context c, String id){
        InClassDataSource icds = new InClassDataSource(c);

        icds.open();
        Log.d(LOG_TAG, "gettAllInClass");
        List<InClass> InClassList = new ArrayList<>();

        Cursor cursor = icds.database.query(KlassenbuchDbHelper.TableInClass, icds.columns, KlassenbuchDbHelper.ClassID + "=?", new String[]{id}, null, null, null);

        String[] output = new String[cursor.getCount()];
        int i = 0;
        cursor.moveToFirst();
        InClass inClass;

        while(!cursor.isAfterLast()) {
            inClass = icds.cursorToInClass(cursor);
            InClassList.add(inClass);

            output[i] = String.valueOf(inClass.getStudentID());

            Log.d(LOG_TAG,  output[i]);
            cursor.moveToNext();
            i++;
        }

        cursor.close();
        icds.close();

        return output;
    }
}
