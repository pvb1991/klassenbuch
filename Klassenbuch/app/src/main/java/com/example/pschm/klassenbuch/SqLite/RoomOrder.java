package com.example.pschm.klassenbuch.SqLite;

/**
 * Created by pschm on 21.12.2017.
 */

public class RoomOrder {
    private long roomnumber;
    private int PosX;
    private int PosY;
    private int bocked;

    public RoomOrder(){}

    public RoomOrder(long roomnumber, int PosX, int PosY, int bocked){
        this.roomnumber = roomnumber;
        this.PosX = PosX;
        this.PosY = PosY;
        this.bocked = bocked;
    }

    public long getRoomnumber(){
        return roomnumber;
    }

    public void setRoomnumber(long roomnumber){
        this.roomnumber = roomnumber;
    }

    public int getPosX(){
        return PosX;
    }

    public void setPosX(int posX){
        this.PosX = posX;
    }

    public int getPosY() {
        return PosY;
    }

    public void setPosY(int posY) {
        PosY = posY;
    }

    public int getBocked() {
        return bocked;
    }

    public void setBocked(int bocked) {
        this.bocked = bocked;
    }

    @Override
    public String toString() {
        String output = roomnumber + " " + PosX + "/" + PosY + " " + bocked;

        return output;
    }
}
