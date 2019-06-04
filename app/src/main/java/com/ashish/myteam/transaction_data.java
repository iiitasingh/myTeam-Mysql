package com.ashish.myteam;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class transaction_data implements Serializable {

    @SerializedName("transaction_id")
    public int id;

    @SerializedName("event_id")
    public int eventId;

    @SerializedName("member_id")
    public int memberId;

    @SerializedName("amount")
    public int tAmount;

    @SerializedName("transaction_date")
    public String tDate;

    @SerializedName("transaction_type")
    public String tType;

    @SerializedName("transaction_desc")
    public String tDesc;

}
