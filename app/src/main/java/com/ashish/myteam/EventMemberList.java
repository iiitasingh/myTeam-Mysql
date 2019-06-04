package com.ashish.myteam;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EventMemberList extends AppCompatActivity {

    String[] mems;
    ListView memList;
    String[] memsArray;
    String[] memsArray1;
    Contri_Status_adapter listAdapter;
    ArrayList<Contri_Status> membersList;
    ArrayList<Contri_Status_details> membersList1;
    ArrayList<String> memsArray2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_member_list);
        mems = getIntent().getStringArrayExtra("members");


        memsArray = mems[0].split(",", 0);
        memsArray1 = mems[1].split(",", 0);
        memsArray2 = new ArrayList<String>(Arrays.asList(memsArray1));
        for (int k = 0; k < memsArray2.size(); k++) {
            if (memsArray2.get(k).equals("")) {
                memsArray2.remove(k);
            }
        }


        if (memsArray.length > 0) {
            memList = (ListView) findViewById(R.id.memberListOfEvent);
            membersList = new ArrayList<>();
            membersList1 = new ArrayList<>();
            String sql = "SELECT ID, name FROM table_user WHERE ID IN (" + DatabaseHelper.makePlaceholders(memsArray.length) + ")";
            membersList.clear();
            membersList1.clear();
            HashMap<String, String> getPreEvents = new HashMap<>();
            getPreEvents.put("events", mems[0]);
            getPreEvents.put("sql", sql);
            PostResponseAsyncTask taskRead = new PostResponseAsyncTask(EventMemberList.this, getPreEvents, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    membersList = new JsonConverter<Contri_Status>().toArrayList(s, Contri_Status.class);
                    if (s.contains("Error")) {
                        listAdapter = new Contri_Status_adapter(EventMemberList.this, R.layout.contri_status_template, membersList1);
                        Toast.makeText(EventMemberList.this, "No member for this event!", Toast.LENGTH_LONG).show();
                    } else {
                        for (int i = 0; i < membersList.size(); i++) {
                            membersList1.add(new Contri_Status_details(membersList.get(i).id, membersList.get(i).name, false));
                        }

                        for (int i = 0; i < memsArray2.size(); i++) {
                            for (int j = 0; j < memsArray.length; j++) {
                                if (membersList1.get(j).getID() == Integer.parseInt(memsArray2.get(i))) {
                                    membersList1.get(j).setStatus(true);
                                }
                            }
                        }
                        listAdapter = new Contri_Status_adapter(EventMemberList.this, R.layout.contri_status_template, membersList1);
                    }
                    memList.setAdapter(listAdapter);
                }
            });
            taskRead.setExceptionHandler(new ExceptionHandler() {
                @Override
                public void handleException(Exception e) {
                    if (e != null && e.getMessage() != null) {
                        Toast.makeText(EventMemberList.this,
                                "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            taskRead.execute("https://voteforashish.000webhostapp.com/myTeam/getEvents.php");

        }
    }

    public class Contri_Status implements Serializable {
        @SerializedName("ID")
        public int id;

        @SerializedName("name")
        public String name;
    }
}
