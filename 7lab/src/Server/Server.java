package Server;

import Organization.Organization;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server{
    private static File file;
    public static Connection connection;

    public static Connection getConnection() {
        return connection;
    }

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASS = "root";
    public static Statement statement;
    public static Vector<Organization> collection=new Vector<Organization>();

    public static  int core=Runtime.getRuntime().availableProcessors();
    public static ExecutorService fixedThreadPool= Executors.newFixedThreadPool(core);
    public static File getFile() {
        return file;
    }
  //// java18 -jar name.jar 1431 /home/s423241/txes.txt
    public static void main(String[] args) {

        /*Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Exited!");
            }
        });

         */


        if(args.length!=1){
            System.out.println("Ожидался порт ");
            return;
        }
       
        if(isNumeric(args[0])==true){
            ConnectToTheDB();


            Thread thread=new Thread(new MultiThreadReading(Integer.parseInt(args[0])),"Test");
            thread.start();
            //new MultiThreadReading(Integer.parseInt(args[0])).MainServer();
        }else {
            System.out.println("Чо то тут не то");
            return;
        }

        //for(;;);
    }
    public static boolean isNumeric(String strNum) {
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    public static void ConnectToTheDB(){
        System.out.println("Testing connection to PostgreSQL JDBC");
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("PostgreSQL JDBC Driver successfully connected");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            statement = connection.createStatement();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
