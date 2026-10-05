package bo;

import Enums.DeleteType;
import Enums.SearchType;
import Enums.Privilege;
import Exeptions.NoSuchUserExeption;
import bo.Model.*;
import Util.exeptions.ConnectionFailExeption;
import dao.OrderDAO;
import dao.ProductDAO;
import dao.UserDAO;
import ui.ViewItems.*;
import dao.*;
import bo.Model.User;
import dao.CategoryDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class Facade {
    /*public static ViewItem getItem() {
        TestItem testItem = new TestItem("Test", 100);
        return new ViewItem(testItem);
    }*/

    public static List<ViewProduct> getAllProducts() throws SQLException {
        //return toViewProductList(ProductDAO.findAll());

        List<ViewProduct> products = new ArrayList<>();

        for (Product product : ProductDAO.findAll()) {
            products.add(new ViewProduct(product));
        }

        return products;
    }

    public static ViewProduct getProductById(int id) throws  SQLException {
        Product product = ProductDAO.findById(id);
        if(product == null) return null;
        return new ViewProduct(product);
    }

    public static List<ViewProduct> getProductByName(String name) throws SQLException{
        //return toViewProductList(ProductDAO.findByName(name));

        List<ViewProduct> products = new ArrayList<>();

        for (Product product : ProductDAO.findByName(name)) {
            products.add(new ViewProduct(product));
        }

        return products;
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

    public static List<ViewCartItem> addToCart(List<ViewCartItem> cart, int id, int quantity) throws SQLException{
        Product product = ProductDAO.findById(id);

        if (product == null) {
            throw new SQLException("Product not found.");
        }

        int currentQuantity = 0;

        for (ViewCartItem item : cart) {
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

        return addToCart(cart, product, quantity);
    }


    /*public static double getTotal(List<CartItem> cart) {
        if (cart == null || cart.isEmpty()) {
            return 0;
        }
        return CartService.getTotal(cart);
    }*/

    //This should never return null
    public static Privilege validateUser(String username, String password) throws NoSuchUserExeption, SQLException {
        return login(username, password).getRole();
    }

    public static User getUser(String username, String password)
            throws NoSuchUserExeption, SQLException {

        return login(username, password);
    }

    public static List<ViewUser> getUsers(String searchType, String searchValue) throws IllegalArgumentException, SQLException {
        SearchType search;
        if (searchType != null) {
            search = SearchType.valueOf(searchType);
        } else {
            search = SearchType.ALL;
        }
        List<User> users = new ArrayList<>();
        User user;
        List<ViewUser> viewUsers = new ArrayList<>();

        switch (search) {
            case ALL:
                users.addAll(UserDAO.findAll());
                break;
            case ID:
                try {
                    int id = Integer.parseInt(searchValue);
                    user = UserDAO.getUserById(id);
                    if (user != null) users.add(user);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid Id");
                }
                break;
            case USERNAME:
                user = UserDAO.getUserByUsername(searchValue);
                if (user != null) users.add(user);
                break;
            case ROLE:
                users.addAll(UserDAO.getUsersByRole(Privilege.valueOf(searchValue)));
                break;
            case EMAIL:
                users.addAll(UserDAO.getUsersByEmail(searchValue));
                break;
        }
        for (User u: users) {
            viewUsers.add(new ViewUser(u));
        }
        return viewUsers;
    }
    public static List<ViewCategory> getAllCategories() throws SQLException {
        //return CategoryDAO.findAll();

        List<ViewCategory> products = new ArrayList<>();

        for (Category category : CategoryDAO.findAll()) {
            products.add(new ViewCategory(category));
        }

        return products;
    }

    /*public static ViewCategory getCategoryById(int id) throws SQLException {
        return CategoryDAO.findById(id);
    }

    public static ViewCategory getCategoryByName(String name) throws SQLException {
        return CategoryDAO.findByName(name);

    }*/

    public static void createCategory(String name) throws SQLException{
        if(name == null || name.trim().isEmpty()) throw new SQLException("Category name can't be empty");

        CategoryDAO category = new CategoryDAO();
        category.setName(name.trim());

        CategoryDAO.createCategory(category);
    }

    public static void updateCategory(int id, String name) throws SQLException{
        if(name == null || name.trim().isEmpty()) throw new SQLException("Category name can't be empty");

        CategoryDAO category = new CategoryDAO();
        category.setId(id);
        category.setName(name);

        CategoryDAO.updateCategory(category);
    }

    public static void createProduct(String name, String description, double price, int stock, int categoryId) throws SQLException{
        validateProduct(name, price, stock);

        ProductDAO product = new ProductDAO(0, name.trim(), description, price, stock, categoryId);

        ProductDAO.createProduct(product);

    }

    public static void updateProduct(int id, String name, String description, double price, int stock, int categoryId) throws SQLException{
        validateProduct(name, price, stock);

        ProductDAO product = new ProductDAO(
                id,
                name.trim(),
                description,
                price,
                stock,
                categoryId
        );

        ProductDAO.updateProduct(product);
    }

    public static void createOrder(int userId, List<ViewCartItem> cart) throws SQLException {
        List<CartItem> modelCart = new ArrayList<>();
        for (ViewCartItem v: cart) {
            modelCart.add(new CartItem(ProductDAO.findById(v.getProduct().getId()), v.getQuantity()));
        }
        OrderDAO.createOrder(userId, modelCart);
    }

    private static void validateProduct(String name, double price, int stock)
            throws SQLException {

        if (name == null || name.trim().isEmpty()) {
            throw new SQLException("Product name cannot be empty.");
        }

        if (price < 0) {
            throw new SQLException("Price cannot be negative.");
        }

        if (stock < 0) {
            throw new SQLException("Stock cannot be negative.");
        }
    }


    public static void addUser(String userName, String password, String role, String email) throws IllegalArgumentException, SQLException {
        if (userName == null || userName.isEmpty()) {
            throw new IllegalArgumentException("Invalid username: " +userName);
        } else if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Invalid password:" +password);
        } else if (role == null || role.isEmpty() || (!role.equals("WAREHOUSE") && !role.equals("CUSTOMER"))) {
            throw new IllegalArgumentException("Invalid role");
        }

        Privilege privilege = Privilege.valueOf(role);
        UserDAO.add(userName, password, privilege, email);

    }

    public static List<ViewOrder> getOrdersToPack()
            throws SQLException {

        List<ViewOrder> orders = new ArrayList<>();

        for (Order order : OrderDAO.findOrdersToPack()) {
            orders.add(new ViewOrder(order));
        }

        return orders;
    }

    public static List<ViewOrderLine> getOrderLines(int orderId)
            throws SQLException {

        List<ViewOrderLine> lines = new ArrayList<>();

        for (OrderLine line : OrderLineDAO.findOrderLines(orderId)) {
            lines.add(new ViewOrderLine(line));
        }

        return lines;
    }

    public static void packOrder(int orderId)
            throws SQLException {

        OrderDAO.packOrder(orderId);
    }

    public static ViewOrder getOrderById(int orderId)
            throws SQLException {

        Order order =
                OrderDAO.findById(orderId);

        if (order == null) {
            return null;
        }

        return new ViewOrder(order);
    }

    public static List<ViewOrder> getPackedOrders()
            throws SQLException {

        List<ViewOrder> orders = new ArrayList<>();

        for (Order order : OrderDAO.findPackedOrders()) {
            orders.add(new ViewOrder(order));
        }

        return orders;
    }

    public static void deleteUser(String deleteType, String deleteValue) throws IllegalArgumentException, SQLException, NoSuchUserExeption {
        if (deleteType == null || deleteType.isEmpty()) {
            throw new IllegalArgumentException("Select a parameter to delete by");
        } else if (deleteValue == null || deleteValue.isEmpty()) {
            throw new IllegalArgumentException("Give a value to delete by");
        }
        try {
            switch (DeleteType.valueOf(deleteType)) {
                case ID:
                    int id = Integer.parseInt(deleteValue);
                    User userById = UserDAO.getUserById(id);
                    if (userById == null) {
                        throw new NoSuchUserExeption("No user with given id");
                    } else if (userById.getRole() == Privilege.ADMIN) {
                        throw new NoSuchUserExeption("Can not delete an admin");
                    } else {
                        UserDAO.deleteById(Integer.parseInt(deleteValue));
                    }
                    break;
                case USERNAME:
                    User userByUsername = UserDAO.getUserByUsername(deleteValue);
                    if (userByUsername == null) {
                        throw new NoSuchUserExeption("No user with given id");
                    } else if (userByUsername.getRole() == Privilege.ADMIN) {
                        throw new NoSuchUserExeption("Can not delete an admin");
                    } else {
                        UserDAO.deleteByUsername(deleteValue);
                    }
                    break;
            }
        }catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid id value: "+deleteValue);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid deletion method");
        }
    }

    public static User login(String username, String password) throws ConnectionFailExeption, SQLException, NoSuchUserExeption {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) throw new NoSuchUserExeption("Invalid user name or password");
        User user = UserDAO.findByUserNameAndPassword(username, password);
        if (user == null) throw new NoSuchUserExeption("Invalid user name or password");
        return user;
    }

    public static List<ViewCartItem> addToCart(List<ViewCartItem> cart, Product product, int quantity){
        if(cart == null){
            cart = new ArrayList<>();
        }

        /*
        Checking if the product already exists in the cart, if true then increase the quantity of that item
         */
        for(ViewCartItem item : cart){
            if(item.getProduct().getId() == product.getId()){
                cart.remove(item);
                item = new ViewCartItem(item.getProduct(), item.getQuantity() + quantity);
                cart.add(item);
                return cart;
            }
        }



        /*
        Else add the product to the cart
         */
        cart.add(new ViewCartItem(new ViewProduct(product), quantity));
        return cart;
    }

    public static double getTotal(List<ViewCartItem> cart){
        if(cart == null) return 0;
        return cart.stream().mapToDouble(ViewCartItem::getSubtotal).sum();
    }

}
