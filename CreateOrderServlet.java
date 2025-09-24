package com.emporiumz.servlet;

import com.emporiumz.dao.OrderDAO;
import com.emporiumz.dao.UserDAO;
import com.emporiumz.model.Order;
import com.emporiumz.model.OrderItem;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name="CreateOrderServlet", urlPatterns={"/orders"})
public class CreateOrderServlet extends HttpServlet {

  private final Gson gson = new Gson();

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    resp.setCharacterEncoding("UTF-8");
    resp.setContentType("application/json;charset=UTF-8");

    JsonObject root;
    try {
      root = gson.fromJson(req.getReader(), JsonObject.class);
    } catch (JsonSyntaxException ex) {
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      try (PrintWriter out = resp.getWriter()) {
        out.write("{\"ok\":false,\"error\":\"JSON inválido\"}");
      }
      return;
    }

    // Resolver userId por identification_unc
    int userId = 0;
    if (root.has("identification_unc") && !root.get("identification_unc").isJsonNull()) {
      String ident = root.get("identification_unc").getAsString();
      try {
        UserDAO udao = new UserDAO();
        userId = udao.getUserIdByIdentificationUnc(ident);
        if (userId == 0) {
          resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
          try (PrintWriter out = resp.getWriter()) {
            out.write("{\"ok\":false,\"error\":\"identification_unc no encontrada\"}");
          }
          return;
        }
      } catch (Exception ex) {
        ex.printStackTrace();
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        try (PrintWriter out = resp.getWriter()) {
          out.write("{\"ok\":false,\"error\":\"Error al resolver identification_unc\"}");
        }
        return;
      }
    } else if (root.has("userId") && !root.get("userId").isJsonNull()) {
      try {
        userId = root.get("userId").getAsInt();
      } catch (Exception ex) {
        userId = 0;
      }
    }

    // Deserializar Order completo (Gson)
    Order order;
    try {
      order = gson.fromJson(root, Order.class);
    } catch (Exception ex) {
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      try (PrintWriter out = resp.getWriter()) {
        out.write("{\"ok\":false,\"error\":\"JSON de order inválido\"}");
      }
      return;
    }

    order.setUserId(userId);

    if (order.getUserId() == 0 || order.getItems() == null || order.getItems().isEmpty()) {
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      try (PrintWriter out = resp.getWriter()) {
        out.write("{\"ok\":false,\"error\":\"Faltan identification_unc (o userId) o items\"}");
      }
      return;
    }

    BigDecimal calcTotal = BigDecimal.ZERO;
    List<OrderItem> items = order.getItems();
    for (OrderItem it : items) {
      if (it.getUnitPrice() == null) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        try (PrintWriter out = resp.getWriter()) {
          out.write("{\"ok\":false,\"error\":\"unitPrice requerido en items\"}");
        }
        return;
      }
      if (it.getSubtotal() == null) {
        it.setSubtotal(it.getUnitPrice().multiply(new BigDecimal(it.getQty())));
      }
      calcTotal = calcTotal.add(it.getSubtotal());
    }
    order.setTotal(calcTotal);

    try {
      OrderDAO dao = new OrderDAO();
      int orderId = dao.createOrder(order);
      try (PrintWriter out = resp.getWriter()) {
        resp.setStatus(HttpServletResponse.SC_CREATED);
        out.write("{\"ok\":true,\"message\":\"pedido realizado, esperando confirmacion\",\"orderId\":" + orderId + "}");
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      try (PrintWriter out = resp.getWriter()) {
        out.write("{\"ok\":false,\"error\":\"" + ex.getMessage().replace("\"","'") + "\"}");
      }
    }
  }
}
