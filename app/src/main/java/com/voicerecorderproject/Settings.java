package com.voicerecorderproject;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;



@RequiresApi(api = Build.VERSION_CODES.Q)
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

            final Preference audioCodec = findPreference("codec");
            assert audioCodec != null;
            audioCodec.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.Q)
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String audioInputValue = newValue.toString();
                    new SettingsPreferences().handleAudioCodecChange(audioInputValue);
                    return true;
                }
            });

            final Preference outputFormat = findPreference("outputFormat");
            assert outputFormat != null;
            outputFormat.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.Q)
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String outputInputValue = newValue.toString();
                    new SettingsPreferences().handleOutputFormatChange(outputInputValue);
                    return true;
                }
            });

            final Preference sampleRate = findPreference("sampleRate");
            assert sampleRate != null;
            sampleRate.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.Q)
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String sampleRateValue = newValue.toString();
                    new SettingsPreferences().handleSampleRateChange(sampleRateValue);
                    return true;
                }
            });

            final Preference bitRate = findPreference("bitRate");
            assert bitRate != null;
            bitRate.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.Q)
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String bitRateValue = newValue.toString();
                    new SettingsPreferences().handleBitRateChange(bitRateValue);
                    return true;
                }
            });

            final Preference dnd = findPreference("doNotDisturb");
            assert  dnd != null;
            dnd.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String boolCheck = newValue.toString();
                    if (boolCheck.equals("true")) {
                        new SettingsPreferences().checkIfNotifGranted(getContext());
                        new SettingsPreferences().ifDndOn();
                        Log.d("Test", "Switched on");
                    }
                    else if (boolCheck.equals("false")) {
                        new SettingsPreferences().checkIfNotifGranted(getContext());
                        new SettingsPreferences().ifDndOff();
                        Log.d("Test", "Switched off");
                    }
                    return true;
                }
            });

            final Preference keepAwake = findPreference("keepScreenAwake");
            assert keepAwake != null;
            keepAwake.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String screenCheck = newValue.toString();
                    if (screenCheck.equals("true")) {
                        new SettingsPreferences().ifScreenOn();
                    }
                    else if (screenCheck.equals("false")) {
                        new SettingsPreferences().ifScreenOff();
                    }
                    return true;
                }
            });
        }
    }

    }
