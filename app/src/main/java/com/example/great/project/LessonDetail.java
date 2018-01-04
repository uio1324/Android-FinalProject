package com.example.great.project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LessonDetail extends AppCompatActivity {


    //课程详细信息。根据main传来的intent中课程名称去数据库搜索相关信息，填充页面。
    //初步计划信息：课程名，课程时间，课程地点，老师名称，以及一个课程任务列表
    //课程要有一个修改按钮，跳转到LessonEdit进行编辑。可以不提供删除，因为加了之后act的跳转比较难。
    //页面排版很重要，记得做美观一点。
    //记得每次修改后要及时更新信息，包括数据库和界面上的内容
    //记得设置返回按键之类的东西
    //单击课程表里的任务表，跳转到任务界面
    //课程下可以新增任务，类似于从主界面新增课程。TaskEdit类为新增任务


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_detail);
    }
}
