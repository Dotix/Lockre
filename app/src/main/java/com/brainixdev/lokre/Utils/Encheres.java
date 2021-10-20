package com.brainixdev.lokre.Utils;

public class Encheres {

    private int auction_start_price;
    private int auction_close_price;
    private int auction_register_price;
    private String auction_product;
    private String auction_register_date;
    private String auction_start_date;
    private String auction_end_date;
    private String auction_type;

    private Produit product;

    public int getAuction_close_price() {
        return auction_close_price;
    }

    public void setAuction_close_price(int auction_close_price) {
        this.auction_close_price = auction_close_price;
    }

    public String getAuction_end_date() {
        return auction_end_date;
    }

    public void setAuction_end_date(String auction_end_date) {
        this.auction_end_date = auction_end_date;
    }

    public Produit getProduct() {
        return product;
    }

    public void setProduct(Produit product) {
        this.product = product;
    }

    public int getAuction_start_price() {
        return auction_start_price;
    }

    public void setAuction_start_price(int auction_start_price) {
        this.auction_start_price = auction_start_price;
    }

    public String getAuction_product() {
        return auction_product;
    }

    public void setAuction_product(String auction_product) {
        this.auction_product = auction_product;
    }

    public String getAuction_register_date() {
        return auction_register_date;
    }

    public void setAuction_register_date(String auction_register_date) {
        this.auction_register_date = auction_register_date;
    }

    public String getAuction_start_date() {
        return auction_start_date;
    }

    public void setAuction_start_date(String auction_start_date) {
        this.auction_start_date = auction_start_date;
    }

    public String getAuction_type() {
        return auction_type;
    }

    public void setAuction_type(String auction_type) {
        this.auction_type = auction_type;
    }

    public int getAuction_register_price() {
        return auction_register_price;
    }

    public void setAuction_register_price(int auction_register_price) {
        this.auction_register_price = auction_register_price;
    }
}
