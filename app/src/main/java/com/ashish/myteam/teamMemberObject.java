package com.ashish.myteam;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class teamMemberObject implements Serializable {

    @SerializedName("ID")
    public int id;

    @SerializedName("name")
    public String name;

}
