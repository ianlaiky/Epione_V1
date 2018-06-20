package sg.gowild.sademo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.io.File;
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



    public  MainActivity app;

    private EV3Configuration ev3Box = new EV3Configuration();


    public  boolean isValidUser;

    public EpioneController(MainActivity app)
    {
        this.app = app;

    }


    public  Patient getPatient(int patientID)
    {
        String pid = String.valueOf(patientID);

        try {


            List<Patient> pt = new Patient().execute(new String[]{"getAllPatientDetailsById", pid}).get();
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
            @Override
            public void run() {
                //for testing
                count ++;
               // System.out.println("Calling remainder");
                try {
                    System.out.println("f me");
                    List<Reminder> reminders = new Reminder().execute(new String[]{"getNextFiveMinuteReminder"}).get();
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
                }catch (Exception e){
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
            boolean isValid = new FacialRecognitionConfiguration().execute(imageFile,pt.getFaceId()).get();
            return isValid;

        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }


    public  Prescription getPatientPrescription(String patientID)
    {
        try {


            List<Prescription> pt = new Prescription().execute(new String[]{"getAllPrescriptionByPatientId", patientID}).get();
            for (int i = 0; i < pt.size(); i++) {
                System.out.println(pt.get(i).getDosage());
                System.out.println(pt.get(i).getInstruction());
                System.out.println(pt.get(i).getRemarks());
                return pt.get(i);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    public  void openBox()
    {
        //open box
        app.AlertUserAddOn("Opening box 1");
        ev3Box.GetRequest("out");
    }
    

    public void closeBox(String remindID)
    {
        try {
            //will check the past all the way and if the adherence is false
            // will check 5 min after as buffer
            new Reminder().execute(new String[]{"updateReminderPrescriptionTaken",remindID,"true"}).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //close box
        ev3Box.GetRequest("in");
    }

}
