package com.brainixdev.lokre.Utils;

import android.content.Context;

import com.brainixdev.lokre.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;

public class RecherchePays {

    public interface mCall{
        void onCallBack(ListePays val);
        void onLoad(ListePays val);
        void onVilleLoad(ArrayList<String> ville);
    }

    private static RecherchePays.ListePays mPays;
    private static ArrayList<String> mVilles;

    private static String getJsonFromRaw(Context context) {
        String json;
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.liste_pays);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static void recupererPays(Context context, final mCall myCall){
        if (mPays != null) {
            myCall.onCallBack(mPays);
            return;
        }

        mPays = new ListePays();
            //Reécupération de la liste des pays pris en charge dans un fichier ---------------------
       JSONArray pays = null;
        try {
            pays = new JSONArray(getJsonFromRaw(context));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int index;
            for (int i = 0; i < Objects.requireNonNull(pays).length(); i++) {
                index = 0;
                try {
                    JSONObject country = (JSONObject) pays.get(i);
                    //if(mPays.size()>0)
                    for(int j=0;j<i;j++)
                        if(country.getString("country_name").compareTo(mPays.get(j).getNom())>0)
                            index++;
                    mPays.add(index, new Pays(country.getString("country_name"), country.getString("country_iso2")));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // Récupération de la liste des pays pris en charge depuis firebase -----------------------
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Countries");

            Query q = ref.orderByChild("country_name");

        q.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        mPays = new ListePays();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        mPays.add(new  Pays(snap.child("country_name").getValue(String.class), snap.child("country_iso2").getValue(String.class)));
                    }
                        myCall.onCallBack(mPays);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        myCall.onLoad(mPays);
    }

    public static void recupererVille(Context context, final mCall myCall, String Pays){
        if (mVilles != null) {
            myCall.onVilleLoad(mVilles);
            return;
        }
        mVilles = new ArrayList<>();
        // Récupération de la liste des villes prises en charge depuis firebase -----------------------
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Countries/"+ Pays + "/country_cities");
        ref.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    mVilles = new ArrayList<>();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        mVilles.add(snap.getValue(String.class));
                    }
                    myCall.onVilleLoad(mVilles);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static class ListePays extends ArrayList<Pays> {

        public int indexIso(String iso) {
            for (int i = 0; i < this.size(); i++) {
                if (this.get(i).getIso().equalsIgnoreCase(iso)) {
                    return i;
                }
            }
            return -1;
        }
    }
}
