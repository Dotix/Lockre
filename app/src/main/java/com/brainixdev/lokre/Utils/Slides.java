package com.brainixdev.lokre.Utils;

public class Slides {

    int idImage;
    String titre;
    String description;

    public Slides(){ }

    public int getIdImage(){
        return this.idImage;
    }

    public void setIdImage(int ID){
        this.idImage = ID;
    }

    public String getTitre(){
        return this.titre;
    }

    public void setTitre(String t){
        this.titre = t;
    }

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String d){
        this.description = d;
    }
}
