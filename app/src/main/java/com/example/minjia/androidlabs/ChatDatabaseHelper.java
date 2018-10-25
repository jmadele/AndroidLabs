package com.example.minjia.androidlabs;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class ChatDatabaseHelper extends SQLiteOpenHelper {
    protected static final String DATABASE_NAME="message.db";
    protected static final String TABLE_NAME="message";
    protected static final String KEY_ID ="id";
    protected static final String KEY_MESSAGE = "message";
    protected static final int VERSION_NUM =3;

//    static final String DATABASE_CREATE = "Create table messages (KEY_ID integer primary key autoincrement,"
//            + " KEY_MESSAGE text not null)";
    //constructor
    public ChatDatabaseHelper(Context ctx) {

        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
       // db.execSQL(DATABASE_CREATE);
        db.execSQL("Create table " + TABLE_NAME +
                "(" + KEY_ID + " integer primary key autoincrement, " + KEY_MESSAGE + " text)" );
        Log.i("ChatDatabaseHelper", "Calling onCreate");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer){
        Log.i("ChatDatabaseHelper","Calling onUpgrade, oldVersion=" + oldVer + " newVersion=" + newVer);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void getMessage(SQLiteDatabase db){
        Cursor cursor=db.query("select * from " + TABLE_NAME, null, null, null, null, null,null);
    }
}
