package com.ashish.myteam;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;


public class Home extends AppCompatActivity {

    FloatingActionButton usericon;
    TextView tmname;
    public static ArrayList<User> TeamList;
    ListView your_teams;
    Dialog popupDial;
    private User homeUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        popupDial = new Dialog(this);
        tmname = (TextView) findViewById(R.id.team_name);
        TeamList = new ArrayList<>();
        your_teams = (ListView) findViewById(R.id.yourteamlist);
        usericon = (FloatingActionButton) findViewById(R.id.homefab);

        homeUser = list_test.profileUser.get(0);

        usericon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup1(Home.this, homeUser);
            }
        });


        tmname.setText("Team " + homeUser.team);

        HashMap<String, String> getUser = new HashMap<>();
        getUser.put("team", homeUser.team);
        TeamList.clear();
        PostResponseAsyncTask taskRead = new PostResponseAsyncTask(this, getUser, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                TeamList = new JsonConverter<User>().toArrayList(s, User.class);
                if (TeamList.size() == 0) {
                    your_teams.setAdapter(new your_team_list_adapter(getApplicationContext(), R.layout.your_team_list_view, TeamList));
                    Toast.makeText(Home.this, "No Member in this Team!", Toast.LENGTH_LONG).show();
                } else {
                    your_teams.setAdapter(new your_team_list_adapter(getApplicationContext(), R.layout.your_team_list_view, TeamList));
                }
            }
        });
        taskRead.setExceptionHandler(new ExceptionHandler() {
            @Override
            public void handleException(Exception e) {
                if (e != null && e.getMessage() != null) {
                    Toast.makeText(getApplicationContext(),
                            "" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        taskRead.execute("https://voteforashish.000webhostapp.com/myTeam/getTeamMembers.php");


        your_teams.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int Membid = TeamList.get(position).id;
                if (homeUser.id == Membid) {
                    Intent memberIntent = new Intent(Home.this, resume_Page.class);
                    startActivity(memberIntent);
                } else {
                    Intent memberIntent = new Intent(Home.this, teammate_resume.class);
                    memberIntent.putExtra("ID", position);
                    startActivity(memberIntent);
                }
            }
        });

    }
    //#####################  PopUp1 Attributes  ##################

    ImageView Usimg;
    TextView MemName, MemEmail, MemTeam, MemDob, MemFood, MemMob, MemBgp, MemNknam, MemDesig1;
    Button edit;
    byte[] imageUser;

    private void showPopup1(Activity popActivity, final User Usrmail) {
        popupDial.setContentView(R.layout.activity_bottomappbar);

        int width1 = (int) (popActivity.getResources().getDisplayMetrics().widthPixels * .82);
        int height1 = (int) (popActivity.getResources().getDisplayMetrics().heightPixels * 0.8);
        popupDial.getWindow().setLayout(width1, height1);
        popupDial.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupDial.show();

        Usimg = (ImageView) popupDial.findViewById(R.id.PopUpUserImg);
        MemNknam = (TextView) popupDial.findViewById(R.id.MemNknam1);
        MemName = (TextView) popupDial.findViewById(R.id.MemName1);
        MemEmail = (TextView) popupDial.findViewById(R.id.MemEmail1);
        MemTeam = (TextView) popupDial.findViewById(R.id.MemTeam1);
        MemDob = (TextView) popupDial.findViewById(R.id.MemDob1);
        MemFood = (TextView) popupDial.findViewById(R.id.MemFood1);
        MemMob = (TextView) popupDial.findViewById(R.id.MemMob1);
        MemBgp = (TextView) popupDial.findViewById(R.id.MemBgp1);
        MemDesig1 = (TextView) popupDial.findViewById(R.id.MemDesig1);
        edit = (Button) popupDial.findViewById(R.id.Edit);

        MemEmail.setText("Email: " + Usrmail.email);
        MemName.setText(Usrmail.name);
        MemNknam.setText(Usrmail.nickname);
        MemDesig1.setText(Usrmail.designation);
        MemTeam.setText("Team: " + Usrmail.team);
        MemDob.setText("DOB: " + Usrmail.dob);
        MemFood.setText("Food: " + Usrmail.food);
        MemBgp.setText("BGrp: " + Usrmail.bl_grp);
        MemMob.setText("Mob: " + Usrmail.mobile);

        imageUser = org.apache.commons.codec.binary.Base64.decodeBase64(Usrmail.image);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageUser, 0, imageUser.length);
        Usimg.setImageBitmap(bitmap);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent memberIntent = new Intent(Home.this, resume_Page.class);
                startActivity(memberIntent);
                popupDial.dismiss();
            }
        });
    }

}
