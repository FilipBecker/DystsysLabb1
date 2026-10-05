package ui.Serlvets;

import Enums.Privilege;
import bo.Facade;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ui.ViewItems.ViewCategory;
import ui.ViewItems.ViewProduct;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet responsible for handling administartion functionality
 *
 * The servlet communicates with the business layer through {@link Facade}, not by accessing DAOs directly
 *
 * Both Administators and warehouse users are currently allowed.
 */
@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (!hasAdminAccess(req)) {
            req.setAttribute("error", "Insufficient privilege");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }

        try{

            List<ViewProduct> products = Facade.getAllProducts();
            List<ViewCategory> categories = Facade.getAllCategories();

            req.setAttribute("products", products);
            req.setAttribute("categories", categories);

            req.getRequestDispatcher("/admin.jsp").forward(req,resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    /**
     * The supported actions are:
     *     createProduct
     *     updateProduct
     *     createCategory
     *     updateCategory
     *
     * After a successful operation the user is redirected to the test-product page
     * @param req the HTTP request containing the submitted form data
     * @param resp the HTTP response
     * @throws ServletException if an error occurs while processing the request
     * @throws IOException if an input/output error occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (!hasAdminAccess(req)) {
            req.setAttribute("error", "Insufficient privilege");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }

        String action = req.getParameter("action");

        try{
            if("createProduct".equals(action)){
                String name = req.getParameter("name");
                String description = req.getParameter("description");
                double price = Double.parseDouble(req.getParameter("price"));
                int stock = Integer.parseInt(req.getParameter("stock"));
                int categoryId = Integer.parseInt(req.getParameter("categoryId"));

                Facade.createProduct(name, description, price, stock, categoryId);

            } else if ("updateProduct".equals(action)){

                int id = Integer.parseInt(req.getParameter("id"));
                String name = req.getParameter("name");
                String description = req.getParameter("description");
                double price = Double.parseDouble(req.getParameter("price"));
                int stock = Integer.parseInt(req.getParameter("stock"));
                int categoryId = Integer.parseInt(req.getParameter("categoryId"));

                Facade.updateProduct(id, name, description, price, stock, categoryId);

            } else if("createCategory".equals(action)){
                String name = req.getParameter("name");

                Facade.createCategory(name);
            } else if("updateCategory".equals(action)){
                int id = Integer.parseInt(req.getParameter("id"));
                String name = req.getParameter("name");

                Facade.updateCategory(id, name);
            }

            resp.sendRedirect(req.getContextPath() + "/test-product");

        } catch (SQLException | NumberFormatException e) {

            req.setAttribute("error", e.getMessage());

            doGet(req, resp);
        }
    }

    private boolean hasAdminAccess(HttpServletRequest req) {

        Privilege privilege =
                (Privilege) req.getSession().getAttribute("privilege");

        return privilege == Privilege.ADMIN
                || privilege == Privilege.WAREHOUSE;
    }
}
