package com.example.alertschedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity  {

    FloatingActionButton floatingActionButton;
    static String params  = "datafile.json";
    ListView listView;
    AlertDialog.Builder alertDialogBuilder;
    TextClock currentTime;
    AudioManager audioManager;
    FirebaseAuth mAuth;


    ArrayList<String> starttimes = new ArrayList<String>();
    ArrayList<String> endtimes = new ArrayList<String>();
    ArrayList<String> modes = new ArrayList<String>();
    Timer t = new Timer();
    int ch = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //checkFileExist(params);


        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingactionbuttonID);
        listView = (ListView) findViewById(R.id.listviewID);
        currentTime= (TextClock) findViewById(R.id.textclockID);
        audioManager = (AudioManager) getSystemService(getApplicationContext().AUDIO_SERVICE);
        mAuth = FirebaseAuth.getInstance();



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Add_New_Schedule.class);
                startActivity(intent);
                //Toast.makeText(MainActivity.this, "OnResumed called", Toast.LENGTH_SHORT).show();
            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,"Item no"+ position, Toast.LENGTH_SHORT).show();
                        alertDialog(position);

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_layout,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.aboutmeID){
                Intent intent = new Intent(MainActivity.this, about_me.class);
                startActivity(intent);
        }
        else if(item.getItemId()==R.id.signinID){
            Intent intent = new Intent(MainActivity.this, signin.class);
            startActivity(intent);
        }
        else if(item.getItemId()==R.id.signupID){
            Intent intent = new Intent(MainActivity.this, sign_up.class);
            startActivity(intent);
        }
        else if(item.getItemId()==R.id.logoutID){
            FirebaseAuth.getInstance().signOut();
        }


        return super.onOptionsItemSelected(item);
    }

    public void checkFileExist(String params) {
        File f = new File("/data/data/" + "/" + params);
        if(!f.exists())
        {
            try {
                FileWriter file = new FileWriter("/data/data/" + getApplicationContext().getPackageName() + "/" + params);
                file.write("[]");
                file.flush();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }}
    }



    private void TimerMethod()
    {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(Timer_Tick);
    }

    private Runnable Timer_Tick = new Runnable() {
        public void run() {

            //This method runs in the same thread as the UI.

            //Do something to the UI thread here

            setMode();


        }
    };






    @Override
    protected void onResume() {
        super.onResume();
        PutDatatoListView();
        //Toast.makeText(MainActivity.this, "onResumed called", Toast.LENGTH_SHORT).show();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, 5000);
    }






    void setMode(){

        String cTime = currentTime.getText().toString();

        //Toast.makeText(MainActivity.this,"this is timer : "+cTime,Toast.LENGTH_SHORT).show();

        for(int i=0;i<modes.size();i++){

                if(cTime.equals(starttimes.get(i))){
                    changeMode(modes.get(i));
                    break;


                }
                if(cTime.equals(endtimes.get(i))){
                    changeMode("Ringmode");
                    break;
                }

        }

    }

    public void changeMode(String mode){






        switch (mode){

            case "Vibration":
                if(audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL || audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT){
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                }
                if(ch==0){
                    Toast.makeText(MainActivity.this,"Mode is: "+ mode,Toast.LENGTH_SHORT).show();
                    ch++;
                }
                break;

            case "Silent":
                if(audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL || audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT){
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                }
                if(ch==0){
                    Toast.makeText(MainActivity.this,"Mode is: "+ mode,Toast.LENGTH_SHORT).show();
                    ch++;
                }
                break;

            case "Ringmode":
                if(audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT || audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE){
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                }
                if(ch==0){
                    Toast.makeText(MainActivity.this,"Mode is: "+ mode,Toast.LENGTH_SHORT).show();
                    ch++;
                }
                break;
        }

    }



    public  void PutDatatoListView(){

        starttimes = new ArrayList<String>();
        endtimes = new ArrayList<String>();
        modes = new ArrayList<String>();

        try {
            File f = new File("/data/data/" + getApplicationContext().getPackageName()  + "/" + params);
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String mResponse = new String(buffer);
            JSONArray jsonArray = new JSONArray(mResponse);

            for(int i=0; i<jsonArray.length(); i++){

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String starttime = jsonObject.getString("starttime");
                String endtime = jsonObject.getString("endtime");
                String mode = jsonObject.getString("mode");

                starttimes.add(starttime);
                endtimes.add(endtime);
                modes.add(mode);

            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        CustomAdapter customAdapter = new CustomAdapter(MainActivity.this,starttimes,endtimes,modes);
        listView.setAdapter(customAdapter);


    }


    public void alertDialog(final int Position){
        alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("Do");
        alertDialogBuilder.setIcon(R.drawable.ic_done_all_black_24dp);
        alertDialogBuilder.setMessage("What do you wanna do?");

        alertDialogBuilder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    DeleteEntry(Position);
            }
        });

        alertDialogBuilder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditEntry(Position);
            }
        });



        alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();



    }


    public void EditEntry(int position){

        Intent intent = new Intent(MainActivity.this, Add_New_Schedule.class);
        intent.putExtra("position", position);
        startActivity(intent);
        PutDatatoListView();
    }






    public void DeleteEntry(int position){

        starttimes = new ArrayList<String>();
        endtimes = new ArrayList<String>();
        modes = new ArrayList<String>();

        try {
            File f = new File("/data/data/" + getApplicationContext().getPackageName()  + "/" + params);
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String mResponse = new String(buffer);
            JSONArray jsonArray = new JSONArray(mResponse);
            Toast.makeText(MainActivity.this,"Deleted",Toast.LENGTH_SHORT).show();

            for(int i=0; i<jsonArray.length(); i++){

                if(i!=position){

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String starttime = jsonObject.getString("starttime");
                    String endtime = jsonObject.getString("endtime");
                    String mode = jsonObject.getString("mode");

                    starttimes.add(starttime);
                    endtimes.add(endtime);
                    modes.add(mode);

                }

            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            FileWriter file = new FileWriter("/data/data/" + getApplicationContext().getPackageName() + "/" + params);
            file.write("[]");
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            File f = new File("/data/data/" + getApplicationContext().getPackageName() + "/" + "datafile.json");
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String mResponse = new String(buffer);
            JSONArray jsonArray = new JSONArray(mResponse);

            for(int i=0;i<modes.size();i++){

                JSONObject js = new JSONObject();

                js.put("starttime", starttimes.get(i));
                js.put("endtime", endtimes.get(i));
                js.put("mode", modes.get(i));


                jsonArray.put(js);

            }


            FileWriter file = new FileWriter("/data/data/" + getApplicationContext().getPackageName() + "/" + "datafile.json");
            file.write(jsonArray.toString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        PutDatatoListView();



    }



}



