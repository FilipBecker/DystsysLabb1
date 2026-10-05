package ui.ViewItems;

import bo.Model.Product;

public class ViewProduct {
    private int id;
    private String name;
    private String description;
    private double price;
    private int stock;

    public ViewProduct(Product product) {
        id = product.getId();
        name = product.getName();
        description = product.getDescription();
        price = product.getPrice();
        stock = product.getStock();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }
}
