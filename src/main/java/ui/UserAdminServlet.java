package ui;

import Enums.Privilege;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/userAdmin")
public class UserAdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Privilege privlige = LoginServlet.validateSessionUser(req, resp);
        switch (privlige) {
            case null:
                return;
            case ADMIN:
                req.getRequestDispatcher("/userAdmin.jsp").forward(req, resp);
                break;
            default:
                req.setAttribute("error", "Insufficient privilege");
                req.getRequestDispatcher("/index.jsp").forward(req, resp);
                break;
        }
    }
}
