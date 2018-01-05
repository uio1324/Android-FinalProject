package com.example.great.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    private Button login;
    private Button reg;
    private Button confirmreg;
    private EditText username;
    private EditText pwd;
    private EditText confirmpwd;

    private void initial(){
        login = findViewById(R.id.login_login);
        reg = findViewById(R.id.login_reg);
        confirmreg = findViewById(R.id.login_confirmreg);
        username = findViewById(R.id.login_username);
        pwd = findViewById(R.id.login_pwd);
        confirmpwd = findViewById(R.id.login_confirmpwd);
        confirmreg.setVisibility(View.INVISIBLE);
        confirmpwd.setVisibility(View.INVISIBLE);
    }

    private void setListener(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameStr = username.getText().toString();
                String pwdStr = pwd.getText().toString();
                if(usernameStr.isEmpty()){
                    //toast
                }
                else if(pwdStr.isEmpty()){
                    //toast
                }
                else{
                    //search username in Database
                    //if username not exist : toast
                    //else if taskName and pwd do not match : toast
                    //else
                    {
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.putExtra("username", usernameStr);
                        Login.this.setResult(1, intent);
                        Login.this.finish();
                    }
                }
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmreg.setVisibility(View.VISIBLE);
                confirmpwd.setVisibility(View.VISIBLE);
                login.setVisibility(View.INVISIBLE);
                reg.setVisibility(View.INVISIBLE);
            }
        });

        confirmreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameStr = username.getText().toString();
                String pwdStr = pwd.getText().toString();
                String confirmpwdStr = confirmpwd.getText().toString();
                if(usernameStr.isEmpty()){
                    //toast
                }
                //else if (username in Database) toast
                else if(pwdStr.isEmpty()){
                    //toast
                }
                else if(!pwdStr.equals(confirmpwdStr)){
                    //toast
                }
                else{
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.putExtra("username", usernameStr);
                        Login.this.setResult(1, intent);
                        Login.this.finish();
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initial();
        setListener();
    }
}
