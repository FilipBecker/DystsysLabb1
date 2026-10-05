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

public class UserDAO{

    /*private UserDAO(int id, String username, Privilege role, String email) {
        super(id, username, role, email);
    }*/

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
                    case "CUSTOMER" -> Privilege.CUSTOMER;
                            default -> throw new SQLException("User has improper role: "+ resultSet.getString("role"));
                        };
                String email = resultSet.getString("email");
                return new User(id, username, role, email);
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
                    case "CUSTOMER" -> Privilege.CUSTOMER;
                    default -> throw new SQLException("User has improper role: "+ resultSet.getString("role"));
                };
                String email = resultSet.getString("email");
                users.add(new User(id, username, role, email));
            }
            return users;
        }
    }

    public static User getUserById(int id) throws ConnectionFailExeption, SQLException {
        Connection conn = DBConnection.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(
                "SELECT id, username, role, email FROM users WHERE id = ?"
        )){

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                String username = resultSet.getString("username");
                Privilege role = switch (resultSet.getString("role")) {
                    case "ADMIN" -> Privilege.ADMIN;
                    case "WAREHOUSE" -> Privilege.WAREHOUSE;
                    case "CUSTOMER" -> Privilege.CUSTOMER;
                    default -> throw new SQLException("User has improper role: "+ resultSet.getString("role"));
                };
                String email = resultSet.getString("email");
                return new User(id, username, role, email);
            }
            return null;
        }
    }

    public static User getUserByUsername(String username) throws ConnectionFailExeption, SQLException {
        Connection conn = DBConnection.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(
                "SELECT id, username, role, email FROM users WHERE username = ?"
        )){

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                int id = resultSet.getInt("id");
                Privilege role = switch (resultSet.getString("role")) {
                    case "ADMIN" -> Privilege.ADMIN;
                    case "WAREHOUSE" -> Privilege.WAREHOUSE;
                    case "CUSTOMER" -> Privilege.CUSTOMER;
                    default -> throw new SQLException("User has improper role: "+ resultSet.getString("role"));
                };
                String email = resultSet.getString("email");
                return new User(id, username, role, email);
            }
            return null;
        }
    }

    public static List<User> getUsersByRole(Privilege role) throws ConnectionFailExeption, SQLException {
        List<User> users = new ArrayList<>();

        Connection conn = DBConnection.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(
                "SELECT id, username, role, email FROM users WHERE role=?")) {
            statement.setString(1, role.name());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                users.add(new User(id, username, role, email));
            }
            return users;
        }
    }

    public static List<User> getUsersByEmail(String email) throws ConnectionFailExeption, SQLException {
        List<User> users = new ArrayList<>();

        Connection conn = DBConnection.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(
                "SELECT id, username, role, email FROM users WHERE email=?")) {
            statement.setString(1, email);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                Privilege role = switch (resultSet.getString("role")) {
                    case "ADMIN" -> Privilege.ADMIN;
                    case "WAREHOUSE" -> Privilege.WAREHOUSE;
                    case "CUSTOMER" -> Privilege.CUSTOMER;
                    default -> throw new SQLException("User has improper role: "+ resultSet.getString("role"));
                };
                users.add(new User(id, username, role, email));
            }
            return users;
        }
    }

    public static void add(String userName, String password, Privilege role, String email) throws ConnectionFailExeption, SQLException {
        Connection conn = DBConnection.getConnection();

        try (PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO users (username, password, role, email)" +
                            "VALUES(?, ?, ?, ?)")) {
            statement.setString(1, userName);
            statement.setString(2, password);
            statement.setString(3, role.name());
            statement.setString(4, email);

            statement.executeUpdate();
        }
    }

    public static void deleteById(int id) throws ConnectionFailExeption, SQLException {
        Connection conn = DBConnection.getConnection();

        try (PreparedStatement statement = conn.prepareStatement(
                "DELETE FROM users WHERE id=?")){
            statement.setInt(1, id);

            statement.executeUpdate();
        }
    }

    public static void deleteByUsername(String username) throws ConnectionFailExeption, SQLException {
        Connection conn = DBConnection.getConnection();

        try (PreparedStatement statement = conn.prepareStatement(
                "DELETE FROM users WHERE username=?")){
            statement.setString(1, username);

            statement.executeUpdate();
        }
    }
}
