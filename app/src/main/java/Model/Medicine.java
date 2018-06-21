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

public class Medicine extends AsyncTask<String[],Void,List<Medicine>>{

    int medId;
    String medName;
    String medType;
    String metricValue;

    public int getMedId() {
        return medId;
    }

    public void setMedId(int medId) {
        this.medId = medId;
    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public String getMedType() {
        return medType;
    }

    public void setMedType(String medType) {
        this.medType = medType;
    }

    public String getMetricValue() {
        return metricValue;
    }

    public void setMetricValue(String metricValue) {
        this.metricValue = metricValue;
    }

    public Medicine(){};

    public Medicine(int medId, String medName, String medType, String metricValue) {
        this.medId = medId;
        this.medName = medName;
        this.medType = medType;
        this.metricValue = metricValue;
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

    public static List<Medicine> getAllMedicineDetails(){
        List<Medicine> allMed = new ArrayList<>();

        JsonArray jsArr = getRequestBuilder("getAllMedicine","","");

        for(int i=0;i<jsArr.size();i++){
            JsonObject jobj = (JsonObject) jsArr.get(i);
            System.out.println(jobj);

            Medicine medObj = new Medicine(jobj.get("medId").getAsInt(),jobj.get("medName").getAsString(),jobj.get("medType").getAsString(),jobj.get("metricValue").getAsString());
            allMed.add(medObj);


        }

        return allMed;


    }

    public static List<Medicine> getAllMedicineDetailsByMedId(String medId){
        List<Medicine> allMed = new ArrayList<>();

        JsonArray jsArr = getRequestBuilder("getAllMedicineById","medId",medId);

        for(int i=0;i<jsArr.size();i++){
            JsonObject jobj = (JsonObject) jsArr.get(i);
            System.out.println(jobj);

            Medicine medObj = new Medicine(jobj.get("medId").getAsInt(),jobj.get("medName").getAsString(),jobj.get("medType").getAsString(),jobj.get("metricValue").getAsString());
            allMed.add(medObj);


        }

        return allMed;


    }




    public static void main(String[]args){

        List<Medicine> jsarr = getAllMedicineDetailsByMedId("2");

        for(int i=0;i<jsarr.size();i++){
            System.out.println(jsarr.get(i).getMedId());
            System.out.println(jsarr.get(i).getMedName());
            System.out.println(jsarr.get(i).getMedType());
            System.out.println(jsarr.get(i).getMetricValue());
            System.out.println("---------------");


        }


    }


    @Override
    protected List<Medicine> doInBackground(String[]... strings) {


//        System.out.println(strings[0][0]);
//        System.out.println(strings[0][0]);


        if(strings[0][0].equalsIgnoreCase("getAllMedicineDetails")){
            return getAllMedicineDetails();
        }else if(strings[0][0].equalsIgnoreCase("getAllMedicineDetailsByMedId")){
            System.out.println("MEDIDINEKNSJNHJDSHJH");
            return getAllMedicineDetailsByMedId(strings[0][1].toString());
        }


        return null;
    }
}
