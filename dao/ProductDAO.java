package com.emporiumz.dao;

import com.emporiumz.model.Product;
import com.emporiumz.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class ProductDAO {

  private static final String INSERT_SQL =
    "INSERT INTO products(name,description,price,sku,stock) VALUES(?,?,?,?,?)";
  private static final String SELECT_BY_ID =
    "SELECT id,name,description,price,sku,stock,created_at,updated_at FROM products WHERE id = ?";
  private static final String SELECT_ALL =
    "SELECT id,name,description,price,sku,stock,created_at,updated_at FROM products ORDER BY id DESC";

  
    

  public Product findById(int id) throws SQLException {
    try (Connection c = DBConnection.getConnection();
         PreparedStatement ps = c.prepareStatement(SELECT_BY_ID)) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return mapRow(rs);
        return null;
      }
    }
  }

  public List<Product> listAll() throws SQLException {
    List<Product> out = new ArrayList<>();
    try (Connection c = DBConnection.getConnection();
         PreparedStatement ps = c.prepareStatement(SELECT_ALL);
         ResultSet rs = ps.executeQuery()) {
      while (rs.next()) out.add(mapRow(rs));
    }
    return out;
  }

  private Product mapRow(ResultSet rs) throws SQLException {
    Product p = new Product();
    p.setId(rs.getInt("id"));
    p.setName(rs.getString("name"));
    p.setDescription(rs.getString("description"));
    p.setPrice(rs.getBigDecimal("price"));
    p.setSku(rs.getString("sku"));
    p.setStock(rs.getInt("stock"));
    p.setCreatedAt(rs.getTimestamp("created_at"));
    p.setUpdatedAt(rs.getTimestamp("updated_at"));
    return p;
  }

    public int create(Product p)throws SQLException {
    try (Connection c = DBConnection.getConnection();
         PreparedStatement ps = c.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, p.getName());
      ps.setString(2, p.getDescription());
      ps.setBigDecimal(3, p.getPrice() == null ? BigDecimal.ZERO : p.getPrice());
      ps.setString(4, p.getSku()); // puede ser null o vacío
      ps.setInt(5, p.getStock());
      int affected = ps.executeUpdate();
      if (affected != 1) throw new SQLException("No se insertó producto");
      try (ResultSet gk = ps.getGeneratedKeys()) {
        if (gk.next()) {
          int id = gk.getInt(1);
          // Si no se envió sku, calculamos y actualizamos con formato 3 dígitos ("001")
          if (p.getSku() == null || p.getSku().trim().isEmpty()) {
            String computedSku = String.format("%03d", id);
            try (PreparedStatement ps2 = c.prepareStatement("UPDATE products SET sku = ? WHERE id = ?")) {
              ps2.setString(1, computedSku);
              ps2.setInt(2, id);
              ps2.executeUpdate();
            }
          }
          return id;
        }
        throw new SQLException("No se obtuvo id de producto");
      }
    }
  }  
}
