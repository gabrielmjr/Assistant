package com.gabrielMJr.twaire.assistant;

import com.gabrielMJr.twaire.assistant.data.DataLoader;
import android.os.Bundle;
import com.gabrielMJr.twaire.assistant.R;
import com.gabrielMJr.twaire.assistant.Actions;
import android.widget.EditText;
import android.widget.ImageView;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;
import java.util.Locale;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.speech.SpeechRecognizer;
import android.speech.RecognizerIntent;
import android.os.CountDownTimer;
import android.speech.RecognitionListener;
import java.util.ArrayList;
import android.os.Handler;
import com.gabrielMJr.twaire.assistant.device_hw_sw.BatteryReceiver;
import android.content.IntentFilter;
import com.gabrielMJr.twaire.assistant.device_hw_sw.DateGetter;

public class MainActivity extends DataLoader
{

    // Attributes
    // Widgets
    private static EditText text_command;
    private static ImageView mic_status;
    private static ImageView send;

    // Text to speech
    private static TextToSpeech tts;

    // Speech recognizer 
    private static Intent speechRecognizerIntent;
    private SpeechRecognizer speechRecognizer;

    // Switcher
    private static int count = 0;

    // Actions object
    protected static Actions actions;

    // Battery object
    private static BatteryReceiver batteryReceiver;
    private static IntentFilter battery_intent_filter;
    
    // Date object
    private static DateGetter dateGetter;

    // The assistant will remind the 3 last actions
    private static String action_1 = null;
    private static String action_2 = null;
    private static String action_3 = null;

    // String that contains values like yes or no
    private static String[] accept = new String[] {"yes", "exatly", "obvious", "correct"};
    private static String[] refuse = new String[] {"no", "not", "forget"};

    // Here is the last questions and answers told by user
    private static String lastQuestion=  null;
    private static String lastAnswer = null;

    



    /*
     HERE IS THE CENTER "BRAIN" OF THE ASISSTANT
     */
    private void assistBrain(String command)
    {


        String[] commandVetor = command.split(" ");
        boolean status = true;
        int i = 1;
        int j = 0;

        while (true)
        {
            // Check the answer on shared preferences
            if (status)
            {
                // Checking from SP
                String[] sp_questions = getQuestions();
                for (String u: sp_questions)
                {
                    if (u != null && u.equals(command))
                    {
                        speak(getAnswer(i));
                        return;

                    }
                    i++;
                }
                status = false;
            }

            // On SP doent have answer, check the actions
            else
            {

                // Checking actions
                // Learning status 1
                if (actions.LEARNING_0.equals(action_1))
                { 
                    for (String u: commandVetor)
                    {

                        // Check if user acepted
                        for (String accepted: accept)
                        {             
                            if (u.equals(accepted))
                            {
                                speak("What is the answer?");
                                setAction_1(actions.LEARNING_1);
                                return;
                            }

                            // Check if user refused
                            for (String refused: refuse)
                            {
                                if (u.equals(refused))
                                {
                                    speak("Alright");
                                    setAction_1(actions.MENU);
                                    return;
                                }
                            }
                        }
                    }
                }

                // Learning status 2
                if (actions.LEARNING_1.equals(action_1))
                {
                    lastAnswer = command;
                    addQuestionAnswer(lastQuestion, lastAnswer);
                    setAction_1(actions.MENU);
                    return;
                }


                // Starting from search results
                for (String u: commandVetor)
                {

                    if ("are".equals(u))
                    {
                        for (String v: command.split(" "))
                        {
                            if ("listening".equals(v))
                            {
                                speak("I'm listening");
                                return;
                            }
                        }
                    }

                    // Check for "what" questions
                    else if ("what".equals(u))
                    {
                        for (String v: commandVetor)
                        {
                            
                            // Battery status
                            if ("battery".equals(v))
                            {
                                for (String w: commandVetor)
                                {
                                    if ("about".equals(w))
                                    {
                                        speak("Your battery status is " + batteryReceiver.toString());
                                        return;
                                    }
                                    else if ("status".equals(w))
                                    {
                                        speak("Your battery status is " + batteryReceiver.getStatus());
                                        return;
                                    }
                                    else if ("health".equals(w))
                                    {
                                        speak("Your battery health is " + batteryReceiver.getHealth());
                                        return;
                                    }
                                    else if ("percentage".equals(w))
                                    {
                                        speak("Your battery have " + batteryReceiver.getPercentage() + " percent of charge");
                                        return;
                                    }
                                }
                            }
                            
                            // Time
                            else if ("is".equals(v))
                            {
                                for (String w: commandVetor)
                                {
                                    
                                    // Time
                                    if ("time".equals(w))
                                    {
                                        speak("Now is " + sinthonizeDate());
                                        return;
                                    }
                                    
                                    // Date
                                    else if ("date".equals(w))
                                    {
                                        speak("Today is " + dateGetter.getDay()
                                              + " of " + dateGetter.getMonthName()
                                              + " of " + dateGetter.getYear());
                                              return;
                                    }
                                    
                                    // Second, minute, hour, day, month, year and year_day
                                    else if ("second".equals(w))
                                    {
                                        speak("Now is " + dateGetter.getSecond() + " second");
                                        return;
                                    }
                                    else if ("minute".equals(w))
                                    {
                                        speak("Now is " + dateGetter.getMinute() + " minute");
                                        return;
                                    }
                                    else if ("hour".equals(w))
                                    {
                                        speak("Now is " + dateGetter.getHour() + " hour");
                                        return;
                                    }
                                    else if ("day".equals(w))
                                    {
                                        for (String x: commandVetor)
                                        {
                                            if ("year".equals(x))
                                            {
                                                speak("Today is " + dateGetter.getDayYear() + "° day of year");
                                                return;
                                            }
                                        }
                                        speak("today is day" + dateGetter.getDay());
                                        return;
                                    }
                                    else if ("month".equals(w))
                                    {
                                        speak("we are in " + dateGetter.getMonthName());
                                        return;
                                    }
                                    else if ("year".equals(w))
                                    {
                                        speak("we are in " + dateGetter.getYear());
                                        return;
                                    }
                                }
                            }                   
                        }

                        speak("I don't know! Do you know?");
                        setAction_1(actions.LEARNING_0);
                        lastQuestion = command;
                        return;
                    } 
                }

                // The loop must repeat 2 times, just 2 times
                if (j == 2)
                {
                    return;
                }
                j++;
            }
        }
    }








    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the assistant
        initialize();      

