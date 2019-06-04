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


public class UpcomingEvents extends Fragment {



    public UpcomingEvents() {
        // Required empty public constructor
    }

    ArrayList<Events_Card> cardList;
    ListView eventlist;
    Event_List_Adapter adapter;
    String teamName;
    User owner_Up;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_upcoming_events, container, false);
        eventlist = (ListView) v.findViewById(R.id.eventCardList);
        cardList = new ArrayList<>();

        owner_Up = list_test.profileUser.get(0);
        teamName = owner_Up.team;
        String events = owner_Up.events;
        if(events.length() > 0) {
            String[] names = events.split(",", 0);
            String sql = "SELECT * FROM table_event WHERE event_date >= CURDATE() AND event_ID IN (" + DatabaseHelper.makePlaceholders(names.length -1) + ")  ORDER BY event_date";

            HashMap<String, String> getEvents = new HashMap<>();
            getEvents.put("events", events);
            getEvents.put("sql", sql);
            cardList.clear();
            PostResponseAsyncTask taskRead = new PostResponseAsyncTask(getActivity(), getEvents, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    cardList = new JsonConverter<Events_Card>().toArrayList(s, Events_Card.class);
                    if (cardList.size() == 0) {
                        adapter = new Event_List_Adapter(getActivity(), R.layout.event_list_template, cardList,teamName);
                        eventlist.setAdapter(adapter);
                        Toast.makeText(getActivity(), "No Upcoming Event",Toast.LENGTH_LONG).show();
                    } else {
                        adapter = new Event_List_Adapter(getActivity(), R.layout.event_list_template, cardList,teamName);
                        eventlist.setAdapter(adapter);
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
            adapter = new Event_List_Adapter(getActivity(), R.layout.event_list_template, cardList,teamName);
            eventlist.setAdapter(adapter);
            Toast.makeText(getActivity(), "No Event",Toast.LENGTH_LONG).show();
        }

//        Cursor eventDetails = MainActivity.db.getEvents(sql,names);
//        cardList.clear();
//        if(eventDetails.getCount() == 0) {
//            adapter = new Event_List_Adapter(getActivity(), R.layout.event_list_template, cardList);
//            Toast.makeText(getActivity(), "There is no upcoming event!",Toast.LENGTH_LONG).show();
//        }
//        else {
//            while(eventDetails.moveToNext()){
//                String evtname = eventDetails.getString(1);
//                String evtDesc = eventDetails.getString(3);
//                String evtDate = eventDetails.getString(4);
//                String evtContri = eventDetails.getString(6);
//                Long evtId = eventDetails.getLong(0);
//                String evtMems = eventDetails.getString(5);
//                Long contri = eventDetails.getLong(8);
//                Long spent = eventDetails.getLong(9);
//                Long rem = eventDetails.getLong(10);
//                Long approx = eventDetails.getLong(7);
//                String CrMems = eventDetails.getString(13);
//                cardList.add(new Events_Card(evtname,teamName,evtDesc,evtDate,evtContri,evtId,evtMems,contri,spent,rem,approx,CrMems));
//            }
//            adapter = new Event_List_Adapter(getActivity(), R.layout.event_list_template, cardList);
//        }


        return v;
    }

}
