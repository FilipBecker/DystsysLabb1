package ui.Serlvets;

import bo.Facade;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ui.ViewItems.ViewProduct;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/test-product")
public class ControllerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<ViewProduct> products = Facade.getAllProducts();
            request.setAttribute("products", products);
        } catch (SQLException e) {
            request.setAttribute("error", e.getMessage());
        }
        request.getRequestDispatcher("/test-product.jsp").forward(request, response);
    }
}

