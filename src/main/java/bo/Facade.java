package bo;

import Enums.SearchType;
import Enums.Privilege;
import Exeptions.NoSuchUserExeption;
import Model.CartItem;
import Model.Product;
import Model.User;
import Util.exeptions.ConnectionFailExeption;
import dao.OrderDAO;
import dao.ProductDAO;
import dao.UserDAO;
import ui.ViewCartItem;
import ui.ViewItem;
import ui.ViewProduct;
import ui.ViewUser;
import Model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class Facade {
    public static ViewItem getItem() {
        TestItem testItem = new TestItem("Test", 100);
        return new ViewItem(testItem);
    }

    public static List<ViewProduct> getAllProducts() throws SQLException {
        return toViewProductList(ProductDAO.findAll());

    }

    public static ViewProduct getProductById(int id) throws  SQLException {
        Product product = ProductDAO.findById(id);
        if(product == null) return null;
        return new ViewProduct(product);
    }

    public static List<ViewProduct> getProductByName(String name) throws SQLException{
        return toViewProductList(ProductDAO.findByName(name));
    }

    private static List<ViewProduct> toViewProductList(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return null;
        }

        List<ViewProduct> viewProducts = new ArrayList<>();
        for (Product product : products) {
            viewProducts.add(new ViewProduct(product));
        }
        return viewProducts;
    }

    public static List<ViewCartItem> getCartView(List<CartItem> cart){
        if(cart == null ||cart.isEmpty()) return new ArrayList<>();

        List<ViewCartItem> viewItems = new ArrayList<>();
        for(CartItem item : cart){
            ViewProduct viewProduct = new ViewProduct(item.getProduct());
            viewItems.add(new ViewCartItem(viewProduct, item.getQuantity()));
        }

        return viewItems;
    }

    public static List<CartItem> addToCart(List<CartItem> cart, int id, int quantity) throws SQLException{
        Product product = ProductDAO.findById(id);

        if (product == null) {
            throw new SQLException("Product not found.");
        }

        int currentQuantity = 0;

        for (CartItem item : cart) {
            if (item.getProduct().getId() == product.getId()) {
                currentQuantity = item.getQuantity();
                break;
            }
        }

        if (currentQuantity + quantity > product.getStock()) {
            throw new SQLException(
                    "Not enough stock. Only "
                            + (product.getStock() - currentQuantity)
                            + " more available."
            );
        }

        return CartService.addToCart(cart, product, quantity);
    }


    public static double getTotal(List<CartItem> cart) {
        if (cart == null || cart.isEmpty()) {
            return 0;
        }
        return CartService.getTotal(cart);
    }

    //This should never return null
    public static Privilege validateUser(String username, String password) throws NoSuchUserExeption, SQLException {
        return UserService.login(username, password).getRole();
    }

    public static User getUser(String username, String password)
            throws NoSuchUserExeption, SQLException {

        return UserService.login(username, password);
    }

    public static void placeOrder(int userId, List<CartItem> cart)
            throws SQLException {

        OrderDAO.createOrder(userId, cart);
    }

    public static List<ViewUser> getUsers(String searchType, String searchValue) throws IllegalArgumentException, SQLException {
        List<User> users = new ArrayList<>();
        List<ViewUser> viewUsers = new ArrayList<>();

        switch (SearchType.valueOf(searchType)) {
            case ALL: users.addAll(UserDAO.findAll());
        }
        for (User u: users) {
            viewUsers.add(new ViewUser(u));
        }
        return viewUsers;
    }
}
