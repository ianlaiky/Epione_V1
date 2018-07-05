package sg.gowild.sademo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import FacialRecognition.FacialRecognitionConfiguration;

public class CameraActivity extends Activity implements PictureCallback, SurfaceHolder.Callback {

    public static final String EXTRA_CAMERA_DATA = "camera_data";

    private static final String KEY_IS_CAPTURING = "is_capturing";

    private Camera mCamera;
    private ImageView mCameraImage;
    private SurfaceView mCameraPreview;
    private Button mCaptureImageButton;
    private byte[] mCameraData;
    private boolean mIsCapturing;

    private ProgressBar progressBar;
    private TextView textView;

     static String patientFaceID;

    private OnClickListener mCaptureImageButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            captureImage();
        }
    };


    private OnClickListener mDoneButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mCameraData != null) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_CAMERA_DATA, mCameraData);
                setResult(RESULT_OK, intent);
            } else {
                setResult(RESULT_CANCELED);
            }
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera);


        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);

        textView = (TextView)findViewById(R.id.label_timer);

        mCameraImage = (ImageView) findViewById(R.id.camera_image_view);
        mCameraImage.setVisibility(View.INVISIBLE);


        mCameraPreview = (SurfaceView) findViewById(R.id.preview_view);
        final SurfaceHolder surfaceHolder = mCameraPreview.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mCaptureImageButton = (Button) findViewById(R.id.capture_image_button);
        mCaptureImageButton.setOnClickListener(mCaptureImageButtonClickListener);


        Button doneButton = (Button) findViewById(R.id.done_button);
        doneButton.setOnClickListener(mDoneButtonClickListener);


        mCaptureImageButton.setVisibility(View.INVISIBLE);
        doneButton.setVisibility(View.INVISIBLE);

        mIsCapturing = true;
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putBoolean(KEY_IS_CAPTURING, mIsCapturing);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mIsCapturing = savedInstanceState.getBoolean(KEY_IS_CAPTURING, mCameraData == null);
        if (mCameraData != null) {
            //setupImageDisplay();
        } else {
            //setupImageCapture();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mCamera == null) {
            try {

                //switch to camera view(either front or back)
                int cameraIndex = -1;
                int cameraCount = Camera.getNumberOfCameras();
                for (int i = 0; i < cameraCount && cameraIndex == -1; i++) {
                    Camera.CameraInfo info = new Camera.CameraInfo();
                    Camera.getCameraInfo(i, info);
                                                    //change to what you want (CAMERA_FACING_FRONT,CAMERA_FACING_BACK)
                    if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        cameraIndex = i;
                    }
                }
                if (cameraIndex != -1) {}
                mCamera = Camera.open(cameraIndex);

                //set camera to continually auto-focus
                Camera.Parameters params = mCamera.getParameters();
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                mCamera.setParameters(params);

                //set view of camera
                mCamera.setPreviewDisplay(mCameraPreview.getHolder());
                if (mIsCapturing) {
                    //start preview
                    mCamera.startPreview();
                }
            } catch (Exception e) {
                Toast.makeText(CameraActivity.this, "Unable to open camera.", Toast.LENGTH_LONG)
                        .show();
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        //once preview is started
        if (mCamera != null) {
            try {
                //mCamera.setDisplayOrientation(90);
                mCamera.setPreviewDisplay(holder);
                if (mIsCapturing) {

                    //create a timer for label to count down to take picture
                    final int count = 5;
                    new Timer().scheduleAtFixedRate(new TimerTask() {
                        int c = count;
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    // Stuff that updates the UI
                                    textView.setText(String.valueOf(c));

                                }
                            });

                            c--;
                            if(c==0){
                                this.cancel();
                            }


                        }
                    }, 0, 1000);//put here time 1000 milliseconds=1 second

                    //start preview for user to view
                    mCamera.startPreview();

                    //function is called after 5 seconds
                    //takes a photo and processes back to MAINACTIVITY
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textView.setVisibility(View.INVISIBLE);
                            MainActivity.textToSpeech.speak(MainActivity.lang.getVERIFYING_FACE_RESPONSE(), TextToSpeech.QUEUE_FLUSH,null);
                            System.out.println("fafjafakfjakjfakjf " );
                                    captureImage();
                                    progressBar.setVisibility(View.VISIBLE);
                                    mCameraPreview.setVisibility(View.INVISIBLE);

                        }
                    }, 5000);

