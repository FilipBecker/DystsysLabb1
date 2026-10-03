package ui;

import Enums.Privlige;
import Exeptions.NoSuchUserExeption;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import bo.Facade;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = req.getParameter("userName");
        String password = req.getParameter("password");
        HttpSession session = req.getSession();
        session.setAttribute("userName", userName);
        session.setAttribute("password", password);
        if (validateUser(req, resp) == null) return;
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    public static Privlige validateUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String userName = (String) session.getAttribute("userName");
        String password = (String) session.getAttribute("password");
        try {
            return Facade.validateUser(userName, password);
        } catch (NoSuchUserExeption e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
        return null;
    }
}
