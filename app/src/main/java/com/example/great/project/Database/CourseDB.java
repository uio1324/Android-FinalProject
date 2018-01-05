package com.example.great.project.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.great.project.Models.CourseModel;
import com.example.great.project.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by great on 2018/1/5.
 */

public class CourseDB extends SQLiteOpenHelper {
    private static String DB_NAME;
    private static final int DB_VERSION = 1;

    public CourseDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        DB_NAME = context.getResources().getString(R.string.DB_name);
    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE1 = "create table " + "Courses"
                + " (courseId integer primary key autoincrement, "
                + "courseName text , "
                + "weekday integer , "
                + "startTime text , "
                + "endTime text , "
                + "teacherName text);";
        String CREATE_TABLE2 = "create table " + "StuCourses"
                + " (Id integer primary key autoincrement, "
                + "sname text , "
                + "cid integer)";
        db.execSQL(CREATE_TABLE1);
        db.execSQL(CREATE_TABLE2);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}

    public int existCourse(CourseModel course){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Courses " +
                "where courseName = ? and weekday = ? and startTime = ? and endTime = ? and teacherName = ?",
                new String[]{course.getCourseName(), Integer.toString(course.getWeekDay()), course.getStartTime(), course.getEndTime(), course.getTeacherName()});
        if(cursor.getCount() == 0) return -1;
        else return cursor.getInt(cursor.getColumnIndex("courseId"));
    }


    //根据学生的名字，返回该学生参与的所有课程
    public List<CourseModel> queryCourseBySname(String sname){
        SQLiteDatabase db = getWritableDatabase();
        List<CourseModel> queryRes = new ArrayList();
        Cursor cursor = db.rawQuery("select courseId, courseName, weekday, startTime, endTime, tearherName " +
                "from Courses, StuCourses " +
                "where Courses.courseId = StuCourses.cid and StuCourses.sname = ?", new String[]{sname});
        while (cursor.moveToNext()){
            CourseModel tmp = new CourseModel();
            tmp.setCourseId(cursor.getInt(cursor.getColumnIndex("courseID")));
            tmp.setCourseName(cursor.getString(cursor.getColumnIndex("courseName")));
            tmp.setWeekDay(cursor.getInt(cursor.getColumnIndex("weekday")));
            tmp.setStartTime(cursor.getString(cursor.getColumnIndex("startTime")));
            tmp.setEndTime(cursor.getString(cursor.getColumnIndex("endTime")));
            tmp.setTeacherName(cursor.getString(cursor.getColumnIndex("teacherName")));
            queryRes.add(tmp);
        }
        return queryRes;
    }

    //返回所有课程
    public List<CourseModel> getAllCourses(){
        SQLiteDatabase db = getWritableDatabase();
        List<CourseModel> queryRes = new ArrayList();
        Cursor cursor = db.rawQuery("select * from Courses", null);
        while (cursor.moveToNext()){
            CourseModel tmp = new CourseModel();
            tmp.setCourseId(cursor.getInt(cursor.getColumnIndex("courseID")));
            tmp.setCourseName(cursor.getString(cursor.getColumnIndex("courseName")));
            tmp.setWeekDay(cursor.getInt(cursor.getColumnIndex("weekday")));
            tmp.setStartTime(cursor.getString(cursor.getColumnIndex("startTime")));
            tmp.setEndTime(cursor.getString(cursor.getColumnIndex("endTime")));
            tmp.setTeacherName(cursor.getString(cursor.getColumnIndex("teacherName")));
            queryRes.add(tmp);
        }
        return queryRes;
    }

    //传入参数为课程id和学生名称，用于学生从已存在课程列表选择课程加入
    public void addExistedCourse(int courseId, String sname){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from StuCourses " +
                "where cid = ? and sname = ?", new String[]{Integer.toString(courseId), sname});
        if(cursor.getCount() > 0) return;
        ContentValues values = new ContentValues();
        //values.put("Id", null);
        values.put("sname", sname);
        values.put("cid", courseId);
        db.insert("StuCourses", null, values);
        db.close();
    }

    //用于添加新课程，传入执行添加操作的学生名和课程的model类。
    public void addNewCourse(String sname, CourseModel course) {
        SQLiteDatabase db = getWritableDatabase();
        if(existCourse(course) > 0){
            addExistedCourse(existCourse(course), sname);
        }
        else{
            ContentValues values = new ContentValues();
            //values.put("Id", null);
            values.put("courseName", course.getCourseName());
            values.put("weekday", course.getWeekDay());
            values.put("startTime", course.getStartTime());
            values.put("endTime", course.getEndTime());
            values.put("teacherName", course.getTeacherName());
            db.insert("Courses", null, values);
            Cursor cursor = db.rawQuery("select * from Courses " +
                            "where courseName = ? and weekday = ? and startTime = ? and endTime = ? and teacherName = ?",
                    new String[]{course.getCourseName(), Integer.toString(course.getWeekDay()), course.getStartTime(), course.getEndTime(), course.getTeacherName()});
            int id = cursor.getInt(cursor.getColumnIndex("courseId"));
            addExistedCourse(id, sname);
        }
        db.close();
    }

    //用于修改课程内容，传入执行修改的学生名和课程的model类
    public void updateCourse(String sname, CourseModel course){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from StuCourses " +
                "where cid = ?", new String[]{Integer.toString(course.getCourseId())});
        if(cursor.getCount() == 1){
            String whereClause = "courseId = ?";
            String[] whereArgs = {Integer.toString(course.getCourseId())};
            db.delete("Courses", whereClause, whereArgs);
        }
        String whereClause = "cid = ?";
        String[] whereArgs = {Integer.toString(course.getCourseId())};
        db.delete("StuCourses", whereClause, whereArgs);
        addNewCourse(sname, course);
        db.close();
    }

    //用于删除课程，传入执行删除的学生名和要删除的课程id
    public void deleteCourse(String sname, int cid){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from StuCourses " +
                "where cid = ?", new String[]{Integer.toString(cid)});
        if(cursor.getCount() == 1){
            String whereClause = "courseId = ?";
            String[] whereArgs = {Integer.toString(cid)};
            db.delete("Courses", whereClause, whereArgs);
        }
        String whereClause = "cid = ?";
        String[] whereArgs = {Integer.toString(cid)};
        db.delete("StuCourses", whereClause, whereArgs);
        db.close();
    }

}
