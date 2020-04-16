package com.voicerecorderproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "AudioRecordTest";
    private  static MediaRecorder mediaRecorder;
    final int REQUEST_PERMISSION_CODE = 1000;

   private static String fileName = "";

   Button startButton, stopButton;


    public static void setUpMediaRecorder() {
        // set up new MediaRecorder here so that it is done each time user starts recording
        mediaRecorder = new MediaRecorder();
        //set new fileName each time mediaRecorder is setup with UUID
        fileName = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DOWNLOADS + File.separator +
                UUID.randomUUID().toString() + "_audioRecordTest.m4a";
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mediaRecorder.setAudioSamplingRate(44100);
        mediaRecorder.setAudioEncodingBitRate(96000);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
        mediaRecorder.setOutputFile(fileName);
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
        startButton.setEnabled(false);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);

        //check permissions at runtime
        if (!checkPermissions()) {
            requestPermission();
        }

        //when record is clicked
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpMediaRecorder();
                startRecording();
                startButton.setEnabled(false);
//                recording = true;
                Toast.makeText(MainActivity.this, "Recording started", Toast.LENGTH_SHORT).show();
            }
        });

        //when stop is clicked
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();
//                recording = false;
                startButton.setEnabled(true);
                Toast.makeText(MainActivity.this, "Recording stopped", Toast.LENGTH_SHORT).show();
            }
        });

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

}