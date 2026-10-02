package ui;

import Model.Product;
import Util.DBConnection;
import Util.exeptions.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/test-product")
public class ControllerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Product> products = new ArrayList<>();

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement st = conn.prepareStatement(
                    "SELECT id, name, description, price, stock FROM products"
            )){

            ResultSet rs = st.executeQuery();

            while(rs.next()){
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("price"));
                product.setStock(rs.getInt("stock"));
                products.add(product);
            }

        } catch (SQLException | ConnectionFailExeption e){
            e.printStackTrace();
            request.setAttribute("Error", "Database error: " + e.getMessage());
        }

        request.setAttribute("products", products);
        request.getRequestDispatcher("/test-product.jsp").forward(request, response);
    }
}
