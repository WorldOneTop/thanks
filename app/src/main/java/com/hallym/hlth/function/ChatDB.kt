package com.hallym.hlth.function

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import com.hallym.hlth.models.ChatRoom
import com.hallym.hlth.models.Chatting
import java.util.ArrayList

class ChatDB(
    val context: Context,
    factory: SQLiteDatabase.CursorFactory? = null,
    name: String = "chat.db",
    version: Int = 1
) : SQLiteOpenHelper(context, name, factory, version) {
    object Column : BaseColumns {
        const val OTHER_ID = "otherID"
        const val NAME = "name"
        const val IS_RECEIVED = "isReceived"
        const val CONTENT = "content"
        const val DATE = "date"
        const val NO_READ = "isRead"
    }
    var tableName:String = "t_" + LoginStorage.id!!

    override fun onCreate(db: SQLiteDatabase) {
        val sql = "CREATE TABLE if not exists $tableName (" +
                "_id integer primary key autoincrement," +
                "${Column.OTHER_ID} INTEGER NOT NULL, " +
                "${Column.NAME} TEXT NOT NULL, " +
                "${Column.IS_RECEIVED} INTEGER NOT NULL, " +
                "${Column.CONTENT} TEXT NOT NULL, " +
                "${Column.DATE} TEXT NOT NULL, " +
                "${Column.NO_READ} INTEGER DEFAULT 1 " +
                ");"
        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val sql = "DROP TABLE if exists $tableName"

        db.execSQL(sql)
        onCreate(db)
    }
}
class ChatController(private val db: SQLiteDatabase){
    var tableName:String = "t_" + LoginStorage.id!!
    init{
        val sql = "CREATE TABLE if not exists $tableName (" +
                "_id integer primary key autoincrement," +
                "${ChatDB.Column.OTHER_ID} INTEGER NOT NULL, " +
                "${ChatDB.Column.NAME} TEXT NOT NULL, " +
                "${ChatDB.Column.IS_RECEIVED} INTEGER NOT NULL, " +
                "${ChatDB.Column.CONTENT} TEXT NOT NULL, " +
                "${ChatDB.Column.DATE} TEXT NOT NULL, " +
                "${ChatDB.Column.NO_READ} INTEGER DEFAULT 1 " +
                ");"
        db.execSQL(sql)
    }
    fun sendChat(otherID:Int, content:String, date:String,name:String){
        val values = ContentValues()
        values.put(ChatDB.Column.OTHER_ID,otherID)
        values.put(ChatDB.Column.NAME,name)
        values.put(ChatDB.Column.IS_RECEIVED,0)
        values.put(ChatDB.Column.CONTENT,content)
        values.put(ChatDB.Column.DATE,date)
        db.insert(tableName, null, values)
        db.close()
    }
    fun recvChat(otherID:Int, content:String, date:String,name:String, isRead:Boolean){
        val values = ContentValues()
        values.put(ChatDB.Column.OTHER_ID,otherID)
        values.put(ChatDB.Column.NAME,name)
        values.put(ChatDB.Column.IS_RECEIVED,1)
        values.put(ChatDB.Column.CONTENT,content)
        values.put(ChatDB.Column.DATE,date)
        if(isRead){
            values.put(ChatDB.Column.NO_READ,0)
        }
        db.insert(tableName, null, values)
        db.close()
    }
    fun lastChatList():ArrayList<ChatRoom>{
        val sql = "SELECT MAX(_id), ${ChatDB.Column.NAME}, ${ChatDB.Column.OTHER_ID},${ChatDB.Column.DATE}," +
                "${ChatDB.Column.CONTENT}, SUM(${ChatDB.Column.NO_READ}), ${ChatDB.Column.IS_RECEIVED} FROM $tableName " +
                "GROUP BY ${ChatDB.Column.OTHER_ID} " +
                "ORDER BY _id;"

        val cursor = db.rawQuery(sql,null)
        val result = ArrayList<ChatRoom>()

        while(cursor.moveToNext()){
            result.add(ChatRoom(
                cursor.getString(1),
                cursor.getInt(2),
                cursor.getString(3),
                cursor.getString(4),
                if(cursor.getInt(6)==1) cursor.getInt(5) else 0,
            ))
        }

        cursor.close()
        db.close()
        return result
    }
    fun getInRoom(userId:Int):ArrayList<Chatting>{
        val sql = "SELECT ${ChatDB.Column.OTHER_ID}, ${ChatDB.Column.NAME}, ${ChatDB.Column.DATE}" +
                ", ${ChatDB.Column.CONTENT}, ${ChatDB.Column.NO_READ}, ${ChatDB.Column.IS_RECEIVED} " +
                "FROM $tableName " +
                "WHERE ${ChatDB.Column.OTHER_ID}=$userId " +
                "ORDER BY _id"

        val cursor = db.rawQuery(sql,null)
        val result = ArrayList<Chatting>()

        while(cursor.moveToNext()) {
            val chatRoom = Chatting(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getInt(4),
                cursor.getInt(5) == 0
            )
            result.add(chatRoom)
        }

        cursor.close()
        // 읽음표시
        val sql2 = "UPDATE $tableName SET ${ChatDB.Column.NO_READ}=0 WHERE ${ChatDB.Column.OTHER_ID} = $userId AND ${ChatDB.Column.NO_READ}=1 AND ${ChatDB.Column.IS_RECEIVED}=1;"
        db.execSQL(sql2)
        db.close()
        return result
    }
    fun readAllChat(userId:String){
        val sql = "UPDATE $tableName SET ${ChatDB.Column.NO_READ}=0 WHERE ${ChatDB.Column.OTHER_ID} = $userId AND ${ChatDB.Column.NO_READ}=1;"
        db.execSQL(sql)
        db.close()
    }
    fun deleteChatRoom(userId:String){
        val sql = "DELETE FROM $tableName WHERE ${ChatDB.Column.OTHER_ID} = $userId;"
        db.execSQL(sql)
        db.close()
    }
}