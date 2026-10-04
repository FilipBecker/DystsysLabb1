package ui;

import Enums.Privilege;
import Exeptions.NoSuchUserExeption;
import Model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

import bo.Facade;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = req.getParameter("userName");
        String password = req.getParameter("password");

        User user = validateUser(req, resp, userName, password);
        if (user == null) return;

        Privilege privilege = validateUserPrivilege(req, resp, userName, password);

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

    private Privilege validateUserPrivilege(HttpServletRequest req, HttpServletResponse resp, String userName, String password) throws ServletException, IOException {
        try {
            return Facade.validateUser(userName, password);
        } catch (SQLException | NoSuchUserExeption e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
        return null;
    }

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
