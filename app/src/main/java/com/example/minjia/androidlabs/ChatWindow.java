package com.example.minjia.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.*;
import android.widget.ArrayAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;

public class ChatWindow extends Activity {
    //add class variables
    static final String ACTIVITY_NAME="ChatWindowActivity";
    ListView myList;

    EditText editText ;
    Button btn;
    private ArrayList <String> messageList;
    private ChatDatabaseHelper dbHelper;
    private static SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        //initialize the class variables
        myList = (ListView) findViewById(R.id.chatView);
        editText = (EditText) findViewById(R.id.editText);
        btn = (Button) findViewById(R.id.Send);
        messageList = new ArrayList<>();
        //this:ChatWindow - a Context object
        final ChatAdapter messageAdapter = new ChatAdapter(this);
        myList.setAdapter(messageAdapter);

        dbHelper = new ChatDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        cursor = db.query(dbHelper.TABLE_NAME, null, null, null, null,null,null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            String message = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + message);
            messageList.add(message);
            cursor.moveToNext();
        }
        int count = cursor.getColumnCount();
        Log.i(ACTIVITY_NAME, "Cursor's column count = " + count);
        for (int i=0; i<count; i++){
            String column_name = cursor.getColumnName(i);
            Log.i(ACTIVITY_NAME, "Cursor's column name = " + cursor.getColumnName(i));
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                messageList.add(text);
                messageAdapter.notifyDataSetChanged();
                editText.setText("");
                ContentValues contentValues = new ContentValues();
                contentValues.put(ChatDatabaseHelper.KEY_MESSAGE, text);
                db.insert(ChatDatabaseHelper.TABLE_NAME, null,contentValues);
            }

        });
    }

    private class ChatAdapter extends ArrayAdapter<String>{

        protected ChatAdapter(Context ctx){

            super(ctx, 0);
        }
        //getCount() returns the number of rows in the ListView, here it's
        // number of strings in the array list objects
        @Override
        public int getCount(){

            return messageList.size();

        }
        //this function returns the item to show the list at the specified position
        @Override
        public String getItem(int position){

            return messageList.get(position);
        }
        //returns the layout that will be positioned at the specified row in the list
        //step #9
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View resultView = null;
            if(position%2==0){
                resultView = inflater.inflate(R.layout.chat_row_incoming,null);
            }
            else
                resultView = inflater.inflate(R.layout.chat_row_outgoing,null);

            TextView message = (TextView)resultView.findViewById(R.id.message_text);
            message.setText(getItem(position));
            return resultView;
        }
        //database id of the item at position. here just return the number:position
        @Override
        public long getItemId(int position){

            return position;
        }
    }
    @Override
    public void onDestroy(){
        db.close();
        Log.i(ACTIVITY_NAME, "In onDestroy");
        super.onDestroy();
    }
}
