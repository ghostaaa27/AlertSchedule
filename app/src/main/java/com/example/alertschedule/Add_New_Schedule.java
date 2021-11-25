package com.example.alertschedule;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;

public class Add_New_Schedule extends AppCompatActivity {


    Button saveButton, starttimeButton, endtimeButton;
    private TimePickerDialog timePickerDialog;
    TextView starttimeTextView, endtimeTextView;
    Spinner spinner;
    String[] ModesNames;
    String SelectedmodeName;
    String starttime = null;
    String endtime = null;
    JSONArray jsonArray = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__new__schedule);

        saveButton = (Button) findViewById(R.id.savebuttonID);
        starttimeButton = (Button) findViewById(R.id.starttimebuttonID);
        endtimeButton = (Button) findViewById(R.id.endtimebuttonID);
        starttimeTextView = (TextView) findViewById(R.id.starttimetextviewID);
        endtimeTextView = (TextView) findViewById(R.id.endtimetextviewID);
        spinner = (Spinner) findViewById(R.id.modespinnerID);


        ModesNames = getResources().getStringArray(R.array.ModesNames);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Add_New_Schedule.this, R.layout.spinner_sample_view
                , R.id.spinner_sample_view_text_view_ID, ModesNames);

        spinner.setAdapter(arrayAdapter);





        starttimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TimePicker timePicker = new TimePicker(Add_New_Schedule.this);
                int currentHour = timePicker.getCurrentHour();
                int currentMinute = timePicker.getCurrentMinute();

                timePickerDialog = new TimePickerDialog(Add_New_Schedule.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String AMorPm = "AM";
                                if (hourOfDay >= 12) {
                                    AMorPm = "PM";
                                    if (hourOfDay == 12) {
                                        hourOfDay = hourOfDay;
                                    } else {
                                        hourOfDay -= 12;
                                    }
                                }
                                if(minute<10){
                                    starttime = Integer.toString(hourOfDay) + ":0" + Integer.toString(minute) + " " + AMorPm;
                                }
                                else{
                                    starttime = Integer.toString(hourOfDay) + ":" + Integer.toString(minute) + " " + AMorPm;
                                }
                                starttimeTextView.setText(starttime);
                            }
                        }, currentHour, currentMinute, false
                );
                timePickerDialog.show();
            }
        });


        endtimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TimePicker timePicker = new TimePicker(Add_New_Schedule.this);
                int currentHour = timePicker.getCurrentHour();
                int currentMinute = timePicker.getCurrentMinute();

                timePickerDialog = new TimePickerDialog(Add_New_Schedule.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String AMorPm = "AM";
                                if (hourOfDay >= 12) {
                                    AMorPm = "PM";
                                    if (hourOfDay == 12) {
                                        hourOfDay = hourOfDay;
                                    } else {
                                        hourOfDay -= 12;
                                    }
                                }
                                if(minute<10){
                                    endtime = Integer.toString(hourOfDay) + ":0" + Integer.toString(minute) + " " + AMorPm;
                                }
                                else{
                                    endtime = Integer.toString(hourOfDay) + ":" + Integer.toString(minute) + " " + AMorPm;
                                }
                                endtimeTextView.setText(endtime);
                            }
                        }, currentHour, currentMinute, false
                );
                timePickerDialog.show();
            }
        });




        Bundle bundle = getIntent().getExtras();
        int a = 0;

        if(bundle!=null){
            a = (bundle.getInt("position"))+1;
        }


        final int finalA = a;
        saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(finalA ==0){
                        addDatatoJSON();

                    }
                    else{
                        EditJson(finalA);
                    }
                }
            });






    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @SuppressLint("ResourceType")
    public void addDatatoJSON() {

        try {
            File f = new File("/data/data/" + getApplicationContext().getPackageName() + "/" + "datafile.json");
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String mResponse = new String(buffer);
            jsonArray = new JSONArray(mResponse);


            JSONObject js = new JSONObject();
            if(starttime!=null){
                js.put("starttime", starttime);
            }
            else {
                starttime = "10:45 AM";
                js.put("starttime", starttime);
            }

            if(endtime!=null){
                js.put("endtime", endtime);
            }
            else {
                endtime = "10:45 AM";
                js.put("endtime", endtime);
            }
            if(SelectedmodeName!=null){
                js.put("mode", SelectedmodeName);
            }
            else {
                SelectedmodeName = spinner.getSelectedItem().toString();
                js.put("mode", SelectedmodeName);
            }

            jsonArray.put(js);

            FileWriter file = new FileWriter("/data/data/" + getApplicationContext().getPackageName() + "/" + "datafile.json");
            file.write(jsonArray.toString());
            file.flush();
            file.close();
        }


        catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }







        Toast.makeText(Add_New_Schedule.this,"addDatatoJSON called", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }



    public void EditJson(int position){

        position--;

        ArrayList<String> starttimes = new ArrayList<String>();
        ArrayList<String> endtimes = new ArrayList<String>();
        ArrayList<String> modes = new ArrayList<String>();

        try {
            File f = new File("/data/data/" + getApplicationContext().getPackageName()  + "/" + "datafile.json");
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String mResponse = new String(buffer);
            jsonArray = new JSONArray(mResponse);


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


        try {
            FileWriter file = new FileWriter("/data/data/" + getApplicationContext().getPackageName() + "/" + "datafile.json");
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
                if(i!=position){


                    js.put("starttime", starttimes.get(i));
                    js.put("endtime", endtimes.get(i));
                    js.put("mode", modes.get(i));

                }
                if(i==position){

                    if(starttime!=null){
                        js.put("starttime", starttime);
                    }
                    else {
                        starttime = "10:45 AM";
                        js.put("starttime", starttime);
                    }

                    if(endtime!=null){
                        js.put("endtime", endtime);
                    }
                    else {
                        endtime = "10:45 AM";
                        js.put("endtime", endtime);
                    }
                    if(SelectedmodeName!=null){
                        js.put("mode", SelectedmodeName);
                    }
                    else {
                        SelectedmodeName = spinner.getSelectedItem().toString();
                        js.put("mode", SelectedmodeName);
                    }

                }
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



        Toast.makeText(Add_New_Schedule.this,"Edit Json is called"+position, Toast.LENGTH_SHORT).show();

        onBackPressed();
    }


}
