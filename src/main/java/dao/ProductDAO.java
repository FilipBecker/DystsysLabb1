package dao;

import Model.Product;
import Util.DBConnection;
import Util.exeptions.ConnectionFailExeption;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO{

    /*public ProductDAO(int id, String name, String description, double price, int stock, int category) {
        super(id, name, description, price, stock, category);
    }*/

    public static List<Product> findAll() throws ConnectionFailExeption, SQLException{
        List<Product> products = new ArrayList<>();

        Connection conn = DBConnection.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(
                    "SELECT id, name, description, price, stock, category_id  FROM products");
            ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Product product = new Product(
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

    public static Product findById(int id) throws ConnectionFailExeption, SQLException{
        Connection conn = DBConnection.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(
                    "SELECT id, name, description, price, stock, category_id  FROM products WHERE id = ?"
            )){
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();


            if (resultSet.next()) {

                return new Product(
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

    public static List<Product> findByName(String name) throws ConnectionFailExeption, SQLException{
        List<Product> products = new ArrayList<>();

        Connection conn = DBConnection.getConnection();
        try(PreparedStatement statement = conn.prepareStatement(
                    "SELECT id, name, description, price, stock, category_id  FROM products WHERE name = ?"
            )){
            statement.setString(1, "%" + name + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Product product = new Product(
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


    public static void createProduct(Product  product) throws SQLException{
        Connection conn = DBConnection.getConnection();

        try(PreparedStatement statement = conn.prepareStatement("INSERT INTO products " +
                "(name, description, price, stock, category_id) " +
                "VALUES (?, ?, ?, ?, ?)")){
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getStock());
            statement.setInt(5, product.getCategoryId());

            statement.executeUpdate();
        }
    }

    public static void updateProduct(Product   product) throws SQLException {

        Connection conn = DBConnection.getConnection();

        try (PreparedStatement statement = conn.prepareStatement("UPDATE products SET " +
                "name = ?, " +
                "description = ?, " +
                "price = ?, " +
                "stock = ?, " +
                "category_id = ? " +
                "WHERE id = ?")) {

            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getStock());
            statement.setInt(5, product.getCategoryId());
            statement.setInt(6, product.getId());

            statement.executeUpdate();
        }
    }
}
