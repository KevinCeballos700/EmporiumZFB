package com.emporiumz.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderItem implements Serializable {
  private int id;
  private int orderId;
  private int productId;
  private String productName;
  private BigDecimal unitPrice;
  private int qty;
  private BigDecimal subtotal;

  public OrderItem() { }

  public int getId() { return id; }
  public void setId(int id) { this.id = id; }
  public int getOrderId() { return orderId; }
  public void setOrderId(int orderId) { this.orderId = orderId; }
  public int getProductId() { return productId; }
  public void setProductId(int productId) { this.productId = productId; }
  public String getProductName() { return productName; }
  public void setProductName(String productName) { this.productName = productName; }
  public BigDecimal getUnitPrice() { return unitPrice; }
  public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
  public int getQty() { return qty; }
  public void setQty(int qty) { this.qty = qty; }
  public BigDecimal getSubtotal() { return subtotal; }
  public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}
