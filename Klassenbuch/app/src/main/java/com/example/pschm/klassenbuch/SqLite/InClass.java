package com.example.pschm.klassenbuch.SqLite;

/**
 * Created by pschm on 16.12.2017.
 */

public class InClass {
    private long classID;
    private long studentID;

    InClass(){};

    InClass(long classID, long studentID){
        this.classID = classID;
        this.studentID = studentID;
    }

    //--------------------------------

    public long getClassID(){
        return classID;
    }

    public void setClassID(long classID){
        this.classID = classID;
    }

    //--------------------------------

    public long getStudentID(){
        return studentID;
    }

    public void setStudentID(long studentID){
        this.studentID = studentID;
    }

    //--------------------------------

    @Override
    public String toString() {
        String output = classID +"- " + studentID;

        return output;
    }
}
