package ui.Serlvets;

import Enums.Privilege;
import Exeptions.NoSuchUserExeption;
import bo.Model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

import bo.Facade;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet responsible for authenticating users
 *
 * The servlet validates the user's username and password through {@link Facade}.
 * After a successful login the user's ID, username, password and privilege are stored in the
 * HTTP session.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    /**
     * Handles a user login request
     * @param req the HTTP request containing the login credentials
     * @param resp the HTTP response
     * @throws ServletException if a servlet-related error occurs
     * @throws IOException if an input/output error occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = req.getParameter("userName");
        String password = req.getParameter("password");

        User user = validateUser(req, resp, userName, password);
        if (user == null) return;

        Privilege privilege = user.getRole();

        if (privilege == null) {
            return;
        }

        HttpSession session = req.getSession();

        session.setAttribute("userId", user.getId());
        session.setAttribute("userName", userName);
        session.setAttribute("password", password);
        session.setAttribute("privilege", privilege);


        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    /**
     * Validates the privilege of a user attempting to log in.
     *
     * @param req the HTTP request
     * @param resp the HTTP response
     * @param userName the username
     * @param password the password
     * @return the user's privilege, or {@code null} if authentication fails
     * @throws ServletException if forwarding fails
     * @throws IOException if an input/output error occurs
     */
    private Privilege validateUserPrivilege(HttpServletRequest req, HttpServletResponse resp, String userName, String password) throws ServletException, IOException {
        try {
            return Facade.validateUser(userName, password);
        } catch (SQLException | NoSuchUserExeption e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
        return null;
    }

    /**
     * Authenticates a user using the supplied credentials.
     *
     * @param req the HTTP request
     * @param resp the HTTP response
     * @param userName the username
     * @param password the password
     * @return the authenticated {@link User}, or {@code null}
     *         if authentication fails
     * @throws ServletException if forwarding fails
     * @throws IOException if an input/output error occurs
     */
    private User validateUser(HttpServletRequest req, HttpServletResponse resp, String userName, String password)
            throws ServletException, IOException {

        try {
            return Facade.getUser(userName, password);

        } catch (SQLException | NoSuchUserExeption e) {
            req.setAttribute("error", e.getMessage());
        }

        req.getRequestDispatcher("/login.jsp").forward(req, resp);
        return null;
    }

    /**
     * Validates the currently logged-in user's session.
     *
     * <p>The username and password stored in the session are
     * validated through the {@link Facade}.</p>
     *
     * @param req the HTTP request containing the user session
     * @param resp the HTTP response
     * @return the user's {@link Privilege}, or {@code null}
     *         if the session is invalid
     * @throws ServletException if forwarding fails
     * @throws IOException if an input/output error occurs
     */
    public static Privilege validateSessionUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        String userName = (String) session.getAttribute("userName");
        String password = (String) session.getAttribute("password");

        try {
            return Facade.validateUser(userName, password);
        } catch (SQLException | NoSuchUserExeption e) {
            req.setAttribute("error", e.getMessage());
        }

        req.getRequestDispatcher("/login.jsp").forward(req, resp);
        return null;
    }
}
