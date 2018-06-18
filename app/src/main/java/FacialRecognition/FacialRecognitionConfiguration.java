package FacialRecognition;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import sg.gowild.sademo.MainActivity;

import android.content.Intent;
import android.content.IntentFilter;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class FacialRecognitionConfiguration extends AsyncTask<String, Void, String> {

    private Bitmap photo;


    public FacialRecognitionConfiguration(Bitmap photo){
        this.photo = photo;
    }


//    Bundle extras = getIntent().getExtras("picture");
//    byte[] byteArray = extras.getByteArray("picture");

//    @RequiresApi(api = Build.VERSION_CODES.O)
public  void postRequest() throws IOException {

        System.out.println("YOLOOO");
//    OkHttpClient client = new OkHttpClient();
//    //application/octet-stream
////        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
////        RequestBody body = RequestBody.create(JSON, "{\"url\":\"https://i.imgur.com/9sAyPWE.jpg\"}");
//
//    //        File file = new File("D:\\AndroidStudio\\Epione_V1\\app\\src\\main\\assets\\images\\9sAyPWE.jpg");
////    Path path = Paths.get("D:\\AndroidStudio\\Epione_V1\\app\\src\\main\\assets\\images\\9sAyPWE.jpg");
//
//    byte[] bttest = Files.readAllBytes(MainActivity.path2);
//
//
////        System.out.println(path + " TEST THE PATH");
//
//    MediaType fdf = MediaType.parse("application/octet-stream");
//    RequestBody body = RequestBody.create(fdf, bttest);
//
//
//    //        RequestBody formBody = new FormBody.Builder()
////                .add("url","https://i.imgur.com/9sAyPWE.jpg")
////                .build();
//    Request request = new Request.Builder()
//            .url("https://southeastasia.api.cognitive.microsoft.com/face/v1.0/detect")
//
//            .post(body)
//            .addHeader("Content-Type", "application/json")
//            .addHeader("Ocp-Apim-Subscription-Key", "f697d347019b4c74976466006f62d811")
//            .build();
//
//        try
//
//    {
//        Response response = client.newCall(request).execute();
//        System.out.println(response.body().string());
//        // Do something with the response.
//    } catch(
//    IOException e)
//
//    {
//        e.printStackTrace();
//    }

}

    @Override
    protected String doInBackground(String... strings) {

        String faceId = "";
        String nyan = "";



        OkHttpClient client = new OkHttpClient();
        //application/octet-stream
//        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//        RequestBody body = RequestBody.create(JSON, "{\"url\":\"https://i.imgur.com/9sAyPWE.jpg\"}");

        //        File file = new File("D:\\AndroidStudio\\Epione_V1\\app\\src\\main\\assets\\images\\9sAyPWE.jpg");
//    Path path = Paths.get("D:\\AndroidStudio\\Epione_V1\\app\\src\\main\\assets\\images\\9sAyPWE.jpg");

        byte[] bttest = new byte[0];
        try {
            bttest = Files.readAllBytes(MainActivity.path2);
        } catch (IOException e) {
            e.printStackTrace();
        }


//        System.out.println(path + " TEST THE PATH");

        MediaType fdf = MediaType.parse("application/octet-stream");
        RequestBody body = RequestBody.create(fdf, bttest);


        //        RequestBody formBody = new FormBody.Builder()
//                .add("url","https://i.imgur.com/9sAyPWE.jpg")
//                .build();
        Request request = new Request.Builder()
                .url("https://southeastasia.api.cognitive.microsoft.com/face/v1.0/detect")

                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Ocp-Apim-Subscription-Key", "f697d347019b4c74976466006f62d811")
                .build();


        try {

            Response response = client.newCall(request).execute();
            nyan = response.body().string();

            System.out.println(nyan + " NYAAAAAN DAFUQQQQ");

//            JSONObject Jobject = new JSONObject(nyan);
//            System.out.println(Jobject + " JOBJECTTTT");
//            JSONArray Jarray = Jobject.getJSONArray("faceId");
//            System.out.println(Jarray + " JARRAYYYYY");

//            JSONObject json = new JSONObject(nyan);
//            System.out.println(json + " JSONOBJECTTTT");

            JSONArray jsonArray = new JSONArray(nyan);
            System.out.println(jsonArray + " JSONARRAY SINGLE WITHOUT GET");
//            jsonArray.getJSONObject(0).getJSONObject("faceId");

            System.out.println(jsonArray.length() + " LENGTHHHH");

//
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObj = jsonArray.getJSONObject(i);

                 faceId = jsonObj.getString("faceId");
                System.out.println(faceId + " FACE ID");
//                String nr = jsonObj.getString("nr");
//                String name = jsonObj.getString("name");
//                jsonMap.put(ean, nr);  // here you put ean as key and nr as value
            }


//            System.out.println(jsonArray.get("faceId"))
//            JSONObject jsnobject = new JSONObject(nyan);
//            JSONArray jsonArray2 = jsonArray.getJSONArray(0);
//            for (int i = 0; i < jsonArray2.length(); i++) {
//                JSONObject explrObject = jsonArray2.getJSONObject(i);
//                System.out.println(explrObject + " JSONOBJECTTTT");
//            }
//            System.out.println(jsonArray + " JSONARRAY SINGLE WITHOUT GET");
//            System.out.println(jsonArray2 + " JSONARRAY");

//

//            System.out.println(response.body().string() +   for (int i = 0; i < Jarray.length(); i++) {
////                JSONObject object = Jarray.getJSONObject(i);
////            } " NANI DAFUQQ ");

//        JsonParser jsonParser = new JsonParser();
//        JsonArray jsonArray = null;

//            jsonArray = (JsonArray) nyan);
//           jsonArray = JSON.parse(nyan);

        System.out.println("TEST TEST");
//            result =  jsonArray.toString();
            // Do something with the response.
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(nyan + " RESULTTTTTTTTTTTTTT");
        return faceId;

    }

    @Override
    protected void onPostExecute(String result) {
//        MainActivity ma = new MainActivity();
//                ma.imageView.setImageBitmap(photo);
    }



//        public static void main(String[] args) throws IOException {
//    }
}
