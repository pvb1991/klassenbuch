package com.example.pschm.klassenbuch.SqLite;


/**
 * Created by pschm on 15.12.2017.
 */

public class Student {
    private long id;
    private String vorname;
    private String name;
    private String street;
    private String housenumber;
    private int zipcode;
    private String locus;

    public Student(){}

    public Student(String vorname, String name, String street, String housenumber, int zipcode, String locus, long id){
        this.vorname = vorname;
        this.name = name;
        this.street = street;
        this.housenumber = housenumber;
        this.zipcode = zipcode;
        this.locus = locus;
        this.id = id;
    }

    //--------------------------------

    public long getID(){
        return id;
    }

    public void setID(long id){
        this.id = id;
    }

    //--------------------------------

    public String getVorname(){
        return vorname;
    }

    public void setVorname(String vorname){
        this.vorname = vorname;
    }

    //--------------------------------

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    //--------------------------------

    public String getStreet(){
        return street;
    }

    public void setStreet(String street){
        this.street = street;
    }

    //--------------------------------

    public String getHousenumber(){
        return housenumber;
    }

    public void setHousenumber(String housenumber){
        this.housenumber = housenumber;
    }

    //--------------------------------

    public int getZipcode(){
        return zipcode;
    }

    public void setZipcode(int zipcode){
        this.zipcode = zipcode;
    }

    //--------------------------------

    public String getLocus(){
        return locus;
    }

    public void setLocus(String locus){
        this.locus = locus;
    }

    //--------------------------------
    @Override
    public String toString() {
        String output ="ID: " + id + " - " + name + ", " + vorname + " - " + street +": "+ housenumber + ", " + zipcode + " " + locus;

        return output;
    }
}
