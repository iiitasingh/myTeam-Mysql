package com.ashish.myteam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;

public class AddTransaction extends AppCompatActivity implements OnItemSelectedListener {

    RadioGroup transacType;
    EditText amount, transDate, transDesc;
    Spinner payee,payeeEvents;
    RadioButton radioDebit, radioCredit;
    Button transacSubmit;
    ArrayList<teamMemberObject> memberDetail;
    ArrayList<Integer> membersIds;
    ArrayList<String> memberNames;
    ArrayList<EventListForTrans> eventDetail;
    ArrayList<String> eventNames;
    ArrayList<String> eventMembers;
    ArrayList<Integer> eventIds;
    String[] nothing = {""};
    String transacMode, payeeName, eventNm, eventMems;
    Integer selectedEventId;
    private DatePickerDialog.OnDateSetListener mDateSet;
    User addUserTrans;
    ArrayList<String> authorized;
    String[] payeeStringArray;
    String payeeid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_transaction);

        addUserTrans = list_test.profileUser.get(0);

        transacType = (RadioGroup) findViewById(R.id.typeGrp);
        amount = (EditText) findViewById(R.id.transactionAmount);
        transDate = (EditText) findViewById(R.id.transactionDate);
        payee = (Spinner) findViewById(R.id.payeeMember);
        payeeEvents = (Spinner) findViewById(R.id.payeeEvents);
        radioDebit = (RadioButton) findViewById(R.id.RadioDebit);
        radioCredit = (RadioButton) findViewById(R.id.RadioCredit);
        transacSubmit = (Button) findViewById(R.id.TransacSubmit);
        transDesc = (EditText) findViewById(R.id.transactionDesc);
        memberNames = new ArrayList<>();
        eventNames = new ArrayList<>();
        membersIds = new ArrayList<>();
        eventMembers = new ArrayList<>();
        eventIds = new ArrayList<>();
        eventDetail = new ArrayList<>();
        memberDetail = new ArrayList<>();;

        authorized = new ArrayList<>();

        String sql = "SELECT event_ID, event_name, event_members FROM table_event WHERE contribution = 'true' AND event_owner = '" + addUserTrans.email + "'";

        HashMap<String, String> getUser = new HashMap<>();
        getUser.put("sql",sql);
        PostResponseAsyncTask taskRead = new PostResponseAsyncTask(this, getUser, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                eventDetail = new JsonConverter<EventListForTrans>().toArrayList(s, EventListForTrans.class);
                if (eventDetail.size() == 0) {
                    Toast.makeText(AddTransaction.this, "No Event to show", Toast.LENGTH_LONG).show();
                } else {
                    eventNames.clear();
                    eventMembers.clear();
                    eventIds.clear();
                    for(int i =0;i< eventDetail.size();i++){
                        eventIds.add(eventDetail.get(i).eid);
                        eventNames.add(eventDetail.get(i).ename);
                        eventMembers.add(eventDetail.get(i).eMembers);
                    }

                    if (transacType.getCheckedRadioButtonId() == radioCredit.getId() && eventNames.size() > 0) {
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddTransaction.this,
                                R.layout.spinner_text, eventNames);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        transDesc.setFocusableInTouchMode(false);
                        transDesc.requestFocus();
                        transDesc.setFocusable(false);
                        transDesc.setClickable(false);
                        payeeEvents.setEnabled(true);
                        payeeEvents.setClickable(true);
                        payeeEvents.setAdapter(dataAdapter);
                        transacMode = "credit";
                    }

                    payee.setOnItemSelectedListener(AddTransaction.this);
                    payeeEvents.setOnItemSelectedListener(AddTransaction.this);

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

        transacType.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.RadioDebit) {
                    ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(AddTransaction.this,
                            R.layout.spinner_text, nothing);
                    dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    payee.setEnabled(false);
                    payee.setClickable(false);
                    payee.setAdapter(dataAdapter1);

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddTransaction.this,
                            R.layout.spinner_text, eventNames);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    payeeEvents.setEnabled(true);
                    payeeEvents.setClickable(true);
                    payeeEvents.setAdapter(dataAdapter);

                    transDesc.setFocusableInTouchMode(true);
                    transDesc.requestFocus();
                    transDesc.setFocusable(true);
                    transDesc.setClickable(true);
                    transacMode = "debit";
                } else {
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(AddTransaction.this,
                            R.layout.spinner_text, memberNames);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    payee.setEnabled(true);
                    payee.setClickable(true);
                    payee.setAdapter(dataAdapter2);

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddTransaction.this,
                            R.layout.spinner_text, eventNames);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    payeeEvents.setEnabled(true);
                    payeeEvents.setClickable(true);
                    payeeEvents.setAdapter(dataAdapter);

                    transDesc.setFocusableInTouchMode(false);
                    transDesc.requestFocus();
                    transDesc.setFocusable(false);
                    transDesc.setClickable(false);
                    transacMode = "credit";
                }
            }

        });

        transDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dobPicker = new DatePickerDialog(
                        AddTransaction.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSet,
                        year, month, day);
                dobPicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dobPicker.show();
            }
        });

        mDateSet = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date;
                String dayS = String.valueOf(day);
                String monthS = String.valueOf(month);
                if (month < 10) {
                    monthS = "0" + monthS;
                }
                if (day < 10) {
                    dayS = "0" + dayS;
                }

                date = year + "-" + monthS + "-" + dayS;
                transDate.setText(date);
            }
        };


        transacSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (transacType.getCheckedRadioButtonId() == radioCredit.getId()) {
                    if (eventNames.size() > 0) {
                        String money = amount.getText().toString().trim();
                        String date = transDate.getText().toString().trim();
                        String desc = "";
                        if (money.length() > 0 && date.length() > 0) {
                            addNewTransaction(selectedEventId.toString(),payeeid,String.valueOf(addUserTrans.id), money, date, transacMode, desc,eventMems);
                        } else if (money.length() <= 0) {
                            Toast.makeText(AddTransaction.this, "Add Some Money", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(AddTransaction.this, "Select Transaction Date", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(AddTransaction.this, "You haven't created any event!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (eventNames.size() > 0) {
                        String money = amount.getText().toString().trim();
                        String date = transDate.getText().toString().trim();
                        String desc = transDesc.getText().toString().trim();
                        if (money.length() > 0 && date.length() > 0 && desc.length() > 5) {
                            addNewTransaction(selectedEventId.toString(),"",String.valueOf(addUserTrans.id), money, date, transacMode, desc,eventMems);
                        } else if (money.length() <= 0) {
                            Toast.makeText(AddTransaction.this, "Add Some Money", Toast.LENGTH_LONG).show();
                        } else if (date.length() <= 0) {
                            Toast.makeText(AddTransaction.this, "Select Transaction Date", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(AddTransaction.this, "Write Some Description", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(AddTransaction.this, "You haven't created any event!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }


    @Override
     public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        if(parent.getId() == R.id.payeeEvents)
        {
            if(eventNames.size() >0) {
                selectedEventId = eventIds.get(position);
                eventNm = parent.getItemAtPosition(position).toString();
                eventMems = eventMembers.get(position).toString();
                payeeStringArray = eventMems.split(",", 0);

                String sql1 = "SELECT ID,name FROM table_user WHERE ID IN (" + DatabaseHelper.makePlaceholders(payeeStringArray.length) + ")";

                HashMap<String, String> getUser1 = new HashMap<>();
                getUser1.put("sql",sql1);
                getUser1.put("events",eventMems);
                PostResponseAsyncTask taskRead = new PostResponseAsyncTask(this, getUser1, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        memberDetail = new JsonConverter<teamMemberObject>().toArrayList(s, teamMemberObject.class);
                        if (memberDetail.size() == 0) {
                            Toast.makeText(AddTransaction.this, "No Member to show", Toast.LENGTH_LONG).show();
                        } else {
                            memberNames.clear();
                            membersIds.clear();
                            for(int i =0;i< memberDetail.size();i++){
                                memberNames.add(memberDetail.get(i).name);
                                membersIds.add(memberDetail.get(i).id);
                            }

                            if (transacType.getCheckedRadioButtonId() == radioDebit.getId()) {
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddTransaction.this,
                                        R.layout.spinner_text, nothing);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                payee.setEnabled(false);
                                payee.setClickable(false);
                                transDesc.setFocusableInTouchMode(true);
                                transDesc.requestFocus();
                                transDesc.setFocusable(true);
                                transDesc.setClickable(true);
                                payee.setAdapter(dataAdapter);
                                transacMode = "debit";
                            } else {
                                ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(AddTransaction.this,
                                        R.layout.spinner_text, memberNames);
                                dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                payee.setEnabled(true);
                                payee.setClickable(true);
                                transDesc.setFocusableInTouchMode(false);
                                transDesc.requestFocus();
                                transDesc.setFocusable(false);
                                transDesc.setClickable(false);
                                payee.setAdapter(dataAdapter1);
                                transacMode = "credit";
                            }
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
                taskRead.execute("https://voteforashish.000webhostapp.com/myTeam/getEvents.php");

            }
        }
        else if(parent.getId() == R.id.payeeMember) {
            if (memberNames.size() > 0) {
                payeeName = parent.getItemAtPosition(position).toString();
                payeeid = membersIds.get(position).toString();
                //Toast.makeText(AddTransaction.this, payeeName +"Selected", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void addNewTransaction(String eventId,String memberId,String ownerid,String amount,String date,String type,String desc, String eventMems){
        HashMap<String, String> transData = new HashMap<>();
        transData.put("eventId", eventId);
        transData.put("memberId", memberId);
        transData.put("ownerId", ownerid);
        transData.put("amount", amount);
        transData.put("date",date);
        transData.put("type", type);
        transData.put("desc", desc);
        transData.put("eventMems", eventMems);

        PostResponseAsyncTask loginTask = new PostResponseAsyncTask(AddTransaction.this,
                transData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if (s.contains("Error")) {
                    Toast.makeText(AddTransaction.this, "transaction , Insert Error", Toast.LENGTH_LONG).show();
                } else if (s.contains("Error1")) {
                    Toast.makeText(AddTransaction.this, "Error: event, credit_transaction", Toast.LENGTH_LONG).show();
                } else if (s.contains("Error2")) {
                    Toast.makeText(AddTransaction.this, "Error: event, credit_member", Toast.LENGTH_LONG).show();
                }else if (s.contains("Error3")) {
                    Toast.makeText(AddTransaction.this, "Error: user, transaction", Toast.LENGTH_LONG).show();
                }else if (s.contains("Error4")) {
                    Toast.makeText(AddTransaction.this, "Error: event, debit_transaction", Toast.LENGTH_LONG).show();
                } else if (s.contains("Error5")) {
                    Toast.makeText(AddTransaction.this, "Error: user, debit, transaction", Toast.LENGTH_LONG).show();
                }else if (s.contains("Error7")) {
                    Toast.makeText(AddTransaction.this, "Error: sum, credit_transaction", Toast.LENGTH_LONG).show();
                } else if (s.contains("Error8")) {
                    Toast.makeText(AddTransaction.this, "Error: sum, debit_transaction", Toast.LENGTH_LONG).show();
                }else if (s.contains("Error9")) {
                    Toast.makeText(AddTransaction.this, "Error: event, update balance", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(AddTransaction.this, "Transaction added successfully", Toast.LENGTH_LONG).show();
                    Intent events = new Intent(AddTransaction.this, list_test.class);
                    events.putExtra("UserEmail", addUserTrans.email);
                    startActivity(events);
                    finish();
                }
            }
        });
        loginTask.setExceptionHandler(new ExceptionHandler() {
            @Override
            public void handleException(Exception e) {
                if (e != null && e.getMessage() != null) {
                    Toast.makeText(getApplicationContext(),
                            "" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        loginTask.execute("https://voteforashish.000webhostapp.com/myTeam/AddTransaction.php");
    }
}
