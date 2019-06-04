package com.ashish.myteam;

public class Contri_Status_details {

    int ID;
    String name;
    boolean status;

    public Contri_Status_details(int ID, String name, boolean status) {
        this.name = name;
        this.ID = ID;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    long getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    boolean isStatus() {
        return status;
    }

    void setStatus(boolean status) {
        this.status = status;
    }
}