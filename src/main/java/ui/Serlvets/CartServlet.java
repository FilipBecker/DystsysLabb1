package ui.Serlvets;


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

/**
 * Serlvet responsible for handling the shopping cart
 *
 * the serlvet allowes the user to view their cart, add products, clear the cart and place orders
 *
 * The cart is stored in the users's HTTP session and
 * cart operations are handled through the {@link Facade}.
 *
 */
@WebServlet("/cart")
public class CartServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        List<ViewCartItem> cart = (List<ViewCartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }

        double total = Facade.getTotal(cart);

        req.setAttribute("cart", cart);
        req.setAttribute("total", total);
        req.getRequestDispatcher("/cart.jsp").forward(req, resp);
    }

    /**
     * The supported actions are:
     *     clear
     *     placeOrder
     *     Adding a product
     *
     * After a successful operation the user is redirected to the test-product page
     * @param req the HTTP request containing the cart action and submitted parameters
     * @param resp the HTTP response
     * @throws ServletException if a servlet-related error occurs
     * @throws IOException if an input/output error occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        List<ViewCartItem> cart = new ArrayList<>();

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

            cart = (List<ViewCartItem>) session.getAttribute("cart");

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
        cart = (List<ViewCartItem>) session.getAttribute("cart");
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
