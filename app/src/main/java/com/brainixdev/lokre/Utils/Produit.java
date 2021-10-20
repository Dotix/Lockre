package com.brainixdev.lokre.Utils;

public class Produit {

    String id_produit;
    String product_name;
    String product_description;
    String product_state;

    String product_cat_1;
    String product_cat_2;
    String product_cat_3;

    String product_pic_1;
    String product_pic_2;
    String product_pic_3;

    int product_price;

    public Produit(){

    }

    public Produit(String id, String nom, String description, int prix, String etat, String cat1, String cat2, String cat3, String photo1, String photo2, String photo3){
        this.id_produit = id;
        this.product_name = nom;
        this.product_description = description;
        this.product_price = prix;
        this.product_state = etat;
        this.product_cat_1 = cat1;
        this.product_cat_2 = cat2;
        this.product_cat_3 = cat3;
        this.product_pic_1 = photo1;
        this.product_pic_2 = photo2;
        this.product_pic_3 = photo3;
    }

    public String getId_produit() {
        return id_produit;
    }

    public void setId_produit(String id_produit) {
        this.id_produit = id_produit;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public int getProduct_price() {
        return product_price;
    }

    public void setProduct_price(int product_price) {
        this.product_price = product_price;
    }

    public String getProduct_state() {
        return product_state;
    }

    public void setProduct_state(String product_state) {
        this.product_state = product_state;
    }

    public String getProduct_cat_1() {
        return product_cat_1;
    }

    public void setProduct_cat_1(String product_cat_1) {
        this.product_cat_1 = product_cat_1;
    }

    public String getProduct_cat_2() {
        return product_cat_2;
    }

    public void setProduct_cat_2(String product_cat_2) {
        this.product_cat_2 = product_cat_2;
    }

    public String getProduct_cat_3() {
        return product_cat_3;
    }

    public void setProduct_cat_3(String product_cat_3) {
        this.product_cat_3 = product_cat_3;
    }

    public String getProduct_pic_1() {
        return product_pic_1;
    }

    public void setProduct_pic_1(String product_pic_1) {
        this.product_pic_1 = product_pic_1;
    }

    public String getProduct_pic_2() {
        return product_pic_2;
    }

    public void setProduct_pic_2(String product_pic_2) {
        this.product_pic_2 = product_pic_2;
    }

    public String getProduct_pic_3() {
        return product_pic_3;
    }

    public void setProduct_pic_3(String product_pic_3) {
        this.product_pic_3 = product_pic_3;
    }
}
