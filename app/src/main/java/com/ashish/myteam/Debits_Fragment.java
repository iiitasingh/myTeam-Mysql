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

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class Debits_Fragment extends Fragment {


    public Debits_Fragment() {
        // Required empty public constructor
    }
    ArrayList<transaction_data> transacList;
    ListView transacListView;
    transactionListAdapter transacAdapter;
    User debitTrans;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View transView = inflater.inflate(R.layout.fragment_debits_, container, false);
        transacListView = (ListView)transView.findViewById(R.id.transDebitList);
        transacList = new ArrayList<>();

        debitTrans = list_test.profileUser.get(0);
        String trans = debitTrans.transactions;

        if(trans.length() > 0) {
            String[] names = trans.split(",",0);
            String sql = "SELECT * FROM table_transaction WHERE transaction_type = 'debit' AND transaction_id IN (" + DatabaseHelper.makePlaceholders((names.length -1)) + ")  ORDER BY transaction_date DESC";
            HashMap<String, String> getPreEvents = new HashMap<>();
            getPreEvents.put("events", trans);
            getPreEvents.put("sql", sql);
            transacList.clear();
            PostResponseAsyncTask taskRead = new PostResponseAsyncTask(getActivity(), getPreEvents, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    transacList = new JsonConverter<transaction_data>().toArrayList(s, transaction_data.class);
                    if (transacList.isEmpty()) {
                        transacAdapter = new transactionListAdapter(getActivity(), R.layout.transaction_list_template, transacList);
                        Toast.makeText(getActivity(), "No Debit Transactions for you",Toast.LENGTH_LONG).show();
                        transacListView.setAdapter(transacAdapter);
                    } else {
                        transacAdapter = new transactionListAdapter(getActivity(), R.layout.transaction_list_template, transacList);
                        transacListView.setAdapter(transacAdapter);
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
            transacAdapter = new transactionListAdapter(getActivity(), R.layout.transaction_list_template, transacList);
            Toast.makeText(getActivity(), "No Transactions ",Toast.LENGTH_LONG).show();
            transacListView.setAdapter(transacAdapter);
        }
        return transView;
    }

}
