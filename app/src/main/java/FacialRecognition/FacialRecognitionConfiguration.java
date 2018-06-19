package FacialRecognition;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import sg.gowild.sademo.EpioneController;

public class FacialRecognitionConfiguration extends AsyncTask<Object,Void,Boolean> {


    static Uri ImageURI;
    static String faceId;


    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void method(File file) throws IOException {
        //ImageURI = image;

        String detectFaceResult = "";

        OkHttpClient client = new OkHttpClient();
        //application/octet-stream
//        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//        RequestBody body = RequestBody.create(JSON, "{\"url\":\"https://i.imgur.com/9sAyPWE.jpg\"}");

      // File file = new File(p);
//        String path1 = image.getPath(); // "file:///mnt/sdcard/FileName.mp3"
//        File file = null;
//        try {
//            file = new File(new URI(path1));
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
        Path path = file.toPath();

        byte[]bttest = Files.readAllBytes(path);


        MediaType fdf = MediaType.parse("application/octet-stream");
        RequestBody body = RequestBody.create(fdf, bttest);






//        RequestBody formBody = new FormBody.Builder()
//                .add("url","https://i.imgur.com/9sAyPWE.jpg")
//                .build();
        Request request = new Request.Builder()
                .url("https://southeastasia.api.cognitive.microsoft.com/face/v1.0/detect")

                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Ocp-Apim-Subscription-Key","f697d347019b4c74976466006f62d811")
                .build();

        try {
            Response response = client.newCall(request).execute();
            detectFaceResult = response.body().string();
            System.out.println(detectFaceResult + "print");
            JSONArray jsonArray = new JSONArray(detectFaceResult);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObj = jsonArray.getJSONObject(i);
                faceId = jsonObj.getString("faceId");
                System.out.println(faceId + " FACE ID");

            }


            // Do something with the response.
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static boolean compareUser(String faceIdFromDB){
        String verifyUserResult = "";
        Boolean isIdentical = false;
        double confidenceLevel = 0;
        String faceIdDB = faceIdFromDB;
        String faceIdCamera = faceId;
        System.out.println(faceIdDB + " DB");
        System.out.println(faceIdCamera + " Camera");
//        System.out.println("{\"faceId1\":\"" + faceIdCamera +
//                "\",\"faceId12\":\"" + faceIdFromDB +
//                "\"}");

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType,
                "{\n" +
                        "    \"faceId1\": \"" + faceIdFromDB +  "\",\n" +
                        "    \"faceId2\": \"" + faceIdCamera +  "\"\n" +
                        "}\n"
                );

        Request request = new Request.Builder()
                .url("https://southeastasia.api.cognitive.microsoft.com/face/v1.0/verify")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Ocp-Apim-Subscription-Key","f697d347019b4c74976466006f62d811")
                .build();

        try {
            Response response = client.newCall(request).execute();
            verifyUserResult = response.body().string();
            System.out.println(verifyUserResult + " verify");

            JSONObject jsonObj = new JSONObject(verifyUserResult);

                isIdentical = jsonObj.getBoolean("isIdentical");
                System.out.println(isIdentical+ " Boolean isIdentical");
                confidenceLevel = jsonObj.getDouble("confidence");
                System.out.println(confidenceLevel+ " Confidence");

            // Do something with the response.
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //returns true if user are identical
        return isIdentical;
    }







    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void main (String[]args) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Path path = Paths.get("C:\\Users\\Ian\\AndroidStudioProjects\\Epione_V1\\app\\src\\main\\assets\\images\\9sAyPWE.jpg");

        byte[]bttest = Files.readAllBytes(path);


        MediaType fdf = MediaType.parse("application/octet-stream");
        RequestBody body = RequestBody.create(fdf, bttest);


        Request request = new Request.Builder()
                .url("https://southeastasia.api.cognitive.microsoft.com/face/v1.0/detect")

                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Ocp-Apim-Subscription-Key","f697d347019b4c74976466006f62d811")
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());
            // Do something with the response.
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected Boolean doInBackground(Object... objects) {

        File file = (File) objects[0];
        String faceIdFromDB = (String) objects[1];

        try {
            //facial detection
            method(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        //facial verification
        //returns the result after comparison of two faceId
        Boolean verificationResult = compareUser(faceIdFromDB);
        return verificationResult;
    }

    @Override
    protected void onPostExecute(Boolean verifiedSuccess) {
        //update verification status
        EpioneController.isValidUser = verifiedSuccess;
        //follow up method after verification
        EpioneController.verifiedUser();

    }
}