package com.ashish.myteam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class transactionListAdapter extends BaseAdapter {

    private Context context;
    private  int layout;
    private ArrayList<transaction_data> transCard;
    String type = "debit";

    //constructor initializing the values
    public transactionListAdapter(Context context, int resource, ArrayList<transaction_data> eventCardDetail) {
        this.context = context;
        this.layout = resource;
        this.transCard = eventCardDetail;
    }

    @Override
    public int getCount() {
        return transCard.size();
    }

    @Override
    public Object getItem(int position) {
        return transCard.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView transTypeImg;
        TextView transFor,transForName,transAmount,transDate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        transactionListAdapter.ViewHolder holder = new transactionListAdapter.ViewHolder();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layout, null, false);


        holder.transTypeImg = view.findViewById(R.id.debitIimageView);
        holder.transFor = view.findViewById(R.id.TextPaidFor);
        holder.transForName = view.findViewById(R.id.textEventName);
        holder.transAmount = view.findViewById(R.id.textAmount);
        holder.transDate = view.findViewById(R.id.textDate);
        transaction_data card = transCard.get(position);

        if(card.tType.equals(type))
        {
            holder.transTypeImg.setImageResource(R.drawable.ic_up_arrow);
            holder.transFor.setText(card.tDesc);
            holder.transForName.setText(String.valueOf(card.eventId));
        }
        else {
            holder.transTypeImg.setImageResource(R.drawable.ic_down_arrow);
            holder.transFor.setText(String.valueOf(card.eventId));
            holder.transForName.setText(String.valueOf(card.memberId));
        }

        holder.transAmount.setText(String.valueOf(card.tAmount));
        holder.transDate.setText(card.tDate);
        return view;
    }
}
