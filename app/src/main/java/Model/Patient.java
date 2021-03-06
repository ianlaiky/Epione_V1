package Model;

import android.os.AsyncTask;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Patient extends AsyncTask<String[], Void, List<Patient>> {

    int patientId;
    String name;
    String faceId;
    String language;

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFaceId() {
        return faceId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public Patient(int patientId, String name, String faceId, String language) {
        this.patientId = patientId;
        this.name = name;
        this.faceId = faceId;
        this.language = language;
    }

    public Patient() {
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

//        System.out.println(jsonArray);
//        System.out.println(jsonArray.get(0));
//
//        JsonObject jobj = (JsonObject) jsonArray.get(0);
//        System.out.println(jobj.get("patientId"));


//        client.newCall(requesthttp).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//                if (!response.isSuccessful()) {
//                    throw new IOException("Unexpected code " + response);
//                } else {
//
//
//
//                    // Show it.
////                    System.out.println(data);
//                    System.out.print(response);
//                    returnStatement[0] = "test";
//
//                }
//            }
//        });


    }


    public static void main(String[] args) {


        List<Patient> paList = Patient.getAllPatientDetails();
        for (int i = 0; i < paList.size(); i++) {
            System.out.println("FROM MAIN");
            System.out.println(paList.get(i).getName());
            System.out.println(paList.get(i).getPatientId());
            System.out.println(paList.get(i).getFaceId());
        }


    }

    public static List<Patient> getAllPatientDetails() {
        List<Patient> allpat = new ArrayList<>();
        JsonArray jsArr = getRequestBuilder("getAllPatient", "", "");

        for (int i = 0; i < jsArr.size(); i++) {
            JsonObject jobj = (JsonObject) jsArr.get(i);
            System.out.println(jobj);

            Patient paObj = new Patient(jobj.get("patientId").getAsInt(), jobj.get("name").getAsString(), jobj.get("faceId").getAsString(),jobj.get("language").getAsString());

            allpat.add(paObj);


        }
        return allpat;
    }

    public static List<Patient> getAllPatientDetailsById(String id) {
        List<Patient> allpat = new ArrayList<>();
        JsonArray jsArr = getRequestBuilder("getPatientThruID", "patientid", id.toString());

        for (int i = 0; i < jsArr.size(); i++) {
            JsonObject jobj = (JsonObject) jsArr.get(i);
            System.out.println(jobj);

            Patient paObj = new Patient(jobj.get("patientId").getAsInt(), jobj.get("name").getAsString(), jobj.get("faceId").getAsString(),jobj.get("languge").getAsString());

            allpat.add(paObj);


        }
        return allpat;
    }

    @Override
    protected List<Patient> doInBackground(String[]... strings) {


        System.out.println(strings[0][0]);
        System.out.println(strings[0][1]);


        if(strings[0][0].equalsIgnoreCase("getAllPatientDetails")){

            return getAllPatientDetails();
        }else if(strings[0][0].equalsIgnoreCase("getAllPatientDetailsById")){
            return getAllPatientDetailsById(strings[0][1].toString());

        }


        return null;
    }
}
