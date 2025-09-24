package com.emporiumz.servlet;

import com.emporiumz.dao.UserDAO;
import com.emporiumz.model.User;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.util.Random;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    private static final int BCRYPT_COST = 10;
    private static final Random RNG = new SecureRandom();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        try (PrintWriter out = resp.getWriter()) {
            out.write("{\"ok\":false,\"error\":\"Use POST to register\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");

        String username = trim(req.getParameter("username"));
        String email = trim(req.getParameter("email"));
        String phone = trim(req.getParameter("phone"));
        String password = req.getParameter("password");

        if (isEmpty(username) || isEmpty(password) || (isEmpty(email) && isEmpty(phone))) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try (PrintWriter out = resp.getWriter()) {
                out.write("{\"ok\":false,\"error\":\"Faltan campos requeridos\"}");
            }
            return;
        }

        try {
            // Generar identification_unc de 7 dígitos (ceros a la izquierda posibles)
            String identificationUnc;
            UserDAO dao = new UserDAO();
            int attempts = 0;
            do {
                identificationUnc = generate7Digit();
                // pre-asignar y probar register; register maneja duplicado devolviendo false
                User temp = new User();
                temp.setUsername(username);
                temp.setIdentificationUnc(identificationUnc);
                temp.setEmail(email);
                temp.setPhone(phone);
                temp.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt(BCRYPT_COST)));
                temp.setRole("customer");

                boolean created = dao.register(temp);
                if (created) {
                    // registro exitoso, respondemos con id y identification_unc
                    try (PrintWriter out = resp.getWriter()) {
                        resp.setStatus(HttpServletResponse.SC_CREATED);
                        out.write("{\"ok\":true,\"message\":\"Usuario creado\",\"id\":" + temp.getId() + ",\"identification_unc\":\"" + identificationUnc + "\"}");
                    }
                    return;
                }
                // si no creado, probablemente duplicado; reintentar
                attempts++;
            } while (attempts < 5);

            // Si llegamos aquí, no se pudo crear por duplicado repetido
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            try (PrintWriter out = resp.getWriter()) {
                out.write("{\"ok\":false,\"error\":\"No se pudo crear usuario (colisión de identification_unc). Intenta de nuevo.\"}");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter out = resp.getWriter()) {
                out.write("{\"ok\":false,\"error\":\"" + ex.getMessage().replace("\"","'") + "\"}");
            }
        }
    }

    private static boolean isEmpty(String s) { return s == null || s.trim().isEmpty(); }
    private static String trim(String s) { return s == null ? null : s.trim(); }

    private static String generate7Digit() {
        int n = RNG.nextInt(10_000_000);
        return String.format("%07d", n);
    }
}
