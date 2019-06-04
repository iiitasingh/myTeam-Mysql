package com.ashish.myteam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class ForgetPassword extends AppCompatActivity {

    EditText email;
    Button mButtonResetPass;
    TextView mSignup;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        db = new DatabaseHelper(this);
        email = (EditText)findViewById(R.id.emailForgetlink);
        mButtonResetPass = (Button) findViewById(R.id.button_resetpass);
        mSignup = (TextView) findViewById(R.id.tvsignup);

        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(ForgetPassword.this,Signup.class);
                startActivity(signupIntent);
            }
        });

        mButtonResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getText().toString().trim();
                boolean res = db.checkUser(mail);
                if(res){
                    Toast.makeText(ForgetPassword.this,"Password reset link sent to mail",Toast.LENGTH_SHORT).show();
                    Intent moveLogin = new Intent(ForgetPassword.this,MainActivity.class);
                    startActivity(moveLogin);
                }
                else {
                    Toast.makeText(ForgetPassword.this,"Email doesn't exist",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
