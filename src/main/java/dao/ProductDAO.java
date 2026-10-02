package dao;

import Model.Product;
import Util.DBConnection;
import Util.exeptions.ConnectionCloseExeption;
import Util.exeptions.ConnectionFailExeption;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public static List<Product> findAll() throws ConnectionFailExeption, SQLException{
        List<Product> products = new ArrayList<>();

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT id, name, description, price, stock FROM products");
            ResultSet resultSet = statement.executeQuery()){

            while (resultSet.next()) {
                Product p = new Product();
                p.setId(resultSet.getInt("id"));
                p.setName(resultSet.getString("name"));
                p.setDescription(resultSet.getString("description"));
                p.setPrice(resultSet.getDouble("price"));
                p.setStock(resultSet.getInt("stock"));
                products.add(p);
            }
        }
        return products;
    }

    public Product findById(int id) throws ConnectionFailExeption, SQLException{
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT id, name, description, price, stock FROM products WHERE id = ?"
            )){
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Product p = new Product();
                p.setId(resultSet.getInt("id"));
                p.setName(resultSet.getString("name"));
                p.setDescription(resultSet.getString("description"));
                p.setPrice(resultSet.getDouble("price"));
                p.setStock(resultSet.getInt("stock"));
                return p;
            }
            return null;
        }
    }

    public Product findByName(String name) throws ConnectionFailExeption, SQLException{
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT id, name, description, price, stock FROM products WHERE name = ?"
            )){
            statement.setString(2, name);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Product p = new Product();
                p.setId(resultSet.getInt("id"));
                p.setName(resultSet.getString("name"));
                p.setDescription(resultSet.getString("description"));
                p.setPrice(resultSet.getDouble("price"));
                p.setStock(resultSet.getInt("stock"));
                return p;
            }
            return null;
        }
    }
}
