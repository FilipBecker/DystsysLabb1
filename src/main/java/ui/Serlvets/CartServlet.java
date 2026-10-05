package ui.Serlvets;

import bo.Model.CartItem;
import bo.Facade;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ui.ViewItems.ViewCartItem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }

        List<ViewCartItem> viewCart = Facade.getCartView(cart);
        double total = Facade.getTotal(cart);

        req.setAttribute("cart", viewCart);
        req.setAttribute("total", total);
        req.getRequestDispatcher("/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        String action = req.getParameter("action");

        /*
        Clear the cart of all items
         */
        if ("clear".equals(action)) {
            session.removeAttribute("cart");

            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        /*
        Place order, if order is successful: remove cart
         */
        if("placeOrder".equals(action)){

            Integer userId = (Integer) session.getAttribute("userId");

            if (userId == null) {
                resp.sendRedirect(req.getContextPath() + "/login.jsp");
                return;
            }

            List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

            if(cart == null || cart.isEmpty()){
                session.setAttribute("error", "Your cart is empty");
                resp.sendRedirect(req.getContextPath() + "/cart");
                return;
            }

            try{
                Facade.createOrder(userId, cart);

                session.removeAttribute("cart");
                session.setAttribute("message", "Order placed successfully.");

                resp.sendRedirect(req.getContextPath() + "/cart");
                return;
            } catch (SQLException e){
                session.setAttribute("error", e.getMessage());
                resp.sendRedirect(req.getContextPath() + "/cart");
                return;
            }
        }


        /*
        Add product to cart
        */
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if(cart == null) cart = new ArrayList<>();

        try{
            int id = Integer.parseInt(req.getParameter("id"));
            int quantity = Integer.parseInt(req.getParameter("quantity"));

            cart = Facade.addToCart(cart, id, quantity);

            session.setAttribute("cart", cart);
        } catch (SQLException e) {
            session.setAttribute("error", e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/test-product");
    }
}