//                    //USING FACE DETECTION
//                    mCamera.startFaceDetection();
//                    mCamera.setFaceDetectionListener(new Camera.FaceDetectionListener() {
//                        @Override
//                        public void onFaceDetection(Camera.Face[] faces, Camera camera) {
//
//                            if(faces.length > 0){
//                                if(faces[0].score == 100){
//                                    mCamera.stopFaceDetection();
//                                    MainActivity.textToSpeech.speak(MainActivity.lang.getVERIFYING_FACE_RESPONSE(), TextToSpeech.QUEUE_FLUSH,null);
//                                    System.out.println("fafjafakfjakjfakjf " + faces[0].score);
//                                    captureImage();
//
//                                    //mCamera.stopPreview();
//                                    progressBar.setVisibility(View.VISIBLE);
//                                    //mCameraPreview.setVisibility(View.INVISIBLE);
//                                }
//                            }
//
//
//
//
//
//
//                        }
//                    });

                }
            } catch (IOException e) {
                Toast.makeText(CameraActivity.this, "Unable to start camera preview.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }



    private void captureImage() {
        //TAKES A PICTURE
        mCamera.takePicture(null, null, this);
    }


    //CODE
    public static final String FACIAL_RECOGNITION_DATA = "fr_DATA";
    public static final int FACIAL_RECOGNITON_RESULT = 888;


    @Override
    /*
    * ONCE mCamera.takePicture(null, null, this); IS CALLED
    * WILL CALL FUNCTION
    * */
    public void onPictureTaken(byte[] data, Camera camera) {

        //GET DATA OF PICTURE
        mCameraData = data;


        //convert byte to bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(mCameraData, 0, mCameraData.length);
       // bitmap = RotateBitmap(bitmap,-90);
        bitmap = Bitmap.createScaledBitmap(bitmap,200,200,true);

        //bitmap to byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bitmap.recycle();



        //ONCE PICTURE TAKEN CALL FACIAL RECONGNITION
        //AND PASS DATA TO MAIN ACTIVITY
        System.out.println("gagagagagagaa" + patientFaceID);
        //DETERMINE IF PHOTO IS VALID PATIENT
//            boolean isValid = new FacialRecognitionConfiguration().execute(byteArray,patientFaceID).get();
        boolean isValid = true;

        //PASS DATA BACK TO MAIN ACTICIVIY
        Intent intent = new Intent();
        intent.putExtra(FACIAL_RECOGNITION_DATA,isValid);
        setResult(FACIAL_RECOGNITON_RESULT,intent);
        finish();
    }


    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private void setupImageCapture() {
        mCameraImage.setVisibility(View.INVISIBLE);
        mCameraPreview.setVisibility(View.VISIBLE);
        mCamera.startPreview();
        mCaptureImageButton.setText("SNAP IT");
        mCaptureImageButton.setOnClickListener(mCaptureImageButtonClickListener);
    }
//
    private void setupImageDisplay() {
        Bitmap bitmap = BitmapFactory.decodeByteArray(mCameraData, 0, mCameraData.length);
        bitmap = RotateBitmap(bitmap,270);
        mCameraImage.setImageBitmap(bitmap);
        mCamera.stopPreview();
        mCameraPreview.setVisibility(View.INVISIBLE);
        mCameraImage.setVisibility(View.VISIBLE);
        mCaptureImageButton.setText("TAKE AGAIN");
        //mCaptureImageButton.setOnClickListener(mRecaptureImageButtonClickListener);
    }
}