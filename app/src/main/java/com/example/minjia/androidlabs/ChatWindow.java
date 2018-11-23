package com.example.minjia.androidlabs;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class ChatWindow extends Activity {
    //add class variables
    static final String ACTIVITY_NAME="ChatWindowActivity";
    private ListView listView;
    private EditText editText ;
    private Button sendButton;

    private ChatDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Cursor cursor ;
    private ChatAdapter messageAdapter;
    private boolean isTablet;

    ArrayList<String> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        listView =  findViewById(R.id.chatView);
        editText =  findViewById(R.id.editText);
        sendButton =  findViewById(R.id.Send);
        messageList = new ArrayList< String>();

        //in this case, “this” is the ChatWindow, which is-A Context object
        messageAdapter =new ChatAdapter( this );
        listView.setAdapter (messageAdapter);

        dbHelper = new ChatDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        cursor =  db.rawQuery( "select * from " + ChatDatabaseHelper.TABLE_NAME, null );
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            String message = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
            long id = cursor.getLong(cursor.getColumnIndex(ChatDatabaseHelper.KEY_ID));
            Log.i(ACTIVITY_NAME, "SQL ID:" + id);
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + message);
            messageList.add(message);

            System.out.println(messageList);
            cursor.moveToNext();
        }

        int count = cursor.getColumnCount();
        Log.i(ACTIVITY_NAME, "Cursor’s  column count = " + count);
        for (int i =0 ; i< count; i++) {
            String column_name = cursor.getColumnName(i);
            Log.i(ACTIVITY_NAME, "Cursor’s  column name: = " + cursor.getColumnName(i));
        }

        FrameLayout frameLayout = findViewById(R.id.chat_message);
        if(frameLayout == null){
            Log.i(ACTIVITY_NAME, "framelayout not loaded:" +frameLayout);
            isTablet = false;
        } else{
            Log.i(ACTIVITY_NAME, "framelayout loaded: "+frameLayout);
            isTablet = true;
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String  message =  messageAdapter.getItem(position);

                long msgId = messageAdapter.getItemId(position);

                if(isTablet){
                    Bundle bundle = new Bundle();
                    bundle.putLong("id",msgId);
                    bundle.putInt("position",position);
                    bundle.putString("message", message);
                    Log.i(ACTIVITY_NAME,"check id when called getItemId():" +messageAdapter.getItemId(position) );

                    MessageFragment fragment = new MessageFragment();
                    fragment.setArguments(bundle);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.chat_message, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                /* sending the activity to the newly created MessageDetails class */
                else{
                    Intent intent = new Intent(ChatWindow.this, MessageDetails.class);
                    intent.putExtra("id",msgId);
                    intent.putExtra("position",position);
                    intent.putExtra("message", message);
                    startActivityForResult(intent, 10);
                }
            }
        });
       sendButton.setOnClickListener(view -> {
            String message = editText.getText().toString();
            messageList.add(message);

            ContentValues contentValues = new ContentValues();
            contentValues.put(ChatDatabaseHelper.KEY_MESSAGE,message);
            db.insert(ChatDatabaseHelper.TABLE_NAME, null, contentValues);
            messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount() & getView()
            editText.setText("");
        });

    }

    private class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View view ;
            if(position%2 == 0)
                view = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                view = inflater.inflate(R.layout.chat_row_outgoing, null);
            TextView message = view.findViewById(R.id.message_text);
            message.setText(getItem(position)); // get the string at position
            return view;
        }

        public int getCount(){
            return messageList.size();
        }

        public String getItem(int position){
            return messageList.get(position);
        }
        //get the ID of the object at the position in the database
        public long getItemId(int position){
            cursor = db.rawQuery("select * from " + ChatDatabaseHelper.TABLE_NAME, null);;
            cursor.moveToPosition(position);
            Log.i(ACTIVITY_NAME, "Position: " + position);
            Log.i(ACTIVITY_NAME, "cursor Position: "+ cursor.moveToPosition(position));
           // Log.i(ACTIVITY_NAME, "returned id: "+ messageList.get(position).getId());
            return cursor.getLong(cursor.getColumnIndex(ChatDatabaseHelper.KEY_ID));
        }
    }

    @Override
    protected void onDestroy() {
        Log.i(ACTIVITY_NAME, "In onDestroy()");
        db.close();
        super.onDestroy();
    }

    public void deleteMessage(int position){
        try {
            //db.delete(ChatDatabaseHelper.TABLE_NAME, ChatDatabaseHelper.KEY_ID+" = ?", new String[]{String.valueOf(messageAdapter.getItemId(position))});
            messageList.remove(position);
            messageAdapter.notifyDataSetChanged();
        }catch(SQLException e){    }
    }



    public void onActivityResult(int requestCode, int responseCode, Intent data){
        if(requestCode == 10  && responseCode == -1) {
            Bundle bundle = data.getExtras();
            int position = bundle.getInt("position");
            long id = bundle.getLong("id");
            deleteMessage(position);
        }
    }
}