package com.voicerecorderproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class About extends AppCompatActivity {

    final int MIN_CHARS_BIT = 10;
    String fullTextBit;
    boolean expandedBit;

    final int MIN_CHARS_SAMPLE = 10;
    String fullTextSample;
    boolean expandedSample;

    final int MIN_CHARS_CODEC = 10;
    String fullTextCodec;
    boolean expandedCodec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        final ImageButton imageButtonBit = findViewById(R.id.expandBtnBit);
        final ImageButton imageButtonSample = findViewById(R.id.expandBtnSample);
        final ImageButton imageButtonCodec = findViewById(R.id.expandBtnCodec);

        imageButtonBit.setImageResource(R.drawable.ic_add_24px);
        imageButtonSample.setImageResource(R.drawable.ic_add_24px);
        imageButtonCodec.setImageResource(R.drawable.ic_add_24px);

        final TextView textViewBit = findViewById(R.id.expandableTextViewBit);
        final TextView textViewSample = findViewById(R.id.expandableTextViewSample);
        final TextView textViewCodec = findViewById(R.id.expandableTextViewCodec);

        fullTextBit = textViewBit.getText().toString();
        textViewBit.setText(fullTextBit.substring(0, MIN_CHARS_BIT));

        fullTextSample = textViewSample.getText().toString();
        textViewSample.setText(fullTextSample.substring(0, MIN_CHARS_SAMPLE));

        fullTextCodec = textViewCodec.getText().toString();
        textViewCodec.setText(fullTextCodec.substring(0, MIN_CHARS_CODEC));

        imageButtonBit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandedBit = ! expandedBit;
                imageButtonBit.setImageResource(expandedBit?R.drawable.remove:R.drawable.ic_add_24px);
                textViewBit.setText(expandedBit?fullTextBit:fullTextBit.substring(0, MIN_CHARS_BIT));
            }
        });

        imageButtonBit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandedSample = ! expandedSample;
                imageButtonSample.setImageResource(expandedSample?R.drawable.remove:R.drawable.ic_add_24px);
                textViewSample.setText(expandedSample?fullTextSample:fullTextSample.substring(0, MIN_CHARS_SAMPLE));
            }
        });

        imageButtonCodec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandedCodec = ! expandedCodec;
                imageButtonCodec.setImageResource(expandedCodec?R.drawable.remove:R.drawable.ic_add_24px);
                textViewCodec.setText(expandedCodec?fullTextCodec:fullTextCodec.substring(0, MIN_CHARS_CODEC));
            }
        });
    }

}
