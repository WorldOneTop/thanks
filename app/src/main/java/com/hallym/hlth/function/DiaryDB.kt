package com.hallym.hlth.function

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import com.hallym.hlth.DiaryData
import com.hallym.hlth.models.ChatRoom
import com.hallym.hlth.models.Chatting
import java.util.ArrayList

class DiaryDB(
    val context: Context,
    factory: SQLiteDatabase.CursorFactory? = null,
    name: String = "chat.db",
    version: Int = 1
) : SQLiteOpenHelper(context, name, factory, version) {
    var tableName:String = "diary_" + LoginStorage.id!!

    override fun onCreate(db: SQLiteDatabase) {
        val sql = "CREATE TABLE if not exists $tableName (" +
                "_id integer primary key autoincrement," +
                "date TEXT NOT NULL, " +
                "content TEXT NOT NULL " +
                ");"
        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val sql = "DROP TABLE if exists $tableName"

        db.execSQL(sql)
        onCreate(db)
    }
}
class DiaryController(private val db: SQLiteDatabase){
    var tableName:String = "diary_" + LoginStorage.id!!
    init{
        val sql = "CREATE TABLE if not exists $tableName (" +
                "_id integer primary key autoincrement," +
                "date TEXT NOT NULL, " +
                "content TEXT NOT NULL " +
                ");"
        db.execSQL(sql)
    }
    fun addDiary(diaryData: DiaryData):Long{
        val values = ContentValues()
        values.put("content",diaryData.content)
        values.put("date",diaryData.date)
        val lastIndex = db.insert(tableName, null, values)
        db.close()
        return lastIndex
    }
    fun selectDiary():ArrayList<DiaryData>{
        val sql = "SELECT _id, date, content FROM $tableName " +
                "ORDER BY -_id"

        val cursor = db.rawQuery(sql,null)
        val result = ArrayList<DiaryData>()

        while(cursor.moveToNext()){
            result.add(
                DiaryData(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                )
            )
        }

        cursor.close()
        db.close()
        return result
    }
    fun changeDiary(data: DiaryData){
        val sql = "UPDATE $tableName SET content = '${data.content}' " +
                "WHERE _id=${data.id};"
        db.execSQL(sql)
    }
}