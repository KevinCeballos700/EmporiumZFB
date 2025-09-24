package com.emporiumz.model;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String username;
    private String identificationUnc; // 7 dígitos
    private String email;
    private String phone;
    private String passwordHash;
    private String role;

    public User() { }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getIdentificationUnc() { return identificationUnc; }
    public void setIdentificationUnc(String identificationUnc) { this.identificationUnc = identificationUnc; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User u = (User) o;
        return id == u.id &&
               Objects.equals(username, u.username) &&
               Objects.equals(identificationUnc, u.identificationUnc);
    }
    @Override
    public int hashCode() { return Objects.hash(id, username, identificationUnc); }
    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', identificationUnc='" + identificationUnc + "'}";
    }
}

