package com.emporiumz.dao;

import com.emporiumz.model.Order;
import com.emporiumz.model.OrderItem;
import com.emporiumz.util.DBConnection;

import java.sql.*;
import java.math.BigDecimal;

public class OrderDAO {

  private static final String INSERT_ORDER = "INSERT INTO orders(user_id,total,shipping_address,status) VALUES(?,?,?,?)";
  private static final String INSERT_ITEM  = "INSERT INTO order_items(order_id,product_id,product_name,unit_price,qty,subtotal) VALUES(?,?,?,?,?,?)";

  public int createOrder(Order order) throws SQLException {
    try (Connection c = DBConnection.getConnection()) {
      c.setAutoCommit(false);
      try (PreparedStatement psOrder = c.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {
        psOrder.setInt(1, order.getUserId());
        psOrder.setBigDecimal(2, order.getTotal());
        psOrder.setString(3, order.getShippingAddress());
        psOrder.setString(4, order.getStatus() == null ? "PENDING" : order.getStatus());
        int affected = psOrder.executeUpdate();
        if (affected != 1) {
          c.rollback();
          throw new SQLException("No se pudo insertar la orden");
        }
        try (ResultSet gk = psOrder.getGeneratedKeys()) {
          if (!gk.next()) {
            c.rollback();
            throw new SQLException("No se obtuvo id de orden");
          }
          int orderId = gk.getInt(1);

          try (PreparedStatement psItem = c.prepareStatement(INSERT_ITEM)) {
            for (OrderItem it : order.getItems()) {
              psItem.setInt(1, orderId);
              psItem.setInt(2, it.getProductId());
              psItem.setString(3, it.getProductName());
              psItem.setBigDecimal(4, it.getUnitPrice());
              psItem.setInt(5, it.getQty());
              psItem.setBigDecimal(6, it.getSubtotal());
              psItem.addBatch();
            }
            int[] results = psItem.executeBatch();
            for (int r : results) {
              if (r == Statement.EXECUTE_FAILED) {
                c.rollback();
                throw new SQLException("Fallo al insertar items");
              }
            }
          }
          c.commit();
          return orderId;
        }
      } catch (SQLException ex) {
        c.rollback();
        throw ex;
      } finally {
        c.setAutoCommit(true);
      }
    }
  }
}
