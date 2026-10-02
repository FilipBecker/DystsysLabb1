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

public class ProductDAO extends Product{

    private ProductDAO(int id, String name, String description, double price, int stock) {
        super(id, name, description, price, stock);
    }

    public static List<Product> findAll() throws ConnectionFailExeption, SQLException{
        List<Product> products = new ArrayList<>();

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT id, name, description, price, stock FROM products");
            ResultSet resultSet = statement.executeQuery()){

            while (resultSet.next()) {

                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                int stock = resultSet.getInt("stock");
                products.add(new ProductDAO(id, name, description, price, stock));
            }
        }
        return products;
    }

    public static Product findById(int id) throws ConnectionFailExeption, SQLException{
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT id, name, description, price, stock FROM products WHERE id = ?"
            )){
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                int stock = resultSet.getInt("stock");
                return new ProductDAO(id, name, description, price, stock);
            }
            return null;
        }
    }

    public static Product findByName(String name) throws ConnectionFailExeption, SQLException{
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT id, name, description, price, stock FROM products WHERE name = ?"
            )){
            statement.setString(2, name);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                int stock = resultSet.getInt("stock");
                return new ProductDAO(id, name, description, price, stock);
            }
            return null;
        }
    }
}
