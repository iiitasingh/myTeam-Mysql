package com.ashish.myteam;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements TextWatcher,
        CompoundButton.OnCheckedChangeListener{

    EditText mTextUsername, mTextPassword;
    Button mButtonLogin;
    CheckBox mCheckboxRememberme;
    TextView mForget, mSignup;
    //public static DatabaseHelper db;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String PREF_NAME = "prefs";
    private final String KEY_REMEMBER = "remember";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASS = "password";
    String userMail;

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        db = new DatabaseHelper(this);
        session = new Session(this);
        mTextUsername = (EditText)findViewById(R.id.edit_test_username);
        mTextPassword = (EditText)findViewById(R.id.edit_test_password);
        mButtonLogin = (Button) findViewById(R.id.button_login);
        mCheckboxRememberme = (CheckBox) findViewById(R.id.rem_user);
        mForget = (TextView) findViewById(R.id.forget);
        mSignup = (TextView) findViewById(R.id.signup);
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if(sharedPreferences.getBoolean(KEY_REMEMBER, false))
            mCheckboxRememberme.setChecked(true);
        else
            mCheckboxRememberme.setChecked(false);

        userMail = session.getUser();
        if(session.loggedin()){
            Intent HomePage = new Intent(MainActivity.this,list_test.class);
            HomePage.putExtra("UserEmail", userMail);
            startActivity(HomePage);
            finish();
        }

        mTextUsername.setText(sharedPreferences.getString(KEY_USERNAME,""));

        mTextUsername.addTextChangedListener(this);
        mTextPassword.addTextChangedListener(this);
        mCheckboxRememberme.setOnCheckedChangeListener(this);

        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(MainActivity.this,Signup.class);
                startActivity(signupIntent);
            }
        });

        mForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgetIntent = new Intent(MainActivity.this,ForgetPassword.class);
                startActivity(forgetIntent);
            }
        });

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        managePrefs();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        managePrefs();
    }

    private void managePrefs(){
        if(mCheckboxRememberme.isChecked()){
            editor.putString(KEY_USERNAME, mTextUsername.getText().toString().trim());
            editor.putString(KEY_PASS, mTextPassword.getText().toString().trim());
            editor.putBoolean(KEY_REMEMBER, true);
            editor.apply();
        }else{
            editor.putBoolean(KEY_REMEMBER, false);
            editor.remove(KEY_PASS);//editor.putString(KEY_PASS,"");
            editor.remove(KEY_USERNAME);//editor.putString(KEY_USERNAME, "");
            editor.apply();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void login(View view)
    {
        String user = mTextUsername.getText().toString().trim();
        String pwd = mTextPassword.getText().toString().trim();
        String type = "login";

        HashMap<String, String> loginData = new HashMap<>();
        loginData.put("email", user);
        loginData.put("password", pwd);

        PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this,
                loginData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(s.contains("LoginSuccess")){
                    session.setLoggedin(true);
                    session.setUser(user);
                    Intent HomePage = new Intent(MainActivity.this,list_test.class);
                    HomePage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    HomePage.putExtra("UserEmail", user);
                    startActivity(HomePage);
                    finish();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Incorrect Username Password",Toast.LENGTH_SHORT).show();
                }
            }
        });
        loginTask.setExceptionHandler(new ExceptionHandler() {
            @Override
            public void handleException(Exception e) {
                if(e != null && e.getMessage() != null){
                    Toast.makeText(getApplicationContext(),
                            ""+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        loginTask.execute("https://voteforashish.000webhostapp.com/myTeam/login_myteam.php");
    }

}
