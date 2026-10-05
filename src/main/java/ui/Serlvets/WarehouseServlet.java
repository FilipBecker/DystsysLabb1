package ui.Serlvets;

import Enums.Privilege;

import bo.Facade;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ui.ViewItems.ViewOrder;
import ui.ViewItems.ViewOrderLine;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet responsible for warehouse order management
 *
 * Warehouse staff and admin can use this servlet to view orders that need to be packed,
 * view individual order details, and mark orders as packed
 *
 * Order operations are delegated to the {@link Facade}
 */
@WebServlet("/warehouse")
public class WarehouseServlet extends HttpServlet{

    private boolean hasWarehouseAccess(HttpServletRequest req){
        Privilege privilege = (Privilege) req.getSession().getAttribute("privilege");

        return privilege == Privilege.ADMIN || privilege == Privilege.WAREHOUSE;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!hasWarehouseAccess(req)){
            req.setAttribute("error", "Insufficient privilege");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }

        try{
            String orderIdParameter = req.getParameter("orderId");

            if(orderIdParameter != null){
                int orderId = Integer.parseInt(orderIdParameter);

                ViewOrder order = Facade.getOrderById(orderId);

                List<ViewOrderLine> lines = Facade.getOrderLines(orderId);

                req.setAttribute("order", order);
                req.setAttribute("lines", lines);

                req.getRequestDispatcher("/warehouse-order.jsp").forward(req,resp);

                return;
            }

            List<ViewOrder> orders = Facade.getOrdersToPack();
            List<ViewOrder> packedOrders = Facade.getPackedOrders();

            req.setAttribute("orders", orders);
            req.setAttribute("packedOrders", packedOrders);

            req.getRequestDispatcher("/warehouse.jsp")
                    .forward(req, resp);

        } catch (SQLException |
                 NumberFormatException e) {

            throw new ServletException(e);
        }
    }

    /**
     * Handles POST requests for warehouse actions.
     *
     * Currently, the supported action is pack, which marks
     * the specified order as packed through the {@link Facade}.
     *
     * After the action has been completed, the user is redirected
     * back to the warehouse page.
     *
     * @param req  the HTTP request containing the action and order ID
     * @param resp the HTTP response used for redirecting back to the warehouse
     * @throws ServletException if an error occurs while processing the request
     *                           or accessing the database
     * @throws IOException if an I/O error occurs during request processing
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!hasWarehouseAccess(req)){
            req.setAttribute("error", "Insufficient privilege");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }

        String action = req.getParameter("action");
        try{
            if("pack".equals(action)){

                int orderId = Integer.parseInt(req.getParameter("orderId"));

                Facade.packOrder(orderId);
            }

            resp.sendRedirect(req.getContextPath() + "/warehouse");
        } catch (SQLException |
                 NumberFormatException e) {

            throw new ServletException(e);
        }
    }
}
