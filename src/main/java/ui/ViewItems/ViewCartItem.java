package ui.ViewItems;

/**
 * A cartItem represented Object in the UI as a ViewCartItem Object instead of the model version
 */
public class ViewCartItem {
    private final ViewProduct product;
    private final int quantity;

    public ViewCartItem(ViewProduct product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public ViewProduct getProduct() { return product; }
    public int getQuantity() { return quantity; }

    public double getSubtotal() {
        return product.getPrice() * quantity;
    }

    public String getName() { return product.getName(); }
    public double getPrice() { return product.getPrice(); }
}
