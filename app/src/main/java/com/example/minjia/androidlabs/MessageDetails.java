package com.example.minjia.androidlabs;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MessageDetails extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        String message = getIntent().getExtras().getString("message");
        int position = getIntent().getExtras().getInt("position");
        long id = getIntent().getExtras().getLong("id");

        MessageFragment fragment= new MessageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        bundle.putInt("position", position);
        bundle.putLong("id", id);
        fragment.setArguments(bundle);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();

    }
}
