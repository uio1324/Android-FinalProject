package com.example.great.project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LessonEdit extends AppCompatActivity {


    //本act提供课程的编辑功能。
    //注意编辑后，不是直接修改课程表里的相应项。而是应该在课程表里新增项或检查修改之后的项是否已存在。
    //然后在上课表里删除和旧项的联系，建立新项。
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_edit);
    }
}
