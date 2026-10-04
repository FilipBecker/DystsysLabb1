package ui;

import Enums.Privilege;
import bo.Facade;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/Users")
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Privilege Privilege = LoginServlet.validateSessionUser(req, resp);
        switch (Privilege) {
            case null:
                return;
            case ADMIN:
                String searchType = req.getParameter("SearchType");
                String searchValue = req.getParameter("SearchValue");
                try {
                    List<ViewUser> users = new ArrayList<>(Facade.getUsers(searchType, searchValue));
                    req.setAttribute("users", users);
                } catch (IllegalArgumentException e) {
                    req.setAttribute("error", "Invalid Search type or search value");
                }
                catch (SQLException e) {
                    req.setAttribute("error", e.getMessage());
                }

                req.getRequestDispatcher("/userAdmin.jsp").forward(req, resp);
                break;
            default:
                req.setAttribute("error", "Insufficient privilege");
                req.getRequestDispatcher("/index.jsp").forward(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Privilege Privilege = LoginServlet.validateSessionUser(req, resp);
        switch (Privilege) {
            case null:
                return;
            case ADMIN:
                String userName = req.getParameter("userName");
                String password = req.getParameter("password");
                String role = req.getParameter("role");
                String email = req.getParameter("email");
                try {
                    Facade.addUser(userName, password, role, email);

                } catch (IllegalArgumentException | SQLException e) {
                    req.setAttribute("error", e.getMessage());
                }

                req.getRequestDispatcher("/userAdmin.jsp").forward(req, resp);
                break;
            default:
                req.setAttribute("error", "Insufficient privilege");
                req.getRequestDispatcher("/index.jsp").forward(req, resp);
                break;
        }
    }
}
