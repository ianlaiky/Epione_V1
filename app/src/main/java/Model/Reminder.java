package Model;

import android.os.AsyncTask;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Language.English;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Reminder extends AsyncTask<String[],Void,List<Reminder>>{

    int reminderId;
    String dateTake;
    String timeTake;
    int patientId;
    int prescriptionId;
    String adhered;

    public Reminder() {
    }

    public Reminder(int reminderId, String dateTake, String timeTake, int patientId, int prescriptionId, String adhered) {
        this.reminderId = reminderId;
        this.dateTake = dateTake;
        this.timeTake = timeTake;
        this.patientId = patientId;
        this.prescriptionId = prescriptionId;
        this.adhered = adhered;
    }

    public int getReminderId() {
        return reminderId;
    }

    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    public String getDateTake() {
        return dateTake;
    }

    public void setDateTake(String dateTake) {
        this.dateTake = dateTake;
    }

    public String getTimeTake() {
        return timeTake;
    }

    public void setTimeTake(String timeTake) {
        this.timeTake = timeTake;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getAdhered() {
        return adhered;
    }

    public void setAdhered(String adhered) {
        this.adhered = adhered;
    }


    public static JsonArray getRequestBuilder(String queryMethod, String paramName, String param) {
//        final String[] returnStatement = new String[1];
        final OkHttpClient client = new OkHttpClient();


        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("epione-dialogflow.herokuapp.com")
                .port(80)
                .addPathSegment("database")
                .addPathSegment(queryMethod)
                .addQueryParameter(paramName, param)
                .build();

        System.out.println("Ev3Dev log url " + httpUrl.toString());


        Request requesthttp = new Request.Builder()
                .addHeader("accept", "application/json")
                .url(httpUrl)
                .build();

        Response responses = null;


        try {
            responses = client.newCall(requesthttp).execute();

//
        } catch (IOException e) {
            e.printStackTrace();
        }

        String jsonData = null;


        try {
            jsonData = responses.body().string();
            System.out.println("Sys call");
            System.out.println(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        }


        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = (JsonArray) jsonParser.parse(jsonData);
        return jsonArray;


    }

    public static JsonObject getRequestBuilderParameter2(String queryMethod, String paramName, String param, String paramName2, String param2) {
//        final String[] returnStatement = new String[1];
        final OkHttpClient client = new OkHttpClient();


        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("epione-dialogflow.herokuapp.com")
                .port(80)
                .addPathSegment("database")
                .addPathSegment(queryMethod)
                .addQueryParameter(paramName, param)
                .addQueryParameter(paramName2, param2)
                .build();

        System.out.println("Ev3Dev log url " + httpUrl.toString());


        Request requesthttp = new Request.Builder()
                .addHeader("accept", "application/json")
                .url(httpUrl)
                .build();

        Response responses = null;


        try {
            responses = client.newCall(requesthttp).execute();

//
        } catch (IOException e) {
            e.printStackTrace();
        }

        String jsonData = null;


        try {
            jsonData = responses.body().string();
            System.out.println("Sys call");
            System.out.println(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        }


        JsonParser jsonParser = new JsonParser();
        JsonObject jsonArray = (JsonObject) jsonParser.parse(jsonData);
        return jsonArray;


    }


    public static JsonObject getRequestBuilderParameterReminderInsert(String dateTake, String timeTake, String patientId, String prescriptionId, String adhered) {
//        final String[] returnStatement = new String[1];
        final OkHttpClient client = new OkHttpClient();


        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("epione-dialogflow.herokuapp.com")
                .port(80)
                .addPathSegment("database")
                .addPathSegment("insertDataIntoReminder")
                .addQueryParameter("dateTake", dateTake)
                .addQueryParameter("timeTake", timeTake)
                .addQueryParameter("patientId", patientId)
                .addQueryParameter("prescriptionId", prescriptionId)
                .addQueryParameter("adhered", adhered)
                .build();

        System.out.println("Ev3Dev log url " + httpUrl.toString());


        Request requesthttp = new Request.Builder()
                .addHeader("accept", "application/json")
                .url(httpUrl)
                .build();

        Response responses = null;


        try {
            responses = client.newCall(requesthttp).execute();

//
        } catch (IOException e) {
            e.printStackTrace();
        }

        String jsonData = null;


        try {
            jsonData = responses.body().string();
            System.out.println("Sys call");
            System.out.println(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        }


        JsonParser jsonParser = new JsonParser();
        JsonObject jsonArray = (JsonObject) jsonParser.parse(jsonData);
        return jsonArray;


    }


    public static List<Reminder> getAllReminder() {
        List<Reminder> allpat = new ArrayList<>();
        JsonArray jsArr = getRequestBuilder("getAllReminder", "", "");

        for (int i = 0; i < jsArr.size(); i++) {
            JsonObject jobj = (JsonObject) jsArr.get(i);
            System.out.println(jobj);

            Reminder obj = new Reminder(jobj.get("reminderId").getAsInt(), jobj.get("dateTake").getAsString(), jobj.get("timeTake").getAsString(), jobj.get("patientId").getAsInt(), jobj.get("prescriptionId").getAsInt(), jobj.get("adhered").getAsString());


            allpat.add(obj);


        }
        return allpat;
    }

    public static List<Reminder> getAllReminderById(String para) {
        List<Reminder> allpat = new ArrayList<>();
        JsonArray jsArr = getRequestBuilder("getAllReminderById", "reminderId", para);

        for (int i = 0; i < jsArr.size(); i++) {
            JsonObject jobj = (JsonObject) jsArr.get(i);
            System.out.println(jobj);

            Reminder obj = new Reminder(jobj.get("reminderId").getAsInt(), jobj.get("dateTake").getAsString(), jobj.get("timeTake").getAsString(), jobj.get("patientId").getAsInt(), jobj.get("prescriptionId").getAsInt(), jobj.get("adhered").getAsString());


            allpat.add(obj);


        }
        return allpat;
    }


    public static List<Reminder> getAllReminderByPatientId(String para) {
        List<Reminder> allpat = new ArrayList<>();
        JsonArray jsArr = getRequestBuilder("getAllReminderByPatientId", "patientId", para);

        for (int i = 0; i < jsArr.size(); i++) {
            JsonObject jobj = (JsonObject) jsArr.get(i);
            System.out.println(jobj);

            Reminder obj = new Reminder(jobj.get("reminderId").getAsInt(), jobj.get("dateTake").getAsString(), jobj.get("timeTake").getAsString(), jobj.get("patientId").getAsInt(), jobj.get("prescriptionId").getAsInt(), jobj.get("adhered").getAsString());


            allpat.add(obj);


        }
        return allpat;
    }

    public static List<Reminder> getAllReminderByDate(String para) {
        List<Reminder> allpat = new ArrayList<>();
        JsonArray jsArr = getRequestBuilder("getAllReminderByDate", "dateTake", para);

        for (int i = 0; i < jsArr.size(); i++) {
            JsonObject jobj = (JsonObject) jsArr.get(i);
            System.out.println(jobj);

            Reminder obj = new Reminder(jobj.get("reminderId").getAsInt(), jobj.get("dateTake").getAsString(), jobj.get("timeTake").getAsString(), jobj.get("patientId").getAsInt(), jobj.get("prescriptionId").getAsInt(), jobj.get("adhered").getAsString());


            allpat.add(obj);


        }
        return allpat;
    }


    public static String updateReminderPrescriptionTaken(String reminderId, String adherence) {

        JsonObject te = getRequestBuilderParameter2("updateReminderPrescriptionTaken", "setAdhered", adherence, "reminderId", reminderId);

        String ret = te.get("message").getAsString();
        return ret;

    }

    public static String insertDataIntoReminder(String patientId, String prescriptionId, int amtToTakeEachDay, int timesToTakeEachDay, int medicineQuantity) {

        int hrsInDay = 24;
        int initTime = 0;


        int hrsToAdd = hrsInDay / timesToTakeEachDay;
        int howManyTimesToLoop = medicineQuantity / amtToTakeEachDay;


        SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatterTime = new SimpleDateFormat("HHmm");

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, initTime);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);


        ArrayList<ArrayList<String>> dateTimeAll = new ArrayList<>();
        for (int i = 0; i < howManyTimesToLoop; i++) {
            ArrayList<String> dateTimeEach = new ArrayList<>();
            cal.add(Calendar.HOUR_OF_DAY, hrsToAdd);


            System.out.println(cal.getTime());
            System.out.println(formatterDate.format(cal.getTime()));
            System.out.println(formatterTime.format(cal.getTime()));

            dateTimeEach.add(formatterDate.format(cal.getTime()));
            dateTimeEach.add(formatterTime.format(cal.getTime()));

            dateTimeAll.add(dateTimeEach);
        }
        String ret = "Empty";
        for (int i = 0; i < dateTimeAll.size(); i++) {

            System.out.println(dateTimeAll.get(i).get(0));
            System.out.println(dateTimeAll.get(i).get(1));

            JsonObject te = getRequestBuilderParameterReminderInsert(dateTimeAll.get(i).get(0), dateTimeAll.get(i).get(1), patientId, prescriptionId, "false");

            ret = te.get("message").getAsString();
            System.out.println("------------");

        }


        return ret;

    }


    public static Reminder getNextFiveMinuteReminder(){




        Date cDate = new Date();
        SimpleDateFormat parserer = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println(parserer.format(cDate));




        List<Reminder> todaysReminder = getAllReminderByDate(parserer.format(cDate.getTime()));


// second

        Date timeNow = new Date();
        SimpleDateFormat parser = new SimpleDateFormat("HHmm");

        int cTime = Integer.parseInt(parser.format(timeNow.getTime()));



        System.out.println(cTime);


        for (int i = 0; i < todaysReminder.size(); i++) {
            int dbTime = Integer.parseInt(todaysReminder.get(i).getTimeTake());
             System.out.println(dbTime - cTime);



            if ((todaysReminder.get(i).getAdhered().toString().equalsIgnoreCase("false") )&& (dbTime - cTime <= 5)) {

                System.out.println("Reminder");
                System.out.println(todaysReminder.get(i).getReminderId());
                System.out.println(todaysReminder.get(i).getTimeTake());
                System.out.println(todaysReminder.get(i).getDateTake());
                System.out.println(todaysReminder.get(i).getPrescriptionId());
                System.out.println(todaysReminder.get(i).getPatientId());

                return todaysReminder.get(i);


            }
        }
        return null;





    }




    @Override
    protected List<Reminder> doInBackground(String[]... strings) {
        //System.out.println(strings[0][0]);
        //System.out.println(strings[0][1]);



        if(strings[0][0].equalsIgnoreCase("getAllReminder")){

            return getAllReminder();
        }else if(strings[0][0].equalsIgnoreCase("getAllReminderById")){
            return getAllReminderById(strings[0][1].toString());
        }else if(strings[0][0].equalsIgnoreCase("getAllReminderByPatientId")){
            return getAllReminderByPatientId(strings[0][1].toString());
        }else if(strings[0][0].equalsIgnoreCase("getAllReminderByDate")){
            return getAllReminderByDate(strings[0][1].toString());
        }else if(strings[0][0].equalsIgnoreCase("updateReminderPrescriptionTaken")){
            updateReminderPrescriptionTaken(strings[0][1].toString(),strings[0][2].toString());
        }else if(strings[0][0].equalsIgnoreCase("insertDataIntoReminder")){
            insertDataIntoReminder(strings[0][1].toString(),strings[0][2].toString(),Integer.parseInt(strings[0][3].toString()),Integer.parseInt(strings[0][4].toString()),Integer.parseInt(strings[0][5].toString()));
        }else if(strings[0][0].equalsIgnoreCase("getNextFiveMinuteReminder")){
            List<Reminder> temp = new ArrayList<>();
            temp.add(getNextFiveMinuteReminder());
            return temp;
        }
        return null;
    }




    public static void main(String[] args) {
        Reminder rem = getNextFiveMinuteReminder();

        if (rem==null){
            System.out.println("Empty");
        }else{
            System.out.println("----------------");
            System.out.println(rem.getPatientId());
            System.out.println("Pat Id");
            System.out.println(rem.getPrescriptionId());
            System.out.println("Pre ID");
            System.out.println(rem.getDateTake());
            System.out.println("Date take");
            System.out.println(rem.getTimeTake());
            System.out.println("Time take");
            System.out.println(rem.getReminderId());
            System.out.println("reminder id");
            System.out.println(rem.getAdhered());
            System.out.println("Adhered");
        }



//        insertDataIntoReminder("1", "1", 2, 2, 30);

//        List<Reminder> arr = getAllReminderByDate("11/6/2018");
//
//        for(int i=0;i<arr.size();i++){
//            System.out.println(arr.get(i).getReminderId());
//            System.out.println(arr.get(i).getDateTake());
//            System.out.println(arr.get(i).getTimeTake());
//            System.out.println(arr.get(i).getPatientId());
//            System.out.println(arr.get(i).getPrescriptionId());
//            System.out.println(arr.get(i).getAdhered());
//
//
//        }

//        System.out.println(insertDataIntoReminder());

        // time calculator

//        SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy");
//        Calendar cal = Calendar.getInstance();
//
//        System.out.println(formatterDate.format(cal.getTime()));
//
//        List<Reminder> todaysReminder = getAllReminderByDate(formatterDate.format(cal.getTime()).toString());
//        System.out.println(todaysReminder.size());
//
//
//        for (int i = 0; i < todaysReminder.size(); i++) {
//            System.out.println("---------------");
//
//
//            System.out.println(todaysReminder.get(i).getTimeTake());
//            System.out.println(todaysReminder.get(i).getAdhered());
//            System.out.println(todaysReminder.get(i).getPatientId());
//            System.out.println(todaysReminder.get(i).getPrescriptionId());
//            System.out.println(todaysReminder.get(i).getReminderId());
//            System.out.println(todaysReminder.get(i).getTimeTake());
//
//        }










    }


}
