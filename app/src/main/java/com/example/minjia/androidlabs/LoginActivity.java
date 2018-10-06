package com.example.minjia.androidlabs;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.minjia.androidlabs.ListItemsActivity;
import com.example.minjia.androidlabs.R;

public class LoginActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);
        Log.i("LoginActivity", "In onCreate");
        //#4
        final Button button_login = (Button)findViewById(R.id.button_Login);
        final EditText editText = (EditText) findViewById(R.id.email);

        final SharedPreferences prefs = getSharedPreferences("User_name", Context.MODE_PRIVATE);
        editText.setText(prefs.getString("DefaultEmail","email@domain.com"));

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("DefaultEmail",editText.getText().toString());
                editor.commit();
                Intent intent = new Intent (LoginActivity.this, StartActivity.class);
                startActivity(intent);
            }
        });
    }
    public void onStart(){
        super.onStart();
        final String LoginActivity = "LoginActivity";
        Log.i(LoginActivity,"In onStart()");
    }
    public void onResume(){
        super.onResume();
        final String LoginActivity = "LoginActivity";
        Log.i(LoginActivity,"In onResume()");
    }
    public void onPause(){
        super.onPause();
        final String LoginActivity = "LoginActivity";
        Log.i(LoginActivity,"In onPause()");
    }
    public void onStrop(){
        super.onStop();
        final String LoginActivity = "LoginActivity";
        Log.i(LoginActivity,"In onStop()");
    }
    public void onDestroy(){
        super.onDestroy();
        final String LoginActivity = "LoginActivity";
        Log.i(LoginActivity,"In onDestroy()");
    }
}
