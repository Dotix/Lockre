package com.brainixdev.lokre.Utils;

public class Utilisateur {

    String user_first_number, user_last_name, user_first_name, user_pseudo, user_profil_pic, user_city, user_country, user_second_number;
    int user_status, user_balance, user_score;

    public Utilisateur(){}
    public Utilisateur(String numero, String user_country) {
        this.user_first_number = numero;
        this.user_country = user_country;
    }

    public Utilisateur(String user_last_name, String user_first_name, String user_pseudo, String user_profil_pic, String user_city, String user_country, String user_first_number, String user_second_number) {
        this.user_first_number = user_first_number;
        this.user_last_name = user_last_name;
        this.user_first_name = user_first_name;
        this.user_pseudo = user_pseudo;
        this.user_profil_pic = user_profil_pic;
        this.user_city = user_city;
        this.user_country = user_country;
        this.user_second_number = user_second_number;
        this.user_score = 0;
        this.user_balance = 0;
        this.user_status = 0;
    }

    public Utilisateur(String user_last_name, String user_first_name, String user_pseudo, String user_profil_pic, String user_city, String user_country, String user_first_number, String user_second_number, int user_status, int user_balance, int user_score) {
        this.user_first_number = user_first_number;
        this.user_last_name = user_last_name;
        this.user_first_name = user_first_name;
        this.user_pseudo = user_pseudo;
        this.user_profil_pic = user_profil_pic;
        this.user_city = user_city;
        this.user_country = user_country;
        this.user_second_number = user_second_number;
        this.user_score = user_score;
        this.user_balance = user_balance;
        this.user_status = user_status;
    }

    public String getUser_first_number() {
        return user_first_number;
    }

    public void setUser_first_number(String user_first_number) {
        this.user_first_number = user_first_number;
    }

    public String getUser_last_name() {
        return user_last_name;
    }

    public void setUser_last_name(String user_last_name) {
        this.user_last_name = user_last_name;
    }

    public String getUser_first_name() {
        return user_first_name;
    }

    public void setUser_first_name(String user_first_name) {
        this.user_first_name = user_first_name;
    }

    public String getUser_pseudo() {
        return user_pseudo;
    }

    public void setUser_pseudo(String user_pseudo) {
        this.user_pseudo = user_pseudo;
    }

    public String getUser_profil_pic() {
        return user_profil_pic;
    }

    public void setUser_profil_pic(String user_profil_pic) {
        this.user_profil_pic = user_profil_pic;
    }

    public String getUser_city() {
        return user_city;
    }

    public void setUser_city(String user_city) {
        this.user_city = user_city;
    }

    public String getUser_country() {
        return user_country;
    }

    public void setUser_country(String user_country) {
        this.user_country = user_country;
    }

    public String getUser_second_number() {
        return user_second_number;
    }

    public void setUser_second_number(String user_second_number) {
        this.user_second_number = user_second_number;
    }

    public int getUser_status() {
        return user_status;
    }

    public void setUser_status(int user_status) {
        this.user_status = user_status;
    }

    public int getUser_balance() {
        return user_balance;
    }

    public void setUser_balance(int user_balance) {
        this.user_balance = user_balance;
    }

    public int getUser_score() {
        return user_score;
    }

    public void setUser_score(int user_score) {
        this.user_score = user_score;
    }
}
