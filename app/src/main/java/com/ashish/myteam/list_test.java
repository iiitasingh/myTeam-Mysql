package com.ashish.myteam;

import android.app.Activity;
import android.app.Dialog;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class list_test extends Activity {

    ImageView yourImage, teamIcon, addEvent, birthdayImg, EventsImg, transactionImg, addTransacImg, ContriWalletImg,addEventImg2;
    TextView yourName, teamName, teamName1, birthdayTxt, addEventTxt, EventsTxt, transactionTxt, addTransacTxt, ContriWalletTxt;
    SwipeRefreshLayout swipeRefresh;
    String TempHolder;
    byte[] usrImg;
    Dialog logoutProfile;
    Animation atg;
    public static ArrayList<User> profileUser;
    private Session session;
    HashMap<String,String> getUser = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listtest);

        session = new Session(this);
        if (!session.loggedin()) {
            Logout();
        }
        TempHolder = getIntent().getStringExtra("UserEmail");
        atg = AnimationUtils.loadAnimation(this, R.anim.atg);

        logoutProfile = new Dialog(this);
        addEvent = (ImageView) findViewById(R.id.addEvent);
        yourImage = (ImageView) findViewById(R.id.userImg);
        teamIcon = (ImageView) findViewById(R.id.teamIcon);
        yourName = (TextView) findViewById(R.id.nickName);
        teamName = (TextView) findViewById(R.id.teamNm);
        teamName1 = (TextView) findViewById(R.id.teamNm1);
        birthdayImg = (ImageView) findViewById(R.id.birthdayImg);
        birthdayTxt = (TextView) findViewById(R.id.birthdayTxt);
        EventsImg = (ImageView) findViewById(R.id.EventsImg);
        EventsTxt = (TextView) findViewById(R.id.EventsTxt);
        addEventTxt = (TextView) findViewById(R.id.addEventTxt);
        transactionImg = (ImageView) findViewById(R.id.transactionImg);
        transactionTxt = (TextView) findViewById(R.id.transactionTxt);
        addTransacImg = (ImageView) findViewById(R.id.addTransacImg);
        addTransacTxt = (TextView) findViewById(R.id.addTransacTxt);
        ContriWalletImg = (ImageView) findViewById(R.id.ContriWalletImg);
        ContriWalletTxt = (TextView) findViewById(R.id.ContriWalletTxt);
        addEventImg2 = (ImageView) findViewById(R.id.addEventImg2);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        profileUser = new ArrayList<>();
        String sql = "SELECT * FROM table_user WHERE email like '" + TempHolder +"'";
        getUser.put("sql",sql);
        PostResponseAsyncTask taskRead = new PostResponseAsyncTask(list_test.this, getUser, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                profileUser = new JsonConverter<User>().toArrayList(s,User.class);
                if(profileUser.size() > 0) {
                    yourName.setText("Welcome " + profileUser.get(0).nickname);
                    teamName.setText(profileUser.get(0).team);
                    teamName1.setText(profileUser.get(0).team);
                    usrImg = org.apache.commons.codec.binary.Base64.decodeBase64(profileUser.get(0).image);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(usrImg, 0, usrImg.length);
                    yourImage.setImageBitmap(bitmap);
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "Check Your Internet and swipe down to refresh", Toast.LENGTH_LONG).show();
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
        taskRead.execute("https://voteforashish.000webhostapp.com/myTeam/getUserData.php");

        yourImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutPopup(list_test.this, profileUser.get(0));
            }
        });
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MemIntent = new Intent(list_test.this, AddEvent.class);
                Bundle args = new Bundle();
                args.putSerializable("USER", profileUser.get(0));
                MemIntent.putExtras(args);
                startActivity(MemIntent);
            }
        });
        addEventTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MemIntent = new Intent(list_test.this, AddEvent.class);
                Bundle args = new Bundle();
                args.putSerializable("USER", profileUser.get(0));
                MemIntent.putExtras(args);
                startActivity(MemIntent);
            }
        });
        EventsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MemIntent = new Intent(list_test.this, Events.class);
                startActivity(MemIntent);
            }
        });
        EventsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MemIntent = new Intent(list_test.this, Events.class);
                startActivity(MemIntent);
            }
        });

        transactionImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MemIntent = new Intent(list_test.this, Transaction.class);
                startActivity(MemIntent);
            }
        });
        transactionTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MemIntent = new Intent(list_test.this, Transaction.class);
                startActivity(MemIntent);
            }
        });


        birthdayTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bdcalender = new Intent(list_test.this, BirthdayCalender.class);
                startActivity(bdcalender);
            }
        });
        birthdayImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bdcalender = new Intent(list_test.this, BirthdayCalender.class);
                startActivity(bdcalender);
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateUserData();
            }
        });

        //TeamMailHolder = new String[]{TempHolder, tm};
        teamName1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(list_test.this, Home.class);
                startActivity(home);
            }
        });

        teamIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(list_test.this, Home.class);
                startActivity(home);
            }
        });

        addTransacImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent home = new Intent(list_test.this, AddTransaction.class);
                    startActivity(home);
            }
        });

        addTransacTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(list_test.this, AddTransaction.class);
                startActivity(home);
            }
        });

        ContriWalletImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(list_test.this, wallet.class);
                startActivity(home);
            }
        });

        ContriWalletTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(list_test.this, wallet.class);
                startActivity(home);
            }
        });

        addEventImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(list_test.this, Main2Activity.class);
                startActivity(home);
            }
        });
    }

    private void updateUserData() {
        PostResponseAsyncTask taskRead = new PostResponseAsyncTask(list_test.this, getUser, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                profileUser = new JsonConverter<User>().toArrayList(s,User.class);
                if(profileUser.size() > 0) {
                    yourName.setText("Welcome " + profileUser.get(0).nickname);
                    teamName.setText(profileUser.get(0).team);
                    teamName1.setText(profileUser.get(0).team);
                    usrImg = org.apache.commons.codec.binary.Base64.decodeBase64(profileUser.get(0).image);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(usrImg, 0, usrImg.length);
                    yourImage.setImageBitmap(bitmap);
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "Check Your Internet and swipe down to refresh", Toast.LENGTH_LONG).show();
                }

            }
        });
        taskRead.execute("https://voteforashish.000webhostapp.com/myTeam/getUserData.php");
        swipeRefresh.setRefreshing(false);
    }

    Button logout, profile;
    ImageView logoutUserImg;

    private void logoutPopup(Activity popActivity, final User membMail) {

        logoutProfile.setContentView(R.layout.logout_popup);

        int width1 = (int) (popActivity.getResources().getDisplayMetrics().widthPixels * .8);
        int height1 = (int) (popActivity.getResources().getDisplayMetrics().heightPixels * .6);
        logoutProfile.getWindow().setLayout(width1, height1);
        logoutProfile.getWindow().setGravity(Gravity.CENTER);
        logoutProfile.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        logoutProfile.show();

        logout = (Button) logoutProfile.findViewById(R.id.btnLogout);
        profile = (Button) logoutProfile.findViewById(R.id.btnProfile);
        logoutUserImg = (ImageView) logoutProfile.findViewById(R.id.logoutUserImg);


        byte[] usrerImg = org.apache.commons.codec.binary.Base64.decodeBase64(membMail.image);
        Bitmap bitmap = BitmapFactory.decodeByteArray(usrerImg, 0, usrerImg.length);
        logoutUserImg.setImageBitmap(bitmap);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(list_test.this, resume_Page.class);
                //newIntent.putExtra("ID",membMail.id);
                startActivity(newIntent);
                logoutProfile.dismiss();
            }
        });
    }

    private void Logout() {
        session.setLoggedin(false);
        finish();
        startActivity(new Intent(list_test.this, MainActivity.class));
    }

}
