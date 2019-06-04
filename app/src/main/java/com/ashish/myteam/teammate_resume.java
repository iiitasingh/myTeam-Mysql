package com.ashish.myteam;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class teammate_resume extends AppCompatActivity {

    User resumeMail;
    int teamMateMail;
    ImageView resumeWallpaper;
    TextView resumeName, resumeDesignation, textVwaboutBtn, resumeHobbies, resumeAbout, resumeEmail;
    CircleImageView circleResumeImageView;
    Dialog aboutPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teammate_resume);

        teamMateMail = getIntent().getIntExtra("ID",0);
        resumeMail = Home.TeamList.get(teamMateMail);

        resumeWallpaper = (ImageView) findViewById(R.id.teamMateresumeWallpaper);
        resumeName = (TextView) findViewById(R.id.teamMateresumeName);
        resumeDesignation = (TextView) findViewById(R.id.teamMateresumeDesignation);
        textVwaboutBtn = (TextView) findViewById(R.id.teamMateResumeAbout);
        resumeHobbies = (TextView) findViewById(R.id.teamMateresumeHobbies);
        resumeAbout = (TextView) findViewById(R.id.teamMateabout);
        resumeEmail = (TextView) findViewById(R.id.teamMateresumeEmail);
        circleResumeImageView = (CircleImageView) findViewById(R.id.teamMateResumeImage);
        aboutPopup = new Dialog(this);

        setResumeData(resumeMail);

        textVwaboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutPopup(teammate_resume.this, resumeMail);
            }
        });
    }
    public void setResumeData(User ower) {

        byte[] usrerImg = org.apache.commons.codec.binary.Base64.decodeBase64(ower.image);
        Bitmap bitmap = BitmapFactory.decodeByteArray(usrerImg, 0, usrerImg.length);
        circleResumeImageView.setImageBitmap(bitmap);
        resumeName.setText(ower.name);
        resumeDesignation.setText(ower.designation);
        resumeHobbies.setText(ower.hobbies);
        resumeAbout.setText(ower.about);
        resumeEmail.setText(ower.email);
    }

    ImageView Usimgpop2;
    TextView MemNamepop2, MemEmailpop2, MemTeampop2, MemDobpop2, MemFoodpop2, MemMobpop2, MemBgppop2, MemNknampop2,MemDesig;
    byte[] userimgpop2;

    private void aboutPopup(Activity popActivity, final User userAbout) {

        aboutPopup.setContentView(R.layout.home_header);

        int width1 = (int) (popActivity.getResources().getDisplayMetrics().widthPixels * .82);
        int height1 = (int) (popActivity.getResources().getDisplayMetrics().heightPixels * 0.75);
        aboutPopup.getWindow().setLayout(width1, height1);
        aboutPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        aboutPopup.show();

        Usimgpop2 = (ImageView) aboutPopup.findViewById(R.id.Uimg);
        MemNknampop2 = (TextView) aboutPopup.findViewById(R.id.MemNknam);
        MemNamepop2 = (TextView) aboutPopup.findViewById(R.id.MemName);
        MemEmailpop2 = (TextView) aboutPopup.findViewById(R.id.MemEmail);
        MemTeampop2 = (TextView) aboutPopup.findViewById(R.id.MemTeam);
        MemDobpop2 = (TextView) aboutPopup.findViewById(R.id.MemDob);
        MemFoodpop2 = (TextView) aboutPopup.findViewById(R.id.MemFood);
        MemMobpop2 = (TextView) aboutPopup.findViewById(R.id.MemMob);
        MemBgppop2 = (TextView) aboutPopup.findViewById(R.id.MemBgp);
        MemDesig = (TextView) aboutPopup.findViewById(R.id.MemDesig);

        MemEmailpop2.setText("Email: " + userAbout.email);
        MemNamepop2.setText(userAbout.name);
        MemNknampop2.setText(userAbout.nickname);
        MemDesig.setText(userAbout.designation);
        MemTeampop2.setText("Team: " + userAbout.team);
        MemFoodpop2.setText("Food: " + userAbout.food);
        MemBgppop2.setText("BGrp: " + userAbout.bl_grp);
        MemMobpop2.setText("Mob: " + userAbout.mobile);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date data = null;
        try {
            data = sdf.parse(userAbout.dob);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        MemDobpop2.setText("DOB: " + DateFormat.format("dd MMMM", data));
        userimgpop2 = org.apache.commons.codec.binary.Base64.decodeBase64(userAbout.image);
        Bitmap bitmap = BitmapFactory.decodeByteArray(userimgpop2, 0, userimgpop2.length);
        Usimgpop2.setImageBitmap(bitmap);
    }
}
