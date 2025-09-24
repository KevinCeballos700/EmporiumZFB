package com.emporiumz.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class Product implements Serializable {
  private int id;
  private String name;
  private String description;
  private BigDecimal price;
  private String sku;
  private int stock;
  private Timestamp createdAt;
  private Timestamp updatedAt;

  public Product() { }

  public int getId() { return id; }
  public void setId(int id) { this.id = id; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }

  public BigDecimal getPrice() { return price; }
  public void setPrice(BigDecimal price) { this.price = price; }

  public String getSku() { return sku; }
  public void setSku(String sku) { this.sku = sku; }

  public int getStock() { return stock; }
  public void setStock(int stock) { this.stock = stock; }

  public Timestamp getCreatedAt() { return createdAt; }
  public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

  public Timestamp getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
