package dao;

import bo.Model.Product;
import Util.DBConnection;
import Util.exeptions.ConnectionFailExeption;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds the access methods for products in the database and creates model Product objects
 */
public class ProductDAO extends Product{

    /**
     * Creates a new model Product object
     * @param id of the new Product object
     * @param name of the new Product object
     * @param description of the new Product object
     * @param price of the new Product object
     * @param stock of the new Product object
     * @param category of the new Product object
     */
    private ProductDAO(int id, String name, String description, double price, int stock, int category) {
        super(id, name, description, price, stock, category);
    }

    /**
     * Finds all products
     * @return A list of all products or an empty list if none are found
     * @throws ConnectionFailExeption If a problem with the connection to the database happens
     * @throws SQLException If a problem when accessing the database happens
     */
    public static List<Product> findAll() throws ConnectionFailExeption, SQLException{
        List<Product> products = new ArrayList<>();

        Connection conn = DBConnection.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(
                    "SELECT id, name, description, price, stock, category_id  FROM products");
            ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                ProductDAO product = new ProductDAO(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("stock"),
                        resultSet.getInt("category_id")
                );
                products.add(product);
            }
        }

        return products;
    }

    /**
     * Finds the product with the specified id
     * @param id of the product to be found
     * @return the found product or null if it can't be found
     * @throws ConnectionFailExeption If a problem with the connection to the database happens
     * @throws SQLException If a problem when accessing the database happens
     */
    public static Product findById(int id) throws ConnectionFailExeption, SQLException{
        Connection conn = DBConnection.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(
                    "SELECT id, name, description, price, stock, category_id  FROM products WHERE id = ?"
            )){
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();


            if (resultSet.next()) {

                return new ProductDAO(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("stock"),
                        resultSet.getInt("category_id")
                );
            }


            return null;
        }
    }

    /**
     * Finds all products with the specified name
     * @param name of the products to be found
     * @return A list of the found products or an empty list if none are to be found
     * @throws ConnectionFailExeption If a problem with the connection to the database happens
     * @throws SQLException If a problem when accessing the database happens
     */
    public static List<Product> findByName(String name) throws ConnectionFailExeption, SQLException{
        List<Product> products = new ArrayList<>();

        Connection conn = DBConnection.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(
                    "SELECT id, name, description, price, stock, category_id  FROM products WHERE name = ?"
            )){
            statement.setString(1, "%" + name + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                ProductDAO product = new ProductDAO(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("stock"),
                        resultSet.getInt("category_id")
                );

                products.add(product);
            }
        }
        return products;
    }

    /**
     * Creates a new product in the database
     * @param name of the new product
     * @param description of the new product
     * @param price of the new product
     * @param stock of the new product
     * @param categoryId of the new product
     * @throws SQLException If a problem when updating the database happens
     */
    public static void createProduct(String name, String description, double price, int stock, int categoryId) throws SQLException{
        Connection conn = DBConnection.getConnection();

        try(PreparedStatement statement = conn.prepareStatement("INSERT INTO products " +
                "(name, description, price, stock, category_id) " +
                "VALUES (?, ?, ?, ?, ?)")){
            statement.setString(1, name);
            statement.setString(2, description);
            statement.setDouble(3, price);
            statement.setInt(4, stock);
            statement.setInt(5, categoryId);

            statement.executeUpdate();
        }
    }

    /**
     * Updates an existing product in the database
     * @param id of the product to be updated
     * @param name new name of the product
     * @param description new description of the product
     * @param price new description of the product
     * @param stock new stock of the product
     * @param categoryId new categoryId of the product
     * @throws SQLException If a problem when updating the database happens
     */
    public static void updateProduct(int id, String name, String description, double price, int stock, int categoryId) throws SQLException {

        Connection conn = DBConnection.getConnection();

        try (PreparedStatement statement = conn.prepareStatement("UPDATE products SET " +
                "name = ?, " +
                "description = ?, " +
                "price = ?, " +
                "stock = ?, " +
                "category_id = ? " +
                "WHERE id = ?")) {

            statement.setString(1, name);
            statement.setString(2, description);
            statement.setDouble(3, price);
            statement.setInt(4, stock);
            statement.setInt(5, categoryId);
            statement.setInt(6, id);

            statement.executeUpdate();
        }
    }
}
