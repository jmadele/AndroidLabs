package com.example.minjia.androidlabs;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import java.util.ArrayList;

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


        btn.setOnClickListener(new View.OnClickListener() {
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
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if(position%2==0){
                result = inflater.inflate(R.layout.chat_row_incoming,null);
            }
            else
                result = inflater.inflate(R.layout.chat_row_outgoing,null);

            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(getItem(position));
            return result;
        }
        @Override
        public long getItemId(int position){
            return super.getItemId(position);
        }

}

}
