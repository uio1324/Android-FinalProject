package com.example.great.project.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.great.project.R;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPref;
    private BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_classes:
                    //隐藏其余三个选项卡的控件
                    return true;
                case R.id.navigation_ddl:

                    return true;
                case R.id.navigation_learn:

                    return true;
                case R.id.navigation_settings:

                    return true;
            }
            return false;
        }
    };

    //请注意控件命名规范:act名_自定义控件名
    //如在taskedit里的控件请命名为taskedit_xxxx


    //已实现功能：打开软件，如果没登录过弹出登陆界面，能注册，如果已经登陆过一次则
    //则直接进入主界面

    //启动时检查一遍任务-user表，该用户是否有未同意加入的邀请，如果有采用弹窗等形式提醒其是否选择加入，同意则该表相应位置1，否则从数据库中删除该条。

    //课程根据用户id，在上课表里选出相应项。可以不显示全部信息，具体显示什么可以写这个act的人决定
    //课程选项卡的列表的点击事件：intent装课程名称，跳转到课程详情LessonDetail。
    //注意使用forResult，返回code不要设为1,1已经被我用了
    //返回后要注意更新
    //提供新增课程功能，新增课程跳转到LessonEdit，进行新课程编辑。
    //长按删除课程，记得弹出确认框。删除记得只删除上课表里对应项，不要直接删除课程表中相应项。

    //ddl选项卡列表默认装最近五个ddl。
    //ddl根据登陆的用户从task表里选出用户符合的项，再根据ddl列排序。列表具体显示内容由写act的人决定。
    //ddl倒计时日期之类的可以使用系统api，请查询实现
    //点击事件：intent装task名称，跳转到任务详情TaskDetail。
    //跳转到的类和在课程界面中
    //使用forResult
    //返回后要注意更新

    //第三个选项卡番茄学习法倒计时，具体的倒计时自己去找，界面好看点
    //能播放一段助学音乐，具体音乐去网上找，一段就行，音乐能选择放或不放
    //倒计时为默认25分钟学习休息5分钟，倒计时结束后要有触发，可以输出提醒之类的
    //注意倒计时的清零。计划设置为不能暂停，只能取消。取消后输出鼓励话语

    //第四个选项卡为设置，目前考虑可以设置的点：最大显示ddl数量，学习和休息时间
    //以及用户退出登录，设置昵称
    //退出登录的逻辑：将sharedPref里面的uesrname清空后转到登录界面。
    //请使用ForResult，返回码为1即可，这部分我已经写好

    //其余各类的具体内容写在各个类里

    private void initial(){
        navigation = findViewById(R.id.navigation);
        sharedPref = MainActivity.this.getSharedPreferences("username", Context.MODE_PRIVATE);
        String username = sharedPref.getString("username","");
        if(username.isEmpty()){
            startActivityForResult(new Intent(MainActivity.this, Login.class), 1);
        }
        else{
            //search in DB to initial classes and taskDDL;
            //Toast 欢迎您username
        }
    }

    private void setListener(){
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initial();
        setListener();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 1){
            String username = data.getStringExtra("username");
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("username", username);
            editor.commit();
            //search in DB to initial classes and taskDDL;
        }
    }

}
