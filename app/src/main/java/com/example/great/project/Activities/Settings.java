package com.example.great.project.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.great.project.Database.StudentDB;
import com.example.great.project.Model.Student;
import com.example.great.project.R;

public class Settings extends AppCompatActivity {

    private AlertDialog.Builder builder;
    private String sName;
    private String nickName;
    private String password;
    private String editOldPW;
    private StudentDB sdb;
    private LinearLayout main_layout;
    private LinearLayout setpw_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sdb = new StudentDB(this);
        
        // 跳转传参
//        Intent intent = getIntent();
        sName = getIntent().getStringExtra("sName");
        Log.d("TAG", sName);
        nickName = getIntent().getStringExtra("nickName");
        password = getIntent().getStringExtra("password");

        // 两个界面
        main_layout = (LinearLayout)findViewById(R.id.main_layout);
        setpw_layout = (LinearLayout)findViewById(R.id.layout_setpw);

        //主界面元素
        ImageView headImage = (ImageView)findViewById(R.id.setheadimage);
        TextView showSName = (TextView) findViewById(R.id.showSName);
        final TextView showNickName = (TextView) findViewById(R.id.showNickName);
        Button pwbtn = (Button) findViewById(R.id.pwbtn);

        showSName.setText(sName);
        showNickName.setText(nickName);

        //修改界面元素
        final EditText setpw1 = (EditText)findViewById(R.id.setpw1);
        final EditText setpw2 = (EditText)findViewById(R.id.setpw2);
        CheckBox showpw = (CheckBox)findViewById(R.id.showpw);
        final TextView hint = (TextView)findViewById(R.id.hint);
        Button ok = (Button) findViewById(R.id.ok);
        Button no = (Button) findViewById(R.id.no);


        builder = new AlertDialog.Builder(Settings.this);

        LinearLayout L3 = (LinearLayout)findViewById(R.id.Line3);
        L3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = LayoutInflater.from(Settings.this).inflate(R.layout.dialog_nickname, null);
                final EditText editName = dialogView.findViewById(R.id.editName);
                builder.setView(dialogView)
                       .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {

                           }
                       })
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                nickName = editName.getText().toString();
                                // 保存到数据库
                                Student stu = new Student(sName, nickName, password);
                                sdb.updateStu(stu);
                                showNickName.setText(nickName);
                            }
                        }).create().show();
            }
        });
        pwbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = LayoutInflater.from(Settings.this).inflate(R.layout.dialog_password, null);
                final EditText oldpw = dialogView.findViewById(R.id.oldpw);
                builder.setView(dialogView)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editOldPW = oldpw.getText().toString();
                                if(password.equals(editOldPW)) {
                                    main_layout.setVisibility(View.GONE);
                                    setpw_layout.setVisibility(View.VISIBLE);
                                } else {
                                    AlertDialog.Builder simple = new AlertDialog.Builder(Settings.this);
                                    simple.setTitle("提示").setMessage("密码错误，请重新输入。")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            }).create().show();
                                }
                            }
                        }).create().show();
            }
        });

        showpw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //如果选中，显示密码
                    setpw1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    setpw2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    //否则隐藏密码
                    setpw1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    setpw2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pw1 = setpw1.getText().toString();
                String pw2 = setpw2.getText().toString();
                if(pw1.equals(pw2)) {
                    // 保存到数据库
                    Student stu = new Student(sName, nickName, pw1);
                    sdb.updateStu(stu);
                    main_layout.setVisibility(View.VISIBLE);
                    setpw_layout.setVisibility(View.GONE);
                } else {
                    hint.setVisibility(View.VISIBLE);
                }
            }
        });
        setpw1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    hint.setVisibility(View.INVISIBLE);
                }
            }
        });
        setpw2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    hint.setVisibility(View.INVISIBLE);
                }
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_layout.setVisibility(View.VISIBLE);
                setpw_layout.setVisibility(View.GONE);
            }
        });
    }
}
