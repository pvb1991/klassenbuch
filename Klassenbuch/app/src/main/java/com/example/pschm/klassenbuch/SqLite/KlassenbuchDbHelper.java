package com.example.pschm.klassenbuch.SqLite;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



/**
 * Created by pschm on 15.12.2017.
 */

public class KlassenbuchDbHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = KlassenbuchDbHelper.class.getSimpleName();

    // Name und Version der Datenbank
    private static final String DATABASE_NAME = "Klassenbuch.db";
    private static final int DATABASE_VERSION = 17;

    //Tabelle Schueler
    public static final String TableStudent = "Schueler";
    public static final String StudentID = "IdSchueler";
    public static final String StudentVorname = "Vorname";
    public static final String StudentName = "SName";
    public static final String StudentStreet ="Strasse";
    public static final String StudentHousenumber = "Hausnummer";
    public static final String StudentZIP = "PLZ";
    public static final String StudentLocus = "Ort";


    //Tabelle Klasse
    public static final String TableClass = "Klasse";
    public static final String ClassID = "IdKlasse";
    public static final String ClassTitle = "Bezeichung";

    //Tabelle Leher
    public static final String TableTeacher = "Lehrer";
    public static final String TeacherID = "IdLehrer";
    public static final String TeacherVorname = "Vorname";
    public static final String TeacherName = "SName";
    public static final String TeacherStreet = "Strasse";
    public static final String TeacherHousenumber = "Hausnummer";
    public static final String TeacherZIP = "PLZ";
    public static final String TeacherLocus = "Ort";
    public static final String TeacherEmail = "Email";
    public static final String TeacherPassword = "Passwort";
    public static final String TeacherAdmin = "Admin";

    //Tabelle Unterrichtsfach
    public static final String TableUnit = "Fach";
    public static final String UnitID = "IdFach";
    public static final String UnitTitel = "Titel";

    //Tabelle Raum
    public static final String TableRoom = "Raum";
    public static final String RoomRoomnumber = "Raumnummer";
    public static final String RoomFloor = "Stockwerk";

    //Tabelle Sitzplan
    public static final String TableSeatingPlan = "Sitzplan";
    public static final String SeatingPlanPosX = "PositionX";
    public static final String SeatingPlanPosY = "PositionY";

    //Tabelle ImRaum
    public static final String TableInClass ="InKlasse";

    //
    public static final String TableInformed = "Unterrichtet";

    //
    public static final String TableTakePlaceIn ="findetstattIm";

    //Tabelle Note
    public static final String TableMark = "Note";
    public static final String MarkType = "Art";
    public static final String MarkMark = "Note";
    public static final String MarkNote = "Notiz";
    public static final String MarkTypeOral = "M";
    public static final String MarkTypeHomework = "H";
    public static final String MarkTypeExam = "K";
    public static final int MarkHomeworkYES = 0;
    public static final int MarkHomeworkNO = 999;
    public static final int MarkSuper = 100;
    public static final int MarkOkay = 50;
    public static final int MarkBad = 0;

    //Tabelle Anwesend
    public static final String TableAttendance = "Anwesenheit";
    public static final String AttendanceDate = "Datum";
    public static final String AttendanceHour = "Stunde";
    public static final String AttendanceDelay = "Verspaetung";
    public static final int AttendanceAbsent = 999;
    public static final int AttendancePresent = 0;

    //Tabelle Raum Ordnung
    public static final String TableRoomOrder = "RaumOrdnung";
    public static final String RoomOrderPosY = "PosY";
    public static final String RoomOrderPosX = "PosX";
    public static final String RoomOrderBocked = "Besetzt";

    // Tabellen anlegen
    private static final String TABLE_Stundent_CREATE =
            "CREATE TABLE " + TableStudent + "("
                    + StudentID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + StudentVorname + " TEXT,"
                    + StudentName + " TEXT,"
                    + StudentStreet + " TEXT,"
                    + StudentHousenumber + " TEXT,"
                    + StudentZIP + " INTEGER,"
                    + StudentLocus + " TEXT);";

    private static final String TABLE_Class_CREATE =
            "CREATE TABLE "+ TableClass +"("
                    + ClassID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + ClassTitle + " TEXT);";

    private static final String TABLE_Teacher_CREATE =
            "CREATE TABLE "+ TableTeacher +"("
                    +TeacherID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    +TeacherVorname + " TEXT,"
                    +TeacherName + " TEXT,"
                    +TeacherStreet + " TEXT,"
                    +TeacherHousenumber + " TEXT,"
                    +TeacherZIP + " INTEGER,"
                    +TeacherLocus + " TEXT,"
                    +TeacherEmail + " TEXT,"
                    +TeacherPassword + " TEXT,"
                    +TeacherAdmin + " INTEGER);";

    private static final String TABLE_Unit_CREATE =
            "CREATE TABLE "+ TableUnit +" ("
                    +UnitID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    +UnitTitel + " TEXT);";

    private static final String TABLE_Room_CREATE =
            "CREATE TABLE " + TableRoom + "("
                    +RoomRoomnumber + " INTEGER PRIMARY KEY,"
                    +RoomFloor + " int);";

    private static final String TABLE_SeatingPlan_CREATE =
            "CREATE TABLE " + TableSeatingPlan + "("
                    +RoomRoomnumber + " INTEGER,"
                    +StudentID + " INTEGER,"
                    +UnitID + " INTEGER,"
                    +TeacherID  + " INTEGER,"
                    +SeatingPlanPosX + " INTEGER,"
                    +SeatingPlanPosY + " INTEGER,"
                    +"FOREIGN KEY(" + RoomRoomnumber + ")REFERENCES " + TableRoom + "(" + RoomRoomnumber + "),"
                    +"FOREIGN KEY(" + StudentID + ")REFERENCES " + TableStudent + "(" + StudentID + "),"
                    +"FOREIGN KEY(" + UnitID + ")REFERENCES " + TableUnit + "(" + UnitID + "),"
                    +"FOREIGN KEY(" + TeacherID + ")REFERENCES " + TableTeacher + "(" +TeacherID+"));";

    private static final String TABLE_InClass_CREATE =
            "CREATE TABLE " + TableInClass + "("
                    +ClassID + " INTEGER,"
                    +StudentID + " INTEGER,"
                    +"FOREIGN KEY(" + ClassID + ")REFERENCES " + TableClass + "(" + ClassID + "),"
                    +"FOREIGN KEY(" + StudentID + ")REFERENCES " + TableStudent + "(" + StudentID +"));";

    private static final String TABLE_Mark_CREATE =
            "CREATE TABLE " + TableMark + "("
                    +StudentID + " INTEGER,"
                    +UnitID + " INTEGER,"
                    +TeacherID + " INTEGER,"
                    +MarkType + " TEXT,"
                    +MarkMark + " INTEGER,"
                    +MarkNote + " TEXT,"
                    +"FOREIGN KEY(" + StudentID + ")REFERENCES " + TableStudent + "(" + StudentID + "),"
                    +"FOREIGN KEY(" + UnitID + ")REFERENCES " + TableUnit + "(" + UnitID + "),"
                    +"FOREIGN KEY(" + TeacherID + ")REFERENCES " + TableTeacher + "("+TeacherID+"));";

    private static final String TABLE_Informed_CREATE =
            "CREATE TABLE " + TableInformed + "("
                    +UnitID + " INTEGER,"
                    +TeacherID + " INTEGER,"
                    +ClassID + " INTEGER,"
                    +"FOREIGN KEY(" + UnitID + ")REFERENCES " + TableUnit + "(" + UnitID +"),"
                    +"FOREIGN KEY(" + TeacherID + ")REFERENCES " + TableTeacher + "(" + TeacherID +"),"
                    +"FOREIGN KEY(" + ClassID + ")REFERENCES " + TableClass + "(" + ClassID + "));";

    private static final String TABLE_TakePlaceIn_CREATE =
            "CREATE TABLE " + TableTakePlaceIn + "("
                    +RoomRoomnumber + " INTEGER,"
                    +UnitID + " INTEGER,"
                    +"FOREIGN KEY(" + RoomRoomnumber + ")REFERENCES " + TableRoom + "(" + RoomRoomnumber + "),"
                    +"FOREIGN KEY(" + UnitID + ")REFERENCES " + TableUnit + "(" + UnitID +"));";

    private static final String TABLE_Attendance_CREATE =
            "CREATE TABLE " + TableAttendance + "("
                    +StudentID + " INTEGER,"
                    +UnitID + " INTEGER,"
                    +AttendanceDate + " INTEGER,"
                    +AttendanceHour + " INTEGER,"
                    +AttendanceDelay + " INTEGER,"
                    +"FOREIGN KEY(" + StudentID + ")REFERENCES " + TableStudent + "(" + StudentID + "),"
                    +"FOREIGN KEY(" + UnitID + ")REFERENCES " + TableUnit + "(" + UnitID + "));";

    private static final String TABLE_RommOrder_CREATE =
            "CREATE TABLE " + TableRoomOrder + "("
                    +RoomRoomnumber + " INTEGER,"
                    +RoomOrderPosX + " INTEGER,"
                    +RoomOrderPosY + " INTEGER,"
                    +RoomOrderBocked + " INTEGER,"
                    +"FOREIGN KEY(" + RoomRoomnumber + ")REFERENCES " + TableRoom + "(" + RoomRoomnumber + "));";

    private static final String createAdmin = "INSERT INTO " + TableTeacher + "(" +
            TeacherName + "," +
            TeacherVorname + "," +
            TeacherEmail + ", " +
            TeacherPassword + ", "+
            TeacherAdmin + ")"+
            " VALUES ('admin', 'admin', 'admin', 'admin', 1);";

    // Tabellen löschen
    private static final String TABLE_Student_DROP      = "DROP TABLE IF EXISTS " + TableStudent;
    private static final String TABLE_Class_DROP        = "DROP TABLE IF EXISTS " + TableClass;
    private static final String TABLE_Teacher_DROP      = "DROP TABLE IF EXISTS " + TableTeacher;
    private static final String TABLE_Unit_DROP         = "DROP TABLE IF EXISTS " + TableUnit;
    private static final String TABLE_Room_DROP         = "DROP TABLE IF EXISTS " + TableRoom;
    private static final String TABLE_SeatingPlan_DROP  = "DROP TABLE IF EXISTS " + TableSeatingPlan;
    private static final String TABLE_InClass_DROP      = "DROP TABLE IF EXISTS " + TableInClass;
    private static final String TABLE_Mark_DROP         = "DROP TABLE IF EXISTS " + TableMark;
    private static final String TABLE_Informed_DROP     = "DROP TABLE IF EXISTS " + TableInformed;
    private static final String TABLE_TakePlaceIn_DROP  = "DROP TABLE IF EXISTS " + TableTakePlaceIn;
    private static final String TABLE_Attendance_DROP   = "DROP TABLE IF EXISTS " + TableAttendance;
    private static final String TABLE_RoomOrder_DROP   = "DROP TABLE IF EXISTS " + TableRoomOrder;



    public KlassenbuchDbHelper(Context context) {
        //super(context, "PLATZHALTER_DATENBANKNAME", null, 1);
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + TABLE_Stundent_CREATE + " angelegt.");
            db.execSQL(TABLE_Stundent_CREATE);


        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }

        //---------------

        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + TABLE_Class_CREATE + " angelegt.");
            db.execSQL(TABLE_Class_CREATE);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }

        //---------------

        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + TABLE_Teacher_CREATE + " angelegt.");
            db.execSQL(TABLE_Teacher_CREATE);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }

        //---------------

        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + TABLE_Unit_CREATE+ " angelegt.");
        db.execSQL(TABLE_Unit_CREATE);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }

        //---------------

        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + TABLE_Room_CREATE + " angelegt.");
        db.execSQL(TABLE_Room_CREATE);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }

        //---------------

        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + TABLE_SeatingPlan_CREATE + " angelegt.");
        db.execSQL(TABLE_SeatingPlan_CREATE);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }

        //---------------

        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + TABLE_InClass_CREATE + " angelegt.");
        db.execSQL(TABLE_InClass_CREATE);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }

        //---------------

        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + TABLE_Mark_CREATE + " angelegt.");
        db.execSQL(TABLE_Mark_CREATE);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }

        //---------------

        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + TABLE_Informed_CREATE + " angelegt.");
        db.execSQL(TABLE_Informed_CREATE);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }

        //---------------

        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + TABLE_TakePlaceIn_CREATE + " angelegt.");
        db.execSQL(TABLE_TakePlaceIn_CREATE);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
        //---------------


        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + TABLE_Attendance_CREATE + " angelegt.");
        db.execSQL(TABLE_Attendance_CREATE);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }

        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + TABLE_RommOrder_CREATE + " angelegt.");
            db.execSQL(TABLE_RommOrder_CREATE);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }

        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + createAdmin + " angelegt.");
            db.execSQL(createAdmin);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrade der Datenbank von Version "
                    + oldVersion + " zu "
                    + newVersion + "; alle Daten werden gelöscht");

        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + createAdmin + " angelegt.");
            db.execSQL(createAdmin);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
        //ResetAllTables(db);
    }

    private void ResetAllTables(SQLiteDatabase db){
        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + TABLE_Student_DROP + " gelöscht.");
            db.execSQL(TABLE_Student_DROP);
        }
        catch (Exception ex) { Log.e(LOG_TAG, "Fehler beim löschen der Tabelle: " + ex.getMessage()); }

        //---------------

        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + TABLE_Class_DROP + " gelöscht.");
            db.execSQL(TABLE_Class_DROP);
        }
        catch (Exception ex) { Log.e(LOG_TAG, "Fehler beim Löschen der Tabelle: " + ex.getMessage()); }

        //---------------

        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + TABLE_Teacher_DROP + " gelöscht.");
            db.execSQL(TABLE_Teacher_DROP);
        }
        catch (Exception ex) { Log.e(LOG_TAG, "Fehler beim Löschen der Tabelle: " + ex.getMessage()); }

        //---------------

        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + TABLE_Unit_DROP + " gelöscht.");
            db.execSQL(TABLE_Unit_DROP);
        }
        catch (Exception ex) { Log.e(LOG_TAG, "Fehler beim Löschen der Tabelle: " + ex.getMessage()); }

        //---------------

        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + TABLE_Room_DROP + " gelöscht.");
            db.execSQL(TABLE_Room_DROP);
        }
        catch (Exception ex) { Log.e(LOG_TAG, "Fehler beim Löschen der Tabelle: " + ex.getMessage()); }

        //---------------

        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + TABLE_SeatingPlan_DROP + " gelöscht.");
            db.execSQL(TABLE_SeatingPlan_DROP);
        }
        catch (Exception ex) { Log.e(LOG_TAG, "Fehler beim Löschen der Tabelle: " + ex.getMessage()); }

        //---------------

        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + TABLE_InClass_DROP + " gelöscht.");
            db.execSQL(TABLE_InClass_DROP);
        }
        catch (Exception ex) { Log.e(LOG_TAG, "Fehler beim Löschen der Tabelle: " + ex.getMessage()); }

        //---------------

        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + TABLE_Mark_DROP + " gelöscht.");
            db.execSQL(TABLE_Mark_DROP);
        }
        catch (Exception ex) { Log.e(LOG_TAG, "Fehler beim Löschen der Tabelle: " + ex.getMessage()); }

        //---------------

        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + TABLE_Informed_DROP + " gelöscht.");
            db.execSQL(TABLE_Informed_DROP);
        }
        catch (Exception ex) { Log.e(LOG_TAG, "Fehler beim Löschen der Tabelle: " + ex.getMessage()); }

        //---------------

        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + TABLE_TakePlaceIn_DROP + " gelöscht.");
            db.execSQL(TABLE_TakePlaceIn_DROP);
        }
        catch (Exception ex) { Log.e(LOG_TAG, "Fehler beim Löschen der Tabelle: " + ex.getMessage()); }

        //---------------

        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + TABLE_Attendance_DROP + " gelöscht.");
            db.execSQL(TABLE_Attendance_DROP);
        }
        catch (Exception ex) { Log.e(LOG_TAG, "Fehler beim Löschen der Tabelle: " + ex.getMessage()); }

        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + TABLE_RoomOrder_DROP + " gelöscht.");
            db.execSQL(TABLE_RoomOrder_DROP);
        }
        catch (Exception ex) { Log.e(LOG_TAG, "Fehler beim Löschen der Tabelle: " + ex.getMessage()); }

        onCreate(db);
    }
}
