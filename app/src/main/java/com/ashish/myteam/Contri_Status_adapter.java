package com.ashish.myteam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class Contri_Status_adapter extends BaseAdapter {

    private Context context;
    private  int layout;
    private ArrayList<Contri_Status_details> Contri_Status;

    //constructor initializing the values
    public Contri_Status_adapter(Context context, int resource, ArrayList<Contri_Status_details> eventCardDetail) {
        this.context = context;
        this.layout = resource;
        this.Contri_Status = eventCardDetail;
    }

    @Override
    public int getCount() {
        return Contri_Status.size();
    }

    @Override
    public Object getItem(int position) {
        return Contri_Status.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView status;
        TextView memName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Contri_Status_adapter.ViewHolder holder = new Contri_Status_adapter.ViewHolder();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layout, null, false);


        holder.status = view.findViewById(R.id.contriStatus);
        holder.memName = view.findViewById(R.id.memberName);
        Contri_Status_details card = Contri_Status.get(position);

        if(card.isStatus() == true)
        {
            holder.status.setImageResource(R.drawable.tick);
        }
        else {
        }

        holder.memName.setText(card.getName());
        return view;
    }
}
