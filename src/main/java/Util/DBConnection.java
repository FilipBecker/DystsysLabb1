package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import Util.exeptions.*;


/**
 * Manages the connection to the webshop MySQL database
 *
 * This class uses the singleton design pattern to ensure that only one {@link DBConnection}
 * instance is created during the lifetime of the application
 *
 * The database connection can be retrieved using {@link #getConnection()}
 */
public class DBConnection {
    private static DBConnection instance = null;
    private Connection conn = null;

    /**
     * @return the single DBConnection instance
     */
    private static DBConnection getInstance(){
        if(instance == null) instance = new DBConnection();
        return instance;
    }

    /**
     * @return the JDBC connection to the webshop database
     */
    public static Connection getConnection() {
        return getInstance().conn;
    }

    private static final String server = "jdbc:mysql://localhost:3306/webshop"
                                + "?useSSL=false"
                                + "&serverTimezone=UTC"
                                + "&allowPublicKeyRetrieval=true"
                                + "&characterEncoding=UTF-8";
    private static final String DB_user = "webshop";
    private static final String DB_password = "webshop123";

    /**
     * Creates a new database connection
     */
    private DBConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(server, DB_user, DB_password);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /*static{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC drivers are missing", e);
        }
    }*/

   /* public static Connection getConnection() throws ConnectionFailExeption{
        try{
            return DriverManager.getConnection(server, DB_user, DB_password);
        } catch (SQLException e){
            throw new ConnectionFailExeption("Could not connect to database" + e.getMessage());
        }
    }*/


}
