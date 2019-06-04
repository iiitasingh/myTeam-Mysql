package com.ashish.myteam;


import android.app.usage.UsageEvents;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.gson.annotations.SerializedName;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import androidx.appcompat.app.AppCompatActivity;


import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BirthdayCalender extends AppCompatActivity {

    private static final String TAG = "BirthdayCalender";

    CompactCalendarView compactCalendar;
    TextView current;
    Calendar cal;
    ListView birthdays;
    ArrayList<BirthdayList> birthdayList;
    ArrayList<EventList> evenCalenderList;
    ArrayList<String> names;
    private String[] dob_arr = {};
    User calenderOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.birthday_calender);

        calenderOwner = list_test.profileUser.get(0);

        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        current = (TextView) findViewById(R.id.currentMonthYr);
        birthdays = (ListView) findViewById(R.id.birtdayList);

        cal = Calendar.getInstance();
        current.setText(DateFormat.format("MMMM yyyy", cal));
        compactCalendar.setFirstDayOfWeek(Calendar.MONDAY);

        ///////  Getting Birthdays from db  //////////////
        birthdayList = new ArrayList<BirthdayList>();

        HashMap<String, String> getUser = new HashMap<>();
        String sql = "SELECT name,dob FROM table_user WHERE team = '" + calenderOwner.team + "'";
        getUser.put("sql", sql);
        birthdayList.clear();
        PostResponseAsyncTask taskRead = new PostResponseAsyncTask(this, getUser, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                birthdayList = new JsonConverter<BirthdayList>().toArrayList(s, BirthdayList.class);
                if (birthdayList.size() == 0) {
                    Toast.makeText(BirthdayCalender.this, "No Data", Toast.LENGTH_SHORT).show();
                }else {
                    setbdEvent(birthdayList);
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

        ///////  Getting Events from db  //////////////
        evenCalenderList = new ArrayList<EventList>();
        String eventString = calenderOwner.events;
        String[] User_events = eventString.split(",", 0);
        String sql1 = "SELECT event_name,event_date FROM table_event WHERE event_ID IN (" + DatabaseHelper.makePlaceholders(User_events.length-1) + ")";
        HashMap<String, String> getEvent = new HashMap<>();
        getEvent.put("events", eventString);
        getEvent.put("sql", sql1);
        evenCalenderList.clear();
        PostResponseAsyncTask taskEventRead = new PostResponseAsyncTask(this, getEvent, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                evenCalenderList = new JsonConverter<EventList>().toArrayList(s, EventList.class);
                if (birthdayList.size() == 0) {
                    Toast.makeText(BirthdayCalender.this, "No Event Data", Toast.LENGTH_SHORT).show();
                    setEvent(evenCalenderList);
                }else {
                    setEvent(evenCalenderList);
                }
            }
        });
        taskEventRead.setExceptionHandler(new ExceptionHandler() {
            @Override
            public void handleException(Exception e) {
                if (e != null && e.getMessage() != null) {
                    Toast.makeText(getApplicationContext(),
                            "" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        taskEventRead.execute("https://voteforashish.000webhostapp.com/myTeam/getEvents.php");

      //////////////////   Checking Events for today's Date   //////////////////
        Date today = Calendar.getInstance().getTime();
        List<Event> events = compactCalendar.getEvents(today);
        names = new ArrayList<String>();
        if (events.size() > 0) {

            names.clear();
            //Toast.makeText(BirthdayCalender.this, "Happy Birthday " + events.get(0).getData(), Toast.LENGTH_SHORT).show();
            for (int i = 0; i < events.size(); i++) {
                Event ash = events.get(i);
                names.add(ash.getData().toString());
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    BirthdayCalender.this,
                    android.R.layout.simple_list_item_1,
                    names);

            birthdays.setAdapter(arrayAdapter);

        } else {
            birthdays.setAdapter(null);
        }

        /////////   define a listener to receive callbacks when certain events happen.   ////////////////

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendar.getEvents(dateClicked);
                names = new ArrayList<String>();
                if (events.size() > 0) {

                    names.clear();
                    //Toast.makeText(BirthdayCalender.this, "Happy Birthday " + events.get(0).getData(), Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < events.size(); i++) {
                        Event ash = events.get(i);
                        names.add(ash.getData().toString());
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                            BirthdayCalender.this,
                            android.R.layout.simple_list_item_1,
                            names);

                    birthdays.setAdapter(arrayAdapter);

                } else {
                    birthdays.setAdapter(null);
                }

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                current.setText(DateFormat.format("MMMM yyyy", firstDayOfNewMonth));
            }
        });
    }

    public void setbdEvent(ArrayList<BirthdayList> dobdate) {

        int i = dobdate.size();
        int today = Calendar.getInstance().get(Calendar.YEAR);
        String currDate;

        for (int j = 0; j < i; j++) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date data = null;
            try {
                data = sdf.parse(dobdate.get(j).dob);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            currDate = DateFormat.format("dd/MM", data) + "/" + today + " 11:00:00";

            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date data1 = null;
            try {
                data1 = sdf1.parse(currDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long millis = data1.getTime();
            Event ev1 = new Event(Color.GREEN, millis, "Happy Birthday " + dobdate.get(j).name);
            compactCalendar.addEvent(ev1);
        }

    }

    public void setEvent(ArrayList<EventList> evtdate) {

        int i = evtdate.size();
        int today = Calendar.getInstance().get(Calendar.YEAR);
        String currDate;

        for (int j = 0; j < i; j++) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date data = null;
            try {
                data = sdf.parse(evtdate.get(j).date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            currDate = DateFormat.format("dd/MM", data) + "/" + today + " 11:00:00";

            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date data1 = null;
            try {
                data1 = sdf1.parse(currDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long millis = data1.getTime();
            Event ev1 = new Event(Color.GREEN, millis, "Event: " + evtdate.get(j).name);
            compactCalendar.addEvent(ev1);
        }

    }

    public class BirthdayList implements Serializable {

        @SerializedName("name")
        public String name;

        @SerializedName("dob")
        public String dob;
    }

    public class EventList implements Serializable {

        @SerializedName("event_name")
        public String name;

        @SerializedName("event_date")
        public String date;
    }
}
