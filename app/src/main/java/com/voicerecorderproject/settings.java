package com.voicerecorderproject;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class settings extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        handleListView();
    }

    public void handleListView() {
        listView = findViewById(R.id.list_view);
        ArrayList<String> arrayList = new ArrayList<>();

        addArrayItems(arrayList);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(settings.this, "Clicked item " + (position + 1), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addArrayItems(ArrayList<String> arrayList) {
        arrayList.add("Audio Codec");
        arrayList.add("Sample Rate");
        arrayList.add("Max File Size");
        arrayList.add("Keep screen awake");
        arrayList.add("Do not disturb");
        arrayList.add("Recording location");
    }



}
