package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import Util.exeptions.*;
import static java.sql.DriverManager.getConnection;

public class DBConnection {
    private static DBConnection instance = null;
    private Connection conn = null;

    private static DBConnection getInstance(){
        if(instance == null) instance = new DBConnection();
        return instance;
    }

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
