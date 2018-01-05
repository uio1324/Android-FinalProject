package com.example.great.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.great.project.Database.StudentDB;
import com.example.great.project.Model.Student;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    private Button login;
    private Button reg;
    private Button confirmreg;
    private EditText username;
    private EditText pwd;
    private EditText confirmpwd;
    private StudentDB sdb = new StudentDB(this);
    private List<Student> stuList = new ArrayList<>();

    private void initial(){
        login = findViewById(R.id.login_login);
        reg = findViewById(R.id.login_reg);
        confirmreg = findViewById(R.id.login_confirmreg);
        username = findViewById(R.id.login_username);
        pwd = findViewById(R.id.login_pwd);
        confirmpwd = findViewById(R.id.login_confirmpwd);
        confirmreg.setVisibility(View.GONE);
        confirmpwd.setVisibility(View.GONE);
    }

    private void setListener(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameStr = username.getText().toString();
                String pwdStr = pwd.getText().toString();
                if(usernameStr.isEmpty()) {
                    Toast.makeText(Login.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                }
                else if(pwdStr.isEmpty()) {
                    Toast.makeText(Login.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                }
                else {
                    /*search username in Database
                    if username not exist : toast
                    else if name and pwd do not match : toast
                    else
                    */
                    stuList = sdb.queryStu(usernameStr);
                    if(stuList.isEmpty() || !pwdStr.equals(stuList.get(0).getPassword())) {
                        Toast.makeText(Login.this, "用户不存在或密码错误", Toast.LENGTH_SHORT).show();
                    } else {
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
                stuList = sdb.queryStu(usernameStr);
                if(usernameStr.isEmpty()){
                    Toast.makeText(Login.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                } else if(!stuList.isEmpty()) {
                    Toast.makeText(Login.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                } else if(pwdStr.isEmpty() || confirmpwdStr.isEmpty()){
                    Toast.makeText(Login.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else if(!pwdStr.equals(confirmpwdStr)){
                    Toast.makeText(Login.this, "密码不匹配", Toast.LENGTH_SHORT).show();
                } else {
                    Student item = new Student();
                    item.setSName(usernameStr);
                    item.setNickName(usernameStr);
                    item.setPassWord(pwdStr);
                    sdb.insertStu(item);
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
