package Hardware;


import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EV3Configuration {

    public void GetRequest(String param) {
        String returnStatement;
        final OkHttpClient client = new OkHttpClient();


        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("192.168.137.200")
                .port(8080)
                .addPathSegment("init")
                .addQueryParameter("variable", param)
                .build();
        Log.e("Ev3Dev log url", httpUrl.toString());


        Request requesthttp = new Request.Builder()
                .addHeader("accept", "application/json")
                .url(httpUrl)
                .build();


        client.newCall(requesthttp).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    // do something wih the result
                    Log.e("PRIN", "SUNCCESS");
                }
            }
        });


//        try (Response response = client.newCall(requesthttp).execute()) {
//            returnStatement = response.body().string();
//        } catch (IOException e) {
//            returnStatement = e.toString();
//            Log.e("ERROR",e.toString());
////            e.printStackTrace();
//        }
//
//        return returnStatement;
//


    }
}






