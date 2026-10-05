package dao;

import bo.Model.OrderLine;
import Util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds the access methods for orderLine in the database and creates model OrderLine objects
 */
public class OrderLineDAO extends OrderLine {
    /**
     * Creates a new model OrderLine object
     * @param id of the new OrderLine object
     * @param orderId of the new OrderLine object
     * @param productId of the new OrderLine object
     * @param quantity of the new OrderLine object
     * @param price of the new OrderLine object
     */
    private OrderLineDAO(int id, int orderId, int productId, int quantity, double price) {
        super(id, orderId, productId, quantity, price);
    }

    /**
     * Finds all orderLines associated with the specified order
     * @param orderId of the order
     * @return A list of OrderLine objects or an empty list if none are found
     * @throws SQLException when a problem with accessing the database happens
     */
    public static List<OrderLine> findOrderLines(int orderId)
            throws SQLException {

        List<OrderLine> lines = new ArrayList<>();

        Connection conn = DBConnection.getConnection();
        try (PreparedStatement statement =
                     conn.prepareStatement("SELECT id, order_id, product_id, " +
                             "quantity, price " +
                             "FROM order_lines " +
                             "WHERE order_id = ?")) {

            statement.setInt(1, orderId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    OrderLine line = new OrderLineDAO(
                            resultSet.getInt("id"),
                            resultSet.getInt("order_id"),
                            resultSet.getInt("product_id"),
                            resultSet.getInt("quantity"),
                            resultSet.getDouble("price")
                    );

                    lines.add(line);
                }
            }
        }

        return lines;
    }
}
