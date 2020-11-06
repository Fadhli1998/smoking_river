package com.example.newbeta;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import android.speech.*;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //Declare Variables
    Button testbutton;
    Button testvoice;
    TextView testtextview;
    TextToSpeech textToSpeech;
    SpeechRecognizer mSpeechRecognizer;
    Intent mSpeechRecognizerIntent;
    TextView testresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testbutton = findViewById(R.id.testbutton);
        testvoice = findViewById(R.id.testvoice);
        testtextview = findViewById(R.id.testtextview);
        testresult = findViewById(R.id.testresult);

        //Text to Speech Declaration
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.ENGLISH);
                    textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {
                        }

                        @Override
                        public void onDone(String utteranceId) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    testvoice.setVisibility(View.VISIBLE);
                                }
                            });
                        }

                        @Override
                        public void onError(String utteranceId) {

                        }
                    });
                }

            }
        });


        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {
                testtextview.setText("Listening...");
            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {
                testtextview.setText("Stopped Listening.");
            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if(matches != null){
                    testresult.setText(matches.get(0));
                    if(matches.get(0).equals("left")){
                        textToSpeech.speak("We are going left", TextToSpeech.QUEUE_FLUSH,null, "MyUniqueUtteranceId");
                        testvoice.setVisibility(View.INVISIBLE);
                    }
                    else if(matches.get(0).equals("right")){
                        textToSpeech.speak("We are going right", TextToSpeech.QUEUE_FLUSH,null, "MyUniqueUtteranceId");
                        testvoice.setVisibility(View.INVISIBLE);
                    }
                }
                else{
                    testresult.setText("I did not quite catch that.");
                }

            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        testbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = "You want to go left or right?";
                textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "MyUniqueUtteranceId");
            }
        });

        testvoice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    mSpeechRecognizer.stopListening();
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                }
                return false;
            }
        });



    }

}