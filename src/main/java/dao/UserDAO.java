package dao;
import Enums.Privilege;
import bo.Model.User;
import Util.DBConnection;
import Util.exeptions.ConnectionFailExeption;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds the access methods for users in the database and creates model User objects
 */
public class UserDAO extends User{

    /**
     * Creates a new model User object
     * @param id of the new User object
     * @param username of the new User object
     * @param role of the new User object
     * @param email of the new User object
     */
    private UserDAO(int id, String username, Privilege role, String email) {
        super(id, username, role, email);
    }

    /**
     * Finds the user with the specified username and password
     * @param username of the user to be found
     * @param password of the user to be found
     * @return the found user or null if it can't be found
     * @throws ConnectionFailExeption If a problem with the connection to the database happens
     * @throws SQLException If a problem when accessing the database happens
     */
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
                return new UserDAO(id, username, role, email);
            }
            return null;
        }
    }

    /**
     * Finds all users in the database
     * @return A list of all users or an empty list if none can be found
     * @throws ConnectionFailExeption If a problem with the connection to the database happens
     * @throws SQLException If a problem when accessing the database happens
     */
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
                users.add(new UserDAO(id, username, role, email));
            }
            return users;
        }
    }

    /**
     * Finds the user with the specified id
     * @param id of the user to be found
     * @return the found user or null if it can't be found
     * @throws ConnectionFailExeption If a problem with the connection to the database happens
     * @throws SQLException If a problem when accessing the database happens
     */
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
                return new UserDAO(id, username, role, email);
            }
            return null;
        }
    }

    /**
     * Finds the user with the specified username
     * @param username of the user to be found
     * @return the found user or null if it can't be found
     * @throws ConnectionFailExeption If a problem with the connection to the database happens
     * @throws SQLException If a problem when accessing the database happens
     */
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
                return new UserDAO(id, username, role, email);
            }
            return null;
        }
    }

    /**
     * Finds all users with the specified role
     * @param role of the users to be found
     * @return A list with all found users or an empty list if none can be found
     * @throws ConnectionFailExeption If a problem with the connection to the database happens
     * @throws SQLException If a problem when accessing the database happens
     */
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
                users.add(new UserDAO(id, username, role, email));
            }
            return users;
        }
    }

    /**
     * Finds all users with the specified email
     * @param email of the users to be found
     * @return A list of all found users or an empty list if none are to be found
     * @throws ConnectionFailExeption If a problem with the connection to the database happens
     * @throws SQLException If a problem when accessing the database happens
     */
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
                users.add(new UserDAO(id, username, role, email));
            }
            return users;
        }
    }

    /**
     * Creates a new user in the database
     * @param userName of the new user
     * @param password of the new user
     * @param role of the new user
     * @param email of the new user
     * @throws ConnectionFailExeption If a problem with the connection to the database happens
     * @throws SQLException If a problem when accessing the database happens
     */
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

    /**
     * Removes the user with the specified id from the database
     * @param id of the user to be removed
     * @throws ConnectionFailExeption If a problem with the connection to the database happens
     * @throws SQLException If a problem when accessing the database happens
     */
    public static void deleteById(int id) throws ConnectionFailExeption, SQLException {
        Connection conn = DBConnection.getConnection();

        try (PreparedStatement statement = conn.prepareStatement(
                "DELETE FROM users WHERE id=?")){
            statement.setInt(1, id);

            statement.executeUpdate();
        }
    }

    /**
     * Removes the user with the specified username from the database
     * @param username of the user to be deleted
     * @throws ConnectionFailExeption If a problem with the connection to the database happens
     * @throws SQLException If a problem when accessing the database happens
     */
    public static void deleteByUsername(String username) throws ConnectionFailExeption, SQLException {
        Connection conn = DBConnection.getConnection();

        try (PreparedStatement statement = conn.prepareStatement(
                "DELETE FROM users WHERE username=?")){
            statement.setString(1, username);

            statement.executeUpdate();
        }
    }
}
