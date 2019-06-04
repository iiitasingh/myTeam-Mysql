package com.ashish.myteam;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("ID")
    public int id;

    @SerializedName("email")
    public String email;

    @SerializedName("aadhaar")
    public String aadhaar;

    @SerializedName("name")
    public String name;

    @SerializedName("nick_name")
    public String nickname;

    @SerializedName("team")
    public String team;

    @SerializedName("image")
    public String image;

    @SerializedName("dob")
    public String dob;

    @SerializedName("food")
    public String food;

    @SerializedName("mobile")
    public String mobile;

    @SerializedName("bl_grp")
    public String bl_grp;

    @SerializedName("pan_no")
    public String pan_no;

    @SerializedName("events")
    public String events;

    @SerializedName("user_transaction")
    public String transactions;

    @SerializedName("user_about")
    public String about;

    @SerializedName("user_designation")
    public String designation;

    @SerializedName("user_hobbies")
    public String hobbies;

}