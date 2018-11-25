package com.example.minjia.androidlabs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class TestToolbar extends AppCompatActivity {
    Button snackbarBtn;
    String currentMessage=("You selected item 1");
    static final String ACTIVITY_NAME="ChatWindowActivity";
    AlertDialog.Builder builder;
    AlertDialog dialog;
    EditText editMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);

        Toolbar lab8_toolbar = findViewById(R.id.lab8_toolbar);
        setSupportActionBar( lab8_toolbar);
        snackbarBtn = findViewById(R.id.snackbarBtn);
        snackbarBtn.setOnClickListener(e->{
            Snackbar.make(findViewById(R.id.snackbarBtn), "message to show", Snackbar.LENGTH_SHORT).show();
                }
        );
    }
    public  boolean onCreateOptionsMenu (Menu m){
        getMenuInflater().inflate(R.menu.toolbar_menu, m );
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem mi){
        int id = mi.getItemId();
        switch (id) {
            case R.id.add:
                //setCurrentMessage("you selected ADD");
                Snackbar.make(findViewById(R.id.add), getCurrentMessage(), Snackbar.LENGTH_SHORT).show();
                Log.d("Toolbar", "you selected add");
                break;
            case R.id.search:
                setCurrentMessage("you selected Search");

                Snackbar.make(findViewById(R.id.add), getCurrentMessage(), Snackbar.LENGTH_SHORT).show();
                //Start an activity…
                builder = new AlertDialog.Builder(this);

                builder.setTitle(R.string.dialog_title);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        Intent intent = new Intent(TestToolbar.this, StartActivity.class);
                        startActivityForResult(intent, 50);
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });
                dialog = builder.create();
                dialog.show();

                break;
            case R.id.settings:
                setCurrentMessage("you selected settings");
                Snackbar.make(findViewById(R.id.add), getCurrentMessage(), Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.About:
                setCurrentMessage("you selected About");
                Snackbar.make(findViewById(R.id.add), getCurrentMessage(), Snackbar.LENGTH_SHORT).show();
                //message.equals( "You selected item 2: About");
                Toast.makeText(getApplicationContext(), "Version 1.0, by Min Jia", Toast.LENGTH_SHORT).show();


                LayoutInflater inflater = this.getLayoutInflater();
                final View view = inflater.inflate(R.layout.toolbar_message, null);
                builder = new AlertDialog.Builder(this);
                builder.setTitle("What is your new message");
                builder.setView(view);

                EditText inputEditText = new EditText(this);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        String newMessage =((EditText) view.findViewById(R.id.editText_lab8)).getText().toString();
                        setCurrentMessage(newMessage);
                    }
                });
                dialog = builder.create();
                dialog.show();

                break;
        }
    return true;
    }

    public void setCurrentMessage(String message){
        this.currentMessage = message;
    }
    public String getCurrentMessage(){
        return currentMessage;
    }
}
