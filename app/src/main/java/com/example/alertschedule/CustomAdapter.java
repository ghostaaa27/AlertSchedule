package com.example.alertschedule;

import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.alertschedule.MainActivity.params;

public class CustomAdapter extends BaseAdapter {

    ArrayList<String> starttimes;
    ArrayList<String> endtimes;
    ArrayList<String> modes;
    Context context;
    LayoutInflater layoutInflater;

    public CustomAdapter(Context context, ArrayList<String> starttimes, ArrayList<String> endtimes, ArrayList<String> modes) {
        this.starttimes = starttimes;
        this.endtimes = endtimes;
        this.modes = modes;
        this.context = context;
    }

    @Override
    public int getCount() {
        return starttimes.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.sample_view,parent,false);
        }


        convertView.setBackgroundColor(Color.CYAN);

        TextView starttime = (TextView) convertView.findViewById(R.id.sampleviewstarttimetextviewID);
        starttime.setText(starttimes.get(position));

        TextView endtime = (TextView) convertView.findViewById(R.id.sampleviewendtimetextviewID);
        endtime.setText(endtimes.get(position));

        TextView m = (TextView) convertView.findViewById(R.id.sampleviewmodetextviewID);
        m.setText(modes.get(position));

        return convertView;
    }












}
