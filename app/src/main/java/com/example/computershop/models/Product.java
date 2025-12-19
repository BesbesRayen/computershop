package com.example.computershop.models;

import java.io.Serializable;

public class Product implements Serializable {
    private String idArt;
    private String libArt;
    private double prixArt;
    private String catArt;
    private String description;
    private int stock;
    private String imageUrl;

    // No-argument constructor required by Firebase Firestore deserialization
    public Product() {
    }

    public Product(String idArt, String name, double price, String category, String description, int stock, int selectedImageResId) {
    }

    public Product(String idArt, String libArt, double prixArt, String catArt,
                   String description, int stock, String imageUrl) {
        this.idArt = idArt;
        this.libArt = libArt;
        this.prixArt = prixArt;
        this.catArt = catArt;
        this.description = description;
        this.stock = stock;
        this.imageUrl = imageUrl;
    }

    public String getIdArt() {
        return idArt;
    }

    public void setIdArt(String idArt) {
        this.idArt = idArt;
    }

    public String getLibArt() {
        return libArt;
    }

    public void setLibArt(String libArt) {
        this.libArt = libArt;
    }

    public double getPrixArt() {
        return prixArt;
    }

    public void setPrixArt(double prixArt) {
        this.prixArt = prixArt;
    }

    public String getCatArt() {
        return catArt;
    }

    public void setCatArt(String catArt) {
        this.catArt = catArt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