        setListening(true);

        // On click of the mic
        mic_status.setOnClickListener(
            new OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    //speak(String.valueOf(getQuestionIndex(getQuestions()[1])));
                    //speak(getAnswer(1));
                    // speak(getAnswer(getQuestionIndex(getQuestions()[2])));
                    // speak(getQuestions()[0][0])

                    // If mic is on, turn off
                    if (count == 1)
                    {
                        setListening(false);
                    }

                    // Of mic is off, turn on
                    else
                    {
                        setListening(true);
                    }
                }
            });

        // Send button on click
        send.setOnClickListener(
            new OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (text_command.getText().toString().isEmpty())

                    {
                        text_command.setError(getText(R.string.null_field));
                    }
                    else
                    {
                        assistBrain(text_command.getText().toString());
                    }
                }
            });

        // Speech recognizer results
        speechRecognizer.setRecognitionListener(
            new RecognitionListener()
            {
                @Override
                public void onReadyForSpeech(Bundle p1)
                {
                }

                @Override
                public void onBeginningOfSpeech()
                {
                }

                @Override
                public void onRmsChanged(float p1)
                {
                }

                @Override
                public void onBufferReceived(byte[] p1)
                {
                }

                @Override
                public void onEndOfSpeech()
                {
                }

                @Override
                public void onError(int p1)
                {
                    // I stoped here
                }

                @Override
                public void onResults(Bundle result)
                {
                    ArrayList<String> data = result.getStringArrayList(speechRecognizer.RESULTS_RECOGNITION);
                    text_command.setText(data.get(0));
                    setListening(false);
                    assistBrain(data.get(0));
                }

                @Override
                public void onPartialResults(Bundle p1)
                {
                }

                @Override
                public void onEvent(int p1, Bundle p2)
                {
                }        

            });
    }



    // Initializing the assistant
    private void initialize()
    {
        text_command = findViewById(R.id.text_command);
        mic_status = findViewById(R.id.statusMic);
        send = findViewById(R.id.send);

        // Initialize actions
        actions = new Actions();
        
        // Initialize Date
        dateGetter = new DateGetter();

        // Initialize battery analyzer
         batteryReceiver = new BatteryReceiver();
         battery_intent_filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
         

        // Initialize sharedPreferences();
        initializeSharedPreferences();

        // Initialize tts and speech recognizer
        initializeTTS();
        initializeSpeechRecognizer();

    }


    // Initializing the tts
    private void initializeTTS()
    {
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener()
            {

                // Initializing the text to speech
                @Override
                public void onInit(int status)
                {
                    // If the start is succes
                    if (status == TextToSpeech.SUCCESS)
                    {

                        // Try uk english
                        int result = tts.setLanguage(Locale.UK);

                        // If the UK id not supoorted, switch to us english
                        if (result == TextToSpeech.LANG_MISSING_DATA
                            || 
                            result == TextToSpeech.LANG_NOT_SUPPORTED)
                        {

                            result = tts.setLanguage(Locale.US);

                            // If us also if failling, it cant be runnable
                            if (result == TextToSpeech.LANG_MISSING_DATA
                                || 
                                result == TextToSpeech.LANG_NOT_SUPPORTED)
                            {

                                Toast.makeText(getApplicationContext(), getText(R.string.lang_insupported), Toast.LENGTH_LONG).show();

                            }

                            // Here the US english is running
                            else
                            {
                                Toast.makeText(getApplicationContext(), getText(R.string.swithed_to_us), Toast.LENGTH_LONG).show();
                            }

                        }
                        else
                        {
                            // Everything from text to soeech is great here
                        }                       
                    }

                    // The initialization failed
                    else
                    {
                        Toast.makeText(getApplication(), getText(R.string.mic_init_failed), Toast.LENGTH_LONG).show();
                    }
                }                     
            });
    }


    // Initializing speech recognizer
    private void initializeSpeechRecognizer()
    {
        // Before check permission, check the SDK version
        // If equals or greater that 6, ask
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            // Check permissiin
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            {
                // The permission was not granted, ask now
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, 100);
            }

            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
            speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        }

    }

    // Method to speak easy
    protected void speak(String text)
    {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    // Method to set mic on easy
    protected void setListening(Boolean value)
    {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
        {
            speak("To use microphone in me you need to give me the permission");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        askMicPermission();
                    }
                }, 4000);
        } 
        else
        {
            // Start listening
            if (value)
            {
                mic_status.setImageDrawable(getDrawable(R.drawable.ic_microphone_on));
                speechRecognizer.startListening(speechRecognizerIntent);
                count = 1;
            } 
            else
            {
                // Stop listening
                mic_status.setImageDrawable(getDrawable(R.drawable.ic_microphone_off));
                speechRecognizer.stopListening();
                count = 0;
            }
        }
    }

    // Shut down tts
    @Override
    protected void onDestroy()
    {
        if (tts != null)
        {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }


    // Register battery receiver broadcast
    @Override
    protected void onResume()
    {
        super.onResume();
        registerReceiver(batteryReceiver, battery_intent_filter);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        unregisterReceiver(batteryReceiver);
    }

    // A simple method to ask mic permission
    private void askMicPermission()
    {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, 100);
    }

    // Passing the actions, 1 to 2, 2 to 3 and actual to 1
    private void setAction_1(String action)
    {
        action_3 = action_2;
        action_2 = action_1;
        action_1 = action;
    }
    
    
    // Sinthonize and normaize date
    private String sinthonizeDate()
    {
        // Strings that contains infixes
        String hh = null;
        String mm = null;
        String ss = null;
        
        // Dates
        int hour = Integer.valueOf(dateGetter.getHour());
        int minute = Integer.valueOf(dateGetter.getMinute());
        int second = Integer.valueOf(dateGetter.getSecond());
        
        // Hours is equals to "hour" if hour <= 1, else is equals to "hours"
        hh = (hour <= 1) ? "hour": "hours";
        mm = (minute <= 1) ? "minute": "minutes";
        ss = (second <= 1) ? "second": "seconds";
        
        return hour + " " + hh 
               + ", " 
               + minute + " " + mm 
               + " and "
               + second + " " + ss;
    }

}
