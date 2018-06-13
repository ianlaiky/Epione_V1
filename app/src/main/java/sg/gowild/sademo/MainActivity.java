package sg.gowild.sademo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import DialogFlow.DialogFlowConfiguration;
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
    private TextView textView;

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


    //need a real android phone to work
    static {
        System.loadLibrary("snowboy-detect-android");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpConfigurations();

        //enable hotword
        startHotword();

        //TODO:FUNCTION TO CONSTANTLY CHECK FOR REMAINDER
        //AND ALERT TO XIAOBAI

        //TODO:SET UP FACIAL RECOGNITION


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
        // TODO: Setup Views if need be
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textview);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set false to disable the hotword detection
                shouldDetect = false;
                startAsr();
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
        // TODO: Change Client Access Token
        String clientAccessToken = client_token;
        AIConfiguration aiConfiguration = new AIConfiguration(clientAccessToken,
                AIConfiguration.SupportedLanguages.English);
        aiDataService = new AIDataService(aiConfiguration);
    }


    // TODO: REPLACE MODEL FILE WITH PERSONAL MODEL FOR HOTWORD
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


    private void startTts(String text) {
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


    //TODO:CONFIGURE HERE TO PASS PATIENT ID TO WEBHOOK
    //TODO:CONFIGURE HERE TO GET INTENT AND CALL RELEVANT FUNCTION
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
                    String responseText = fulfillment.getSpeech();

                    //TODO:GET INTENT AND CONTEXT AND VALIDATE FOR APPROPRIATE ACTION
                    //

                    startTts(responseText);

                } catch (AIServiceException e) {
                    Log.e("nlu",e.getMessage(),e);
                }
            }
        };
        Threadings.runInBackgroundThread(runnable);
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
