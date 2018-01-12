package com.example.pschm.klassenbuch.SqLite;


/**
 * Created by pschm on 16.12.2017.
 */

public class Mark {
    private long studentID;
    private long teacherID;
    private long unitID;
    private String type;
    private int mark;
    private String note;

    Mark(){}

    Mark(long studentID, long teacherID, long unitID, String type, int mark, String note){
        this.unitID = unitID;
        this.teacherID = teacherID;
        this.studentID = studentID;
        this.type = type;
        this.mark = mark;
        this.note = note;
    }

    //--------------------------------

    public long getStudentID(){
        return studentID;
    }

    public void setStudentID(long studentID){
        this.studentID = studentID;
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

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    //--------------------------------

    public int getMark(){
        return mark;
    }

    public void setMark(int mark){
        this.mark = mark;
    }

    //--------------------------------

    public String getNote(){
        return note;
    }

    public void setEmail(String note){
        this.note = note;
    }

    //--------------------------------

    @Override
    public String toString() {
        String output = unitID +" - " + teacherID;

        return output;
    }
}
