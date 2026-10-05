package bo;
/*
public class CartService {

    public static List<CartItem> addToCart(List<CartItem> cart, Product product, int quantity){
        if(cart == null){
            cart = new ArrayList<>();
        }


        Checking if the product already exists in the cart, if true then increase the quantity of that item

        for(CartItem item : cart){
            if(item.getProduct().getId() == product.getId()){
                item.setQuantity(item.getQuantity() + quantity);
                return cart;
            }
        }


        Else add the product to the cart

        cart.add(new CartItem(product, quantity));
        return cart;
    }

    public static double getTotal(List<CartItem> cart){
        if(cart == null) return 0;
        return cart.stream().mapToDouble(CartItem::getSubtotal).sum();
    }
}
*/