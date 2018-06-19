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






    public  MainActivity app;
    public ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private EV3Configuration ev3Box = new EV3Configuration();

    private Patient PatientExecutor = new Patient();
    private Reminder ReminderExecutor = new Reminder();
    private FacialRecognitionConfiguration FRExecutor = new FacialRecognitionConfiguration();

    public  boolean isValidUser;

    public EpioneController(MainActivity app)
    {
        this.app = app;

    }


    public  Patient getPatient(int patientID)
    {
        String pid = String.valueOf(patientID);

        try {


            List<Patient> pt = PatientExecutor.execute(new String[]{"getAllPatientDetailsById", pid}).get();
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
    public void checkRemainder()
    {
        executor = Executors.newSingleThreadScheduledExecutor();

        System.out.println("CHECKING REMAINDER FOR PATIENT:");

        //call remainder database and check
        Runnable helloRunnable = new Runnable() {
            int count = 0;
            public void run() {
                //for testing
               // count ++;
               // System.out.println("Calling remainder");
                try {
                    List<Reminder> reminders = ReminderExecutor.execute(new String[]{"getNextFiveMinuteReminder"}).get();
                    System.out.println("checking remainder");
                    if(reminders.get(0) != null) //if have remainder
                    {   System.out.println("executor stop");
                        executor.shutdown();

                        //WILL ALERT USER IN UI
                        //PASS PATIENT ID TO MAIN ACTIVITY
                        int patientId = reminders.get(0).getPatientId();
                        Patient pt = getPatient(patientId);
                        app.Reminder = reminders.get(0);
                        app.Patient = pt;

                        app.AlertUser("Alert Alert, TIME TO TAKE MEDICINE !! for patient : "+ patientId );
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }



                System.out.println(count);

            }
        };
        executor.scheduleAtFixedRate(helloRunnable, 0, 10, TimeUnit.SECONDS);//TODO: every 5 minute
    }


    /**TODO:ADD FACIAL RECOGNITON API
     * Check if patient is who they say they are
     *
     * @param pt - use patient  to get face id from patient
     * @param imageFile - taken from User camera -
     * @return isValidUser - to determine if user matches patient for authentication
     */
    public boolean ValidatePatient(Patient pt, File imageFile)
    {

//        File finalFile = new File(getRealPathFromURI(tempUri));
//        Path path = Paths.get(pathToPhoto.getPath());
//        new FacialRecognitionConfiguration(path).execute("");
        try {
            boolean isValid = FRExecutor.execute(imageFile,pt.getFaceId()).get();
            return isValidUser;

        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    public  void verifiedUser(){

        if(isValidUser){
            //verification succeed
            //app.AlertUser("Good day" + getPatient().getName() + ", You are verified");
            app.AlertUserAddOn("Please take the panadol in Box 1 and take 2 pills only");
            //add silence to pause the conversation
            app.AddPauseInTTS();
            medicalAherence();

        }else if (!isValidUser){
            //verification fails
            app.AlertUser("I can't recognise who you are");
        }
    }


    public  Prescription getPrescription(String patientID)
    {



        return null;
    }

    public  void openBox()
    {
        //open box
        app.AlertUserAddOn("Opening box 1");
        ev3Box.GetRequest("out");
    }

    public  void medicalAherence(){
        getPrescription("patientId");
        //getPatient();
        System.out.println("Epione open box");
        openBox();
    }

    public void closeBox()
    {
        try {
            //will check the past all the way and if the adherence is false
            // will check 5 min after as buffer
            ReminderExecutor.execute(new String[]{"updateReminderPrescriptionTaken","true"," place reminderid here"}).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //close box
        ev3Box.GetRequest("in");
    }

}
