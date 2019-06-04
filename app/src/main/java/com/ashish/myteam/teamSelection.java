package com.ashish.myteam;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.annotations.SerializedName;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class teamSelection extends AppCompatActivity implements AsyncResponse {

    FloatingActionButton add_team;
    ListView teamslist;
    String[] teamstest;
    ArrayList<teamlist> teams;
    String clickedValue = "Select Team First";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_selection);

        add_team = (FloatingActionButton) findViewById(R.id.addTeamfab);
        teamslist = (ListView) findViewById(R.id.teamlist);

        add_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerTeam = new Intent(teamSelection.this, RegisterTeam.class);
                startActivity(registerTeam);
            }
        });


        teams = new ArrayList<>();
        PostResponseAsyncTask taskRead = new PostResponseAsyncTask(teamSelection.this, this);
        taskRead.execute("https://voteforashish.000webhostapp.com/myTeam/teamsList.php");

        teamslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                clickedValue = teams.get(position).name.toString();

                Intent intent = new Intent(teamSelection.this, Signup.class);

                intent.putExtra("ListViewClickedValue", clickedValue);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(teamSelection.this, Signup.class);
        startActivity(setIntent);
        finish();
    }


    @Override
    public void processFinish(String s) {
        teams = new JsonConverter<teamlist>().toArrayList(s,teamlist.class);
        if(teams.size() == 0) {
            teamslist.setAdapter(new team_list_adapter(this, teams));
            Toast.makeText(teamSelection.this, "Register your team first",Toast.LENGTH_LONG).show();
        }
        else {
            teamslist.setAdapter(new team_list_adapter(this, teams));
        }
    }

    public class teamlist implements Serializable{

        @SerializedName("team_name")
        public String name;
    }
}
