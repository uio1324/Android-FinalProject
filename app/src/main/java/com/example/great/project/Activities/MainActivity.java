package com.example.great.project.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.great.project.Database.CourseDB;
import com.example.great.project.Database.StudentDB;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.example.great.project.Model.CourseModel;
import com.example.great.project.Model.Student;
import com.example.great.project.R;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPref;
    private BottomNavigationView navigation;
    private CourseDB cdb = new CourseDB(this);
    private StudentDB sdb = new StudentDB(this);

    private List<Map<String, Object>> stulist = new ArrayList<>();
    private List<Map<String, Object>> courseItem = new ArrayList<>();
    private RecyclerView courseRecy;
    private FloatingActionButton addButton;
    private CommonAdapter courseListAdp;
    private CommonAdapter courseExistedAdp;
    private Student student;
    private TextView courseHint;
    private RecyclerView courseExisted;
    private AlertDialog.Builder addCourse;
    int addBtnFlag;

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
        addBtnFlag = 0;
        addCourse = new AlertDialog.Builder(MainActivity.this);

        navigation = findViewById(R.id.navigation);
        disableShiftMode(navigation);
        courseRecy = findViewById(R.id.course_recy);
        addButton = findViewById(R.id.addCourse);
        courseHint = findViewById(R.id.course_hint);
        courseExisted = findViewById(R.id.course_existed);
        courseExisted.setVisibility(View.INVISIBLE);
        this.courseListAdp = new CommonAdapter<Map<String, Object>>(this, R.layout.lesson_recy_layout, this.courseItem) {
            @Override
            public void convert(ViewHolder viewHolder, Map<String, Object> s) {
                TextView name = viewHolder.getView(R.id.lesson_name);
                name.setText(s.get("name").toString());
                TextView time = viewHolder.getView(R.id.lesson_time);
                time.setText(s.get("time").toString());
                TextView room = viewHolder.getView(R.id.lesson_room);
                room.setText(s.get("room").toString());
                TextView teacher = viewHolder.getView(R.id.lesson_teacher);
                teacher.setText(s.get("teacher").toString());
            }
        };
        this.courseExistedAdp = new CommonAdapter<Map<String, Object>>(this, R.layout.lesson_recy_layout, this.courseItem) {
            @Override
            public void convert(ViewHolder viewHolder, Map<String, Object> s) {
                TextView name = viewHolder.getView(R.id.lesson_name);
                name.setText(s.get("name").toString());
                TextView time = viewHolder.getView(R.id.lesson_time);
                time.setText(s.get("time").toString());
                TextView room = viewHolder.getView(R.id.lesson_room);
                room.setText(s.get("room").toString());
                TextView teacher = viewHolder.getView(R.id.lesson_teacher);
                teacher.setText(s.get("teacher").toString());
            }
        };

        this.courseRecy.setLayoutManager(new LinearLayoutManager(this));
        this.courseExisted.setLayoutManager(new LinearLayoutManager(this));
        ScaleInAnimationAdapter animationAdapter1 = new ScaleInAnimationAdapter(courseListAdp);
        ScaleInAnimationAdapter animationAdapter2 = new ScaleInAnimationAdapter(courseExistedAdp);
        animationAdapter1.setDuration(1000);
        animationAdapter2.setDuration(1000);
        courseRecy.setAdapter(animationAdapter1);
        courseExisted.setAdapter(animationAdapter2);
        courseRecy.setItemAnimator(new OvershootInLeftAnimator());
        courseExisted.setItemAnimator(new OvershootInLeftAnimator());

        sharedPref = MainActivity.this.getSharedPreferences("username", Context.MODE_PRIVATE);
        String username = sharedPref.getString("username","");
        if(username.isEmpty()){
            startActivityForResult(new Intent(MainActivity.this, Login.class), 1);
        }
        else{
            student = sdb.queryStu(sharedPref.getString("username", "")).get(0);
            Toast.makeText(MainActivity.this, "欢迎" + student.getSName() + "同学", Toast.LENGTH_SHORT).show();
            List<CourseModel> courselist = cdb.queryCourseBySname(student.getSName());

            for(int i = 0; i < courselist.size(); i++){
                Map<String, Object> tmp = new LinkedHashMap<>();
                tmp.put("name", courselist.get(i).getCourseName());
                tmp.put("time", courselist.get(i).getTime());
                tmp.put("room", courselist.get(i).getRoom());
                tmp.put("teacher", courselist.get(i).getTeacherName());
                tmp.put("object", courselist.get(i));
                courseItem.add(tmp);
            }
            courseListAdp.notifyDataSetChanged();
        }
    }

    private void setListener(){
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        /*this.goodsAdp.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this, DatailPage.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("goods", (Serializable)MainActivity.this.data.get(position));
                intent.putExtras(bundle);
                MainActivity.this.startActivityForResult(intent, 1);
            }

            @Override
            public void onLongClick(int position) {
                MainActivity.this.goodsAdp.removeItem(position);
                data.remove(position);
                Toast.makeText(MainActivity.this, "移除第" + position+ "个商品", Toast.LENGTH_LONG).show();
            }
        });*/
        courseListAdp.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

            }

            @Override
            public void onLongClick(int position) {

            }
        });
        courseExistedAdp.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                CourseModel course = (CourseModel) courseItem.get(position).get("object");
                addBtnFlag = 0;
                cdb.addExistedCourse(course.getCourseId(), student.getSName());
                courseHint.setText("添加课程");
                courseRecy.setVisibility(View.VISIBLE);
                courseExisted.setVisibility(View.INVISIBLE);
                List<CourseModel> courselist = cdb.queryCourseBySname(student.getSName());
                courseItem.clear();
                for(int i = 0; i < courselist.size(); i++){
                    Map<String, Object> tmp = new LinkedHashMap<>();
                    tmp.put("name", courselist.get(i).getCourseName());
                    tmp.put("time", courselist.get(i).getTime());
                    tmp.put("room", courselist.get(i).getRoom());
                    tmp.put("teacher", courselist.get(i).getTeacherName());
                    tmp.put("object", courselist.get(i));
                    courseItem.add(tmp);
                }
                courseListAdp.notifyDataSetChanged();
            }

            @Override
            public void onLongClick(int position) {

            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addBtnFlag == 0){
                    courseHint.setText("自定课程");
                    courseRecy.setVisibility(View.INVISIBLE);
                    courseExisted.setVisibility(View.VISIBLE);
                    courseItem.clear();
                    List<CourseModel> courselist = cdb.getAllCourses();
                    for(int i = 0; i < courselist.size(); i++){
                        Map<String, Object> tmp = new LinkedHashMap<>();
                        tmp.put("name", courselist.get(i).getCourseName());
                        tmp.put("time", courselist.get(i).getTime());
                        tmp.put("room", courselist.get(i).getRoom());
                        tmp.put("teacher", courselist.get(i).getTeacherName());
                        tmp.put("object", courselist.get(i));
                        courseItem.add(tmp);
                    }
                    courseListAdp.notifyDataSetChanged();
                    addBtnFlag = 1;
                }
                else{
                    addCourse.setTitle("添加自定义课程");
                    LayoutInflater factor = LayoutInflater.from(MainActivity.this);
                    View view_in = factor.inflate(R.layout.course_edit_dialog_layout, null);
                    addCourse.setView(view_in);
                    final EditText editCourseName = view_in.findViewById(R.id.course_edit_name);
                    final EditText editCourseRoom = view_in.findViewById(R.id.course_edit_room);
                    final EditText editCourseStartHour = view_in.findViewById(R.id.course_edit_start_hour);
                    final EditText editCourseStratMinute = view_in.findViewById(R.id.course_edit_start_minute);
                    final EditText editCourseEndHour = view_in.findViewById(R.id.course_edit_end_hour);
                    final EditText editCourseEndMinute = view_in.findViewById(R.id.course_edit_end_minute);
                    final EditText editCourseTeacher = view_in.findViewById(R.id.course_edit_teacher);
                    final Spinner editCourseweekday = view_in.findViewById(R.id.course_edit_weekday);
                    addCourse.setPositiveButton("添加课程", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CourseModel course = new CourseModel();
                            course.setRoom(editCourseRoom.getText().toString());
                            course.setCourseName(editCourseName.getText().toString());
                            course.setStartTime(editCourseStartHour.getText().toString() + ":" + editCourseStratMinute.getText().toString());
                            course.setEndTime(editCourseEndHour.getText().toString() + ":" + editCourseEndMinute.getText().toString());
                            course.setWeekDay(editCourseweekday.getSelectedItem().toString());
                            course.setTeacherName(editCourseTeacher.getText().toString());
                            cdb.addNewCourse(student.getSName(), course);
                            addBtnFlag = 0;
                            courseHint.setText("添加课程");
                            courseRecy.setVisibility(View.VISIBLE);
                            courseExisted.setVisibility(View.INVISIBLE);
                            List<CourseModel> courselist = cdb.queryCourseBySname(student.getSName());
                            courseItem.clear();
                            for(int i = 0; i < courselist.size(); i++){
                                Map<String, Object> tmp = new LinkedHashMap<>();
                                tmp.put("name", courselist.get(i).getCourseName());
                                tmp.put("time", courselist.get(i).getTime());
                                tmp.put("room", courselist.get(i).getRoom());
                                tmp.put("teacher", courselist.get(i).getTeacherName());
                                tmp.put("object", courselist.get(i));
                                courseItem.add(tmp);
                            }
                            courseListAdp.notifyDataSetChanged();
                        }
                    });
                    addCourse.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    });
                }
            }
        });
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
            editor.apply();
            editor.commit();
            student = sdb.queryStu(sharedPref.getString("username", "")).get(0);
            Toast.makeText(MainActivity.this, "欢迎" + student.getSName() + "同学", Toast.LENGTH_SHORT);
            List<CourseModel> courselist = cdb.queryCourseBySname(student.getSName());

            for(int i = 0; i < courselist.size(); i++){
                Map<String, Object> tmp = new LinkedHashMap<>();
                tmp.put("name", courselist.get(i).getCourseName());
                tmp.put("time", courselist.get(i).getTime());
                tmp.put("room", courselist.get(i).getRoom());
                courseItem.add(tmp);
            }
            courseListAdp.notifyDataSetChanged();
            //search in DB to initial classes and taskDDL;
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