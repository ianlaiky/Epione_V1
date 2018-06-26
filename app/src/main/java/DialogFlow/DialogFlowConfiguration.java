package DialogFlow;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import Database.DBController;

public class DialogFlowConfiguration {

    /* REFERRED TO DIALOGFLOW AGENT SETTINGS */
    public static final String DIALOGFLOW_CLIENT_ACCESS_TOKEN = "cb4c0d8c657847fb9be28a42855c3e32";
                                                               //cb4c0d8c657847fb9be28a42855c3e32
    //for testing
    public static void main(String[] args) {
        System.out.println("Hello World");


        System.out.print("sdf");

        System.out.println("wanying here");



        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

        Runnable run = new Runnable() {
            public void run() {
                System.out.println("test");
            }
        };

      ses.scheduleAtFixedRate(run,0,2,TimeUnit.SECONDS);

    //ses.schedule(run,1,TimeUnit.SECONDS);



    }

}