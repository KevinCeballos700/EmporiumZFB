package com.emporiumz.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
  private int id;
  private int userId;
  private BigDecimal total;
  private String shippingAddress;
  private String status;
  private List<OrderItem> items = new ArrayList<>();

  public Order() { }

  public int getId() { return id; }
  public void setId(int id) { this.id = id; }
  public int getUserId() { return userId; }
  public void setUserId(int userId) { this.userId = userId; }
  public BigDecimal getTotal() { return total; }
  public void setTotal(BigDecimal total) { this.total = total; }
  public String getShippingAddress() { return shippingAddress; }
  public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }
  public List<OrderItem> getItems() { return items; }
  public void setItems(List<OrderItem> items) { this.items = items; }
  public void addItem(OrderItem it){ this.items.add(it); }
}
