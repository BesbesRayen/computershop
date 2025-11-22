package com.example.computershop.models;

import java.io.Serializable;

public class User implements Serializable {
    private String idInt;
    private String login;
    private String email;
    private long dateInscrip;
    private String pays;
    private String role; // "user" or "admin"

    public User() {
    }

    public User(String idInt, String login, String email, long dateInscrip, String pays, String role) {
        this.idInt = idInt;
        this.login = login;
        this.email = email;
        this.dateInscrip = dateInscrip;
        this.pays = pays;
        this.role = role;
    }

    public String getIdInt() {
        return idInt;
    }

    public void setIdInt(String idInt) {
        this.idInt = idInt;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getDateInscrip() {
        return dateInscrip;
    }

    public void setDateInscrip(long dateInscrip) {
        this.dateInscrip = dateInscrip;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAdmin() {
        return "admin".equals(role);
    }
}
