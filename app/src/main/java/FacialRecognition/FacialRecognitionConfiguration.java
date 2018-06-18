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

public class FacialRecognitionConfiguration extends AsyncTask<File,Void,Void> {


    static Uri ImageURI;


    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void method(File file) throws IOException {
        //ImageURI = image;

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
            System.out.println(response.body().string());
            // Do something with the response.
        } catch (IOException e) {
            e.printStackTrace();
        }

    }







    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void main (String[]args) throws IOException {
        OkHttpClient client = new OkHttpClient();
        //application/octet-stream
//        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//        RequestBody body = RequestBody.create(JSON, "{\"url\":\"https://i.imgur.com/9sAyPWE.jpg\"}");

//        File file = new File("D:\\AndroidStudio\\Epione_V1\\app\\src\\main\\assets\\images\\9sAyPWE.jpg");
        Path path = Paths.get("C:\\Users\\Ian\\AndroidStudioProjects\\Epione_V1\\app\\src\\main\\assets\\images\\9sAyPWE.jpg");

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
            System.out.println(response.body().string());
            // Do something with the response.
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected Void doInBackground(File... files) {

        System.out.println("GVMHBNJKHGJI");
        try {
            method(files[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}