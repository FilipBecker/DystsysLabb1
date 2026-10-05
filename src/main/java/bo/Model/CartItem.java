package bo.Model;

/**
 * Represents a type of product and its quantity in the users cart for the model.
 * **/
public class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the total price of the cart item
     */
    public double getSubtotal() {
        return product.getPrice() * quantity;
    }
}
