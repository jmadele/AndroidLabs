package com.example.minjia.androidlabs;

import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MessageFragment extends Fragment {
    TextView msgView;
    TextView idView;
    Button deleteBtn;
    String message;
    int position;
    long id;
    ChatWindow chatWindow;
    public MessageFragment(){

    }
    public static MessageFragment newInstance(){
        MessageFragment myFragment = new MessageFragment();
        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if(bundle!=null){
            message=bundle.getString("message");
            position = bundle.getInt("position");
            id = bundle.getLong("id");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //inflate the layout for this fragment_message
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        msgView = view.findViewById(R.id.messageView);
        msgView.setText(message);

        idView = view.findViewById(R.id.msgId);
        idView.setText(Double.toString(id).split("\\.")[0]);

        deleteBtn = (Button) view.findViewById(R.id.DeleteButton);
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(chatWindow !=null){
                    chatWindow.deleteMessage(position, id);
                    getActivity().getFragmentManager().popBackStack();
                }else {
                    Intent intent = new Intent();
                    intent.putExtra("position", position);
                    intent.putExtra("id", id);
                    getActivity().setResult(10, intent);
                    getActivity().finish();
                }
            }
        });
        return view;
    }

    public void setChatWindow(ChatWindow chatWindow) {
        this.chatWindow = chatWindow;
    }
}
