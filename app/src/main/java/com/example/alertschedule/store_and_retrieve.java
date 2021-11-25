package com.example.alertschedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class store_and_retrieve extends AppCompatActivity {
    String sign_in_email;
    ArrayList<String> starttimes;
    ArrayList<String> endtimes;
    ArrayList<String> modes;
    DatabaseReference databaseReference;
    Button saveButton,retrieveButton;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_and_retrieve);


        saveButton = (Button) findViewById(R.id.savebuttonID);
        retrieveButton = (Button) findViewById(R.id.retrievebuttonID);
        Bundle bundle = getIntent().getExtras();

        if(bundle!=null){
            sign_in_email = bundle.getString("email");
            sign_in_email = sign_in_email.replace("@","");
            sign_in_email = sign_in_email.replace(".","");
            Toast.makeText(getApplicationContext(),sign_in_email,Toast.LENGTH_SHORT).show();

        }
        putDataonArrays();


        databaseReference = FirebaseDatabase.getInstance().getReference(sign_in_email);



        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putDatainFirebase();
            }
        });

        retrieveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrieveData();
            }
        });
    }

    private void RetrieveData() {


         final ArrayList<String> stimes = new ArrayList<String>();
         final ArrayList<String> etimes = new ArrayList<String>();
         final ArrayList<String> ms = new ArrayList<String>();


            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){


                        dataForfireBase dataForfireBaseObj1 = dataSnapshot1.getValue(dataForfireBase.class);
                        String starttime = dataForfireBaseObj1.getStarttime();
                        String endtime = dataForfireBaseObj1.getEndtime();
                        String mode = dataForfireBaseObj1.getMode();

                        stimes.add(starttime);
                        etimes.add(endtime);
                        ms.add(mode);

                       // Toast.makeText(getApplicationContext(),starttime,Toast.LENGTH_SHORT).show();
                       // Toast.makeText(getApplicationContext(),endtime,Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getApplicationContext(),mode,Toast.LENGTH_SHORT).show();
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

                        for(int i=0;i<ms.size();i++){

                            JSONObject js = new JSONObject();

                            js.put("starttime", stimes.get(i));
                            js.put("endtime", etimes.get(i));
                            js.put("mode", ms.get(i));

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
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });











    }


    public void putDatainFirebase(){

        for(int i=0;i<modes.size();i++){
            String starttime = starttimes.get(0);
            String endtime = endtimes.get(0);
            String mode = modes.get(0);

            String key = databaseReference.push().getKey();

            dataForfireBase dataForfireBaseObj = new dataForfireBase(starttime,endtime,mode);

            databaseReference.child(key).setValue(dataForfireBaseObj);
            Toast.makeText(getApplicationContext(),"Setting uploaded successfully!",Toast.LENGTH_SHORT).show();


        }

    }









    public void putDataonArrays(){

        starttimes = new ArrayList<String>();
        endtimes = new ArrayList<String>();
        modes = new ArrayList<String>();

        try {
            File f = new File("/data/data/" + getApplicationContext().getPackageName()  + "/" + "datafile.json");
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

    }
}
