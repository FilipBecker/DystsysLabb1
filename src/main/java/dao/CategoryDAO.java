package dao;

import bo.Model.Category;
import Util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds the access methods for category in the database and creates Category objects
 */
public class CategoryDAO extends Category{

    /**
     * Creates a model Category object
     * @param id the category id
     * @param name the category name
     */
    private CategoryDAO(int id, String name) {
        super(id, name);
    }

    /**
     * Finds all categories in the database
     * @return a list of all categories
     * @throws SQLException when a problem with accessing the database happens
     */
    public static List<Category> findAll() throws SQLException {
        List<Category> categories = new ArrayList<>();

        Connection conn = DBConnection.getConnection();

        try (PreparedStatement statement = conn.prepareStatement("SELECT id, name FROM categories");
             ResultSet resultSet = statement.executeQuery()) {

            while(resultSet.next()) {
                Category category = new CategoryDAO(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                );
                categories.add(category);
            }
        }

        return categories;
    }

    /**
     * Finds the category a specific id
     * @param id to search by
     * @return the model object of the found category or null if it does not find it
     * @throws SQLException when a problem with accessing the database happens
     */
    public static Category findById(int id) throws SQLException{
        Connection conn = DBConnection.getConnection();

        try(PreparedStatement statement = conn.prepareStatement("SELECT id, name FROM categories WHERE id = ?")){

            statement.setInt(1, id);

            try(ResultSet resultSet= statement.executeQuery()){
                if(resultSet.next()){
                    return new CategoryDAO(
                            resultSet.getInt("id"),
                            resultSet.getString("name")
                    );
                }
            }

            return null;
        }
    }

    /**
     * Finds the category a specific name
     * @param name to search by
     * @return the model object of the found category or null if it does not find it
     * @throws SQLException when a problem with accessing the database happens
     */
    public static Category findByName(String name) throws SQLException{
        Connection conn = DBConnection.getConnection();

        try(PreparedStatement statement = conn.prepareStatement("SELECT id, name FROM categories WHERE name = ?")){

            statement.setString(1, name);

            try(ResultSet resultSet= statement.executeQuery()){
                if(resultSet.next()){
                    return new CategoryDAO(
                            resultSet.getInt("id"),
                            resultSet.getString("name")
                    );
                }
            }

            return null;
        }
    }

    /**
     * Creates a new category with the specified name
     * @param name of the new category
     * @throws SQLException when a problem with accessing the database happens
     */
    public static void createCategory(String name) throws SQLException{
        Connection conn = DBConnection.getConnection();

        try(PreparedStatement statement = conn.prepareStatement("INSERT INTO categories (name) VALUES (?)")){
            statement.setString(1, name);
            statement.executeUpdate();
        }
    }

    /**
     * Updates a category
     * @param name the new name of the category
     * @param id specifies which category to update
     * @throws SQLException when a problem with accessing the database happens
     */
    public static void updateCategory(String name, int id) throws SQLException {
        Connection conn = DBConnection.getConnection();

        try(PreparedStatement statement = conn.prepareStatement("UPDATE categories SET NAME = ? WHERE id = ?")){
            statement.setString(1, name);
            statement.setInt(2, id);

            statement.executeUpdate();
        }
    }
}
