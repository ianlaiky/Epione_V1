package FacialRecognition;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FacialRecognitionConfiguration {


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient();
        //application/octet-stream
//        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//        RequestBody body = RequestBody.create(JSON, "{\"url\":\"https://i.imgur.com/9sAyPWE.jpg\"}");

//        File file = new File("D:\\AndroidStudio\\Epione_V1\\app\\src\\main\\assets\\images\\9sAyPWE.jpg");
        Path path = Paths.get("D:\\AndroidStudio\\Epione_V1\\app\\src\\main\\assets\\images\\9sAyPWE.jpg");

        byte[] bttest = Files.readAllBytes(path);


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
            System.out.println(response.body().string());
            // Do something with the response.
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
