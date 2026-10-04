package dao;
import Enums.Privilege;
import Model.User;
import Util.DBConnection;
import Util.exeptions.ConnectionFailExeption;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends User{

    private UserDAO(int id, String username, Privilege role, String email) {
        super(id, username, role, email);
    }

    public static User findByUserNameAndPassword(String username, String password) throws ConnectionFailExeption, SQLException{
        Connection conn = DBConnection.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(
                    "SELECT id, username, role, email FROM users WHERE username = ? AND password = ?"
            )){

            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                int id = resultSet.getInt("id");
                Privilege role = switch (resultSet.getString("role")) {
                    case "ADMIN" -> Privilege.ADMIN;
                    case "WAREHOUSE" -> Privilege.WAREHOUSE;
                    case "CUSTOMER" -> Privilege.COSTUMER;
                            default -> throw new SQLException("User has improper role: "+ resultSet.getString("role"));
                        };
                String email = resultSet.getString("email");
                return new UserDAO(id, username, role, email);
            }
            return null;
        }
    }

    public static List<User> findAll() throws ConnectionFailExeption, SQLException {
        List<User> users = new ArrayList<>();

        Connection conn = DBConnection.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(
                "SELECT id, username, role, email FROM users");
            ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                Privilege role = switch (resultSet.getString("role")) {
                    case "ADMIN" -> Privilege.ADMIN;
                    case "WAREHOUSE" -> Privilege.WAREHOUSE;
                    case "CUSTOMER" -> Privilege.COSTUMER;
                    default -> throw new SQLException("User has improper role: "+ resultSet.getString("role"));
                };
                String email = resultSet.getString("email");
                users.add(new UserDAO(id, username, role, email));
            }
            return users;
        }
    }
}
