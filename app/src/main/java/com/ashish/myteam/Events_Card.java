package com.ashish.myteam;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Events_Card implements Serializable {


    @SerializedName("event_ID")
    public int eId;

    @SerializedName("event_name")
    public String eName;

    @SerializedName("event_owner")
    public String eOwner;

    @SerializedName("event_desc")
    public String eDesc;

    @SerializedName("event_date")
    public String eDate;

    @SerializedName("event_members")
    public String eMembers;

    @SerializedName("contribution")
    public String eContiBool;

    @SerializedName("contri_amount")
    public int eContri;

    @SerializedName("event_contri")
    public int eContriTotal;

    @SerializedName("spent_amount")
    public int eContriSpent;

    @SerializedName("remaining_contri")
    public int eContriRem;

    @SerializedName("credit_transactions")
    public String eCdtTrans;

    @SerializedName("debit_transactions")
    public String eDbtTrans;

    @SerializedName("credit_members")
    public String eCdtMembs;

}