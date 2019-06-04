package com.ashish.myteam;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Event_List_Adapter extends BaseAdapter {

    private Context context;
    private  int layout;
    private String team;
    private ArrayList<Events_Card> eventCard;

    //constructor initializing the values
    public Event_List_Adapter(Context context, int resource, ArrayList<Events_Card> eventCardDetail,String team) {
        this.context = context;
        this.layout = resource;
        this.eventCard = eventCardDetail;
        this.team = team;
    }

    @Override
    public int getCount() {
        return eventCard.size();
    }

    @Override
    public Object getItem(int position) {
        return eventCard.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView contriImg;
        TextView eventName,teamName,eventDesc,eventDate, TotalContri, SpentContri, RemainingContri, eventMemList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Event_List_Adapter.ViewHolder holder = new Event_List_Adapter.ViewHolder();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layout, null, false);
        holder.contriImg = view.findViewById(R.id.contriImg);
        holder.eventName = view.findViewById(R.id.eventTextView7);
        holder.teamName = view.findViewById(R.id.eventCardTeam);
        holder.eventDesc = view.findViewById(R.id.eventCardDesc);
        holder.eventDate = view.findViewById(R.id.EventtextView6);
        holder.TotalContri = view.findViewById(R.id.TotalContri);
        holder.SpentContri = view.findViewById(R.id.SpentContri);
        holder.RemainingContri = view.findViewById(R.id.RemainingContri);
        holder.eventMemList = view.findViewById(R.id.eventMemList);

        Events_Card card = eventCard.get(position);

        holder.eventName.setText(card.eName);
        holder.teamName.setText(team);
        holder.eventDesc.setText(card.eDesc);
        holder.eventDate.setText("Date: "+card.eDate);
        holder.TotalContri.setText("");
        holder.SpentContri.setText("");
        holder.RemainingContri.setText("");

        if(card.eContiBool.equals("true"))
        {
            holder.contriImg.setImageResource(R.drawable.tick);
        }
        else {
            holder.contriImg.setImageResource(R.drawable.cancel);
        }

        holder.eventMemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent memList = new Intent(context,Main2Activity.class);
                context.startActivity(memList);
            }
        });

        return view;
    }
}
