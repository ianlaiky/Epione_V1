package sg.gowild.sademo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;


import Database.DBController;
import DialogFlow.DialogFlowConfiguration;
import FacialRecognition.FacialRecognitionConfiguration;
import Hardware.EV3Configuration;
import Model.Medicine;
import Model.Patient;
import Model.Prescription;
import Model.Reminder;
import Language.*;
import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Fulfillment;
import ai.api.model.Result;
import ai.kitt.snowboy.SnowboyDetect;
import okhttp3.MediaType;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;


public class MainActivity extends AppCompatActivity {
    // View Variables
    private Button button;
    private Button ev3ButtonIn;
    private Button ev3ButtonOut;
    private TextView textView;
    private ImageView imageView;
    private ProgressBar progressBar;

    //for camera
    private static final int CAMERA_REQUEST = 100;
    private String imageFilePath;
    private Uri photoURI;
    private Uri pfile;
    private Path path;

    // ASR Variables
    private SpeechRecognizer speechRecognizer;

    // TTS Variables
    public static TextToSpeech textToSpeech;

    // NLU Variables
    //AIDataService part of google
    private AIDataService aiDataService;

    // Hotword Variables
    private boolean shouldDetect;
    private SnowboyDetect snowboyDetect;

    //Controller
    private EpioneController epione = new EpioneController(this);

    //will get patient data once have a reminder
    public Patient Patient;
    public Reminder Reminder;

    public static Language lang = new Language();





    //need a real android phone to work
    static {
        System.loadLibrary("snowboy-detect-android");
    }

    // TODO: REPLACE MODEL FILE WITH PERSONAL MODEL FOR HOTWORD - DONE
    //TODO:CONFIGURE NLU TO PASS PATIENT ID TO WEBHOOK - LATER PART
    //TODO:CONFIGURE NLU TO GET INTENT AND CALL RELEVANT FUNCTION - PARTIAL DONE


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        //TODO:FUNCTION TO CONSTANTLY CHECK FOR REMAINDER
        //AND ALERT TO XIAOBAI
        //uncomment once everything is done

        setUpConfigurations();
        //


//        new FacialRecognitionConfiguration(pic).execute("");

//        new FacialRecognitionConfiguration().execute("");
        //TODO:MAKE SURE CODE BELOW IS UNCOMMENT FOR WHOLE FLOW TO WORK
        epione.checkRemainder();



        //enable hotword
        startHotword();

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

        // TODO: Setup Views if need be
        button = findViewById(R.id.button);
        ev3ButtonIn = findViewById(R.id.ev3ButtonIn);
        ev3ButtonOut = findViewById(R.id.ev3Buttonout);


        textView = findViewById(R.id.textview);
        imageView = findViewById(R.id.imageview);
        imageView.setImageResource(R.drawable.xiaobaigeneral);

