package com.example.minjia.androidlabs;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;

public class ChatWindow extends Activity {
    //add class variables
    ListView myList;
    EditText editText ;
    Button btn;
    ArrayList <String> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        //initialize the class variables
        myList = (ListView) findViewById(R.id.chatView);
        editText = (EditText) findViewById(R.id.editText);
        btn = (Button) findViewById(R.id.Send);
        messages = new ArrayList<>();


        final ChatAdapter messageAdapter = new ChatAdapter(this);
        myList.setAdapter(messageAdapter);

        btn.setOnClickListener(new View.onClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                messages.add(text);
                messageAdapter.notifyDataSetChanged();
                editText.setText("");
            }

        });
    }

private class ChatAdapter extends ArrayAdapter<String>{

        public ChatAdapter(Context ctx){
            super(ctx, 0);
        }
        @Override
        public int getCount(){
           return messages.size();
        }
        @Override
        public String getItem(int position){
            return messages.get(position);
        }

        //step #9
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            return ;
        }
        @Override
        public long getItemId(int position){
            return super.getItemId(position);
        }

}

}
