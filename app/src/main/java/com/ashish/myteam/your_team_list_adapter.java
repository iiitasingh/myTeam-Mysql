package com.ashish.myteam;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


//we need to extend the ArrayAdapter class as we are building an adapter
public class your_team_list_adapter extends BaseAdapter {

    private Context context;
    private  int layout;
    private ArrayList<User> membersList;

    //constructor initializing the values
    public your_team_list_adapter(Context context, int resource, ArrayList<User> membersList) {
        this.context = context;
        this.layout = resource;
        this.membersList = membersList;
    }

    @Override
    public int getCount() {
        return membersList.size();
    }

    @Override
    public Object getItem(int position) {
        return membersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView memberImg;
        TextView memberName,hometeamnameDesig;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layout, null, false);
        holder.memberImg = view.findViewById(R.id.imgteam);
        holder.memberName = view.findViewById(R.id.hometeamname);
        holder.hometeamnameDesig = view.findViewById(R.id.hometeamnameDesig);
        User member = membersList.get(position);

        holder.memberName.setText(member.name);
        holder.hometeamnameDesig.setText(member.designation);

        byte[] foodImage = org.apache.commons.codec.binary.Base64.decodeBase64(member.image);
        Bitmap bitmap = BitmapFactory.decodeByteArray(foodImage, 0, foodImage.length);
        holder.memberImg.setImageBitmap(bitmap);
        return view;
    }
/*
    //this method will remove the item from the list
    private void removeHero(final int position) {
        //Creating an alert dialog to confirm the deletion
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure you want to delete this?");

        //if the response is positive in the alert
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //removing the item
                heroList.remove(position);

                //reloading the list
                notifyDataSetChanged();
            }
        });

        //if response is negative nothing is being done
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        //creating and displaying the alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    */
}