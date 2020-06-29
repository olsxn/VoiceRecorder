package com.voicerecorderproject;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.preference.PreferenceManager;


class SettingsPreferences {

    private Context contextOfApp = MainActivity.getAppContext();
    private SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(contextOfApp);
    private NotificationManager notificationManager = (NotificationManager) contextOfApp.getSystemService(Context.NOTIFICATION_SERVICE);

    void ifDndOn() {
        SharedPreferences.Editor dndEditor = settings.edit();
        dndEditor.putBoolean("dnd", true);
        dndEditor.apply();
    }

    void ifDndOff() {
        SharedPreferences.Editor dndEditor = settings.edit();
        dndEditor.putBoolean("dnd", false);
        dndEditor.apply();
    }

    void ifScreenOn() {
        SharedPreferences.Editor screenEditor = settings.edit();
        screenEditor.putBoolean("screen", true);
        screenEditor.apply();
    }

    void ifScreenOff() {
        SharedPreferences.Editor screenEditor = settings.edit();
        screenEditor.putBoolean("screen", false);
        screenEditor.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void checkIfNotifGranted(Context context) {
       if (!notificationManager.isNotificationPolicyAccessGranted()) {
           Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
           context.startActivity(intent);
       }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void doNotDisturbOff() {
        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void doNotDisturbOn() {
        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    void handleBitRateChange(String bitRateValue) {
        SharedPreferences.Editor bitEditor = settings.edit();
        switch(bitRateValue) {
            case "1":
                bitEditor.putString("bitRate", "1");
                bitEditor.apply();
                break;
            case "2":
                bitEditor.putString("bitRate", "2");
                bitEditor.apply();
                break;
            case "3":
                bitEditor.putString("bitRate", "3");
                bitEditor.apply();
                break;
            case "4":
                bitEditor.putString("bitRate", "4");
                bitEditor.apply();
                break;
            case "5":
                bitEditor.putString("bitRate", "5");
                bitEditor.apply();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    void handleSampleRateChange(String sampleRateValue) {
        SharedPreferences.Editor sampleEditor = settings.edit();
        switch (sampleRateValue) {
            case "1":
                sampleEditor.putString("sampleRate", "1");
                sampleEditor.apply();
                break;
            case "2":
                sampleEditor.putString("sampleRate", "2");
                sampleEditor.apply();
                break;
            case "3":
                sampleEditor.putString("sampleRate", "3");
                sampleEditor.apply();
                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    void handleOutputFormatChange(String outputInputValue) {
        SharedPreferences.Editor formatEditor = settings.edit();
        switch (outputInputValue) {
            case "1":
                formatEditor.putString("outputFormat", "1");
                formatEditor.apply();
                break;
            case "2":
                formatEditor.putString("outputFormat", "2");
                formatEditor.apply();
                break;
            case "3":
                formatEditor.putString("outputFormat", "3");
                formatEditor.apply();
                break;
            case "4":
                formatEditor.putString("outputFormat", "4");
                formatEditor.apply();
                break;
            case "5":
                formatEditor.putString("outputFormat", "5");
                formatEditor.apply();
                break;
            case "6":
                formatEditor.putString("outputFormat", "6");
                formatEditor.apply();
                break;
            case "7":
                formatEditor.putString("outputFormat", "7");
                formatEditor.apply();
                break;
            case "8":
                formatEditor.putString("outputFormat", "8");
                formatEditor.apply();
                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    void handleAudioCodecChange(String value) {
        SharedPreferences.Editor codecEditor = settings.edit();
        switch (value) {
            case "1":
                codecEditor.putString("codec", "1");
                codecEditor.apply();
                break;
            case "2":
                codecEditor.putString("codec", "2");
                codecEditor.apply();
                break;
            case "3":
                codecEditor.putString("codec", "3");
                codecEditor.apply();
                break;
            case "4":
                codecEditor.putString("codec", "4");
                codecEditor.apply();
                break;
            case "5":
                codecEditor.putString("codec", "5");
                codecEditor.apply();
                break;
            case "6":
                codecEditor.putString("codec", "6");
                codecEditor.apply();
                break;
            case "7":
                codecEditor.putString("codec", "7");
                codecEditor.apply();
                break;
        }
    }
}
