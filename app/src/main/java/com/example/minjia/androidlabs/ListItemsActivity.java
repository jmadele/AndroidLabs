package com.example.minjia.androidlabs;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.minjia.androidlabs.R;

public class ListItemsActivity extends Activity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        Log.i("ListItemActivity","In onCreate()");

        ImageButton imageButton = (ImageButton)findViewById(R.id.ImageButton);
        imageButton.setOnClickListener(new View.OnClickListener(){
           public void onClick(View v){
               Intent takePictureIntent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
               if(takePictureIntent.resolveActivity(getPackageManager())!=null){
                   startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
               }
           }
        });
        Switch switchButton = (Switch)findViewById(R.id.Switch);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton switchButton, boolean isChecked){
                if(isChecked){
                    CharSequence text = "Switch is on";
                    int duration = Toast.LENGTH_SHORT;
                    //if switch is checked, display "Switch on"
                    Toast toast = Toast.makeText(getApplicationContext(),text,duration);
                    toast.show();
                }
                else {
                    CharSequence text = "Switch is off";
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(getApplicationContext(),text,duration);
                    toast.show();
                }
            }
        });
        CheckBox checkBox = (CheckBox)findViewById(R.id.CheckBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
                    //chain several setters to set dialog characteristics
                    builder.setMessage(R.string.dialog_message)
                            .setTitle(R.string.dialog_title)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int id){
                                    Intent resultIntent = new Intent();
                                    resultIntent.putExtra("Response","my response");
                                    setResult(Activity.RESULT_OK,resultIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int id){
                                    dialog.dismiss();
                                }
                            })
                            .show();

                }
            }
        });

    }

    public void onActivityResult(int requestCode, int responseCode, Intent data){
        setResult(Activity.RESULT_OK);
        if(requestCode==REQUEST_IMAGE_CAPTURE && responseCode==RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap bm = (Bitmap)extras.get("data");
            ImageButton imageButton = (ImageButton)findViewById(R.id.ImageButton);
            imageButton.setImageBitmap(bm);
        }
    }
    public void onStart(){
        super.onStart();
        final String ListItemActivity = "ListItemActivity";
        Log.i(ListItemActivity,"In onStart()");
    }
    public void onResume(){
        super.onResume();
        final String ListItemActivity = "ListItemActivity";
        Log.i(ListItemActivity,"In onResume()");
    }
    public void onPause(){
        super.onPause();
        final String ListItemActivity = "ListItemActivity";
        Log.i(ListItemActivity,"In onPause()");
    }
    public void onStrop(){
        super.onStop();
        final String ListItemActivity = "ListItemActivity";
        Log.i(ListItemActivity,"In onStop()");
    }
    public void onDestroy(){
        super.onDestroy();
        final String ListItemActivity = "ListItemActivity";
        Log.i(ListItemActivity,"In onDestroy()");
    }
}
