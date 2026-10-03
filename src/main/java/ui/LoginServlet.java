package ui;

import Enums.Privlige;
import Exeptions.NoSuchUserExeption;
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
        if (validateUser(req, resp, userName, password) == null) return;

        HttpSession session = req.getSession();
        session.setAttribute("userName", userName);
        session.setAttribute("password", password);

        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    private Privlige validateUser(HttpServletRequest req, HttpServletResponse resp, String userName, String password) throws ServletException, IOException {
        try {
            return Facade.validateUser(userName, password);
        } catch (SQLException | NoSuchUserExeption e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
        return null;
    }

    public static Privlige validateSessionUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
