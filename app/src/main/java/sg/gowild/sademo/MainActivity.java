package sg.gowild.sademo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Locale;

import Database.DBController;
import DialogFlow.DialogFlowConfiguration;
import Hardware.EV3Configuration;
import Model.Patient;
import Model.Prescription;
import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Fulfillment;
import ai.api.model.Result;
import ai.kitt.snowboy.SnowboyDetect;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    // View Variables
    private Button button;
    private Button ev3ButtonIn;
    private Button ev3ButtonOut;
    private TextView textView;
    private ImageView imageView;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    // ASR Variables
    private SpeechRecognizer speechRecognizer;

    // TTS Variables
    private TextToSpeech textToSpeech;

    // NLU Variables
    //AIDataService part of google
    private AIDataService aiDataService;

    // Hotword Variables
    private boolean shouldDetect;
    private SnowboyDetect snowboyDetect;

    //Controller
    private EpioneController epione = new EpioneController(this);


    //need a real android phone to work
    static {
        System.loadLibrary("snowboy-detect-android");
    }

    // TODO: REPLACE MODEL FILE WITH PERSONAL MODEL FOR HOTWORD
    //TODO:CONFIGURE NLU TO PASS PATIENT ID TO WEBHOOK
    //TODO:CONFIGURE NLU TO GET INTENT AND CALL RELEVANT FUNCTION


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpConfigurations();

        //enable hotword
        startHotword();

        //TODO:FUNCTION TO CONSTANTLY CHECK FOR REMAINDER
        //AND ALERT TO XIAOBAI
        //uncomment once everything is done
        //epione.checkRemainder("patient id");


    }



    //==== SET UP CONFIGURATIONS ============================================================================
    private void setUpConfigurations() {
        setupViews();
        //TODO:ONCE GET XIAOBAI,ENABLE THIS
        //setupXiaoBaiButton();
        setupAsr();
        setupTts();

        //link to dialogflow
        setupNlu(DialogFlowConfiguration.DIALOGFLOW_CLIENT_ACCESS_TOKEN);
        setupHotword();
    }

    private void setupViews() {
        // Setting up db
//







        // TODO: Setup Views if need be
        button = findViewById(R.id.button);
        ev3ButtonIn = findViewById(R.id.ev3ButtonIn);
        ev3ButtonOut = findViewById(R.id.ev3Buttonout);



        textView = findViewById(R.id.textview);
        imageView = findViewById(R.id.imageview);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set false to disable the hotword detection
               // shouldDetect = false;
               // startAsr();
                //Take Picture
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        ev3ButtonIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                EV3Configuration test = new EV3Configuration();
               test.GetRequest("in");
                Log.e("te","RUNNING eev3");

            }
        });

        ev3ButtonOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EV3Configuration test = new EV3Configuration();
                test.GetRequest("out");
                Log.e("te","RUNNING eev3");
            }
        });




    }

    private void setupXiaoBaiButton() {
        String BUTTON_ACTION = "com.gowild.action.clickDown_action";

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BUTTON_ACTION);

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //set false to disable the hotword detection
                shouldDetect = false;
                startAsr();
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }

    /*TO ENABLE SPEECH TO TEXT */
    private void setupAsr() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                //detect start of speech
            }

            @Override
            public void onRmsChanged(float v) {
                //volume change
            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                    //when it has dectect end of speech
            }

            @Override
            public void onError(int i) {
                Log.e("asr",
                        "Error" + Integer.toString(i));
                //restart hotword
                startHotword();
            }

            @Override
            public void onResults(Bundle results) {
                //Get results from user speech
                List<String> texts = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (texts == null || texts.isEmpty())
                {
                    textView.setText("Please Try again.");
                }
                else
                    {
                        String text = texts.get(0);


//                        String response;
//                        if(text.equalsIgnoreCase("hello"))
//                        {
//                            response = "hi there";
//                        }
//                        else
//                            {
//                                response = "I don't know what you are saying";
//                            }
                        //NLU
                        startNlu(text);

                            //display text
                        textView.setText(text);

                        //speak out response to user
                        //startTts(response);
                    }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                   //returns part of the speech
            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
    }

    /*TO ENABLE TEXT TO SPEECH */
    private void setupTts() {
        textToSpeech = new TextToSpeech(this,null);
    }

    //similar to RESTFUL Request
    /* LINK TO DIALOGFLOW */
    private void setupNlu(String client_token) {
        // TODO: Change Client Access Token - DONE
        String clientAccessToken = client_token;
        AIConfiguration aiConfiguration = new AIConfiguration(clientAccessToken,
                AIConfiguration.SupportedLanguages.English);
        aiDataService = new AIDataService(aiConfiguration);
    }



    private void setupHotword() {
        shouldDetect = false;
        SnowboyUtils.copyAssets(this);

        File snowboyDirectory = SnowboyUtils.getSnowboyDirectory();
        File model = new File(snowboyDirectory, "alexa_02092017.umdl");
        File common = new File(snowboyDirectory, "common.res");

        // TODO: Set Sensitivity
        snowboyDetect = new SnowboyDetect(common.getAbsolutePath(), model.getAbsolutePath());
        snowboyDetect.setSensitivity("0.60");
        snowboyDetect.applyFrontend(true);
    }

    //========================================================================================================


    //==== START CONFIGURATIONS ============================================================================
    private void startAsr() {

        //when getting user feedback
        //stop scheduling remainder to avoid clash
        epione.executor.shutdown();


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // TODO: ABLE TO SWITCH LANGUAGES TO OTHER LANGUAGES
                final Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
                recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en");
                recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
                recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
                //number of time to try to recogzinze
                recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

                // Stop hotword detection in case it is still running
                shouldDetect = false;

                //starts speech to text
                speechRecognizer.startListening(recognizerIntent);
            }
        };
        Threadings.runInMainThread(this, runnable);
    }


    public void startTts(String text) {

        // Start TTS
        //TextToSpeech.QUEUE_FLUSH - remove appening words when speaking
        textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);

        //while text to speech is running
        // have the start hotword started
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (textToSpeech.isSpeaking()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Log.e("tts", e.getMessage(), e);
                    }
                }

                //when finish speaking,re-enable hotword detection
                startHotword();
            }
        };
        Threadings.runInBackgroundThread(runnable);
    }

    private void startHotword() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                shouldDetect = true;
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);

                int bufferSize = 3200;
                byte[] audioBuffer = new byte[bufferSize];
                AudioRecord audioRecord = new AudioRecord(
                        MediaRecorder.AudioSource.DEFAULT,
                        16000,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        bufferSize
                );

                if (audioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
                    Log.e("hotword", "audio record fail to initialize");
                    return;
                }

                audioRecord.startRecording();
                Log.d("hotword", "start listening to hotword");

                while (shouldDetect) {
                    audioRecord.read(audioBuffer, 0, audioBuffer.length);

                    short[] shortArray = new short[audioBuffer.length / 2];
                    ByteBuffer.wrap(audioBuffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shortArray);

                    int result = snowboyDetect.runDetection(shortArray, shortArray.length);
                    if (result > 0) {
                        Log.d("hotword", "detected");
                        shouldDetect = false;
                    }
                }

                audioRecord.stop();
                audioRecord.release();
                Log.d("hotword", "stop listening to hotword");

                // after hotword is detected,enable ASR
                startAsr();
            }
        };
        Threadings.runInBackgroundThread(runnable);
    }



    private void startNlu(final String text) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //create request
                AIRequest aiRequest = new AIRequest();
                //set text to query
                aiRequest.setQuery(text);

                //sent to network
                try {
                    AIResponse aiResponse = aiDataService.request(aiRequest);


                    //get result
                    Result result = aiResponse.getResult();
                    Fulfillment fulfillment = result.getFulfillment();

                    //speech is response text
                    String originalSpeech = fulfillment.getSpeech();

                    String intentname = result.getMetadata().getIntentName();
                    callIntent(intentname,originalSpeech);

                   // startTts(responseText);

                } catch (AIServiceException e) {
                    Log.e("nlu",e.getMessage(),e);
                }
            }
        };
        Threadings.runInBackgroundThread(runnable);
    }


    public void callIntent(String intentname,String originalSpeech)
    {
        //if the intent is to give user medicine
        if(intentname.equalsIgnoreCase("medicine.give"))
        {
            //step 1.first check if is time to give medicine

            //step2. tell user, to scan face to verify
            textToSpeech.speak("Okay,Let me scan your face",TextToSpeech.QUEUE_FLUSH,null);
            while (textToSpeech.isSpeaking())
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //step3.verify user first by facial recognition
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
        else if(intentname.equalsIgnoreCase("medicine.close"))
        {
            startTts("Okay,Closing Cabinet. I will remind you for when's its time for your next medicine");

            //update remainder table

            //update prescription table

            //close cabinet box

            //once user takes medicine already
            //restart checking remainder
            epione.checkRemainder("1");
        }
        else
            {
                startTts(originalSpeech);

                //restart scheduling remainder
                //epione.checkRemainder("1");
            }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //check if is from photo
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            //photo taken
            final Bitmap photo = (Bitmap) data.getExtras().get("data");

            //for testing
            imageView.setImageBitmap(photo);

//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    //TODO: CALL FACIAL RECOGNITION API AND VERIFIY IF IS CORRECT USER
                    if(epione.ValidatePatient("patientid",photo)) //if is correct user
                    {
                        //get patient prescription
                        Prescription PatientPrescription = epione.getPrescription("patientid");

                        startTts("Please take the panadol in Box 1 and take 2 pills only");
                        //then read out instruction to user
//                        textToSpeech.speak("Please take the panadol in Box 1 and take 2 pills only ",TextToSpeech.QUEUE_FLUSH,null);
//                        while (textToSpeech.isSpeaking())
//                        {
//                            try {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }

                        //open cabinet box
                        epione.openBox();
                    }
                }
            };

            Threadings.runInBackgroundThread(runnable);




        }
    }



    public void AlertUser(String text)
    {
        textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
    }



    //========================================================================================================

//    private String getWeather() {
//        //okay http library
//
//        OkHttpClient okHttpClient = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url("https://api.data.gov.sg/v1/environment/2-hour-weather-forecast")
//                .header("accept","application/json")
//                .build();
//
//
//        try {
//            Response response = okHttpClient.newCall(request).execute();
//            String responseBody = response.body().string();
//
//            JSONObject jsonObject = new JSONObject(responseBody);
//            JSONArray forecasts = jsonObject.getJSONArray("items")
//                    .getJSONObject(0)
//                    .getJSONArray("forecasts");
//
//            for (int i = 0;i<forecasts.length();i++)
//            {
//                JSONObject forecastsObject = forecasts.getJSONObject(i);
//                String area = forecastsObject.getString("area");
//
//                if (area.equalsIgnoreCase("clementi"))
//                {
//                    String forecast = forecastsObject.getString("forecasts");
//                    return "The weather in clementi is now " + forecast;
//                }
//            }
//        } catch (IOException e) {
//            Log.e("weather",e.getMessage(),e);
//        } catch (JSONException e) {
//            Log.e("weather",e.getMessage(),e);
//        }
//        return "No weather info";
//    }
}
