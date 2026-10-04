package ui;

import Enums.Privilege;
import Model.Category;
import Model.Product;
import bo.Facade;
import dao.CategoryDAO;
import dao.ProductDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (!hasAdminAccess(req)) {
            req.setAttribute("error", "Insufficient privilege");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }

        try{

            List<Product> products = ProductDAO.findAll();
            List<Category> categories = CategoryDAO.findAll();

            req.setAttribute("products", products);
            req.setAttribute("categories", categories);

            req.getRequestDispatcher("/admin.jsp").forward(req,resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

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
