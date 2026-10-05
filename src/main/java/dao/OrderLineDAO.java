package dao;

import Model.OrderLine;
import Util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderLineDAO{
    /*public OrderLineDAO(int id, int orderId, int productId, int quantity, double price) {
        super(id, orderId, productId, quantity, price);
    }

    public OrderLineDAO() {
    }*/

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

                    OrderLine line = new OrderLine(
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
