package com.ashish.myteam;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EventListForTrans implements Serializable {

    @SerializedName("event_ID")
    public int eid;

    @SerializedName("event_name")
    public String ename;

    @SerializedName("event_members")
    public String eMembers;
}
