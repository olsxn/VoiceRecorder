package com.voicerecorderproject;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

        // let items have context menus
        registerForContextMenu(listView);

        HashMap items = createHashMap();

        List<HashMap<String, String>> listItems = new ArrayList<>();

        // set up format
        SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.list_item,
                new String[]{"First line", "Second line"},
                new int[]{R.id.textView_items, R.id.textView_subItems});

        Iterator it = items.entrySet().iterator();

        // go through hashMap and add the strings to new hashMap resultsMap
        while (it.hasNext()) {
            HashMap<String, String> resultsMap = new HashMap<>();
            Map.Entry pair = (Map.Entry) it.next();
            resultsMap.put("First line", pair.getKey().toString());
            resultsMap.put("Second line", pair.getValue().toString());
            listItems.add(resultsMap);
        }

        // set adapter to listView
        listView.setAdapter(adapter);

        // when an item in the list is clicked, call handleChoice
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                handleChoice(position);
            }
        });
    }

    public HashMap createHashMap() {
        HashMap<String, String> nameDescription = new HashMap<>();
        nameDescription.put("Audio codec", "Change the audio codec used for recordings");
        nameDescription.put("Sample rate", "Set the sample rate used for recordings");
        nameDescription.put("Max file size", "Set the max file size");
        nameDescription.put("Keep screen awake", "Keep the screen awake while recording");
        nameDescription.put("Do not disturb", "Set the phone to do not disturb while recording");
        nameDescription.put("Recording location", "CHANGE THE RECORDING LOCATION");
        return nameDescription;
    }

    public void handleChoice(int position) {
        switch (position) {
            // audio codec
            case 0:
                // in here, bring up options of different audio codecs which the user can choose
                break;

            // sample rate
            case 1:
                // in here, bring sample rate options up
                break;

            // max file size
            case 2:
                // in here, create slider to decide max file size
                break;

            // keep screen awake
            case 3:
                // create button to select yes or no
                break;

            // do not disturb
            case 4:
                // same as above
                break;

            // recording location
            case 5:
                // change recording location
                break;

        }
    }
}
