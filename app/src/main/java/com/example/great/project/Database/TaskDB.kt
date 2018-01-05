package com.example.great.project.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.format.DateUtils
import com.example.great.project.Model.Task
import com.example.great.project.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by acera on 2018/1/5.
 *
 */

class TaskDB(context: Context?) : SQLiteOpenHelper(context, context!!.getString(R.string.DB_name), null, DB_VERSION)
{
    companion object
    {
        private val TABLE_NAME = "Task"
        private val DB_VERSION = 1
        private val createTable = "create table " + TABLE_NAME +
                " (_id integer primary key autoincrement, " +
                "taskName text not null, " +
                "taskBrief text , " +
                "taskDDL text," +
                "creatorID integer);"
        private val DTF: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
    }

    override fun onCreate(db: SQLiteDatabase?)
    {
        db!!.execSQL(createTable)
    }


    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int)
    {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    fun insert(data: Task, creatorId: Int)
    {
        val db = writableDatabase
        val values = ContentValues()
        values.put("taskName", data.taskName)
        values.put("taskBrief", data.taskBrief)
        values.put("taskDDL", DTF.format(data.taskDDL))
        values.put("creatorID", creatorId)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun updateByTaskName(data: Task, creatorId: Int)
    {
        val db = writableDatabase
        val values = ContentValues()
        values.put("taskName", data.taskName)
        values.put("taskBrief", data.taskBrief)
        values.put("taskDDL", DTF.format(data.taskDDL))
        values.put("creatorID", creatorId)
        val whereClause = "taskName = ?"
        val whereArgs = arrayOf(data.taskName)
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun deleteByTaskName(data: Task)
    {
        val db = writableDatabase
        val whereClause = "taskName = ?"
        val whereArgs = arrayOf(data.taskName)
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }

    fun getByTaskName(name: String): List<Task>?
    {
        var ans: LinkedList<Task>? = null
        val db = readableDatabase
        val selection = "taskName = ?"
        val selectionArgs = arrayOf(name)
        val c = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)

        if (c.moveToNext())
        {
            ans = LinkedList()
            ans.add(Task(c.getInt(0), c.getString(1), c.getString(2), DTF.parse(c.getString(3)), c.getInt(4)))
            while (c.moveToNext())
                ans.add(Task(c.getInt(0), c.getString(1), c.getString(2), DTF.parse(c.getString(3)), c.getInt(4)))
        }
        c.close()
        db.close()
        return ans
    }

    fun allTasks(): List<Task>
    {
        val ans = LinkedList<Task>()
        val db = readableDatabase
        val c = db.query(TABLE_NAME, null, null, null, null, null, null)

        while (c.moveToNext())
        {
            val temp = Task(c.getInt(0), c.getString(1), c.getString(2), DTF.parse(c.getString(3)), c.getInt(4))
            ans.add(temp)
        }
        c.close()
        db.close()
        return ans
    }
}