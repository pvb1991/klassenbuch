package com.example.pschm.klassenbuch.SqLite;

/**
 * Created by pschm on 16.12.2017.
 */

public class Informed {
    private long unitID;
    private long teacherID;
    private long classID;

    Informed(){}

    Informed(long unitID, long teacherID, long classID){
        this.unitID = unitID;
        this.teacherID = teacherID;
        this.classID = classID;
    }

    //--------------------------------

    public long getTeacherID(){
        return teacherID;
    }

    public void setTeacherID(long teacherID){
        this.teacherID = teacherID;
    }

    //--------------------------------

    public long getUnitID(){
        return unitID;
    }

    public void setUnitID (long unitID){
        this.unitID = unitID;
    }
    //--------------------------------

    public long getClassID(){
        return classID;
    }

    public void setClassID (long classID){
        this.classID = classID;
    }

    //--------------------------------
    @Override
    public String toString() {
        String output = unitID + " - " + teacherID + " - " + classID;

        return output;
    }
}
