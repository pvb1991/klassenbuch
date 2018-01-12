package com.example.pschm.klassenbuch.SqLite;

/**
 * Created by pschm on 16.12.2017.
 */

public class Attendance {
    private long unitID;
    private long studentID;
    private int date;
    private int delay;

    Attendance(){}

    Attendance(long unitID, long studentID, int date, int delay){
        this.unitID = unitID;
        this.studentID = studentID;
        this.date = date;
        this.delay = delay;
    }

    //--------------------------------

    public long getStudentID(){
        return studentID;
    }

    public void setStudentID(long studentID){
        this.studentID = studentID;
    }

    //--------------------------------

    public long getUnitID(){
        return unitID;
    }

    public void setUnitID (long unitID){
        this.unitID = unitID;
    }

    //--------------------------------

    public int getDate(){
        return date;
    }

    public void setDate (int date){
        this.date = date;
    }

    //--------------------------------

    public int getDelay(){
        return delay;
    }

    public void setDelay (int delay){
        this.delay = delay;
    }

    //--------------------------------

    @Override
    public String toString() {
        String output = unitID +"- " + studentID + " - " + date + " - " + delay;

        return output;
    }
}
