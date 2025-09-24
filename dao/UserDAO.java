package com.emporiumz.dao;

import com.emporiumz.model.User;
import com.emporiumz.util.DBConnection;

import java.sql.*;

public class UserDAO {

    private static final String INSERT_SQL =
        "INSERT INTO users(username, identification_unc, email, phone, password_hash, role) VALUES(?,?,?,?,?,?)";

    public boolean register(User u) throws SQLException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getIdentificationUnc());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getPhone());
            ps.setString(5, u.getPasswordHash());
            ps.setString(6, u.getRole() == null ? "customer" : u.getRole());

            int affected = ps.executeUpdate();
            if (affected == 1) {
                try (ResultSet gk = ps.getGeneratedKeys()) {
                    if (gk.next()) {
                        u.setId(gk.getInt(1));
                    }
                }
                System.out.println("UserDAO.register affected=" + affected);
                return true;
            } else {
                return false;
            }
        } catch (SQLIntegrityConstraintViolationException ex) {
            System.err.println("UserDAO.register duplicate: " + ex.getMessage());
            return false;
        }
    }

    

    public int getUserIdByIdentificationUnc(String ident) throws SQLException {
    final String SQL = "SELECT id FROM users WHERE identification_unc = ?";
    try (Connection c = DBConnection.getConnection();
         PreparedStatement ps = c.prepareStatement(SQL)) {
        ps.setString(1, ident); // <-- usar el parámetro entrante
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("id");
            return 0;
        }
    }
}

}

