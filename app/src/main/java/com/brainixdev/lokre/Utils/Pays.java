package com.brainixdev.lokre.Utils;

import java.util.ArrayList;

public class Pays {

    private String nom, iso;
    private int indicatif;
    private ArrayList<String> villes;

    public Pays(String nom, String iso)
    {
        setNom(nom);
        setIso(iso);
        setIndicatif(226);
    }

    public Pays(String nom, String iso, int ind)
    {
        setNom(nom);
        setIso(iso);
        setIndicatif(ind);
    }

    public String getNom()
    {
        return nom;
    }

    public String getIso()
    {
        return iso;
    }

    public int getIndicatif()
    {
        return indicatif;
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }

    public void setIso(String iso)
    {
        this.iso = iso.toUpperCase();
    }

    public void setIndicatif(int ind)
    {
        this.indicatif = ind;
    }
}
