package bo;

import Enums.DeleteType;
import Enums.SearchType;
import Enums.Privilege;
import Exeptions.NoSuchUserExeption;
import Model.CartItem;
import Model.Product;
import Model.User;
import Util.exeptions.ConnectionFailExeption;
import com.mysql.cj.jdbc.exceptions.NotUpdatable;
import dao.OrderDAO;
import dao.ProductDAO;
import dao.UserDAO;
import ui.ViewCartItem;
import ui.ViewItem;
import ui.ViewProduct;
import ui.ViewUser;
import Model.User;
import Model.Category;
import dao.CategoryDAO;

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
        SearchType search;
        if (searchType != null) {
            search = SearchType.valueOf(searchType);
        } else {
            search = SearchType.ALL;
        }
        List<User> users = new ArrayList<>();
        List<ViewUser> viewUsers = new ArrayList<>();

        switch (search) {
            case ALL: users.addAll(UserDAO.findAll());
        }
        for (User u: users) {
            viewUsers.add(new ViewUser(u));
        }
        return viewUsers;
    }
    public static List<Category> getAllCategories() throws SQLException {
        return CategoryDAO.findAll();
    }

    public static Category getCategoryById(int id) throws SQLException {
        return CategoryDAO.findById(id);
    }

    public static Category getCategoryByName(String name) throws SQLException {
        return CategoryDAO.findByName(name);
    }

    public static void createCategory(String name) throws SQLException{
        if(name == null || name.trim().isEmpty()) throw new SQLException("Category name can't be empty");

        Category category = new Category();
        category.setName(name.trim());

        CategoryDAO.createCategory(category);
    }

    public static void updateCategory(int id, String name) throws SQLException{
        if(name == null || name.trim().isEmpty()) throw new SQLException("Category name can't be empty");

        Category category = new Category();
        category.setId(id);
        category.setName(name);

        CategoryDAO.updateCategory(category);
    }

    public static void createProduct(String name, String description, double price, int stock, int categoryId) throws SQLException{
        validateProduct(name, price, stock);

        Product product = new Product(0, name.trim(), description, price, stock, categoryId);

        ProductDAO.createProduct(product);

    }

    public static void updateProduct(int id, String name, String description, double price, int stock, int categoryId) throws SQLException{
        validateProduct(name, price, stock);

        Product product = new Product(
                id,
                name.trim(),
                description,
                price,
                stock,
                categoryId
        );

        ProductDAO.updateProduct(product);
    }

    public static void createOrder(int userId, List<CartItem> cart) throws SQLException {

        OrderDAO.createOrder(userId, cart);
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
}
