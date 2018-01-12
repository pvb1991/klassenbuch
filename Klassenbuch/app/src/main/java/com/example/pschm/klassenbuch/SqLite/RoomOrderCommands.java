package com.example.pschm.klassenbuch.SqLite;

import android.content.Context;
import android.util.Log;

import com.example.pschm.klassenbuch.openDialog;

/**
 * Created by pschm on 22.12.2017.
 */

public class RoomOrderCommands {
    public static final String LOG_TAG_ROOMORDER = RoomOrder.class.getSimpleName();
    private static RoomOrderDataSource roomOrderDataSource;
    private static RoomOrderCommands roc = new RoomOrderCommands();


    public static void InsertRoomOrder(Context c, long roomNumber, int posX, int posY, int bocked) {
        roc.roomOrderDataSource = new RoomOrderDataSource(c);

        Log.d(LOG_TAG_ROOMORDER, "Die Datenquelle wird geöffnet.");
        roc.roomOrderDataSource.open();

        RoomOrder roomOrder = roc.roomOrderDataSource.createRoomOrder(roomNumber, posX, posY, bocked);

        Log.d(LOG_TAG_ROOMORDER, "Es wurde der folgende Eintrag in die Datenbank geschrieben:");
        Log.d(LOG_TAG_ROOMORDER, "ID: " + roomOrder.getRoomnumber() + ", Inhalt: " + roomOrder.toString());

        Log.d(RoomOrderCommands.class.getSimpleName(), "roomnumber: " + roomNumber + " X: " + posX + " Y:" + posY + " B:" + bocked);

        Log.d(LOG_TAG_ROOMORDER, "Die Datenquelle wird geschlossen.");
        roc.roomOrderDataSource.close();
    }
}
