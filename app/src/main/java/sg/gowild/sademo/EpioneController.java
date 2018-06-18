package sg.gowild.sademo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import FacialRecognition.FacialRecognitionConfiguration;
import Hardware.EV3Configuration;
import Model.Prescription;

//interacts with UI,database and dialogflow
public class EpioneController {

     ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public MainActivity app;

    private EV3Configuration ev3Box = new EV3Configuration();

    public EpioneController(MainActivity app)
    {
        this.app = app;

    }


    /**
    *  Check remainder for patient for every 5 minutes
    *  And alert Patient when it is to remind patient
    * */
    public void checkRemainder(String patientID)
    {
        executor = Executors.newSingleThreadScheduledExecutor();

        System.out.println("CHECKING REMAINDER FOR PATIENT: " + patientID);

        //call remainder database and check
        Runnable helloRunnable = new Runnable() {
            int count = 0;
            public void run() {
                //for testing
                count ++;
                System.out.println("Calling remainder");

                if(count == 5) //if have remainder
                {   System.out.println("executor stop");
                    executor.shutdown();

                    //WILL ALERT USER IN UI
                    app.AlertUser("Alert Alert, TIME TO TAKE MEDICINE !!");

                }

                System.out.println(count);

            }
        };
        executor.scheduleAtFixedRate(helloRunnable, 0, 3, TimeUnit.SECONDS);//every 5 minute
    }


    /**TODO:ADD FACIAL RECOGNITON API
     * Check if patient is who they say they are
     *
     * @param patientID
     * @param pathToPhoto - taken from User -
     * @return
     */
    public boolean ValidatePatient(String patientID, Path pathToPhoto)
    {

//        File finalFile = new File(getRealPathFromURI(tempUri));
//        Path path = Paths.get(pathToPhoto.getPath());
        new FacialRecognitionConfiguration(pathToPhoto).execute("");



        boolean isValidUser = true;
        //step1. verify patient through facial recogntion


        return isValidUser;
    }


    public Prescription getPrescription(String patientID)
    {
        return null;
    }

    public void openBox()
    {
        //open box
        ev3Box.GetRequest("out");
    }

    public void closeBox()
    {
        //close box
        ev3Box.GetRequest("in");
    }

//    public String getRealPathFromURI(Uri uri) {
//        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//        cursor.moveToFirst();
//        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//        return cursor.getString(idx);
//    }

}
