package com.example.minjia.androidlabs;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class ChatDatabaseHelper extends SQLiteOpenHelper {
    protected static final String DATABASE_NAME="message.db";
    protected static final String TABLE_NAME="message";
    protected static final String KEY_ID ="id";
    protected static final String KEY_MESSAGE = "message";
    protected static final int VERSION_NUM =6;
    private static SQLiteDatabase db;

    static final String DATABASE_CREATE = "Create table message (id integer primary key autoincrement,"
            + " message text not null)";
    //constructor
    public ChatDatabaseHelper(Context ctx) {

        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(DATABASE_CREATE);
        Log.i("ChatDatabaseHelper", "Calling onCreate");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer){
        Log.i("ChatDatabaseHelper","Calling onUpgrade, oldVersion=" + oldVer + " newVersion=" + newVer);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void getMessage( SQLiteDatabase db){
        Cursor cursor=db.query("select * from " + TABLE_NAME, null, null, null, null, null,null);

    }

    public int delete(String table, String row, String[] str){

            db.delete(ChatDatabaseHelper.TABLE_NAME, null, str);
            return 0;
    }


}
