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

public class RoomDataSource {
    private String[] columns = {
            KlassenbuchDbHelper.RoomRoomnumber,
            KlassenbuchDbHelper.RoomFloor
    };

    private String[] columnFloor = {
            KlassenbuchDbHelper.RoomFloor
    };

    private static final String LOG_TAG = Room.class.getSimpleName();

    private SQLiteDatabase database;
    private KlassenbuchDbHelper dbHelper;

    public RoomDataSource(Context context) {
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

    public Room createRoom(long roomnumber, int floor) {
        Log.d(LOG_TAG, "1.");

        ContentValues values = new ContentValues();
        values.put(KlassenbuchDbHelper.RoomRoomnumber, roomnumber);
        values.put(KlassenbuchDbHelper.RoomFloor, floor);
        Log.d(LOG_TAG, "2");
        database.insert(KlassenbuchDbHelper.TableRoom, null, values);
        Log.d(LOG_TAG, "3");
        Cursor cursor = database.query(KlassenbuchDbHelper.TableRoom,
                columns, KlassenbuchDbHelper.RoomRoomnumber + "=" + roomnumber,
                null, null, null, null);

        cursor.moveToFirst();
        Room room = cursorToRoom(cursor);
        cursor.close();

        return room;
    }

    private Room cursorToRoom(Cursor cursor) {
        int idRoomnumber = cursor.getColumnIndex(KlassenbuchDbHelper.RoomRoomnumber);
        int idFloor = cursor.getColumnIndex(KlassenbuchDbHelper.RoomFloor);

        long roomnumber = cursor.getLong(idRoomnumber);
        int floor = cursor.getInt(idFloor);

        Room room = new Room(roomnumber, floor);

        return room;
    }

    private Room cursorToFloor (Cursor cursor) {
        int idFloor = cursor.getColumnIndex(KlassenbuchDbHelper.RoomFloor);

        int floor = cursor.getInt(idFloor);

        Room room = new Room(floor);

        return room;
    }

    public List<Room> getAllRoom() {
        Log.d(LOG_TAG, "gettAllRoom");
        List<Room> RoomList = new ArrayList<>();

        Cursor cursor = database.query(KlassenbuchDbHelper.TableRoom, columns, null, null, null, null, null);

        cursor.moveToFirst();
        Room room;

        while(!cursor.isAfterLast()) {
            room = cursorToRoom(cursor);
            RoomList.add(room);
            Log.d(LOG_TAG, "ID: " + room.getRoomnumber() + ", Inhalt: " + room.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return RoomList;
    }

    public static List<Room> getStaticAllRoomList(Context c){
        RoomDataSource obj = new RoomDataSource(c);
        obj.open();
        List<Room> ListRoom = obj.getAllRoom();
        obj.close();

        return ListRoom;
    }

    public  String[] getAllFloors() {
        Log.d(LOG_TAG, "gettAllFloors");
        List<Room> RoomList = new ArrayList<>();

        Cursor cursor = database.query(KlassenbuchDbHelper.TableRoom, columnFloor,KlassenbuchDbHelper.RoomFloor, null, KlassenbuchDbHelper.RoomFloor, null, null);

        cursor.moveToFirst();
        Room room;

        while(!cursor.isAfterLast()) {
            room = cursorToFloor(cursor);
            RoomList.add(room);
            Log.d(LOG_TAG, "Floor: " + room.getFloor());
            cursor.moveToNext();
        }
        cursor.close();

        String[] Floors = new String[RoomList.size()];

        for(int i=0; i!=RoomList.size(); i++){
            Floors[i] = RoomList.get(i).FloorToString();
        }
        return Floors;
    }

    public static String[] getStaticAllFloors(Context c) {
        RoomDataSource obj = new RoomDataSource(c);
        obj.open();
        String[] Floors = obj.getAllFloors();
        obj.close();

        return Floors;
    }

    public static String[] getStaticAllRoomsInFloor(Context c, String Floor) {
        RoomDataSource obj = new RoomDataSource(c);
        obj.open();
        Log.d(LOG_TAG, "gettAllRoomsInFloor");
        List<Room> RoomList = new ArrayList<>();
        Log.d(LOG_TAG, "Gewähltes Stockwerk: " + Floor);
        Cursor cursor = obj.database.query(KlassenbuchDbHelper.TableRoom, obj.columns, KlassenbuchDbHelper.RoomFloor + "=?", new String[]{Floor}, null, null, null);

        cursor.moveToFirst();
        Room room;

        while(!cursor.isAfterLast()) {
            room = obj.cursorToRoom(cursor);
            RoomList.add(room);
            Log.d(LOG_TAG, "Roomnumber: " + room.getRoomnumber());
            cursor.moveToNext();
        }
        cursor.close();

        String[] Room = new String[RoomList.size()];

        for(int i=0; i!=RoomList.size(); i++){
            Room[i] = RoomList.get(i).RoomnumberToString();
        }
        obj.close();

        return Room;
    }
}
