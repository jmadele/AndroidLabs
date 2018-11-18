package com.example.minjia.androidlabs;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
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
    private ListView myList;

    private EditText editText ;
    private Button btn;
    private ArrayList <String> messageList;
    private ChatDatabaseHelper dbHelper;
    private static SQLiteDatabase db;
    private Cursor cursor;
    private ChatAdapter messageAdapter;
    private boolean isTablet;

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
        messageAdapter = new ChatAdapter(this);
        myList.setAdapter(messageAdapter);

        dbHelper = new ChatDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        cursor = db.query(dbHelper.TABLE_NAME, null, null, null, null,null,null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            String message = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
            long id = cursor.getLong(cursor.getColumnIndex(ChatDatabaseHelper.KEY_ID));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + message);
            Log.i(ACTIVITY_NAME, "SQL ID:" + id);
            messageList.add(message);
            cursor.moveToNext();
        }
        int count = cursor.getColumnCount();
        Log.i(ACTIVITY_NAME, "Cursor's column count = " + count);
        for (int i=0; i<count; i++){
            String column_name = cursor.getColumnName(i);
            Log.i(ACTIVITY_NAME, "Cursor's column name = " + cursor.getColumnName(i));
        }

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.chat_message);
        if(frameLayout==null){
            Log.i(ACTIVITY_NAME, "Frame Layout not loaded");
            isTablet = false;
        }else {
            Log.i(ACTIVITY_NAME, "Frame Layoutloaded");
            isTablet = true;
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
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               String message = (String) myList.getItemAtPosition(position);
               Toast.makeText(getBaseContext(),message, Toast.LENGTH_SHORT).show();

                if(isTablet){
                    MessageFragment newFragment = new MessageFragment();
                    newFragment.setChatWindow(ChatWindow.this);
                    Bundle bundle = new Bundle();
                    bundle.putLong("id",id);
                    bundle.putString("text",message);
                    bundle.putInt("position", position);
                    newFragment.setArguments(bundle);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_frame, newFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }else {
                    Intent intent = new Intent(getApplicationContext(),MessageDetails.class);
                    intent.putExtra("message", message);
                    intent.putExtra("position", position);
                    intent.putExtra("id", id);
                    startActivityForResult(intent, 10);
                }
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
            cursor = db.rawQuery("select * from "+ChatDatabaseHelper.TABLE_NAME,null);
            cursor.moveToPosition(position);
            int id =cursor.getInt(cursor.getColumnIndex(ChatDatabaseHelper.KEY_ID));
            return id;
        }
    }
    public void deleteMessage(int position, long id){

        db.delete(ChatDatabaseHelper.TABLE_NAME, ChatDatabaseHelper.KEY_ID+" = ?",
                new String[]{String.valueOf(id)});

        messageList.remove(position);
        messageAdapter.notifyDataSetChanged();
    }
    public void onActivityResult(int requestCode, int responseCode, Intent data){
        if(requestCode==10 && responseCode==10){
            Bundle bundle = data.getExtras();
            int position = bundle.getInt("position");
            long id=bundle.getLong("id");
            deleteMessage(position,id);
        }

    }
    @Override
    public void onDestroy(){
        db.close();
        Log.i(ACTIVITY_NAME, "In onDestroy");
        super.onDestroy();
    }


}
