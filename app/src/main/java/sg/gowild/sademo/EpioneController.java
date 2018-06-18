package sg.gowild.sademo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import FacialRecognition.FacialRecognitionConfiguration;
import Hardware.EV3Configuration;
import Model.Patient;
import Model.Prescription;
import Model.Reminder;

//interacts with UI,database and dialogflow
public class EpioneController {

     ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public MainActivity app;

    private EV3Configuration ev3Box = new EV3Configuration();

    public EpioneController(MainActivity app)
    {
        this.app = app;

    }


    public static Patient getPatient()
    {

        try {


            List<Patient> pt = new Patient().execute(new String[]{"getAllPatientDetailsById", "1"}).get();
            for (int i = 0; i < pt.size(); i++) {
                System.out.println("haahahahahhahahah");
                System.out.println(pt.get(i).getName());
                System.out.println(pt.get(i).getFaceId());
                System.out.println(pt.get(i).getPatientId());
                return pt.get(i);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
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
               // count ++;
               // System.out.println("Calling remainder");
                try {
                    List<Reminder> reminders = new Reminder().execute(new String[]{"getNextFiveMinuteReminder"}).get();
                    System.out.println("checking remainder");
                    if(reminders.get(0) != null) //if have remainder
                    {   System.out.println("executor stop");
                        executor.shutdown();

                        //WILL ALERT USER IN UI
                        app.AlertUser("Alert Alert, TIME TO TAKE MEDICINE !! for patient : "+ reminders.get(0).getPatientId());
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }



                System.out.println(count);

            }
        };
        executor.scheduleAtFixedRate(helloRunnable, 0, 10, TimeUnit.SECONDS);//every 5 minute
    }


    /**TODO:ADD FACIAL RECOGNITON API
     * Check if patient is who they say they are
     *
     * @param patientID
     * @param pathToPhoto - taken from User -
     * @return
     */
    public boolean ValidatePatient(String patientID, Uri pathToPhoto)
    {

//        File finalFile = new File(getRealPathFromURI(tempUri));
        Path path = Paths.get(pathToPhoto.getPath());
        new FacialRecognitionConfiguration(path).execute("");



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
        try {
            //will check the past all the way and if the adherence is false
            // will check 5 min after as buffer
            new Reminder().execute(new String[]{"updateReminderPrescriptionTaken","true"," place reminderid here"}).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

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
