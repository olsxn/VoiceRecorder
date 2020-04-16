package com.voicerecorderproject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            // onClickListener here for fileLocation preference that calls locationSearch
            final Preference fileLocation = findPreference("fileLocation");
            assert fileLocation != null;
            fileLocation.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public boolean onPreferenceClick(Preference preference) {
//                    locationSearch();
                    return true;
                }
            });

            final Preference audioCodec = findPreference("codec");
            audioCodec.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    // in here I need to be able to change which audio codec is used, maybe seperate each option choice into seperate
                    // methods in MainActivity()
                    if(newValue == "1") {
                        System.out.println("Clicked");
                    }

                    return true;
                }
                });
        }
    }

    // search through phone's storage
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private static void locationSearch() {
//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("audio/*");
//    }
}