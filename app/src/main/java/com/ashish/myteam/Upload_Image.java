package com.ashish.myteam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Upload_Image extends AppCompatActivity {
    /*
    ImageView Uimg;
    String UserIdHolder;
    TextView MemName,MemEmail,MemTeam,MemDob,MemFood,MemMob,MemBgp,MemNknam;
    String nam,niknam,tnm,Umail,dob,food;
    byte[] userimg;

*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_header);

        /*
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.82));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        getWindow().setAttributes(params);

        UserIdHolder = getIntent().getStringExtra("User_Id");

        Uimg = findViewById(R.id.Uimg);
        MemNknam = (TextView)findViewById(R.id.MemNknam);
        MemName = (TextView)findViewById(R.id.MemName);
        MemEmail = (TextView)findViewById(R.id.MemEmail);
        MemTeam = (TextView)findViewById(R.id.MemTeam);
        MemDob = (TextView)findViewById(R.id.MemDob);
        MemFood = (TextView)findViewById(R.id.MemFood);
        MemMob = (TextView)findViewById(R.id.MemMob);
        MemBgp = (TextView)findViewById(R.id.MemBgp);

        Cursor data = MainActivity.db.getuserAlldetails(UserIdHolder);
        if (data.getCount() == 0) {
            Toast.makeText(Upload_Image.this, "Database Error", Toast.LENGTH_SHORT).show();
        } else {
            data.moveToFirst();
            Umail = data.getString(data.getColumnIndex("email"));
            nam = data.getString(data.getColumnIndex("name"));
            niknam = data.getString(data.getColumnIndex("nick_name"));
            tnm = data.getString(data.getColumnIndex("team"));
            userimg = data.getBlob(data.getColumnIndex("image"));
            dob = data.getString(data.getColumnIndex("dob"));
            food = data.getString(data.getColumnIndex("food"));
        }

        MemEmail.setText(": "+Umail);
        MemName.setText(nam);
        MemNknam.setText(niknam);
        MemTeam.setText(": "+tnm);
        MemDob.setText(": "+dob);
        MemFood.setText(": "+food);
        MemBgp.setText(": B+");
        MemMob.setText(": 999999999");

        Bitmap bitmap = BitmapFactory.decodeByteArray(userimg, 0, userimg.length);
        Uimg.setImageBitmap(bitmap);
*/

    }

}
