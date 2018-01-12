package com.example.pschm.klassenbuch.SqLite;


/**
 * Created by pschm on 16.12.2017.
 */

public class Room {
    private long roomnumber;
    private int floor;

    public Room(){}

    public Room(long roomnumber, int floor){
        this.roomnumber = roomnumber;
        this.floor = floor;
    }

    public Room(long roomnumber){
        this.roomnumber = roomnumber;
    }

    public Room(int floor){
        this.floor = floor;
    }

    //--------------------------------

    public long getRoomnumber(){
        return roomnumber;
    }

    public void setRoomnumber(long roomnumber){
        this.roomnumber = roomnumber;
    }

    //--------------------------------

    public int getFloor(){
        return floor;
    }

    public void setFloor(int floor){
        this.floor = floor;
    }

    //--------------------------------

    @Override
    public String toString() {
        String output = "Stockwerk: " + floor +" | Raumnummer: " + roomnumber;

        return output;
    }

    public String FloorToString() {
        String output = floor + "";

        return output;
    }

    public String RoomnumberToString() {
        String output = roomnumber + "";

        return output;
    }
}