        progressBar = findViewById(R.id.progressbar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set false to disable the hotword detection
                // shouldDetect = false;
                // startAsr();
                //Take Picture

            }
        });

        //remove later
        ev3ButtonIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                EV3Configuration test = new EV3Configuration();
                test.GetRequest("in");
                Log.e("te", "RUNNING eev3");

            }
        });

        //remove later
        ev3ButtonOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EV3Configuration test = new EV3Configuration();
                test.GetRequest("out");
                Log.e("te", "RUNNING eev3");
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
                //show progress bar
                progressBar.setVisibility(View.VISIBLE);
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
                if (texts == null || texts.isEmpty()) {
                    textView.setText("Please Try again.");
                } else {
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
        textToSpeech = new TextToSpeech(this, null);
    }



    //similar to RESTFUL Request
    /* LINK TO DIALOGFLOW */
    private void setupNlu(String client_token) {
        // TODO: Change Client Access Token - DONE
        String clientAccessToken = client_token;
        AIConfiguration aiConfiguration = new AIConfiguration(clientAccessToken,
                lang.getDIALOGFLOW_LANGUAGE());
        aiDataService = new AIDataService(aiConfiguration);
    }


    private void setupHotword() {
        shouldDetect = false;
        SnowboyUtils.copyAssets(this);

        File snowboyDirectory = SnowboyUtils.getSnowboyDirectory();
        File model = new File(snowboyDirectory, "Hi_Epione.pmdl");
        File common = new File(snowboyDirectory, "common.res");

        // TODO: Set Sensitivity
        snowboyDetect = new SnowboyDetect(common.getAbsolutePath(), model.getAbsolutePath());
        snowboyDetect.setSensitivity("0.50");
        snowboyDetect.applyFrontend(true);
    }

    //========================================================================================================


    //==== START CONFIGURATIONS ============================================================================
    private void startAsr() {

        //when getting user feedback
        //stop scheduling remainder to avoid clash
        epione.executor.shutdown();

        final String lang_preference = lang.getRECONGNIZER_INTENT_LOCALE();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // TODO: ABLE TO SWITCH LANGUAGES TO OTHER LANGUAGES
                final Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, lang_preference);
                recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, lang_preference);
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
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);

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

        //HIDE PROGRESS BAR ONCE TEXT TO SPEECH STARTS
        //ONLY CAN BE DONE IN MAIN THREAD
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
            }
        };
        Threadings.runInMainThread(this,runnable1);
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

        String clientAccessToken = DialogFlowConfiguration.DIALOGFLOW_CLIENT_ACCESS_TOKEN;
        AIConfiguration aiConfiguration = new AIConfiguration(clientAccessToken,
                lang.getDIALOGFLOW_LANGUAGE());
        aiDataService = new AIDataService(aiConfiguration);


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

                    System.out.println(originalSpeech);

                    callIntent(intentname, originalSpeech);

                    // startTts(responseText);

                } catch (AIServiceException e) {
                    Log.e("nlu", e.getMessage(), e);
                }
            }
        };
        Threadings.runInBackgroundThread(runnable);
    }



    public void callIntent(String intentname, String originalSpeech) {
        //if the intent is to give user medicine
        if (intentname.equalsIgnoreCase("medicine.give")) {
            //step 1.first check if is time to give medicine

            //step2. tell user, to scan face to verify
            //textToSpeech.speak("Okay,Let me scan your face", TextToSpeech.QUEUE_FLUSH, null);
            textToSpeech.speak(lang.getSCAN_FACE_RESPONSE(), TextToSpeech.QUEUE_FLUSH, null);
            while (textToSpeech.isSpeaking()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //step3.verify user first by facial recognition
            //startCameraIntent();



            CameraActivity.patientFaceID = Patient.getFaceId();
            startActivityForResult(new Intent(MainActivity.this, CameraActivity.class), CAMERA_REQUEST);


        } else if (intentname.equalsIgnoreCase("medicine.close")) {
            //startTts("Okay,Closing Cabinet. I will remind you for when's its time for your next medicine");
            startTts(lang.getCloseCabinetResponse("1"));

            //close cabinet box
            System.out.println("Epione close box");
            String remindID = String.valueOf(Reminder.getReminderId());
            epione.closeBox(remindID);//will update remind table

            //once user takes medicine already
            //restart checking remainder
            epione.checkRemainder();
        } else {
            startTts(originalSpeech);
            //startTts("你好!");
            //
            //restart scheduling remainder
            epione.checkRemainder();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {




        Boolean iv = false;
        //check if is from photo
        if (requestCode == CAMERA_REQUEST) {


                if(resultCode == CameraActivity.FACIAL_RECOGNITON_RESULT){
                    iv = (Boolean) data.getExtras().get(CameraActivity.FACIAL_RECOGNITION_DATA);
                }

//            //photo taken
//            Bitmap bitmap =null;
//            try {
//                 bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),photoURI);
//                 //bitmap = RotateBitmap(bitmap,-90);
//                //imageView.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            //use photo send to server
//            final Bitmap photo = bitmap;
//
//            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
//            Uri tempUri = getImageUri(getApplicationContext(), photo);
//
//            // CALL THIS METHOD TO GET THE ACTUAL PATH
//            final File finalFile = new File(getRealPathFromURI(tempUri));


                final Boolean isValid = iv;

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        //TODO: CALL FACIAL RECOGNITION API AND VERIFIY IF IS CORRECT USER
                        //epione.ValidatePatient(Patient, finalFile)
                        if (isValid == true) //if is correct user
                        {



                            //get patient prescription
                            String pid = String.valueOf(Patient.getPatientId());

                            String ReminderPrescriptionID =  String.valueOf(Reminder.getPrescriptionId());
                            Prescription PatientPrescription = epione.getPatientPrescriptionBasedOnRemID(ReminderPrescriptionID);

                            String MedID =  String.valueOf(PatientPrescription.getMedId());
                            System.out.println("mam,amamammama "+MedID);
                            Medicine medicine = epione.getMedicineBasedOnPrescription(MedID);


                            String medName = medicine.getMedName();


                            //then read out instruction to user
                            //int timeToTakePerDay = PatientPrescription.getInstruction();
                            int dosage = PatientPrescription.getDosage();
                            String remarks = PatientPrescription.getRemarks();
                           // "Please take Panadol from box 1 and take only 2 pills."


                        //textToSpeech.speak("Please take " + medName + "from Box 1 and take only"+ dosage + " " + medicine.getMetricValue(),TextToSpeech.QUEUE_FLUSH,null);
                            textToSpeech.speak(lang.getGiveMedInstructionResponse("1",medName,dosage,medicine.getMetricValue()),TextToSpeech.QUEUE_FLUSH,null);
                        while (textToSpeech.isSpeaking())
                        {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                            //open cabinet box
                            System.out.println("Epione open box");

                        //TODO
                            epione.openBox();
                        }
                        else  //if not patient ask to try again
                            {
                                //textToSpeech.speak("Sorry, Could you please try that again ? ",TextToSpeech.QUEUE_FLUSH,null);
                                textToSpeech.speak(lang.getVERIFYING_FACE_FAIL_RESPONSE(),TextToSpeech.QUEUE_FLUSH,null);
                                while (textToSpeech.isSpeaking())
                                {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                //startCameraIntent();
                                CameraActivity.patientFaceID = Patient.getFaceId();
                                startActivityForResult(new Intent(MainActivity.this, CameraActivity.class), CAMERA_REQUEST);
                            }
                    }
                };

                Threadings.runInBackgroundThread(runnable);
                startHotword();

            Runnable runnable1 = new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            };
            Threadings.runInMainThread(this,runnable1);

        }
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


    private void startCameraIntent()
    {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        if(cameraIntent.resolveActivity(getPackageManager()) != null){
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            if (photoFile != null) {

                photoURI = FileProvider.getUriForFile(this.getApplication().getApplicationContext(),
                       "sg.gowild.sademo.provider", photoFile);

                //uncomment below for xiaobai, comment above
               // photoURI = Uri.fromFile(photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);
                startActivityForResult(cameraIntent,
                        100);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }


    public void AlertUser(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
    public void AlertUserAddOn(String text){
        textToSpeech.speak(text, TextToSpeech.QUEUE_ADD,null);
    }
//    public void AddPauseInTTS(){
//        textToSpeech.playSilentUtterance(1000, TextToSpeech.QUEUE_ADD, null);
//    }

    //========================================================================================================

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    public void changeTTSLang(){
        textToSpeech.setLanguage(lang.getTtsLanguage());
    }


}