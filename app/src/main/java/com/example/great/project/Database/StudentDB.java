package com.example.great.project.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.great.project.Model.Student;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Danboard on 18-1-5.
 */

public class StudentDB extends SQLiteOpenHelper {
    private static final String DB_NAME = "SCHOOL.db";          // 数据库名字
    private static final String STU_TABLE_NAME = "STUDENTS";    // "学生"表名
    private static final String SET_TABLE_NAME = "SETTINGS";    // "设置"表名
    private static final int DB_VERSION = 1;                    // 版本号



    public StudentDB(Context context) {  // 构造函数，方便创建，只传入context
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String STU_TABLE = "create table " + STU_TABLE_NAME
                + "(sname text primary key, "
                + "nickname text not null "
                + "password text not null);";
        db.execSQL(STU_TABLE);
        String SET_TABLE = "create table " + SET_TABLE_NAME
                + "(sName text primary key, "
                + "maxtask integer not null, "
                + "studytime text, "
                + "resttime text);";
        db.execSQL(SET_TABLE);
    }

    /* DB_VERSION 变化时调用此函数 */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + STU_TABLE_NAME);
        db.execSQL("drop table if exists " + SET_TABLE_NAME);
        onCreate(db);
    }

    /* 注册（添加）用户 */
    public void insertStu(Student item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("sname", item.getSName());
        values.put("nickname", item.getNickName());
        values.put("password", item.getPassword());

        db.insert(STU_TABLE_NAME, null, values);
        Log.d("TAG", "Student Insert Successfully");
    }

    /* 查询sName | 所有用户 */
    public List<Map<String, Object>> queryStu(String sName) {
        SQLiteDatabase db = getReadableDatabase();
        String selection;
        String[] selectionArgs;
        if(sName == null){
            selection = null;
            selectionArgs = null;
        }else {
            selection = "sname = ? ";
            selectionArgs = new String[]{sName};
        }
        Cursor c = db.query(STU_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        List<Map<String, Object>> list = new ArrayList<>();
        while (c.moveToNext()) {
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("sname", c.getString(c.getColumnIndex("sname")));
            temp.put("nickname", c.getString(c.getColumnIndex("nickname")));
            temp.put("password", c.getString(c.getColumnIndex("password")));
            list.add(temp);
        }
        c.close();
        Log.d("TAG", "Student Query Successfully");
        return list;
    }

    /* 修改密码 | 昵称 */
    public void updateStu(Student item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        String whereClause = "sname = ?";
        String[] whereArgs = {item.getSName()};

        values.put("username", item.getSName());
        values.put("nickname", item.getNickName());
        values.put("password", item.getPassword());;

        db.update(STU_TABLE_NAME, values, whereClause, whereArgs);
        Log.d("TAG", "Student Update Successfully");
    }

    /* 学习设置 */
    public void StudySetting(Student item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        String whereClause = "sid = ?";
        String[] whereArgs = {item.getSName()};

        values.put("maxtask", item.getMaxTask());
        values.put("studytime", item.getStudyTime());
        values.put("resttime", item.getRestTime());

        db.update(SET_TABLE_NAME, values, whereClause, whereArgs);
        Log.d("TAG", "StuTask Update Successfully");
    }
}
