package com.example.pschm.klassenbuch.SqLite;


/**
 * Created by pschm on 16.12.2017.
 */

public class SeatingPlan {
    private long roomnumber;
    private long studentID;
    private long unitId;
    private long teacherID;
    private int posY;
    private int posX;

    public SeatingPlan(){}

    SeatingPlan(long roomnumber, long studentID, long unitId, long teacherID, int posY, int posX){
        this.roomnumber = roomnumber;
        this.studentID = studentID;
        this.unitId = unitId;
        this.teacherID = teacherID;
        this.posY = posY;
        this.posX = posX;
    }

    //--------------------------------

    public long getRoomnumber(){
        return roomnumber;
    }

    public void setRoomnumber(long roomnumber){
        this.roomnumber = roomnumber;
    }

    //--------------------------------

    public long getStudentID(){
        return studentID;
    }

    public void setStudentID(long studentID){
        this.studentID = studentID;
    }

    //--------------------------------

    public long getUnitId(){
        return unitId;
    }

    public void setUnitId(long unitId){
        this.unitId = unitId;
    }

    //--------------------------------

    public long getTeacherID(){
        return teacherID;
    }

    public void setTeacherID(long teacherID){
        this.teacherID = teacherID;
    }

    //--------------------------------

    public int getPosY(){
        return posY;
    }

    public void setPosY(int posY){
        this.posY = posY;
    }

    //--------------------------------

    public int getPosX(){
        return posX;
    }

    public void setPosX(int posX){
        this.posX = posX;
    }

    //--------------------------------

    @Override
    public String toString() {
        String output = roomnumber +"- " + studentID + " - " + unitId + " - " + teacherID +" - "+ posX + ", " + posY ;

        return output;
    }
}
