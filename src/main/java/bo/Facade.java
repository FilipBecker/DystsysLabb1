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

/**
 * Provides a simplified interface between the UI layer and the Data access layer
 *
 * The Facade is responsible for:
 *      Calling the appropriate DAO methods
 *      Converting Model objects into View objects
 *      Validating input before passing it to the DAO layer
 *      Handling application level operations
 * The UI Layer should communicate with the database through this class instead of accessing DAOs directly
 *
 */

public class Facade {

    public static List<ViewProduct> getAllProducts() throws SQLException {

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

    /**
     * Adds a product to a shopping cart
     *
     * Checks that the requested quantity does not exceed the available stock. If the product
     * already exists in the cart, its quantity is increased.
     *
     * @param cart the current shopping cart
     * @param id the ID of the product to add
     * @param quantity quantity to add
     * @return the updated shopping cart
     * @throws SQLException if the product can't be found or there isn't enough stock
     */
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

    /**
     * Adds a product to a shopping cart
     *
     *  If the product already exists in the cart, its quantity is increased.
     *  Otherwise, a new cart item is created.
     *
     * @param cart the current shopping cart
     * @param product the product to add
     * @param quantity the quantity to add
     * @return the updated shopping cart
     */
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

    /**
     * Calculates the total value of all items in a shopping cart
     * @param cart the shopping cart
     * @return the total value of the cart
     */
    public static double getTotal(List<ViewCartItem> cart){
        if(cart == null) return 0;
        return cart.stream().mapToDouble(ViewCartItem::getSubtotal).sum();
    }



    //This should never return null
    public static Privilege validateUser(String username, String password) throws NoSuchUserExeption, SQLException {
        return login(username, password).getRole();
    }

    public static User getUser(String username, String password)
            throws NoSuchUserExeption, SQLException {

        return login(username, password);
    }


    /**
     * Retrieves users according to a selected search criteria
     * @param searchType
     * @param searchValue
     * @return a list of matching {@link ViewUser} objects
     * @throws IllegalArgumentException if a database error occurs
     * @throws SQLException if the username or password is invalid
     */
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

    public static User login(String username, String password) throws ConnectionFailExeption, SQLException, NoSuchUserExeption {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) throw new NoSuchUserExeption("Invalid user name or password");
        User user = UserDAO.findByUserNameAndPassword(username, password);
        if (user == null) throw new NoSuchUserExeption("Invalid user name or password");
        return user;
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

    public static List<ViewCategory> getAllCategories() throws SQLException {

        List<ViewCategory> products = new ArrayList<>();

        for (Category category : CategoryDAO.findAll()) {
            products.add(new ViewCategory(category));
        }

        return products;
    }


    /**
     * Creates a new product category.
     *
     * @param name the name of the category
     * @throws SQLException if the category name is invalid or a database error occurs
     */
    public static void createCategory(String name) throws SQLException{
        if(name == null || name.trim().isEmpty()) throw new SQLException("Category name can't be empty");

        CategoryDAO.createCategory(name.trim());
    }

    /**
     * Updates an existing product category.
     *
     * @param id the ID of the category
     * @param name the new category name
     * @throws SQLException if the category name is invalid or a database error occurs
     */
    public static void updateCategory(int id, String name) throws SQLException{
        if(name == null || name.trim().isEmpty()) throw new SQLException("Category name can't be empty");

        CategoryDAO.updateCategory(name.trim(), id);
    }

    public static void createProduct(String name, String description, double price, int stock, int categoryId) throws SQLException{
        validateProduct(name, price, stock);

        ProductDAO.createProduct(name.trim(), description, price, stock, categoryId);

    }

    public static void updateProduct(int id, String name, String description, double price, int stock, int categoryId) throws SQLException{
        validateProduct(name, price, stock);

        ProductDAO.updateProduct(id, name, description, price, stock, categoryId);
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

    /**
     * Retrieves all orders that have the status NEW and are ready to be packed by warehouse staff.
     *
     * @return a list of orders waiting to be packed
     * @throws SQLException if a database error occurs
     */
    public static List<ViewOrder> getOrdersToPack()
            throws SQLException {

        List<ViewOrder> orders = new ArrayList<>();

        for (Order order : OrderDAO.findOrdersToPack()) {
            orders.add(new ViewOrder(order));
        }

        return orders;
    }

    /**
     * Retrieves all order lines belonging to an order.
     *
     * @param orderId the ID of the order
     * @return a list of {@link ViewOrderLine} objects
     * @throws SQLException if a database error occurs
     */
    public static List<ViewOrderLine> getOrderLines(int orderId)
            throws SQLException {

        List<ViewOrderLine> lines = new ArrayList<>();

        for (OrderLine line : OrderLineDAO.findOrderLines(orderId)) {
            lines.add(new ViewOrderLine(line));
        }

        return lines;
    }

    /**
     * Changes an order's status from NEW to PACKED.
     *
     * @param orderId the ID of the order to pack
     * @throws SQLException if the order cannot be packed
     *         or a database error occurs
     */
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


}
