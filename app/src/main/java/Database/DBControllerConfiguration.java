package Database;




import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DBControllerConfiguration {

    /*DON'T CONNECT USING SCHOOL NETWORK*/
    public static final String HOSTNAME = "lai-family.dyndns.org/";
//    public static final String HOSTNAME = "192.168.1.137/";
    public static final String DATABASE = "xiaobai";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";


    public static void main(String[] args)
    {
        //DBController dbController = new DBController();
        //dbController.getConnection();
        getAllAppointments();
    }


    public static void getAllAppointments(){

        DBController db = new DBController();
        String query;
        PreparedStatement ps;


        try{
            db.getConnection();
            query = "select * from patient";
            ps = db.getPreparedStatement(query);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int apptid = rs.getInt("patientId");
                String name = rs.getString("name");
               System.out.println(apptid);
                System.out.println(name);

            }
            db.terminate();

        }catch(SQLException e){e.printStackTrace();}




    }
}
