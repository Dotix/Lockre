package com.brainixdev.lokre.Utils;

import android.os.AsyncTask;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class TacheAsync extends AsyncTask <String, Void, String> {

    public interface Listeners
    {
        void onPreExecute();
        void doInBackground();
        void onPostExecute(String result);
    }

    private final WeakReference <Listeners> callbak;
    private String numero;

    public TacheAsync(Listeners callbak) {
        this.callbak = new WeakReference<>(callbak);
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        this.callbak.get().onPreExecute();
    }

    @Override
    protected void onPostExecute(String success){
        super.onPostExecute(success);
        this.callbak.get().onPostExecute(success);
    }

    @Override
    protected String doInBackground(String... str) {
        this.callbak.get().doInBackground();
            RequeteUrl r;

            if(str[1].compareTo("firebase") == 0)
                return "OK";
            else {
                if(str[1].compareTo("validation") == 0)
                    r = new RequeteUrl(str[0],str[1],str[2]);
                else
                    r = new RequeteUrl(str[0],str[1]);

                try {
                    r.connecter();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return r.getRetour();
            }
    }
}
