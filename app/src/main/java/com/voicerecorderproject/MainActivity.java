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
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static String inputtedText;
    private static Context appContext;
    private static MediaRecorder mediaRecorder;
    final int REQUEST_PERMISSION_CODE = 1000;

    // to increment the fileName
    public int count = 0;

    Button startButton;
    Button changeButton;
    EditText changeText;
    Chronometer chronometer;

    // prefs to hold defaults
    SharedPreferences initialSettings = null;
    final String PREFS_NAME = "initialSettingsFile";

    // prefs to hold settings
    SharedPreferences settings = null;
    int outputFormat;
    int sampleRate;
    int bitRate;
    int codec;

    // for checking how many times button has been pressed
    private int clickCount = 0;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get context of app
        appContext = getApplicationContext();

        // create/get prefs
        initialSettings = getSharedPreferences(PREFS_NAME, 0);
        settings = PreferenceManager.getDefaultSharedPreferences(appContext);

        // init button/text/timer vars
        startButton = findViewById(R.id.startButton);
        changeButton = findViewById(R.id.changeButton);
        changeText = findViewById(R.id.editText);
        chronometer = findViewById(R.id.timer);

        // check permissions at runtime
        if (!checkPermissions()) {
            requestPermission();
        }

        // check if its the first time the app has been run
        checkIfFirstTime();

        // handle the keep screen on option in on create
        handleScreen();

        // when change button is clicked file name is changed to the editText changeText
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputtedText = changeText.getText().toString();
            }
        });

        // when record is clicked: call initialise MediaRecorder, turn DND on (if wanted), record
        // turn timer on, change button colour and text
        // click count introduced to implement changing button colour and text
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
                    turnTimerOn();
                    recordClicked();
                } else if (clickCount == 2) {
                    stopRecording();
                    if (checkIfDnd()) {
                        new SettingsPreferences().doNotDisturbOff();
                    }
                    turnTimerOff();
                    stopClicked();
                    clickCount = 0;
                }
            }
        });
    }

    // getter for context
    public static Context getAppContext() {
        return appContext;
    }

    // I could use this method in the future to put the last inputtedText into a sharedPref String
    // so that it would remember the last inputted name from the user and then add count to that
    // but I like the way it handles this problem already with "my_recording" + count
    // would be called when the save button is clicked
    private void resetDefaultFileName() {
        SharedPreferences.Editor fileNameEditor = settings.edit();
        fileNameEditor.putString("fileName", inputtedText);
        fileNameEditor.apply();
    }

    // starts recording timer
    public void turnTimerOn() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    // stops recording timer
    public void turnTimerOff() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.stop();
    }

    // turns button red and changes text
    public void recordClicked() {
        startButton.setBackgroundResource(R.drawable.circle_red);
        startButton.setText(R.string.stop_recording);
    }

    // resets button
    public void stopClicked() {
        startButton.setBackgroundResource(R.drawable.circle);
        startButton.setText(R.string.start_recording);
    }

    // check if do not disturb mode is preferred when recording
    public boolean checkIfDnd() {
        return settings.getBoolean("dnd", false);
    }

    // method to keep the screen on if checkScreen is true
    public void handleScreen() {
        if (checkScreen()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else if (!checkScreen()) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    // check if keep screen awake is on
    public boolean checkScreen() {
        return settings.getBoolean("screen", false);
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
    public void setDefaults() {
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
    private void firstTimeInitialise() {
        mediaRecorder = new MediaRecorder();
        setAudioSource();
        setDefaults();
        setUpFileFirstTime();
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

    public void setAudioSource() {
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
    }

    //set new fileName for first time, count is incremented
    public void setUpFileFirstTime() {
        String fileName = Environment.getExternalStorageDirectory() + File.separator +
                Environment.DIRECTORY_DOWNLOADS + File.separator + "my_recording" + ".m4a";
        mediaRecorder.setOutputFile(fileName);
        count++;
    }

    //set new fileName each time, count used so that recordings are never overwritten
    public void setUpFile() {
        if (inputtedText == null) {
            String fileName = Environment.getExternalStorageDirectory() + File.separator +
                    Environment.DIRECTORY_DOWNLOADS + File.separator + "my_recording" + count + addFileType();
            mediaRecorder.setOutputFile(fileName);
            count++;
        } else {
            String fileName = Environment.getExternalStorageDirectory() + File.separator +
                    Environment.DIRECTORY_DOWNLOADS + File.separator + inputtedText + addFileType();
            mediaRecorder.setOutputFile(fileName);
        }
    }

    // adds the correct file extension to fileName
    public String addFileType() {
        String fileExt = null;
        int fileType = Integer.parseInt(settings.getString("outputFormat", "5"));
        switch (fileType) {
            case 1:
                fileExt = ".aac";
                break;
            case 2:
                fileExt = ".3ga";
                break;
            case 3:
                fileExt = ".3ga";
                break;
            case 4:
                fileExt = ".ts";
                break;
            case 5:
                fileExt = ".m4a";
                break;
            case 6:
                fileExt = ".ogg";
                break;
            case 7:
                fileExt = ".3gpp";
                break;
            case 8:
                fileExt = ".webm";
                break;
        }
        return fileExt;
    }

    public void setAudioSamplingRate(int sampleRate) {
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

    public void setAudioEncodingBitRate(int bitRate) {
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
    public void setOutputFormat(int outputFormat) {
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
    public void setAudioCodec(int codec) {
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

    // prepare and start recording after mediaRecorder set up
    // log error if prepare failed, for testing
    private void startRecording() {
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            Log.e("mRecorder error", "Prepare failed");
            System.out.println("" + e);
        }
        mediaRecorder.start();
    }

    // stop and call release method
    private void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    // could be switch statement in future if more permission codes needed, if statement fine
    // for now as it's just the one
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // request permissions
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
    }

    // check if app has read, write and mic permissions
    private boolean checkPermissions() {
        int writeExternalStorageResult = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int recordAudioResult = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int readExternalStorageResult = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return writeExternalStorageResult == PackageManager.PERMISSION_GRANTED &&
                recordAudioResult == PackageManager.PERMISSION_GRANTED && readExternalStorageResult == PackageManager.PERMISSION_GRANTED;
    }

    // menu in the top right
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

    // creates intent to start Settings activity
    public void openSettings() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    // create intent to open About page activity
    public void openAbout() {
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

    // handle the keep screen on option in on resume
    @Override
    protected void onResume() {
        super.onResume();
        handleScreen();
    }
}