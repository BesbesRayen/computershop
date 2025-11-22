package com.example.computershop.models;

import java.io.Serializable;

public class CartItem implements Serializable {
    private String numPanier;
    private String idArt;
    private String idInt;
    private int quantité;
    private String emballage;
    private long dateAjout;

    public CartItem() {
    }

    public CartItem(String numPanier, String idArt, String idInt, 
                    int quantité, String emballage, long dateAjout) {
        this.numPanier = numPanier;
        this.idArt = idArt;
        this.idInt = idInt;
        this.quantité = quantité;
        this.emballage = emballage;
        this.dateAjout = dateAjout;
    }

    public String getNumPanier() {
        return numPanier;
    }

    public void setNumPanier(String numPanier) {
        this.numPanier = numPanier;
    }

    public String getIdArt() {
        return idArt;
    }

    public void setIdArt(String idArt) {
        this.idArt = idArt;
    }

    public String getIdInt() {
        return idInt;
    }

    public void setIdInt(String idInt) {
        this.idInt = idInt;
    }

    public int getQuantité() {
        return quantité;
    }

    public void setQuantité(int quantité) {
        this.quantité = quantité;
    }

    public String getEmballage() {
        return emballage;
    }

    public void setEmballage(String emballage) {
        this.emballage = emballage;
    }

    public long getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(long dateAjout) {
        this.dateAjout = dateAjout;
    }
}
