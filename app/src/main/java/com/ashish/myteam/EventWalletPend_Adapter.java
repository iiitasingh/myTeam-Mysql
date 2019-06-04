package com.ashish.myteam;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class EventWalletPend_Adapter extends BaseAdapter {

    private Context context;
    private  int layout;
    private ArrayList<Events_Card> eventCard;
    private String team;

    //constructor initializing the values
    public EventWalletPend_Adapter(Context context, int resource, ArrayList<Events_Card> eventCardDetail,String team) {
        this.context = context;
        this.layout = resource;
        this.eventCard = eventCardDetail;
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
        TextView eventName,teamName,eventDesc,eventDate, pendContriAmt, pendeventMemList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        EventWalletPend_Adapter.ViewHolder holder = new EventWalletPend_Adapter.ViewHolder();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layout, null, false);
        holder.contriImg = view.findViewById(R.id.pendcontriImg);
        holder.eventName = view.findViewById(R.id.pendEventTextView7);
        holder.teamName = view.findViewById(R.id.pendeventCardTeam);
        holder.eventDesc = view.findViewById(R.id.pendeventCardDesc);
        holder.eventDate = view.findViewById(R.id.pendEventtextView6);
        holder.pendContriAmt = view.findViewById(R.id.pendContriAmt);
        holder.pendeventMemList = view.findViewById(R.id.pendeventMemList);
        final Events_Card card1 = eventCard.get(position);

        holder.eventName.setText(card1.eName);
        holder.teamName.setText(team);
        holder.eventDesc.setText(card1.eDesc);
        holder.eventDate.setText("Date: "+card1.eDate);
        holder.pendContriAmt.setText("Contribution Amt: "+card1.eContri);

        if(card1.eContiBool.equals("true"))
        {
            holder.contriImg.setImageResource(R.drawable.tick);
        }
        else {
            holder.contriImg.setImageResource(R.drawable.cancel);
        }

        holder.pendeventMemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mempendList = new Intent(context,EventMemberList.class);
                mempendList.putExtra("members",new String[]{card1.eMembers,card1.eCdtMembs});
                context.startActivity(mempendList);
            }
        });
        return view;
    }
}