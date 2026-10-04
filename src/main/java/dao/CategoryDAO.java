package dao;

import Model.Category;
import Util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public static List<Category> findAll() throws SQLException {
        List<Category> categories = new ArrayList<>();

        Connection conn = DBConnection.getConnection();

        try (PreparedStatement statement = conn.prepareStatement("SELECT id, name FROM categories");
             ResultSet resultSet = statement.executeQuery()) {

            while(resultSet.next()) {
                Category category = new Category(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                );
                categories.add(category);
            }
        }

        return categories;
    }

    public static Category findById(int id) throws SQLException{
        Connection conn = DBConnection.getConnection();

        try(PreparedStatement statement = conn.prepareStatement("SELECT id, name FROM categories WHERE id = ?")){

            statement.setInt(1, id);

            try(ResultSet resultSet= statement.executeQuery()){
                if(resultSet.next()){
                    return new Category(
                            resultSet.getInt("id"),
                            resultSet.getString("name")
                    );
                }
            }

            return null;
        }
    }

    public static Category findByName(String name) throws SQLException{
        Connection conn = DBConnection.getConnection();

        try(PreparedStatement statement = conn.prepareStatement("SELECT id, name FROM categories WHERE name = ?")){

            statement.setString(1, name);

            try(ResultSet resultSet= statement.executeQuery()){
                if(resultSet.next()){
                    return new Category(
                            resultSet.getInt("id"),
                            resultSet.getString("name")
                    );
                }
            }

            return null;
        }
    }

    public static void createCategory(Category category) throws SQLException{
        Connection conn = DBConnection.getConnection();

        try(PreparedStatement statement = conn.prepareStatement("INSERT INTO categories (name) VALUES (?)")){
            statement.setString(1, category.getName());
            statement.executeUpdate();
        }
    }

    public static void updateCategory(Category category) throws SQLException {
        Connection conn = DBConnection.getConnection();

        try(PreparedStatement statement = conn.prepareStatement("UPDATE categories SET NAME = ? WHERE id = ?")){
            statement.setString(1, category.getName());
            statement.setInt(2, category.getId());

            statement.executeUpdate();
        }
    }
}
