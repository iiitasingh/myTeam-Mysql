package com.ashish.myteam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class wallet extends AppCompatActivity {

    TextView remainingAmount;
    ListView pendingContri;
    public static ArrayList<Events_Card> cardList;
    ArrayList<Events_Card> pendingCardList;
    User owner;
    String teamName;
    EventWalletPend_Adapter adapter;
    String remSum = "0";
    String[] creditMems;
    String ownerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        remainingAmount = (TextView)findViewById(R.id.remainingAmount);
        pendingContri = (ListView)findViewById(R.id.pendingContri);
        cardList = new ArrayList<>();
        pendingCardList = new ArrayList<>();

        owner = list_test.profileUser.get(0);
        teamName = owner.team;
        ownerId = String.valueOf(owner.id);
        String events = owner.events;

        if(events.length() > 0) {
            String[] names = events.split(",", 0);
            String sql = "SELECT SUM(remaining_contri) FROM table_event WHERE contribution = 'true' AND event_ID IN (" + DatabaseHelper.makePlaceholders(names.length -1) + ")  ORDER BY event_date";
            HashMap<String, String> getPreEvents = new HashMap<>();
            getPreEvents.put("events", events);
            getPreEvents.put("sql", sql);
            PostResponseAsyncTask taskRead = new PostResponseAsyncTask(wallet.this, getPreEvents, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    if(s.contains("Error")){
                        remSum = "0";
                        Toast.makeText(wallet.this, ""+s,Toast.LENGTH_LONG).show();
                    }
                    else{
                        remSum = s;
                    }
                    remainingAmount.setText(""+remSum+".00");
                    pendingContribution(events);
                }
            });
            taskRead.setExceptionHandler(new ExceptionHandler() {
                @Override
                public void handleException(Exception e) {
                    if (e != null && e.getMessage() != null) {
                        Toast.makeText(wallet.this,
                                "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            taskRead.execute("https://voteforashish.000webhostapp.com/myTeam/wallet.php");
        }
        else {
            Toast.makeText(wallet.this, "No events", Toast.LENGTH_LONG).show();
        }

        remainingAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contri = new Intent(wallet.this,RemainingContri.class);
                startActivity(contri);
            }
        });


    }
    public void pendingContribution(String events) {
        String[] names = events.split(",", 0);
        String sql2 = "SELECT * FROM table_event WHERE contribution = 'true' AND event_ID IN (" + DatabaseHelper.makePlaceholders(names.length - 1) + ")  ORDER BY event_date";
        cardList.clear();
        pendingCardList.clear();
        HashMap<String, String> getPreEvents = new HashMap<>();
        getPreEvents.put("events", events);
        getPreEvents.put("sql", sql2);
        PostResponseAsyncTask taskRead = new PostResponseAsyncTask(wallet.this, getPreEvents, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                cardList = new JsonConverter<Events_Card>().toArrayList(s, Events_Card.class);
                if (s.contains("Error")) {
                    adapter = new EventWalletPend_Adapter(wallet.this, R.layout.pending_contri_template, pendingCardList, teamName);
                    Toast.makeText(wallet.this, "There is no contribution event!", Toast.LENGTH_LONG).show();
                } else {
                    for (int i = 0; i < cardList.size(); i++) {
                        creditMems = cardList.get(i).eCdtMembs.split(",", 0);
                        if (Arrays.asList(creditMems).contains(ownerId)) {
                            //pendingCardList.add(cardList.get(i));
                        } else {
                            pendingCardList.add(cardList.get(i));
                        }
                    }
                    adapter = new EventWalletPend_Adapter(wallet.this, R.layout.pending_contri_template, pendingCardList, teamName);
                }
                pendingContri.setAdapter(adapter);
            }
        });
        taskRead.setExceptionHandler(new ExceptionHandler() {
            @Override
            public void handleException(Exception e) {
                if (e != null && e.getMessage() != null) {
                    Toast.makeText(wallet.this,
                            "" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        taskRead.execute("https://voteforashish.000webhostapp.com/myTeam/getEvents.php");

    }

}
