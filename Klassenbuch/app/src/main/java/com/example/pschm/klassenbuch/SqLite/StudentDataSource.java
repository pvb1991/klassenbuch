package com.example.pschm.klassenbuch.SqLite;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;

import com.example.pschm.klassenbuch.ListOfStudents;
import com.example.pschm.klassenbuch.NewClass;
import com.example.pschm.klassenbuch.admin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pschm on 15.12.2017.
 */

public class StudentDataSource {
    private static String[] columns = {
            KlassenbuchDbHelper.StudentID,
            KlassenbuchDbHelper.StudentVorname,
            KlassenbuchDbHelper.StudentName,
            KlassenbuchDbHelper.StudentStreet,
            KlassenbuchDbHelper.StudentHousenumber,
            KlassenbuchDbHelper.StudentZIP,
            KlassenbuchDbHelper.StudentLocus
    };

    private final String LOG_TAG = Student.class.getSimpleName();

    private static SQLiteDatabase database;
    private KlassenbuchDbHelper dbHelper;

    public StudentDataSource() {

    }

    public StudentDataSource(Context context) {
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

    public Student createStudent(String vorname, String name, String street, String housenumber, int zipcode, String locus) {

        ContentValues values = new ContentValues();
        values.put(KlassenbuchDbHelper.StudentVorname, vorname);
        values.put(KlassenbuchDbHelper.StudentName, name);
        values.put(KlassenbuchDbHelper.StudentStreet,street);
        values.put(KlassenbuchDbHelper.StudentHousenumber, housenumber);
        values.put(KlassenbuchDbHelper.StudentZIP, zipcode);
        values.put(KlassenbuchDbHelper.StudentLocus,locus);

        long insertId = database.insert(KlassenbuchDbHelper.TableStudent, null, values);

        Cursor cursor = database.query(KlassenbuchDbHelper.TableStudent,
                columns, KlassenbuchDbHelper.StudentID + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Student student = cursorToStudent(cursor);
        cursor.close();

        return student;
    }

    public static Student cursorToStudent(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(KlassenbuchDbHelper.StudentID);
        int idVorname = cursor.getColumnIndex(KlassenbuchDbHelper.StudentVorname);
        int idName = cursor.getColumnIndex(KlassenbuchDbHelper.StudentName);
        int idStreet = cursor.getColumnIndex(KlassenbuchDbHelper.StudentStreet);
        int idHousenumber = cursor.getColumnIndex(KlassenbuchDbHelper.StudentHousenumber);
        int idZip = cursor.getColumnIndex(KlassenbuchDbHelper.StudentZIP);
        int idLocus = cursor.getColumnIndex(KlassenbuchDbHelper.StudentLocus);

        String vorname = cursor.getString(idVorname);
        String name = cursor.getString(idName);
        String street = cursor.getString(idStreet);
        String housenumber = cursor.getString(idHousenumber);
        int zipcode = cursor.getInt(idZip);
        String locus = cursor.getString(idLocus);
        long id = cursor.getLong(idIndex);

        Student student = new Student(vorname, name, street, housenumber, zipcode, locus, id);

        return student;
    }

    public static Student getStudent(Context c, String studentID) {
        StudentDataSource sds = new StudentDataSource(c);

        Log.d(sds.LOG_TAG, "getAllStudents");

        sds.open();

        Cursor cursor = database.query(KlassenbuchDbHelper.TableStudent, columns, KlassenbuchDbHelper.StudentID + "=?", new String[]{studentID}, null, null, null);

        cursor.moveToFirst();
        Student student = cursorToStudent(cursor);
        Log.d(sds.LOG_TAG, "ID: " + student.getID() + ", Inhalt: " + student.toString());


        cursor.close();
        sds.close();

        return student;
    }

    public List<Student> getAllStudents() {
        Log.d(LOG_TAG, "getAllStudents");
        List<Student> StudentList = new ArrayList<>();

        Cursor cursor = database.query(KlassenbuchDbHelper.TableStudent, columns, null, null, null, null, KlassenbuchDbHelper.StudentName + " ASC");

        cursor.moveToFirst();
        Student student;

        while(!cursor.isAfterLast()) {
            student = cursorToStudent(cursor);
            StudentList.add(student);
            Log.d(LOG_TAG, "ID: " + student.getID() + ", Inhalt: " + student.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return StudentList;
    }

    public static List<Student> getStaticAllStudentList(Context c){
        StudentDataSource obj = new StudentDataSource(c);
        obj.open();
        List<Student> ListStudent = obj.getAllStudents();
        obj.close();

        return ListStudent;
    }

    public static String[] getAllStudentsNames(Context c){
        StudentDataSource obj = new StudentDataSource(c);
        obj.open();
        List<Student> ListStudent = obj.getAllStudents();
        obj.close();
        String[] strStudentList = new String[ListStudent.size()];

       for(int i=0; i != ListStudent.size(); i++){
            Student student = ListStudent.get(i);
            strStudentList[i] = student.getName() + ", " + student.getVorname();
       }

       return strStudentList;
    }

    public static String[][] getAllStudentsNamesPlusID(Context c){
        StudentDataSource obj = new StudentDataSource(c);
        obj.open();
        List<Student> ListStudent = obj.getAllStudents();
        obj.close();
        String[][] strStudentList = new String[2][ListStudent.size()];

        for(int i=0; i != ListStudent.size(); i++){
            Student student = ListStudent.get(i);
            strStudentList[0][i] = Long.toString(student.getID());
            strStudentList[1][i] = student.getName() + ", " + student.getVorname();
        }

        return strStudentList;
    }

    public static String[][] getAllStudentTable(Context c){
        StudentDataSource obj = new StudentDataSource(c);
        obj.open();
        List<Student> ListStudent = obj.getAllStudents();
        obj.close();
        String[][] strStudentList = new String[ListStudent.size()][7];

        for(int i=0; i != ListStudent.size(); i++){
            Student student = ListStudent.get(i);
            strStudentList[i][0] = Long.toString(student.getID());
            strStudentList[i][1] = student.getVorname();
            strStudentList[i][2] = student.getName();
            strStudentList[i][3] = student.getStreet();
            strStudentList[i][4] = student.getHousenumber();
            strStudentList[i][5] = Integer.toString(student.getZipcode());
            strStudentList[i][6] = student.getLocus();
        }

        return strStudentList;
    }

    public static String getStudentWithID(Context c, String ID){

        StudentDataSource obj = new StudentDataSource(c);
        obj.open();

        Log.d(obj.LOG_TAG, "getAllStudents");

        Cursor cursor = database.query(KlassenbuchDbHelper.TableStudent, columns, KlassenbuchDbHelper.StudentID + "=?", new String[]{ID}, null, null, KlassenbuchDbHelper.StudentName + " ASC");

        cursor.moveToFirst();
        Student student;

        student = cursorToStudent(cursor);
        Log.d(obj.LOG_TAG, "ID: " + student.getID() + ", Inhalt: " + student.toString());
        cursor.close();
        String output = student.getName() + ", " + student.getVorname();
        Log.d(obj.LOG_TAG, output);
        obj.close();

        return output;
    }

    public static String getStudentByID(Context c, String id){
        StudentDataSource obj = new StudentDataSource(c);
        obj.open();

        Cursor cursor = database.query(KlassenbuchDbHelper.TableStudent, columns, KlassenbuchDbHelper.StudentID + "=?", new String[]{id}, null, null, null);

        cursor.moveToFirst();

        Student student;

        student = cursorToStudent(cursor);
        Log.d(NewClass.class.getSimpleName(), student.toString());
        String output = student.getName() + ", " + student.getVorname();

        Log.d(obj.LOG_TAG, "ID: " + student.getID() + ", Inhalt: " + student.toString());

        cursor.close();
        obj.close();
        return output;
    }

    public static List<Student> getStudentListByTeacherID(Context c, String teacherID){

        StudentDataSource sds = new StudentDataSource(c);

        List<Student> StudentList = new ArrayList<>();
        sds.open();

        String select = "SELECT " + "ST" + "." + KlassenbuchDbHelper.StudentID + " AS " + KlassenbuchDbHelper.StudentID + ", " +
                "ST" + "." + KlassenbuchDbHelper.StudentName + " AS " + KlassenbuchDbHelper.StudentName + ", " +
                "ST" + "." + KlassenbuchDbHelper.StudentVorname + " AS " + KlassenbuchDbHelper.StudentVorname + ", " +
                "ST" + "." + KlassenbuchDbHelper.StudentStreet + " AS " + KlassenbuchDbHelper.StudentStreet + ", " +
                "ST" + "." + KlassenbuchDbHelper.StudentHousenumber + " AS " + KlassenbuchDbHelper.StudentHousenumber + ", " +
                "ST" + "." + KlassenbuchDbHelper.StudentZIP + " AS " + KlassenbuchDbHelper.StudentZIP + ", " +
                "ST" + "." + KlassenbuchDbHelper.StudentLocus + " AS " + KlassenbuchDbHelper.StudentLocus +
                " FROM " + KlassenbuchDbHelper.TableStudent + " AS ST, " + KlassenbuchDbHelper.TableInformed+
                " INNER JOIN " + KlassenbuchDbHelper.TableStudent + " ON " + KlassenbuchDbHelper.TableStudent + "." + KlassenbuchDbHelper.StudentID + "=" + KlassenbuchDbHelper.TableInClass + "." + KlassenbuchDbHelper.StudentID +
                " INNER JOIN " + KlassenbuchDbHelper.TableInClass + " ON " + KlassenbuchDbHelper.TableInClass + "." + KlassenbuchDbHelper.ClassID + "=" + KlassenbuchDbHelper.TableInformed + "." + KlassenbuchDbHelper.ClassID +
                " WHERE " + KlassenbuchDbHelper.TableInformed+ "." + KlassenbuchDbHelper.TeacherID + "=? " +
                " GROUP BY ST." + KlassenbuchDbHelper.StudentID +
                " ORDER BY ST." + KlassenbuchDbHelper.StudentName + ";";



        Cursor cursor = null;

        cursor= sds.database.rawQuery(select, new String[]{teacherID});

        cursor.moveToFirst();
        Student student;

        while(!cursor.isAfterLast()) {
            student = StudentDataSource.cursorToStudent(cursor);
            StudentList.add(student);
            Log.d(sds.LOG_TAG,  "Inhalt: " + student.toString());
            cursor.moveToNext();
        }

        cursor.close();
        sds.close();

        return StudentList;
    }

    public static List<Student> getStudentListWhereInClass(Context c, String classID) {

        StudentDataSource sds = new StudentDataSource(c);

        List<Student> StudentList = new ArrayList<>();
        sds.open();

        String select = "SELECT " + "ST" + "." + KlassenbuchDbHelper.StudentID + " AS " + KlassenbuchDbHelper.StudentID + ", " +
                "ST" + "." + KlassenbuchDbHelper.StudentName + " AS " + KlassenbuchDbHelper.StudentName + ", " +
                "ST" + "." + KlassenbuchDbHelper.StudentVorname + " AS " + KlassenbuchDbHelper.StudentVorname + ", " +
                "ST" + "." + KlassenbuchDbHelper.StudentStreet + " AS " + KlassenbuchDbHelper.StudentStreet + ", " +
                "ST" + "." + KlassenbuchDbHelper.StudentHousenumber + " AS " + KlassenbuchDbHelper.StudentHousenumber + ", " +
                "ST" + "." + KlassenbuchDbHelper.StudentZIP + " AS " + KlassenbuchDbHelper.StudentZIP + ", " +
                "ST" + "." + KlassenbuchDbHelper.StudentLocus + " AS " + KlassenbuchDbHelper.StudentLocus +
                " FROM " + KlassenbuchDbHelper.TableStudent + " AS ST, " + KlassenbuchDbHelper.TableInClass + " AS IC" +
                " INNER JOIN " + KlassenbuchDbHelper.TableStudent + " ON ST." + KlassenbuchDbHelper.StudentID + "= IC." + KlassenbuchDbHelper.StudentID +
                " WHERE IC." + KlassenbuchDbHelper.ClassID + "=? " +
                " GROUP BY ST." + KlassenbuchDbHelper.StudentID +
                " ORDER BY ST." + KlassenbuchDbHelper.StudentName + ";";


        Cursor cursor = null;
        try {
            cursor = sds.database.rawQuery(select, new String[]{classID});
        }
        catch (Exception ex) { Log.e(sds.LOG_TAG, "Fehler: " + ex.getMessage()); }

        cursor.moveToFirst();
        Student student;

        while(!cursor.isAfterLast()) {
            student = StudentDataSource.cursorToStudent(cursor);
            StudentList.add(student);
            Log.d(sds.LOG_TAG,  "Inhalt: " + student.toString());
            cursor.moveToNext();
        }

        cursor.close();
        sds.close();

        return StudentList;
    }
}
