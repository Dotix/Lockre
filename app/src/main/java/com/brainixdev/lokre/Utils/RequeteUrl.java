package com.brainixdev.lokre.Utils;

import com.brainixdev.lokre.R;

import java.io.IOException;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequeteUrl {

    private String cryptage,url,retour;
    private final OkHttpClient client = new OkHttpClient();

    public RequeteUrl(String numero, String type){

        cryptage = cryptageNumero(numero);

        if(type.compareTo("requete") == 0)
            url = AppContext.getContext().getString(R.string.base_url,AppContext.getContext().getString(R.string.script_requete_code)).concat(cryptage);
        else if(type.compareTo("renvoi") == 0)
            url = AppContext.getContext().getString(R.string.base_url,AppContext.getContext().getString(R.string.script_renvoi_code)).concat(cryptage);

        try {
            retour = connecter();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RequeteUrl(String numero, String type, String code){
        cryptage = cryptageNumero(numero);
        if(type.compareTo("validation") == 0)
            url = AppContext.getContext().getString(R.string.base_url,AppContext.getContext().getString(R.string.script_validation_code).concat(cryptage).concat("&co=" + code));
        try {
            retour = connecter();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String connecter() throws IOException{
        Request rq = new Request.Builder().url(url).build();
        try (Response rp = client.newCall(rq).execute()){
            if (rp.body() != null) {
                return rp.body().string();
            }
        }
        return "erreur";
    }

    public String getRetour()
    {
        return this.retour;
    }

     private String cryptageNumero(String numb){
        String crypt = "";
        char temp ;
        Random random = new Random();

        for(int i=0;i<=numb.length();i++)
        {
            temp = (char) (65+random.nextInt(90-65));
            if (!crypt.isEmpty()) {
                crypt = crypt.concat(String.valueOf(temp));
                if(i != numb.length())
                    crypt = crypt.concat(numb.substring(i, i+1));
                else
                    crypt = crypt.concat(numb.substring(i));
            }
            else
                crypt = numb.substring(0,1);
        }
         return crypt;
     }
}
