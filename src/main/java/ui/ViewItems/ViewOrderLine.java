package ui.ViewItems;

import bo.Model.OrderLine;

/**
 * An OrderLine Object represented in the UI as a ViewOrderLine Object instead of the model version
 */
public class ViewOrderLine {

    private int id;
    private int orderId;
    private int productId;
    private int quantity;
    private double price;

    public ViewOrderLine(OrderLine orderLine) {
        id = orderLine.getId();
        orderId = orderLine.getOrderId();
        productId = orderLine.getProductId();
        quantity = orderLine.getQuantity();
        price = orderLine.getPrice();
    }

    public int getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}