package bo;

import Model.User;
import Util.exeptions.ConnectionFailExeption;
import dao.UserDAO;

import java.sql.SQLException;

public class UserService {
    private final UserDAO userDAO = new UserDAO();

    public User login(String username, String password) throws ConnectionFailExeption, SQLException {
        return userDAO.findByUserNameAndPassword(username, password);
    }
}
