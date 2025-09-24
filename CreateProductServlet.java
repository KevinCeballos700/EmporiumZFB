package com.emporiumz.servlet;

import com.emporiumz.dao.ProductDAO;
import com.emporiumz.model.Product;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name="CreateProductServlet", urlPatterns={"/products"})
public class CreateProductServlet extends HttpServlet {
  private final Gson gson = new Gson();

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    resp.setCharacterEncoding("UTF-8");
    resp.setContentType("application/json;charset=UTF-8");

    Product p;
    try {
      p = gson.fromJson(req.getReader(), Product.class);
    } catch (JsonSyntaxException ex) {
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      try (PrintWriter out = resp.getWriter()) {
        out.write("{\"ok\":false,\"error\":\"JSON inválido\"}");
      }
      return;
    }

    if (p == null || p.getName() == null || p.getPrice() == null) {
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      try (PrintWriter out = resp.getWriter()) {
        out.write("{\"ok\":false,\"error\":\"name y price son requeridos\"}");
      }
      return;
    }

    try {
      ProductDAO dao = new ProductDAO();
      int productId = dao.create(p);
      try (PrintWriter out = resp.getWriter()) {
        resp.setStatus(HttpServletResponse.SC_CREATED);
        out.write("{\"ok\":true,\"productId\":" + productId + "}");
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
