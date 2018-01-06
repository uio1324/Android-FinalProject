package com.example.great.project.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.great.project.Database.StudentDB;
import com.example.great.project.Model.Student;
import com.example.great.project.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPref;
    private String username;

    private ViewPager vpager;
    private BottomNavigationView navigation;
    private StudentDB sdb;
    private List<Student> stulist;
    private List<View> viewList;                // view数组

    private String sNameStr;
    private String nickNameStr;
    private String pwStr;
    private Student stu;

    // view4
    private Button settings;
    private Button exit;
    private TextView sName;
    private TextView nickName;

    private void initial(){
        sdb = new StudentDB(this);
        stulist = new ArrayList<>();

        navigation = findViewById(R.id.navigation);
        disableShiftMode(navigation);
        vpager = (ViewPager) findViewById(R.id.viewpager);

        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.layout_course, null);
        View view2 = inflater.inflate(R.layout.layout_task, null);
        View view3 = inflater.inflate(R.layout.layout_study, null);
        View view4 = inflater.inflate(R.layout.layout_setting, null);

        viewList = new ArrayList<View>();   // 将要分页显示的View装入数组中
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        viewList.add(view4);

        // view4
        sName = (TextView) view4.findViewById(R.id.sName);
        nickName = (TextView) view4.findViewById(R.id.nickName);
        settings = (Button) view4.findViewById(R.id.settings);
        exit = (Button) view4.findViewById(R.id.exit);


        sharedPref = MainActivity.this.getSharedPreferences("username", Context.MODE_PRIVATE);
        username = sharedPref.getString("username","");
        if(username.isEmpty()){
            startActivityForResult(new Intent(MainActivity.this, Login.class), 1);
        }
        else{
            //search in DB to initial classes and ddl;
            //Toast 欢迎您username
            stulist = sdb.queryStu(username);
            for(Student item:stulist) {
                sNameStr = item.getSName();
                nickNameStr = item.getNickName();
                pwStr = item.getPassword();
            }
            sName.setText(sNameStr);
            nickName.setText(nickNameStr);
        }
    }

    private void switchPage() {
        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewList.get(position));
            }
        };

        vpager.setAdapter(pagerAdapter);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_classes:
                    vpager.setCurrentItem(0);
                    break;
                case R.id.navigation_ddl:
                    vpager.setCurrentItem(1);
                    break;
                case R.id.navigation_learn:
                    vpager.setCurrentItem(2);
                    break;
                case R.id.navigation_settings:
                    vpager.setCurrentItem(3);
                    break;
            }
            return true;
        }
    };

    private void setListener(){
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    /*
     导航栏设置界面
     settings按钮，弹出修改信息对话框
     exit按钮，退出到登录页面
     */
    private void settingPage() {
        // 设置界面元素
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Settings.class);
                intent.putExtra("sName", sNameStr);
                intent.putExtra("nickName", nickNameStr);
                intent.putExtra("password", pwStr);
                startActivity(intent);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 清空sharepreference保存的用户信息
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear();
                editor.apply();
                // 跳转到登录界面
                startActivityForResult(new Intent(MainActivity.this, Login.class), 1);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initial();
        setListener();
        switchPage();
        settingPage();
    }

    @Override
    protected void onStart() {
        super.onStart();
        stulist = sdb.queryStu(username);
        for(Student item:stulist) {
            sNameStr = item.getSName();
            nickNameStr = item.getNickName();
            pwStr = item.getPassword();
        }
        sName.setText(sNameStr);
        nickName.setText(nickNameStr);
        Log.d("TAG", "onStart");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 1){
            username = data.getStringExtra("username");
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("username", username);
            editor.apply();
            //search in DB to initial classes and ddl;
            Log.d("TAG", "onActivityResult");
        }

    }

    // 移除bottombutton动画
    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}