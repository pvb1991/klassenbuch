package com.example.pschm.klassenbuch.SqLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.pschm.klassenbuch.openDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pschm on 16.12.2017.
 */

public class ClassDataSource {
    private String[] columns = {
            KlassenbuchDbHelper.ClassID,
            KlassenbuchDbHelper.ClassTitle
    };

    private static final String LOG_TAG = Class.class.getSimpleName();

    private SQLiteDatabase database;
    private KlassenbuchDbHelper dbHelper;

    public ClassDataSource(Context context) {
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

    public Class createClass(String title) {

        ContentValues values = new ContentValues();
        values.put(KlassenbuchDbHelper.ClassTitle, title);

        long insertId = database.insert(KlassenbuchDbHelper.TableClass, null, values);

        Cursor cursor = database.query(KlassenbuchDbHelper.TableClass,
                columns, KlassenbuchDbHelper.ClassID + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Class Classclass = cursorToClass(cursor);
        cursor.close();

        return Classclass;
    }

    private Class cursorToClass(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(KlassenbuchDbHelper.ClassID);
        int idTitle = cursor.getColumnIndex(KlassenbuchDbHelper.ClassTitle);

        String title = cursor.getString(idTitle);
        long id = cursor.getLong(idIndex);

        Class Classclass = new Class(id, title);

        return Classclass;
    }

    public List<Class> getAllClass() {
        Log.d(LOG_TAG, "gettAllClasses");
        List<Class> ClassList = new ArrayList<>();

        Cursor cursor = database.query(KlassenbuchDbHelper.TableClass, columns, null, null, null, null, KlassenbuchDbHelper.ClassTitle + " ASC");

        cursor.moveToFirst();
        Class Classclass;

        while(!cursor.isAfterLast()) {
            Classclass = cursorToClass(cursor);
            ClassList.add(Classclass);
            Log.d(LOG_TAG, "ID: " + Classclass.getID() + ", Inhalt: " + Classclass.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return ClassList;
    }

    public static List<Class> getStaticAllClassList(Context c){
        ClassDataSource obj = new ClassDataSource(c);
        obj.open();
        List<Class> ListClass = obj.getAllClass();
        obj.close();

        return ListClass;
    }

    public static String[] getAllClassNames(Context c){
        ClassDataSource obj = new ClassDataSource(c);

        obj.open();

        List<Class> ListClass = obj.getStaticAllClassList(c);

        obj.close();

        String[] strStudentClass = new String[ListClass.size()];

        for(int i=0; i != ListClass.size(); i++){
            Class classClass = ListClass.get(i);
            strStudentClass[i] = classClass.getTitle() ;
        }

        return strStudentClass;
    }

    public static String[][] getAllClassNamesPlusID(Context c){
        ClassDataSource obj = new ClassDataSource(c);

        obj.open();

        List<Class> ListClass = obj.getStaticAllClassList(c);

        obj.close();

        String[][] strStudentClass = new String[2][ListClass.size()];

        for(int i=0; i != ListClass.size(); i++){
            Class classClass = ListClass.get(i);
            strStudentClass[0][i] = Long.toString(classClass.getID());
            strStudentClass[1][i] = classClass.getTitle() ;
        }

        return strStudentClass;
    }

    public static String getClassWithID(Context c, String ID){

        ClassDataSource obj = new ClassDataSource(c);
        obj.open();

        Log.d(obj.LOG_TAG, "getSelectClass");

        Cursor cursor = obj.database.query(KlassenbuchDbHelper.TableClass, obj.columns, KlassenbuchDbHelper.ClassID + "=?", new String[]{ID}, null, null, null);

        cursor.moveToFirst();
        Class classClass;

        classClass = obj.cursorToClass(cursor);
        Log.d(obj.LOG_TAG, "ID: " + classClass.getID() + ", Inhalt: " + classClass.toString());
        cursor.close();
        String output = classClass.getTitle();

        obj.close();

        return output;
    }

    public static Class getClass(Context c, String classID){
        ClassDataSource cds = new ClassDataSource(c);
        Class classclass = null;

        String select = "SELECT * FROM " + KlassenbuchDbHelper.TableClass + " WHERE " + KlassenbuchDbHelper.ClassID + "=?;";

        cds.open();
        Cursor cursor = null;

        try{
            cursor= cds.database.rawQuery(select, new String[]{classID});

            cursor.moveToFirst();
            classclass = cds.cursorToClass(cursor);
            Log.d(LOG_TAG, "ID: " + classclass.getID() + ", Inhalt: " + classclass.toString());

            cursor.close();
        }
        catch (Exception ex) { Log.e(LOG_TAG, "Fehler: " + ex.getMessage()); }

        cds.close();

        return classclass;

    }
}
