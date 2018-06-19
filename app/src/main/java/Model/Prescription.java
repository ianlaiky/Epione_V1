package Model;

import android.os.AsyncTask;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Prescription extends AsyncTask<String[], Void, List<Prescription>> {


    int prescriptionId;
    int medId;
    int dosage;
    int instruction;
    int patientId;
    int quantity;
    String remarks;
    int precedenceOrder;

    public Prescription(int prescriptionId, int medId, int dosage, int instruction, int patientId, int quantity, String remarks, int precedenceOrder) {
        this.prescriptionId = prescriptionId;
        this.medId = medId;
        this.dosage = dosage;
        this.instruction = instruction;
        this.patientId = patientId;
        this.quantity = quantity;
        this.remarks = remarks;
        this.precedenceOrder = precedenceOrder;
    }

    public Prescription(){

    }

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public int getMedId() {
        return medId;
    }

    public void setMedId(int medId) {
        this.medId = medId;
    }

    public int getDosage() {
        return dosage;
    }

    public void setDosage(int dosage) {
        this.dosage = dosage;
    }

    public int getInstruction() {
        return instruction;
    }

    public void setInstruction(int instruction) {
        this.instruction = instruction;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getPrecedenceOrder() {
        return precedenceOrder;
    }

    public void setPrecedenceOrder(int precedenceOrder) {
        this.precedenceOrder = precedenceOrder;
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


    public static List<Prescription> getAllPrescription(){
        List<Prescription> allpat = new ArrayList<>();
        JsonArray jsArr = getRequestBuilder("getAllPrescription", "", "");

        for(int i =0;i<jsArr.size();i++){
            JsonObject jobj = (JsonObject) jsArr.get(i);
            System.out.println(jobj);

            Prescription obj = new Prescription(jobj.get("prescriptionId").getAsInt(),jobj.get("medId").getAsInt(),jobj.get("dosage").getAsInt(),jobj.get("instruction").getAsInt(),jobj.get("patientId").getAsInt(),jobj.get("quantity").getAsInt(),jobj.get("remarks").getAsString(),jobj.get("precedenceOrder").getAsInt());

            allpat.add(obj);


        }
        return allpat;
    }

    public static List<Prescription> getAllPrescriptionById(String par){
        List<Prescription> allpat = new ArrayList<>();
        JsonArray jsArr = getRequestBuilder("getAllPrescriptionById", "prescriptionId", par.toString());

        for(int i =0;i<jsArr.size();i++){
            JsonObject jobj = (JsonObject) jsArr.get(i);
            System.out.println(jobj);

            Prescription obj = new Prescription(jobj.get("prescriptionId").getAsInt(),jobj.get("medId").getAsInt(),jobj.get("dosage").getAsInt(),jobj.get("instruction").getAsInt(),jobj.get("patientId").getAsInt(),jobj.get("quantity").getAsInt(),jobj.get("remarks").getAsString(),jobj.get("precedenceOrder").getAsInt());

            allpat.add(obj);


        }
        return allpat;
    }

    public static List<Prescription> getAllPrescriptionByPatientId(String par){
        List<Prescription> allpat = new ArrayList<>();
        JsonArray jsArr = getRequestBuilder("getAllPrescriptionByPatientId", "patientId", par.toString());

        for(int i =0;i<jsArr.size();i++){
            JsonObject jobj = (JsonObject) jsArr.get(i);
            System.out.println(jobj);

            Prescription obj = new Prescription(jobj.get("prescriptionId").getAsInt(),jobj.get("medId").getAsInt(),jobj.get("dosage").getAsInt(),jobj.get("instruction").getAsInt(),jobj.get("patientId").getAsInt(),jobj.get("quantity").getAsInt(),jobj.get("remarks").getAsString(),jobj.get("precedenceOrder").getAsInt());

            allpat.add(obj);


        }
        return allpat;
    }

    public static List<Prescription> getAllPrescriptionByMedId(String par){
        List<Prescription> allpat = new ArrayList<>();
        JsonArray jsArr = getRequestBuilder("getAllPrescriptionByMedId", "medId", par.toString());

        for(int i =0;i<jsArr.size();i++){
            JsonObject jobj = (JsonObject) jsArr.get(i);
            System.out.println(jobj);

            Prescription obj = new Prescription(jobj.get("prescriptionId").getAsInt(),jobj.get("medId").getAsInt(),jobj.get("dosage").getAsInt(),jobj.get("instruction").getAsInt(),jobj.get("patientId").getAsInt(),jobj.get("quantity").getAsInt(),jobj.get("remarks").getAsString(),jobj.get("precedenceOrder").getAsInt());

            allpat.add(obj);


        }
        return allpat;
    }


    public static void main (String[]args){

        List<Prescription> preArr = getAllPrescriptionByMedId("2");

        for(int i = 0;i<preArr.size();i++){

            System.out.println(preArr.get(i).getPrescriptionId());
            System.out.println(preArr.get(i).getMedId());
            System.out.println(preArr.get(i).getDosage());
            System.out.println(preArr.get(i).getInstruction());
            System.out.println(preArr.get(i).getPatientId());
            System.out.println(preArr.get(i).getQuantity());
            System.out.println(preArr.get(i).getRemarks());
            System.out.println(preArr.get(i).getPrecedenceOrder());


        }

    }

    @Override
    protected List<Prescription> doInBackground(String[]... strings) {


        System.out.println(strings[0][0]);
        System.out.println(strings[0][1]);


        if(strings[0][0].equalsIgnoreCase("getAllPrescription")){

            return getAllPrescription();
        }else if(strings[0][0].equalsIgnoreCase("getAllPrescriptionById")){
            return getAllPrescriptionById(strings[0][1].toString());

        }else if(strings[0][0].equalsIgnoreCase("getAllPrescriptionByPatientId")){
            return getAllPrescriptionByPatientId(strings[0][1].toString());
        }else if(strings[0][0].equalsIgnoreCase("getAllPrescriptionByMedId")){

            return getAllPrescriptionByMedId(strings[0][1].toString());

        }


        return null;
    }



}
