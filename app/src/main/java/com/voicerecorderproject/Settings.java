package com.voicerecorderproject;

import android.os.Build;
import android.os.Bundle;

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
        }
    }

    }
