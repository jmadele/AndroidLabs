package com.example.minjia.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.minjia.androidlabs.LoginActivity;
import com.example.minjia.androidlabs.R;

public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Log.i("StartActivity", "In onCreate()");

        final Button Login = findViewById(R.id.Button);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, ListItemsActivity.class);
                startActivityForResult(intent, 50);
            }
        });

        //4.2
        final Button StartChat = findViewById(R.id.StartChatButton);
        StartChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, ChatWindow.class);
                startActivityForResult(intent, 50);
                //Log.i("StartAvtivity", "User clicked Start Chat");
            }
        });
    }

        public void onActivityResult ( int requestCode, int responseCode, Intent data){
            if (requestCode == 50 && responseCode == Activity.RESULT_OK) {
                Log.i("StartActivity", "returned to StartActivity.onActivityResult");
                Log.i("StartAvtivity", "User clicked Start Chat");

                String messagePassed = data.getStringExtra("Response");
                CharSequence text = "ListItemsActivity passed ";
                Toast toast = Toast.makeText(this, messagePassed, Toast.LENGTH_LONG);
                toast.show();
            }
        }

        public void onStart () {
            super.onStart();
            final String StartActivity = "StartActivity";
            Log.i(StartActivity, "In onStart()");
        }
        public void onResume () {
            super.onResume();
            final String StartActivity = "StartActivity";
            Log.i(StartActivity, "In onResume()");
        }
        public void onPause () {
            super.onPause();
            final String StartActivity = "StartActivity";
            Log.i(StartActivity, "In onPause()");
        }
        public void onStrop () {
            super.onStop();
            final String StartActivity = "StartActivity";
            Log.i(StartActivity, "In onStop()");
        }
        public void onDestroy () {
            super.onDestroy();
            final String StartActivity = "StartActivity";
            Log.i(StartActivity, "In onDestroy()");
        }
    }

