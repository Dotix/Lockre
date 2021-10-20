package com.brainixdev.lokre.Utils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;



public class ChargerEncheres {

    private ArrayList<Encheres> mEnchere;

    public interface myCalls{
         void onEnchereLoaded(ArrayList<Encheres> en, String jj);
    }

    public static void chargerEncheres(final myCalls c, String end, int limit, String categorie){
        DatabaseReference dbAuctionRef = FirebaseDatabase.getInstance().getReference("Auctions");
        Query q;

        if(end == null)
            q = dbAuctionRef.orderByKey().limitToLast(limit);
        else
            q = dbAuctionRef.orderByKey().endAt(end).limitToLast(limit);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            final ArrayList<Encheres> p = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String key = null;
                if(snapshot.exists()) {
                    for (DataSnapshot d : snapshot.getChildren()) {
                        p.add(d.getValue(Encheres.class));
                    }
                    key = snapshot.getChildren().iterator().next().getKey();
                }
                trouverProduit(c,p,key);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void trouverProduit(final myCalls c, final ArrayList<Encheres> encheres, final String lastKey){
        DatabaseReference dbProdRef = FirebaseDatabase.getInstance().getReference("Products");
        dbProdRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        for (Encheres e : encheres) {
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                if (snap.getKey().equals(e.getAuction_product()))
                                    e.setProduct(snap.getValue(Produit.class));
                            }
                        }
                    }
                    c.onEnchereLoaded(encheres,lastKey);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

}
