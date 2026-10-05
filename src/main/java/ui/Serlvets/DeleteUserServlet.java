package ui.Serlvets;

import Enums.Privilege;
import Exeptions.NoSuchUserExeption;
import bo.Facade;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/Users/Delete")
public class DeleteUserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Privilege Privilege = LoginServlet.validateSessionUser(req, resp);
        switch (Privilege) {
            case null:
                return;
            case ADMIN:
                String deleteType = req.getParameter("deleteType");
                String deleteValue = req.getParameter("deleteValue");
                try {
                    Facade.deleteUser(deleteType, deleteValue);
                } catch (SQLException | IllegalArgumentException | NoSuchUserExeption e) {
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
