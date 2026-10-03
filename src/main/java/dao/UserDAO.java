package dao;
import Enums.Privlige;
import Model.User;
import Util.DBConnection;
import Util.exeptions.ConnectionFailExeption;

import java.awt.image.DataBufferDouble;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public static User findByUserNameAndPassword(String username, String password) throws ConnectionFailExeption, SQLException{
        Connection conn = DBConnection.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(
                    "SELECT id, username, role, email FROM users WHERE username = ? AND password = ?"
            )){

            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setRole(switch (resultSet.getString("role")) {
                    case "ADMIN" -> Privlige.ADMIN;
                    case "WAREHOUSE" -> Privlige.WAREHOUSE;
                    case "CUSTOMER" -> Privlige.COSTUMER;
                            default -> throw new SQLException("User has improper role: "+ resultSet.getString("role"));
                        }
                        );
                user.setEmail(resultSet.getString("email"));
                return user;
            }
            return null;
        }
    }
}
