package bo;

import Exeptions.NoSuchUserExeption;
import Model.User;
import Util.exeptions.ConnectionFailExeption;
import dao.UserDAO;

import java.sql.SQLException;

public class UserService {

    public static User login(String username, String password) throws ConnectionFailExeption, SQLException, NoSuchUserExeption {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) throw new NoSuchUserExeption("Invalid user name or password");
        User user = UserDAO.findByUserNameAndPassword(username, password);
        if (user == null) throw new NoSuchUserExeption("Invalid user name or password");
        return user;
    }
}
