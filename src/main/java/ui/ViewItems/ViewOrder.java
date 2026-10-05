package ui.ViewItems;

import bo.Model.Order;

import java.sql.Timestamp;

/**
 * An Order Object represented in the UI as a ViewOrder Object instead of the model version
 */
public class ViewOrder {


    private int id;
    private int userId;
    private Timestamp orderDate;
    private String status;

    public ViewOrder() {
    }

    public ViewOrder(Order order) {
        id = order.getId();
        userId = order.getUserId();
        orderDate = order.getOrderDate();
        status = order.getStatus();
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public String getStatus() {
        return status;
    }
}
