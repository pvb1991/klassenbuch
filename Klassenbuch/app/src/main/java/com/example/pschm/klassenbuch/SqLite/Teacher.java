package com.example.pschm.klassenbuch.SqLite;


/**
 * Created by pschm on 16.12.2017.
 */

public class Teacher {
    private long id;
    private String vorname;
    private String name;
    private String street;
    private String housenumber;
    private int zipcode;
    private String locus;
    private String email;
    private int admin;
    private String password;

    public Teacher(){}

    public Teacher(String vorname, String name, String street, String housenumber, int zipcode, String locus, String email, String password, int admin, long id){
        this.vorname = vorname;
        this.name = name;
        this.street = street;
        this.housenumber = housenumber;
        this.zipcode = zipcode;
        this.locus = locus;
        this.email = email;
        this.password = password;
        this.admin = admin;
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

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    //--------------------------------

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    //--------------------------------

    public int getAdmin(){
        return admin;
    }

    public void setAdmin(int admin){
        this.admin = admin;
    }

    //--------------------------------
    @Override
    public String toString() {
        String output = "ID: " + id +" - " + name + ", " + vorname + " - " + street +" "+ housenumber + ", " + zipcode + " " + locus + " - " + email + " - " + password;

        return output;
    }
}
