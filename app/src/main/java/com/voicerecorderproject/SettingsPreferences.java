package com.voicerecorderproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.annotation.RequiresApi;


public class SettingsPreferences {

    // sharedPrefs for remembering settings
    final static String PREFERENCES_NAME = "settingsFile";
    SharedPreferences settings = null;

    settings = getSharedPreferences(PREFERENCES_NAME, 0);

    public SettingsPreferences(Context context) {
        settings = context.getSharedPreferences(PREFERENCES_NAME, 0);
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void handleBitRateChange(String bitRateValue) {
        SharedPreferences.Editor bitEditor = settings.edit();
        switch(bitRateValue) {
            case "1":
                bitEditor.putInt("bitRate", 1);
                bitEditor.apply();
                break;
            case "2":
                bitEditor.putInt("bitRate", 2);
                bitEditor.apply();
                break;
            case "3":
                bitEditor.putInt("bitRate", 3);
                bitEditor.apply();
                break;
            case "4":
                bitEditor.putInt("bitRate", 4);
                bitEditor.apply();
                break;
            case "5":
                bitEditor.putInt("bitRate", 5);
                bitEditor.apply();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void handleSampleRateChange(String sampleRateValue) {
        SharedPreferences.Editor sampleEditor = settings.edit();
        switch (sampleRateValue) {
            case "1":
                sampleEditor.putInt("sampleRate", 1);
                sampleEditor.apply();
                break;
            case "2":
                sampleEditor.putInt("sampleRate", 2);
                sampleEditor.apply();
                break;
            case "3":
                sampleEditor.putInt("sampleRate", 3);
                sampleEditor.apply();
                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void handleOutputFormatChange(String outputInputValue) {
        SharedPreferences.Editor formatEditor = settings.edit();
        switch (outputInputValue) {
            case "1":
                formatEditor.putInt("outputFormat", 1);
                formatEditor.apply();
                break;
            case "2":
                formatEditor.putInt("outputFormat", 2);
                formatEditor.apply();
                break;
            case "3":
                formatEditor.putInt("outputFormat", 3);
                formatEditor.apply();
                break;
            case "4":
                formatEditor.putInt("outputFormat", 4);
                formatEditor.apply();
                break;
            case "5":
                formatEditor.putInt("outputFormat", 5);
                formatEditor.apply();
                break;
            case "6":
                formatEditor.putInt("outputFormat", 6);
                formatEditor.apply();
                break;
            case "7":
                formatEditor.putInt("outputFormat", 7);
                formatEditor.apply();
                break;
            case "8":
                formatEditor.putInt("outputFormat", 8);
                formatEditor.apply();
                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void handleAudioCodecChange(String value) {
        SharedPreferences.Editor codecEditor = settings.edit();
        switch (value) {
            case "1":
                codecEditor.putInt("codec", 1);
                codecEditor.apply();
                break;
            case "2":
                codecEditor.putInt("codec", 2);
                codecEditor.apply();
                break;
            case "3":
                codecEditor.putInt("codec", 3);
                codecEditor.apply();
                break;
            case "4":
                codecEditor.putInt("codec", 4);
                codecEditor.apply();
                break;
            case "5":
                codecEditor.putInt("codec", 5);
                codecEditor.apply();
                break;
            case "6":
                codecEditor.putInt("codec", 6);
                codecEditor.apply();
                break;
            case "7":
                codecEditor.putInt("codec", 7);
                codecEditor.apply();
                break;
        }
    }
}
