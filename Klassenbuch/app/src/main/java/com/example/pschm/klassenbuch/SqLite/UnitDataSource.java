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

public class UnitDataSource {
    private String[] columns = {
            KlassenbuchDbHelper.UnitID,
            KlassenbuchDbHelper.UnitTitel
    };

    private static final String LOG_TAG = Unit.class.getSimpleName();

    private SQLiteDatabase database;
    private KlassenbuchDbHelper dbHelper;

    public UnitDataSource(Context context) {
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

    public Unit createUnit(String titel) {

        ContentValues values = new ContentValues();
        values.put(KlassenbuchDbHelper.UnitTitel, titel);

        long insertId = database.insert(KlassenbuchDbHelper.TableUnit, null, values);

        Cursor cursor = database.query(KlassenbuchDbHelper.TableUnit,
                columns, KlassenbuchDbHelper.UnitID + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Unit unit = cursorToUnit(cursor);
        cursor.close();

        return unit;
    }

    private Unit cursorToUnit(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(KlassenbuchDbHelper.UnitID);
        int idTitel = cursor.getColumnIndex(KlassenbuchDbHelper.UnitTitel);

        long id = cursor.getLong(idIndex);
        String titel = cursor.getString(idTitel);

        Unit unit = new Unit(id, titel);

        return unit;
    }

    public List<Unit> getAllUnit() {
        Log.d(LOG_TAG, "gettAllUnit");
        List<Unit> UnitList = new ArrayList<>();

        Cursor cursor = database.query(KlassenbuchDbHelper.TableUnit, columns, null, null, null, null, null);

        cursor.moveToFirst();
        Unit unit;

        while(!cursor.isAfterLast()) {
            unit = cursorToUnit(cursor);
            UnitList.add(unit);
            Log.d(LOG_TAG, "ID: " + unit.getID() + ", Inhalt: " + unit.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return UnitList;
    }

    public static List<Unit> getStaticAllUnitList(Context c){
        UnitDataSource obj = new UnitDataSource(c);
        obj.open();
        List<Unit> ListUnit = obj.getAllUnit();
        obj.close();

        return ListUnit;
    }

    public static String[][] getAllUnitNamesPlusID(Context c){
        UnitDataSource obj = new UnitDataSource(c);

        obj.open();

        List<Unit> ListUnit = obj.getStaticAllUnitList(c);

        obj.close();

        String[][] strUnit = new String[2][ListUnit.size()];

        for(int i=0; i != ListUnit.size(); i++){
            Unit unit = ListUnit.get(i);
            strUnit[0][i] = Long.toString(unit.getID());
            strUnit[1][i] = unit.getTitle() ;
        }

        return strUnit;
    }

    public static String getUnitWithID(Context c, String ID){

        UnitDataSource obj = new UnitDataSource(c);
        obj.open();

        Log.d(obj.LOG_TAG, "getSelectUnit");

        Cursor cursor = obj.database.query(KlassenbuchDbHelper.TableUnit, obj.columns, KlassenbuchDbHelper.UnitID + "=?", new String[]{ID}, null, null, null);

        cursor.moveToFirst();
        Unit unit;

        unit = obj.cursorToUnit(cursor);
        Log.d(obj.LOG_TAG, "ID: " + unit.getID() + ", Inhalt: " + unit.toString());
        cursor.close();
        String output = unit.getTitle();

        obj.close();

        return output;
    }

    public static List<Unit> SelectUnitFromStudentByTeacher(Context c, String studentID, String teacherID){
        UnitDataSource uds = new UnitDataSource(c);
        Unit unit = null;
        List<Unit> UnitList = new ArrayList<>();
        String select = "SELECT " + "u." + KlassenbuchDbHelper.UnitID + " AS " +  KlassenbuchDbHelper.UnitID + "," +
                " u." + KlassenbuchDbHelper.UnitTitel + " AS " + KlassenbuchDbHelper.UnitTitel +
                " FROM " + KlassenbuchDbHelper.TableUnit + " AS u," +
                KlassenbuchDbHelper.TableInformed + " AS i," +
                KlassenbuchDbHelper.TableInClass + " AS ic," +
                KlassenbuchDbHelper.TableClass + " AS c" +
                " INNER JOIN " + KlassenbuchDbHelper.TableUnit + " ON u." + KlassenbuchDbHelper.UnitID + "=i." + KlassenbuchDbHelper.UnitID +
                " INNER JOIN " + KlassenbuchDbHelper.TableInformed + " ON i." + KlassenbuchDbHelper.ClassID + "=c." + KlassenbuchDbHelper.ClassID +
                " INNER JOIN " + KlassenbuchDbHelper.TableInClass + " ON ic." + KlassenbuchDbHelper.ClassID + "=c." + KlassenbuchDbHelper.ClassID +
                " WHERE " + "ic." + KlassenbuchDbHelper.StudentID + "=? AND " +
                "i." + KlassenbuchDbHelper.TeacherID + "=?"+
                " GROUP BY u." + KlassenbuchDbHelper.UnitID;

        uds.open();
        Cursor cursor = null;

        try{
            cursor= uds.database.rawQuery(select, new String[]{studentID, teacherID});

            cursor.moveToFirst();

            while(!cursor.isAfterLast()) {
                unit = uds.cursorToUnit(cursor);
                UnitList.add(unit);
                Log.d(LOG_TAG, "ID: " + unit.getID() + ", Inhalt: " + unit.toString());
                cursor.moveToNext();
            }

            cursor.close();
        }
        catch (Exception ex) { Log.e(LOG_TAG, "Fehler: " + ex.getMessage()); }

        uds.close();

        return UnitList;

    }

    public static Unit getUnit(Context c, String unitID){
        UnitDataSource uds = new UnitDataSource(c);
        Unit unit = null;

        String select = "SELECT * FROM " + KlassenbuchDbHelper.TableUnit + " WHERE " + KlassenbuchDbHelper.UnitID + "=?;";

        uds.open();
        Cursor cursor = null;

        try{
            cursor= uds.database.rawQuery(select, new String[]{unitID});

            cursor.moveToFirst();
            unit = uds.cursorToUnit(cursor);
            Log.d(LOG_TAG, "ID: " + unit.getID() + ", Inhalt: " + unit.toString());

            cursor.close();
        }
        catch (Exception ex) { Log.e(LOG_TAG, "Fehler: " + ex.getMessage()); }

        uds.close();

        return unit;

    }
}
