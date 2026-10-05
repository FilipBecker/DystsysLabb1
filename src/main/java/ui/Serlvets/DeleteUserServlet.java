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

/**
 * Servlet responsible for deleting users.
 *
 * Users with the privilege ADMIN are allowed to delete users.
 * The deletion request is handled by {@link Facade}
 */
@WebServlet("/Users/Delete")
public class DeleteUserServlet extends HttpServlet {

    /**
     * Handles a request to delete a user.
     *
     * @param req the HTTP request containing the deletion parameters
     * @param resp the HTTP response
     * @throws ServletException if a servlet-related error occurs
     * @throws IOException if an input/output error occurs
     */
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
