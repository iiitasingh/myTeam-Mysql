package com.ashish.myteam;


import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class PreviousEvents extends Fragment {


    public PreviousEvents() {
        // Required empty public constructor
    }

    ArrayList<Events_Card> prvCardList;
    ListView previouseventlist;
    Event_List_Adapter adapterPrev;
    String prvTeamName;
    User owner_P;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View previousView = inflater.inflate(R.layout.fragment_previous_events, container, false);
        previouseventlist = (ListView) previousView.findViewById(R.id.previousEventCardList);
        prvCardList = new ArrayList<>();

        owner_P = list_test.profileUser.get(0);
        prvTeamName = owner_P.team;
        String events = owner_P.events;

        if(events.length() > 0) {
            String[] names = events.split(",", 0);
            String sql = "SELECT * FROM table_event WHERE event_date < CURDATE() AND event_ID IN (" + DatabaseHelper.makePlaceholders(names.length -1) + ")  ORDER BY event_date DESC";
            HashMap<String, String> getPreEvents = new HashMap<>();
            getPreEvents.put("events", events);
            getPreEvents.put("sql", sql);
            prvCardList.clear();
            PostResponseAsyncTask taskRead = new PostResponseAsyncTask(getActivity(), getPreEvents, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    prvCardList = new JsonConverter<Events_Card>().toArrayList(s, Events_Card.class);
                    if (prvCardList.size() == 0) {
                        adapterPrev = new Event_List_Adapter(getActivity(), R.layout.event_list_template, prvCardList,prvTeamName);
                        previouseventlist.setAdapter(adapterPrev);
                        Toast.makeText(getActivity(), "No Previous Event",Toast.LENGTH_LONG).show();
                    } else {
                        adapterPrev = new Event_List_Adapter(getActivity(), R.layout.event_list_template, prvCardList,prvTeamName);
                        previouseventlist.setAdapter(adapterPrev);
                    }
                }
            });
            taskRead.setExceptionHandler(new ExceptionHandler() {
                @Override
                public void handleException(Exception e) {
                    if (e != null && e.getMessage() != null) {
                        Toast.makeText(getActivity(),
                                "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            taskRead.execute("https://voteforashish.000webhostapp.com/myTeam/getEvents.php");
        }
        else {
            adapterPrev = new Event_List_Adapter(getActivity(), R.layout.event_list_template, prvCardList,prvTeamName);
            previouseventlist.setAdapter(adapterPrev);
            Toast.makeText(getActivity(), "No Event",Toast.LENGTH_LONG).show();
        }

        return previousView;
    }

}
