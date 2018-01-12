package com.example.pschm.klassenbuch.SqLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pschm on 21.12.2017.
 */

public class RoomOrderDataSource {
    private String[] columns = {
            KlassenbuchDbHelper.RoomRoomnumber,
            KlassenbuchDbHelper.RoomOrderPosX,
            KlassenbuchDbHelper.RoomOrderPosY,
            KlassenbuchDbHelper.RoomOrderBocked
    };

    private static final String LOG_TAG = RoomOrder.class.getSimpleName();

    private SQLiteDatabase database;
    private KlassenbuchDbHelper dbHelper;

    public RoomOrderDataSource(Context context) {
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

    public RoomOrder createRoomOrder(long roomnumber, int posX, int posY, int bocked) {
        Log.d(RoomOrderCommands.class.getSimpleName(), "roomnumber: " + roomnumber + " X: " + posX + " Y:" + posY + " B:" + bocked);
        ContentValues values = new ContentValues();
        values.put(KlassenbuchDbHelper.RoomRoomnumber, roomnumber);
        values.put(KlassenbuchDbHelper.RoomOrderPosX, posX);
        values.put(KlassenbuchDbHelper.RoomOrderPosY, posY);
        values.put(KlassenbuchDbHelper.RoomOrderBocked, bocked);

        database.insert(KlassenbuchDbHelper.TableRoomOrder, null, values);

        Cursor cursor = database.query(KlassenbuchDbHelper.TableRoomOrder,
                columns, KlassenbuchDbHelper.RoomRoomnumber + "=" + roomnumber,
                null, null, null, null);

        cursor.moveToFirst();
        RoomOrder roomOrder = cursorToRoomOrder(cursor);
        cursor.close();

        return roomOrder;
    }

    private RoomOrder cursorToRoomOrder(Cursor cursor) {
        int idRoomnumber = cursor.getColumnIndex(KlassenbuchDbHelper.RoomRoomnumber);
        int idPosX = cursor.getColumnIndex(KlassenbuchDbHelper.RoomOrderPosX);
        int idPosY = cursor.getColumnIndex(KlassenbuchDbHelper.RoomOrderPosY);
        int idBocked = cursor.getColumnIndex(KlassenbuchDbHelper.RoomOrderBocked);

        long roomnumber = cursor.getLong(idRoomnumber);
        int posX = cursor.getInt(idPosX);
        int posY = cursor.getInt(idPosY);
        int bocked = cursor.getInt(idBocked);

        RoomOrder roomOrder = new RoomOrder(roomnumber, posX, posY, bocked);

        return roomOrder;
    }

    public List<RoomOrder> getRoomOrderForRoom(long Roomnumber) {
        Log.d(LOG_TAG, "gettAllRoom");
        List<RoomOrder> RoomOrderList = new ArrayList<>();

        Cursor cursor = null;
        try{
            cursor= database.query(KlassenbuchDbHelper.TableRoomOrder, columns, KlassenbuchDbHelper.RoomRoomnumber + "=?", new String[]{ String.valueOf(Roomnumber) }, null, null, null);

        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler: " + ex.getMessage());
        }

        cursor.moveToFirst();
        RoomOrder roomOrder;

        while(!cursor.isAfterLast()) {
            roomOrder = cursorToRoomOrder(cursor);
            RoomOrderList.add(roomOrder);
            Log.d(LOG_TAG, "ID: " + roomOrder.getRoomnumber() + ", Inhalt: " + roomOrder.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return RoomOrderList;
    }

    public static List<RoomOrder> getStaticAllRoomList(Context c, int roomnumber){
        RoomOrderDataSource obj = new RoomOrderDataSource(c);
        obj.open();
        List<RoomOrder> ListRoomOrder = new ArrayList<>();

        ListRoomOrder = obj.getRoomOrderForRoom(roomnumber);

        obj.close();

        return ListRoomOrder;
    }

    public static void deleteRoomOrderInRoom(Context c, int roomnumber)
    {
        RoomOrderDataSource obj = new RoomOrderDataSource(c);
        obj.open();
        Log.d(LOG_TAG, "Delete All with Roomnumber: " + roomnumber);
        obj.database.delete(KlassenbuchDbHelper.TableRoomOrder, KlassenbuchDbHelper.RoomRoomnumber + "=?", new String[]{ String.valueOf(roomnumber) });
        obj.close();
    }
}
