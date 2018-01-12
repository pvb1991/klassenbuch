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
 * Created by pschm on 17.12.2017.
 */

public class InformedDataSource {
    private String[] columns = {
            KlassenbuchDbHelper.UnitID,
            KlassenbuchDbHelper.TeacherID,
            KlassenbuchDbHelper.ClassID
    };

    private static final String LOG_TAG = InformedDataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private KlassenbuchDbHelper dbHelper;

    public InformedDataSource(Context context) {
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

    public Informed createInformed(long unitID, long teacherID, long classID) {

        ContentValues values = new ContentValues();
        values.put(KlassenbuchDbHelper.UnitID, unitID);
        values.put(KlassenbuchDbHelper.TeacherID, teacherID);
        values.put(KlassenbuchDbHelper.ClassID, classID);

        database.insert(KlassenbuchDbHelper.TableInformed, null, values);

        Cursor cursor = database.query(KlassenbuchDbHelper.TableInformed,
                columns, KlassenbuchDbHelper.UnitID + "=" + unitID + " AND " + KlassenbuchDbHelper.TeacherID + "=" + teacherID+ " AND " + KlassenbuchDbHelper.ClassID + "=" + classID,
                null, null, null, null);

        cursor.moveToFirst();
        Informed informed = cursorToInformed(cursor);
        cursor.close();

        return informed;
    }

    private Informed cursorToInformed(Cursor cursor) {
        int idUnitID = cursor.getColumnIndex(KlassenbuchDbHelper.UnitID);
        int idTeacherID = cursor.getColumnIndex(KlassenbuchDbHelper.TeacherID);
        int idClassID = cursor.getColumnIndex(KlassenbuchDbHelper.ClassID);

        long unitID = cursor.getLong(idUnitID);
        long teacherID = cursor.getLong(idTeacherID);
        long classID = cursor.getLong(idClassID);

        Informed informed = new Informed(unitID, teacherID, classID);

        return informed;
    }

    public List<Informed> getAllInformed() {
        Log.d(LOG_TAG, "gettAllInformed");
        List<Informed> InformedList = new ArrayList<>();

        Cursor cursor = database.query(KlassenbuchDbHelper.TableInformed, columns, null, null, null, null, null);

        cursor.moveToFirst();
        Informed informed;

        while(!cursor.isAfterLast()) {
            informed = cursorToInformed(cursor);
            InformedList.add(informed);
            Log.d(LOG_TAG,  "Inhalt: " + informed.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return InformedList;
    }

    public static List<Informed> getAllStaticInformedFromTeacher(Context c, String teacherID) {
        InformedDataSource ids = new InformedDataSource(c);

        ids.open();
        Log.d(LOG_TAG, "gettAllInformed");
        List<Informed> InformedList = new ArrayList<>();


        Cursor cursor = null;
        try{
            cursor = ids.database.query(KlassenbuchDbHelper.TableInformed, ids.columns, KlassenbuchDbHelper.TeacherID + "=?", new String[]{teacherID}, null, null, null);

            cursor.moveToFirst();
            Informed informed;

            while(!cursor.isAfterLast()) {
                informed = ids.cursorToInformed(cursor);
                InformedList.add(informed);
                Log.d(LOG_TAG,  "Inhalt: " + informed.toString());
                cursor.moveToNext();
            }
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler: " + ex.getMessage());
        }
        cursor.close();

        ids.close();
        return InformedList;
    }

    public static String[] getStaticAllInformed(Context c){
        InformedDataSource ids = new InformedDataSource(c);

        ids.open();
        Log.d(LOG_TAG, "gettAllInClass");
        List<Informed> InformedList = new ArrayList<>();

        Cursor cursor = ids.database.query(KlassenbuchDbHelper.TableInformed, ids.columns, null, null, null, null, null);

        String[] output = new String[cursor.getCount()];
        int i = 0;
        cursor.moveToFirst();
        Informed informed;

        while(!cursor.isAfterLast()) {
            informed = ids.cursorToInformed(cursor);
            InformedList.add(informed);

            output[i] = TeacherDataSource.getTeacherWithID(c, String.valueOf(informed.getTeacherID())) + " - " + UnitDataSource.getUnitWithID(c, String.valueOf(informed.getUnitID()))
                        + " - " + ClassDataSource.getClassWithID(c, String.valueOf(informed.getClassID()));

            Log.d(LOG_TAG,  output[i]);
            cursor.moveToNext();
            i++;
        }

        cursor.close();
        ids.close();

        return output;
    }



    public static String[][] getStaticAllInformedWithID(Context c){
        InformedDataSource ids = new InformedDataSource(c);

        ids.open();
        Log.d(LOG_TAG, "gettAllInClass");
        List<Informed> InformedList = new ArrayList<>();

        Cursor cursor = ids.database.query(KlassenbuchDbHelper.TableInformed, ids.columns, null, null, null, null, null);

        String[][] output = new String[3][cursor.getCount()];
        int i = 0;
        cursor.moveToFirst();
        Informed informed;

        while(!cursor.isAfterLast()) {
            informed = ids.cursorToInformed(cursor);
            InformedList.add(informed);

            output[0][i] = String.valueOf(informed.getUnitID());
            output[1][i] = String.valueOf(informed.getClassID());
            output[2][i] = UnitDataSource.getUnitWithID(c, String.valueOf(informed.getUnitID())) + " - " + ClassDataSource.getClassWithID(c, String.valueOf(informed.getClassID()));

            Log.d(LOG_TAG,  "UnitID:" + output[0][i] + " ClassID:" + output[1][i] + " String:" + output[2][i]);
            cursor.moveToNext();
            i++;
        }

        cursor.close();
        ids.close();

        return output;
    }
}
/*
        SELECT
            Schueler.IdSchueler AS IdSchueler,
            Schueler.SName AS SName,
            Schueler.Vorname AS Vorname,
            Schueler.Strasse AS Strasse,
            Schueler.Hausnummer AS Hausnummer,
            Schueler.PLZ AS PLZ,
            Schueler.Ort AS Ort
            FROM Schueler
            INNER JOIN Schueler ON Schueler.IdSchueler=InKlasse.IdSchueler
            INNER JOIN InKlasse ON InKlasse.IdKlasse=Unterrichtet.IdKlasse
            WHERE Unterrichtet=?




12-31 11:48:10.975 24385-24385/com.example.pschm.klassenbuch D/InformedDataSource:
SELECT
    Schueler.IdSchueler AS IdSchueler,
    Schueler.SName AS SName,
    Schueler.Vorname AS Vorname,
    Schueler.Strasse AS Strasse,
    Schueler.Hausnummer AS Hausnummer,
    Schueler.PLZ AS PLZ,
    Schueler.Ort AS Ort
FROM Schueler,Unterrichtet,InKlasse
INNER JOIN Schueler ON Schueler.IdSchueler=InKlasse.IdSchueler
INNER JOIN InKlasse ON InKlasse.IdKlasse=Unterrichtet.IdKlasse
WHERE Unterrichtet=?;
*/