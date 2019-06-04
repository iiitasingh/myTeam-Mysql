package com.ashish.myteam;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.ashish.myteam.wallet.cardList;

public class RemainingContri extends AppCompatActivity {

    ListView contriEvents;
    User owner;
    String teamName;
    EventListWallet_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remaining_contri);

        contriEvents = (ListView)findViewById(R.id.contriEvents);

        owner = list_test.profileUser.get(0);
        teamName = owner.team;
        String events = owner.events;

        if(cardList.size() == 0) {
            adapter = new EventListWallet_Adapter(RemainingContri.this, R.layout.event_list_template, cardList,teamName);
            Toast.makeText(RemainingContri.this, "There is no event!",Toast.LENGTH_LONG).show();
        }
        else {
            adapter = new EventListWallet_Adapter(RemainingContri.this, R.layout.event_list_template, cardList,teamName);
        }
        contriEvents.setAdapter(adapter);

    }
}
