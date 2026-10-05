package ui.Serlvets;

import Enums.Privilege;
import bo.Facade;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ui.ViewItems.ViewUser;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Servlet responsible for user administration operations
 *
 * This servlet allows admin to view, search for, and add users.
 * Access is restricted to ADMIN users.
 */
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

    /**
     * Handles POST requests for adding a new user.
     *
     * The current user's session is validated first. Only administrators
     * are allowed to add users. User information is read from the request
     * and passed to {@link Facade}.
     *
     * If adding the user fails due to invalid input or a database error,
     * the error message is stored in the request before forwarding back to
     * {@code userAdmin.jsp}.
     *
     * @param req  the HTTP request containing the new user's information
     * @param resp the HTTP response used for forwarding to the appropriate page
     * @throws ServletException if an error occurs while forwarding the request
     * @throws IOException if an I/O error occurs during request processing
     */
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
