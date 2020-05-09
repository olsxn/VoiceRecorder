package com.voicerecorderproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "AudioRecordTest";
    public static String inputtedText = null;
    public static String fileName = null;
    private static MediaRecorder mediaRecorder;
    final int REQUEST_PERMISSION_CODE = 1000;
    private int clickCount = 0;
    private static Context appContext;

    Button startButton;
    Button changeButton;
    EditText changeText;


    // handle first time run of app
    final String PREFS_NAME = "initialSettingsFile";
    SharedPreferences initialSettings = null;

    // prefs
    SharedPreferences settings = null;
    int outputFormat;
    int sampleRate;
    int bitRate;
    int codec;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // sort context out
        appContext = getApplicationContext();

        // create/get prefs
        initialSettings = getSharedPreferences(PREFS_NAME, 0);
        settings = PreferenceManager.getDefaultSharedPreferences(appContext);

        // init button/text vars
        startButton = findViewById(R.id.startButton);
        changeButton = findViewById(R.id.changeButton);
        changeText = findViewById(R.id.editText);

        // check permissions at runtime
        if (!checkPermissions()) {
            requestPermission();
        }

        // check if its the first time the app has been run
        checkIfFirstTime();

        // handle the keep screen on option in on create
        handleScreen();

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputtedText = changeText.getText().toString();
            }
        });

        // when record is clicked: initialise MediaRecorder, turn DND on (if wanted), record
        // click count is used in order to change text on the button
        startButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                clickCount++;
                if (clickCount == 1) {
                    initialiseMediaRecorder();
                    if (checkIfDnd()) {
                        new SettingsPreferences().doNotDisturbOn();
                    }
                    startRecording();
                    startButton.setText(R.string.stop_recording);
                } else if (clickCount == 2) {
                    stopRecording();
                    new SettingsPreferences().doNotDisturbOff();
                    startButton.setText(R.string.start_recording);
                    clickCount = 0;
                }
            }
        });
    }

    // check if do not disturb mode is preferred when recording
    public boolean checkIfDnd() {
        return settings.getBoolean("dnd", false);
    }

    public void handleScreen() {

        if (checkScreen()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        else if (!checkScreen()) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    // check if keep screen awake is on, if so turn it on for the app
    public boolean checkScreen() {
        return settings.getBoolean("screen", false);
    }

    // getter for context
    public static Context getAppContext() {
        return appContext;
    }

    // apply the int values from settings prefs in the same way as method below
    // if they can't be retrieved used default values
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void applySettings() {
        // assign ints
        outputFormat = Integer.parseInt(settings.getString("outputFormat", "5"));
        sampleRate = Integer.parseInt(settings.getString("sampleRate", "2"));
        bitRate = Integer.parseInt(settings.getString("bitRate", "3"));
        codec = Integer.parseInt(settings.getString("codec", "5"));
        // apply
        setOutputFormat(outputFormat);
        setAudioSamplingRate(sampleRate);
        setAudioEncodingBitRate(bitRate);
        setAudioCodec(codec);
    }

    // default settings before user changes them
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void setDefaults() {
        // variables to use as defaults
        int defCodec = 5;
        int defOutput = 5;
        int defSample = 2;
        int defBit = 3;
        // set defaults
        setOutputFormat(defOutput);
        setAudioSamplingRate(defSample);
        setAudioEncodingBitRate(defBit);
        setAudioCodec(defCodec);
    }


    // sets up mediaRecorder but only when the app is started for the first time
    // using defaults above
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private static void firstTimeInitialise() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        setDefaults();
        setUpFile();
    }

    // set up mediaRecorder in this method so it can be called in Settings activity
    // audio source is also set as it will always be the default the device is currently using
    // set up file location here also
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void initialiseMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        setAudioSource();
        applySettings();
        setUpFile();
    }

    public static void setAudioSource() {
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
    }

    public static void setUpFile() {
        //set new fileName each time mediaRecorder is setup with UUID
        // UUID.randomUUID().toString()
        String fileName = Environment.getExternalStorageDirectory() + File.separator +
                Environment.DIRECTORY_DOWNLOADS + File.separator + inputtedText;
        mediaRecorder.setOutputFile(fileName);
    }

    public static void setAudioSamplingRate(int sampleRate) {
        switch (sampleRate) {
            case 1:
                mediaRecorder.setAudioSamplingRate(22000);
                break;
            case 2:
                mediaRecorder.setAudioSamplingRate(44100);
                break;
            case 3:
                mediaRecorder.setAudioSamplingRate(48000);
                break;
        }
    }

    public static void setAudioEncodingBitRate(int bitRate) {
        switch (bitRate) {
            case 1:
                mediaRecorder.setAudioEncodingBitRate(24000);
                break;
            case 2:
                mediaRecorder.setAudioEncodingBitRate(56000);
                break;
            case 3:
                mediaRecorder.setAudioEncodingBitRate(96000);
                break;
            case 4:
                mediaRecorder.setAudioEncodingBitRate(128000);
                break;
            case 5:
                mediaRecorder.setAudioEncodingBitRate(196000);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void setOutputFormat(int outputFormat) {
        switch (outputFormat) {
            case 1:
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
                break;
            case 2:
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                break;
            case 3:
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);
                break;
            case 4:
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_2_TS);
                break;
            case 5:
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                break;
            case 6:
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.OGG);
                break;
            case 7:
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                break;
            case 8:
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.WEBM);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void setAudioCodec(int codec) {
        switch (codec) {
            case 1:
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                break;
            case 2:
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC_ELD);
                break;
            case 3:
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                break;
            case 4:
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
                break;
            case 5:
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
                break;
            case 6:
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.OPUS);
                break;
            case 7:
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.VORBIS);
                break;
        }
    }

    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    //prepare and start recording after mediaRecorder set up
    private void startRecording() {
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare failed");
            System.out.println("" + e);    //to display the error
        }
        mediaRecorder.start();
    }

    //stop and call release method
    private void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //request permissions
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
    }

    //check if app has read, write and mic permissions
    private boolean checkPermissions() {
        int writeExternalStorageResult = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int recordAudioResult = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int readExternalStorageResult = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return writeExternalStorageResult == PackageManager.PERMISSION_GRANTED &&
                recordAudioResult == PackageManager.PERMISSION_GRANTED && readExternalStorageResult == PackageManager.PERMISSION_GRANTED;
    }

    //menu in the top right
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_right_menu, menu);
        return true;
    }

    //receives the item selected from the menu and handles the choice
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                openSettings();
                return true;
            case R.id.about:
                openAbout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    //creates intent to start Settings activity
    public void openSettings() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void openAbout() {
        //create intent to open About page activity
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void checkIfFirstTime() {
        if (initialSettings.getBoolean("my_first_time", true)) {
            Log.d("Comments", "First time run");
            firstTimeInitialise();
            // apply runs in background rather than immediately like commit
            initialSettings.edit().putBoolean("my_first_time", false).apply();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaRecorder();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // handle the keep screen on option in on resume
        handleScreen();
    }
}