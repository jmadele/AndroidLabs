package com.example.minjia.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.graphics.Bitmap;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class WeatherActivity extends Activity {
    private static final String ACTIVITY_NAME = "Weather Activity";
    private ProgressBar progressBar;
    private ImageView weatherImage;
    private TextView currentTempText;
    private TextView minTempText;
    private TextView maxTempText;
    private TextView windSpeedText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        progressBar = findViewById(R.id.progressBar);

        weatherImage = findViewById(R.id.weatherImage);
        currentTempText = findViewById(R.id.currentTemp);
        minTempText = findViewById(R.id.minTemp);
        maxTempText =  findViewById(R.id.maxTemp);
        windSpeedText=findViewById(R.id.windSpeed);
        ForcastQuery forecast = new ForcastQuery();
        forecast.execute();
    }

    private class ForcastQuery extends AsyncTask<String, Integer, String> {
        private String windSpeed;
        private String min_temp, max_temp, current_temp;
        private Bitmap bitmap;
        private String iconName;

       @Override
        protected String doInBackground(String ...args){
           InputStream stream;
            URL url = null;
            //check network connection
           ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

           NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
           if(networkInfo !=null && networkInfo.isConnected()){
               Log.i(ACTIVITY_NAME, "Device is connecting to the network");
           }else {
               Log.i(ACTIVITY_NAME,"Device is not connecting to network");
           }
           try {
             url = new URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");
             HttpURLConnection conn = (HttpURLConnection) url.openConnection();
               conn.setReadTimeout(10000 /* milliseconds */);
               conn.setConnectTimeout(15000 /* milliseconds */);
               conn.setRequestMethod("GET");
               conn.setDoInput(true);
               Log.d(ACTIVITY_NAME,"Connecting with URL...");

               conn.connect();
               stream = conn.getInputStream();
               Log.d(ACTIVITY_NAME,"Reading stream. Stream is: " + stream);

               XmlPullParser parser = Xml.newPullParser();
               parser.setInput(stream,null);

               while(parser.next()!=XmlPullParser.END_DOCUMENT){
                   if(parser.getEventType()!= XmlPullParser.START_TAG){
                       continue;
                   }

                   if(parser.getName().equals("weather")) {
                       iconName = parser.getAttributeValue(null, "icon");
                   }
                   if(parser.getName().equals("temperature")){
                       current_temp = parser.getAttributeValue(null,"value");
                       publishProgress(0);
                       android.os.SystemClock.sleep(500);
                       min_temp = parser.getAttributeValue(null,"min");
                       publishProgress(25);
                       android.os.SystemClock.sleep(500);
                       max_temp = parser.getAttributeValue(null,"max");
                       publishProgress(50);
                       android.os.SystemClock.sleep(500);
                   }
                   if(parser.getName().equals("speed")){
                       windSpeed = parser.getAttributeValue(null, "value");
                       publishProgress(75);
                       android.os.SystemClock.sleep(500);
                   }

               }
               conn.disconnect();
               String imageFile = iconName + ".png";
               if(fileExistence(imageFile)){
                   FileInputStream fileInput = null;
                   try{
                       fileInput = new FileInputStream(getBaseContext().getFileStreamPath(imageFile));
                      // fileInput = openFileInput(imageFile);
                   }catch(FileNotFoundException e){
                       e.printStackTrace();
                   }
                   bitmap = BitmapFactory.decodeStream(fileInput);
                   Log.i(ACTIVITY_NAME,"Weather image exists, read from file");
               }else {
                   URL imageUrl =  new URL("http://openweathermap.org/img/w/" + iconName + ".png");
                   conn = (HttpURLConnection) imageUrl.openConnection();
                   conn.connect();

                   stream = conn.getInputStream();
                   bitmap = BitmapFactory.decodeStream(stream);
                   FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                   bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                   outputStream.flush();
                   outputStream.close();
                   Log.i(ACTIVITY_NAME, "Weather image does not exist, adding new image from URL");
               }
               Log.i(ACTIVITY_NAME,"File Name = " + imageFile );
               publishProgress(100);

           } catch (MalformedURLException e) {
               Log.e(ACTIVITY_NAME,e.getMessage());
           } catch (IOException e) {
               Log.e(ACTIVITY_NAME,e.getLocalizedMessage());
           } catch (XmlPullParserException parseException) {
              Log.e(ACTIVITY_NAME,parseException.getLocalizedMessage());
           }

           return null;
       }

        @Override
        protected void onProgressUpdate(Integer ... value){
            progressBar.setProgress(value[0]);
            Log.i(ACTIVITY_NAME, "In onProgressUpdate");
        }

        //@Override
        protected void onPostExecute(String result) {
           super.onPostExecute(result);
            progressBar.setVisibility(View.INVISIBLE);
            currentTempText.setText("current temperature is: "+current_temp + "°c");
            minTempText.setText("lowest temperature is: "+min_temp + " °c");
            maxTempText.setText("highest temperature is: "+max_temp + " °c");
            windSpeedText.setText("wind speed is: "+windSpeed + "mph");
            weatherImage.setImageBitmap(bitmap);
        }
        public boolean fileExistence(String fileName) {
            File file = getBaseContext().getFileStreamPath(fileName);
            return file.exists();
        }

    }

}
