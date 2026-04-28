package dao;

import model.*;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {
    private FoodDao foodDao = new FoodDao();

    //创建订单
    public int createOrder(User user, List<CartItem> cartItems) {
        Connection conn = null;
        PreparedStatement pstmt1 = null, pstmt2 = null;
        ResultSet rs = null;
        int orderId = -1;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            //插入订单主表
            String sql1 = "INSERT INTO orders (user_id, total_price) VALUES (?, ?)";
            pstmt1 = conn.prepareStatement(sql1, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt1.setInt(1, user.getId());
            pstmt1.setDouble(2, calculateTotal(cartItems));
            pstmt1.executeUpdate();
            rs = pstmt1.getGeneratedKeys();
            if (rs.next()) {
                orderId = rs.getInt(1);
            }

            //插入订单明细表
            String sql2 = "INSERT INTO order_details (order_id, dish_id, quantity, subtotal) VALUES (?, ?, ?, ?)";
            pstmt2 = conn.prepareStatement(sql2);
            for (CartItem item : cartItems) {
                pstmt2.setInt(1, orderId);
                pstmt2.setInt(2, item.getFood().getId());
                pstmt2.setInt(3, item.getQuantity());
                pstmt2.setDouble(4, item.getSubtotal());
                pstmt2.addBatch();
            }
            pstmt2.executeBatch();

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn, pstmt1, rs);
            DBUtil.closeConnection(null, pstmt2, null);
        }
        return orderId;
    }

    //计算购物车总价
    private double calculateTotal(List<CartItem> cartItems) {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getSubtotal();
        }
        return total;
    }

    //查询所有订单
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT o.*, u.username FROM orders o JOIN users u ON o.user_id = u.id ORDER BY o.create_time DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("o.id"),
                        rs.getInt("o.user_id"),
                        new User(
                                rs.getInt("o.user_id"),
                                rs.getString("u.username"),
                                "",
                                ""
                        ),
                        rs.getDouble("o.total_price"),
                        rs.getTimestamp("o.create_time"),
                        getOrderDetails(rs.getInt("o.id"))
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn, pstmt, rs);
        }
        return orders;
    }

    //查询订单明细
    public List<OrderDetail> getOrderDetails(int orderId) {
        List<OrderDetail> details = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT od.*, f.name, f.price FROM order_details od JOIN food f ON od.dish_id = f.id WHERE od.order_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                OrderDetail detail = new OrderDetail();
                detail.setId(rs.getInt("od.id"));
                detail.setOrderId(orderId);
                detail.setDishId(rs.getInt("dish_id"));
                detail.setDishName(rs.getString("f.name"));
                detail.setQuantity(rs.getInt("od.quantity"));
                detail.setSubtotal(rs.getDouble("od.subtotal"));
                details.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn, pstmt, rs);
        }
        return details;
    }
}
