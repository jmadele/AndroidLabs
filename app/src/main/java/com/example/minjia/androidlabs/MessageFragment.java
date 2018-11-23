package com.example.minjia.androidlabs;

import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MessageFragment extends Fragment {
    View view;
    TextView msgView, idView;
    Button deleteBtn;
    String message;
    int position;
    long id;
    Bundle bundle;
    boolean isPhone;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        bundle = this.getArguments();
        if(bundle!=null){
            message=bundle.getString("message");
            position = bundle.getInt("position");
            id = bundle.getLong("id");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //inflate the layout for this fragment_message
        view = inflater.inflate(R.layout.fragment_message, container, false);
        msgView = view.findViewById(R.id.messageView);
        idView = view.findViewById(R.id.msgId);
        msgView.setText(message);
        idView.setText("ID: " + id);

        deleteBtn = view.findViewById(R.id.DeleteButton);
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (isPhone) {
                    Intent intent = new Intent(getActivity(),ChatWindow.class);
                    intent.putExtra("position", position);
                    intent.putExtra("id", id);
//                    MessageDetails md =(MessageDetails) getActivity();
//                    md.setResult(-1, intent);
//                    md.finish();
                    getActivity().setResult(-1, intent);
                    getActivity().finish();
                } else {
                    Log.i("tag", "trying to delete a message: " + position);
                    ChatWindow cw = (ChatWindow) getActivity();
                    cw.deleteMessage(position);
                    //chatWindow.deleteMessage(position);
                    //((ChatWindow) getActivity()).deleteMessage(getArguments().getInt("position"));
                    getFragmentManager().beginTransaction().remove(MessageFragment.this).commit();
                }
            }
        });
        return view;
    }
}
