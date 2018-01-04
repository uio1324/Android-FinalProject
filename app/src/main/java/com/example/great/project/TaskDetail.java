package com.example.great.project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TaskDetail extends AppCompatActivity {

    //该act为任务详情act。根据用户id、课程id和任务名，从Database中读取详情。
    //页面包括DB中读取的信息填充相应文本框
    //提供编辑功能_TaskEdit
    //随后根据任务id从taskinfo表里读取该任务的相关信息，填充列表
    //该页面显示该任务所有共享者，从任务-user表中根据taskid，选择已经同意加入的用户进来
    //同时可以邀请用户加入。邀请时在任务_user表中新建条目，是否加入置否。
    //可以发布新的info，taskid在task_info表中更新内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
    }
}
