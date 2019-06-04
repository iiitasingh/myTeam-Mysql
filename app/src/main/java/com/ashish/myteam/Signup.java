package com.ashish.myteam;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;


public class Signup extends AppCompatActivity {

    EditText fullname, email, password, cnfpassword,teampin,nickName;
    Button mButtonSignup;
    TextView mlogin,selectedteam;
    ImageView userImage;
    String img;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        id = getResources().getIdentifier("user_1","drawable",getPackageName());
        fullname = (EditText)findViewById(R.id.userFullName);
        email = (EditText)findViewById(R.id.userEmail);
        password = (EditText) findViewById(R.id.userPass);
        cnfpassword = (EditText) findViewById(R.id.userCnfPass);
        mButtonSignup = (Button) findViewById(R.id.button_signup);
        mlogin = (TextView) findViewById(R.id.tvlogin);
        selectedteam = (TextView)findViewById(R.id.selectedTeam);
        teampin = (EditText)findViewById(R.id.signupTeamPin);
        nickName = (EditText) findViewById(R.id.userNickName);

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent( Signup.this,MainActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);
                finish();
            }
        });

        selectedteam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectteam = new Intent(Signup.this,teamSelection.class);
                startActivity(selectteam);
            }
        });
        Intent incomingIntent = getIntent();
        String TempHolder = incomingIntent.getStringExtra("ListViewClickedValue");
        selectedteam.setText(TempHolder);
    }

    public static String capitailizeWord(String str) {

        str = str.toLowerCase();
        StringBuffer s = new StringBuffer();
        char ch = ' ';
        for (int i = 0; i < str.length(); i++) {
            if (ch == ' ' && str.charAt(i) != ' ')
                s.append(Character.toUpperCase(str.charAt(i)));
            else
                s.append(str.charAt(i));
            ch = str.charAt(i);
        }
        return s.toString().trim();
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Signup.this, MainActivity.class);
        setIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setIntent);
        finish();
    }
    public String imageResourceToByte() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.user_1);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encodedImage;
    }

    public void signUpUser(View view)
    {
        String mail = email.getText().toString().trim();
        String pwd = password.getText().toString().trim();
        String name = fullname.getText().toString().trim();
        String cnfpwd = cnfpassword.getText().toString().trim();
        String team = selectedteam.getText().toString().trim();
        String pin = teampin.getText().toString().trim();
        String nickname = nickName.getText().toString().trim();
        img = imageResourceToByte();
        String type = "signUpUser";

        if (team.length() != 0 && name.length() != 0 && nickname.length() != 0 && mail.length() != 0 && pwd.length() >= 4){
            if (pwd.equals(cnfpwd)) {

                HashMap<String, String> loginData = new HashMap<>();
                loginData.put("mail", mail.toLowerCase());
                loginData.put("pwd", pwd);
                loginData.put("name", capitailizeWord(name));
                loginData.put("team", team.toUpperCase());
                loginData.put("pin", pin);
                loginData.put("nickname", capitailizeWord(nickname));
                loginData.put("img", img);

                PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this,
                        loginData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if (s.contains("UserPresent")) {
                            Toast.makeText(Signup.this, "User already exist, Please Login", Toast.LENGTH_SHORT).show();
                            Intent moveToLogin = new Intent(Signup.this, MainActivity.class);
                            startActivity(moveToLogin);
                        } else if (s.contains("pinError")) {
                            Toast.makeText(Signup.this, "Incorrect team pin", Toast.LENGTH_SHORT).show();
                        } else if (s.contains("ErrorInsert")) {
                            Toast.makeText(Signup.this, "Registration Error, Try Again!", Toast.LENGTH_SHORT).show();
                        }else if (s.contains("ErrorInLogin")) {
                            Toast.makeText(Signup.this, "Error in login table", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Signup.this, "You have registered successfully", Toast.LENGTH_SHORT).show();
                            Intent login = new Intent(Signup.this, MainActivity.class);
                            startActivity(login);
                        }
                    }
                });
                loginTask.setExceptionHandler(new ExceptionHandler() {
                    @Override
                    public void handleException(Exception e) {
                        if (e != null && e.getMessage() != null) {
                            Toast.makeText(getApplicationContext(),
                                    "" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                loginTask.execute("https://voteforashish.000webhostapp.com/myTeam/signupUser.php");
            }else {
                Toast.makeText(Signup.this, "Password is not matching", Toast.LENGTH_SHORT).show();
            }
        } else {

            if (team.length() < 1){
                Toast.makeText(Signup.this, "Select Team First", Toast.LENGTH_SHORT).show();
            }
            else if (name.length() < 1) {
                Toast.makeText(Signup.this, "Name should not be null", Toast.LENGTH_SHORT).show();
            }
            else if (nickname.length() < 1){
                Toast.makeText(Signup.this, "Nick Name should not be null", Toast.LENGTH_SHORT).show();
            }
            else if (mail.length() < 1){
                Toast.makeText(Signup.this, "Email should not be null", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(Signup.this, "Password should have at least 4 Characters", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
