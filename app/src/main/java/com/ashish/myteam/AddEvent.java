package com.ashish.myteam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class AddEvent extends AppCompatActivity {

    EditText eventName, eventDesc, eventDate, approxContri;
    CheckBox selectallFrnd, contriButton;
    ListView eventMembers;
    Button eventAddImgBtn;
    ArrayList<String> eventMemb;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    ArrayList<Integer> mUserItems;
    ArrayList<teamMemberObject> team;
    String eventMembersString;
    String eventOwner;
    User owner;
    int ownerID;
    int ownerPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        eventName = (EditText)findViewById(R.id.eventName);
        eventDesc = (EditText)findViewById(R.id.eventDesc);
        eventDate = (EditText)findViewById(R.id.eventDate);
        selectallFrnd = (CheckBox) findViewById(R.id.selectFrnd);
        contriButton = (CheckBox) findViewById(R.id.collection_Box);
        eventMembers = (ListView) findViewById(R.id.eventMembers);
        eventAddImgBtn = (Button) findViewById(R.id.eventAddImgBtn);
        approxContri = (EditText)findViewById(R.id.approxContri);
        eventMemb = new ArrayList<String>();
        mUserItems = new ArrayList<>();

        eventMembers.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        owner = list_test.profileUser.get(0);
        eventOwner = owner.email;
        ownerID = owner.id;

        team = new ArrayList<>();
        String sql = "SELECT ID,name FROM table_user WHERE team = '"+owner.team+"'";
        HashMap<String, String> getUser = new HashMap<>();
        getUser.put("sql", sql);
        team.clear();
        PostResponseAsyncTask taskRead = new PostResponseAsyncTask(this, getUser, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                team = new JsonConverter<teamMemberObject>().toArrayList(s, teamMemberObject.class);
                if (team.size() == 0) {
                    Toast.makeText(AddEvent.this, "No Team Member", Toast.LENGTH_SHORT).show();
                    eventMembers.setAdapter(new choose_member_adapter(AddEvent.this,team));
                }else {
                    eventMembers.setAdapter(new choose_member_adapter(AddEvent.this,team));
                    ownerPosition = getOwnerPosition();
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


        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dobPicker = new DatePickerDialog(
                        AddEvent.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dobPicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dobPicker.show();
            }
        });

        contriButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contriButton.isChecked())
                {
                    approxContri.setFocusableInTouchMode(true);
                    approxContri.requestFocus();
                    approxContri.setFocusable(true);
                    approxContri.setClickable(true);
                }else{
                    approxContri.setFocusableInTouchMode(false);
                    approxContri.requestFocus();
                    approxContri.setText("");
                    approxContri.setFocusable(false);
                    approxContri.setClickable(false);
                }
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date;
                String dayS = String.valueOf(day);
                String monthS = String.valueOf(month);
                if(month < 10 )
                {
                    monthS = "0" + monthS;
                }
                if(day < 10 )
                {
                    dayS = "0" + dayS;
                }

                date = year + "-" + monthS + "-" + dayS;
                eventDate.setText(date);
            }
        };

        eventMembers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(selectallFrnd.isChecked())
                {
                    selectallFrnd.setChecked(false);
                }
                if(!mUserItems.contains(position)){
                    mUserItems.add(position);
                }else{
                    mUserItems.remove((Integer.valueOf(position)));
                }
            }
        });
        eventAddImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                eventMembersString ="";
                if(!mUserItems.contains(ownerPosition))
                {
                    mUserItems.add(ownerPosition);
                }

                for (int i = 0; i < mUserItems.size(); i++) {
                    eventMembersString = eventMembersString + String.valueOf(team.get(mUserItems.get(i)).id);
                    if (i != mUserItems.size() - 1) {
                        eventMembersString = eventMembersString + ",";
                    }
                }

                String name = eventName.getText().toString().trim();
                String desc = eventDesc.getText().toString().trim();
                String date = eventDate.getText().toString().trim();
                String contri = Boolean.toString(contriButton.isChecked());
                String contriAmount = approxContri.getText().toString().trim();

                if(name.length() >= 3 && desc.length() >=5 && date.length() >4)
                {
                    if(contriButton.isChecked()) {
                        if(contriAmount.length() > 0) {
                            addNewEvent(name, desc, date, contri, contriAmount );
                        }
                        else {
                            Toast.makeText(AddEvent.this, "Put Some approximate Contri Amount", Toast.LENGTH_LONG).show();

                        }
                    }
                    else {
                        contriAmount = "0";
                        addNewEvent(name, desc, date, contri, contriAmount );
                    }
                }
                else if (name.length() < 3)
                {
                    Toast.makeText(AddEvent.this, "Name length is less than 3", Toast.LENGTH_LONG).show();
                }
                else if (desc.length() <5)
                {
                    Toast.makeText(AddEvent.this, "Description Venue atleast", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(AddEvent.this, "Date should not be null", Toast.LENGTH_LONG).show();
                }
            }
        });

        selectallFrnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectallFrnd.isChecked())
                {
                    for ( int i=0; i < eventMembers.getChildCount(); i++) {
                        eventMembers.setItemChecked(i, true);
                    }
                    mUserItems.clear();
                    for (int i = 0; i < team.size(); i++) {
                        mUserItems.add(i);
                    }

                }
                else {
                    for ( int i=0; i < eventMembers.getChildCount(); i++) {
                        eventMembers.setItemChecked(i, false);
                    }
                    mUserItems.clear();
                    //Toast.makeText(AddEvent.this, eventMembersString, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public int getOwnerPosition(){
        for (int i = 0; i < team.size(); i++)
        {
            int id = team.get(i).id;
            if(id == owner.id)
            {
                return i;
            }
        }
        return -1;
    }
    public void addNewEvent(String name, String desc, String date, String contri, String contriAmount ){
        HashMap<String, String> EventData = new HashMap<>();
        EventData.put("name", Signup.capitailizeWord(name));
        EventData.put("owner", eventOwner);
        EventData.put("desc", desc);
        EventData.put("date", date);
        EventData.put("eventMemb", eventMembersString);
        EventData.put("contriBool", contri);
        EventData.put("ApxContri", contriAmount);

        PostResponseAsyncTask AddEvent = new PostResponseAsyncTask(AddEvent.this,
                EventData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if (s.contains("Error")) {
                    Toast.makeText(AddEvent.this, "Event Creation failed", Toast.LENGTH_LONG).show();
                } else if (s.contains("ErrorUpdate")){
                    Toast.makeText(AddEvent.this, "Event Update failed", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(AddEvent.this, "Event Created", Toast.LENGTH_LONG).show();
                    Intent events = new Intent(AddEvent.this, list_test.class);
                    events.putExtra("UserEmail", eventOwner);
                    startActivity(events);
                    finish();
                }
            }
        });
        AddEvent.setExceptionHandler(new ExceptionHandler() {
            @Override
            public void handleException(Exception e) {
                if (e != null && e.getMessage() != null) {
                    Toast.makeText(getApplicationContext(),
                            "" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        AddEvent.execute("https://voteforashish.000webhostapp.com/myTeam/AddEvent.php");
    }
}
