package dao;

import Model.CartItem;
import Util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class OrderDAO {

    public static void createOrder(int userId, List<CartItem> cart) throws SQLException {
        if (cart == null || cart.isEmpty()) throw new SQLException("Cart is empty");

        Connection conn = DBConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            int orderId;

            try (PreparedStatement statement =
                         conn.prepareStatement("INSERT INTO orders (user_id) VALUES (?)",
                                 PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, userId);
                statement.executeUpdate();

                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (!keys.next()) throw new SQLException("Could not create order");

                    orderId = keys.getInt(1);
                }
            }

            for (CartItem item : cart) {
                int productId = item.getProduct().getId();
                int quantity = item.getQuantity();
                double price = item.getProduct().getPrice();

                try (PreparedStatement statement =
                             conn.prepareStatement("UPDATE products " +
                                     "SET stock = stock - ? " +
                                     "WHERE id = ? AND stock >= ?")) {
                    statement.setInt(1, quantity);
                    statement.setInt(2, productId);
                    statement.setInt(3, quantity);

                    int updatedRows = statement.executeUpdate();

                    if (updatedRows == 0) {
                        throw new SQLException("Not enough stock for product" + item.getProduct().getName());
                    }
                }

                try (PreparedStatement statement =
                             conn.prepareStatement("INSERT INTO order_lines " +
                                     "(order_id, product_id, quantity, price) " +
                                     "VALUES (?, ?, ?, ?)"
                             )) {
                    statement.setInt(1, orderId);
                    statement.setInt(2, productId);
                    statement.setInt(3, quantity);
                    statement.setDouble(4, price);

                    statement.executeUpdate();
                }
            }

            conn.commit();
        } catch(SQLException e){
            conn.rollback();
            throw e;
        } finally{
            conn.setAutoCommit(true);
        }

    }
}
