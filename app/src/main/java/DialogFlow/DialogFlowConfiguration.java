package DialogFlow;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Database.DBController;

public class DialogFlowConfiguration {

    /* REFERRED TO DIALOGFLOW AGENT SETTINGS */
    public static final String DIALOGFLOW_CLIENT_ACCESS_TOKEN = "cb4c0d8c657847fb9be28a42855c3e32";

    //for testing
    public static void main(String[] args) {

//        Runnable helloRunnable = new Runnable() {
//            public void run() {
//                System.out.println("Hello world");
//            }
//        };
//
//        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
//        executor.scheduleAtFixedRate(helloRunnable, 0, 3, TimeUnit.SECONDS);
    }
}
