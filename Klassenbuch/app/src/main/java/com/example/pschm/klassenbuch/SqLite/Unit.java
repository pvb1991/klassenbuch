package com.example.pschm.klassenbuch.SqLite;


/**
 * Created by pschm on 16.12.2017.
 */

public class Unit {
    private long id;
    private String title;

    public Unit(){}

    public Unit(long id, String title){
        this.id = id;
        this.title = title;
    }

    //--------------------------------

    public long getID(){
        return id;
    }

    public void setID(long id){
        this.id = id;
    }

    //--------------------------------

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    //--------------------------------

    @Override
    public String toString() {
        String output = id +"- " + title;

        return output;
    }
}
