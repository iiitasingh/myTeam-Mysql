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
public class Credits_Fragment extends Fragment {


    public Credits_Fragment() {
        // Required empty public constructor
    }
    ArrayList<transaction_data> transList;
    ListView transListView;
    transactionListAdapter transAdapter;
    User creditTrans;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View transacView = inflater.inflate(R.layout.fragment_credits, container, false);
        transListView = (ListView)transacView.findViewById(R.id.transCreditList);
        transList = new ArrayList<>();

        creditTrans = list_test.profileUser.get(0);
        String trans1 = creditTrans.transactions;

        if(trans1.length() > 0) {
            String[] names1 = trans1.split(",",0);
            String sql = "SELECT * FROM table_transaction WHERE transaction_type = 'credit' AND transaction_id IN (" + DatabaseHelper.makePlaceholders((names1.length -1)) + ")  ORDER BY transaction_date DESC";
            HashMap<String, String> getPreEvents = new HashMap<>();
            getPreEvents.put("events", trans1);
            getPreEvents.put("sql", sql);
            transList.clear();
            PostResponseAsyncTask taskRead = new PostResponseAsyncTask(getActivity(), getPreEvents, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    if (s.contains("Error")){
                        Toast.makeText(getActivity(), ""+s, Toast.LENGTH_LONG).show();
                    }else {
                        transList = new JsonConverter<transaction_data>().toArrayList(s, transaction_data.class);
                        if (transList.isEmpty()) {
                            transAdapter = new transactionListAdapter(getActivity(), R.layout.transaction_list_template, transList);
                            transListView.setAdapter(transAdapter);
                            Toast.makeText(getActivity(), "No Credit Transactions for you in DB", Toast.LENGTH_LONG).show();
                        } else {
                            transAdapter = new transactionListAdapter(getActivity(), R.layout.transaction_list_template, transList);
                            transListView.setAdapter(transAdapter);
                        }
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
            transAdapter = new transactionListAdapter(getActivity(), R.layout.transaction_list_template, transList);
            transListView.setAdapter(transAdapter);
            Toast.makeText(getActivity(), "No Transactions ",Toast.LENGTH_LONG).show();
        }

        return transacView;
    }

}
