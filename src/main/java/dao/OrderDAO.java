package dao;

import bo.Model.CartItem;
import bo.Model.Order;
import Util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds the access methods for order in the database and creates order objects
 */
public class OrderDAO extends Order{

    /**
     * Creates a model Order object
     * @param id of the new order
     * @param userId of the new order
     * @param orderDate of the new order
     * @param status of the new order
     */
    public OrderDAO(int id, int userId, Timestamp orderDate, String status) {
        super(id, userId,orderDate, status);
    }

    /**
     * Creates a new order
     * @param userId of the user which created the order
     * @param cart holds the products and their amounts to be included in the order
     * @throws SQLException when a problem with accessing the database happens
     */
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

    /**
     * Finds all orders in the database
     * @return a List with all the found orders or an empty list if none are found
     * @throws SQLException when a problem with accessing the database happens
     */
    public static List<Order> findAllOrders()
            throws SQLException {

        List<Order> orders = new ArrayList<>();

        Connection conn = DBConnection.getConnection();
        try (PreparedStatement statement =
                     conn.prepareStatement("SELECT id, user_id, order_date, status " +
                             "FROM orders " +
                             "ORDER BY order_date DESC");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Order order = new OrderDAO(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getTimestamp("order_date"),
                        resultSet.getString("status")
                );

                orders.add(order);
            }
        }

        return orders;
    }

    /**
     * Changes an orders status from NEW to PACKED
     * @param orderId specifying the order to be changed
     * @throws SQLException when a problem with accessing the database happens
     */
    public static void packOrder(int orderId)
            throws SQLException {

        Connection conn = DBConnection.getConnection();

        try (PreparedStatement statement =
                     conn.prepareStatement("UPDATE orders " +
                             "SET status = 'PACKED' " +
                             "WHERE id = ? " +
                             "AND status = 'NEW'")) {

            statement.setInt(1, orderId);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated == 0) {
                throw new SQLException(
                        "Order could not be packed.");
            }
        }
    }

    /**
     * Finds the order with the specified id
     * @param orderId of the order to be found
     * @return the found order or null if none are found
     * @throws SQLException when a problem with accessing the database happens
     */
    public static Order findById(int orderId) throws SQLException{
        Connection conn = DBConnection.getConnection();

        try(PreparedStatement statement = conn.prepareStatement("SELECT id, user_id, order_date, status " +
                "FROM orders " +
                "WHERE id = ?")){
            statement.setInt(1, orderId);

            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    return new OrderDAO(
                            resultSet.getInt("id"),
                            resultSet.getInt("user_id"),
                            resultSet.getTimestamp("order_date"),
                            resultSet.getString("status")
                    );
                }
            }
        }
        return null;
    }

    /**
     * Finds all packed orders
     * @return A list of all packed orders or an empty list if none are found
     * @throws SQLException when a problem with accessing the database happens
     */
    public static List<Order> findPackedOrders()
            throws SQLException {

        return findByStatus("PACKED");
    }

    /**
     * Finds all unpacked (NEW) orders
     * @return A list of all packed orders or an empty list if none are found
     * @throws SQLException when a problem with accessing the database happens
     */
    public static List<Order> findOrdersToPack()
            throws SQLException {

        return findByStatus("NEW");
    }

    /**
     * Finds all orders with the specified status
     * @param status to search orders by
     * @return A list of all packed orders or an empty list if none are found
     * @throws SQLException when a problem with accessing the database happens
     */
    private static List<Order> findByStatus(String status)
            throws SQLException {

        List<Order> orders = new ArrayList<>();

        Connection conn = DBConnection.getConnection();


        try (PreparedStatement statement =
                     conn.prepareStatement("SELECT id, user_id, order_date, status " +
                             "FROM orders " +
                             "WHERE status = ? " +
                             "ORDER BY order_date DESC")) {

            statement.setString(1, status);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    Order order = new OrderDAO(
                            resultSet.getInt("id"),
                            resultSet.getInt("user_id"),
                            resultSet.getTimestamp("order_date"),
                            resultSet.getString("status")
                    );

                    orders.add(order);
                }
            }
        }

        return orders;
    }
}
