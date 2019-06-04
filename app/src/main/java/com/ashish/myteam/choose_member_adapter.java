package com.ashish.myteam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class choose_member_adapter extends BaseAdapter {

    Context context;
    ArrayList<teamMemberObject> data;
    private static LayoutInflater inflater = null;

    public choose_member_adapter(Context context, ArrayList<teamMemberObject> data) {

        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    private class ViewHolder{
        CheckedTextView checkMember;
        TextView memName;
        CheckBox check;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        ViewHolder holder;
        if (vi == null) {
            vi = inflater.inflate(R.layout.event_member_list, null);
            holder = new ViewHolder();
            holder.checkMember = (CheckedTextView)vi.findViewById(R.id.member);
            vi.setTag(holder);
        }
        else {
            holder = (ViewHolder) vi.getTag();
        }
        holder.checkMember.setText(data.get(position).name);
        return vi;
    }
}
