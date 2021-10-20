package com.brainixdev.lokre.Utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brainixdev.lokre.R;
import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class AdaptateurEncheres extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_LOADING = 0;
    private final int VIEW_TYPE_PRODUCT = 1;

    private final boolean isFull;

    public List<Encheres> auctions;

    public AdaptateurEncheres(List<Encheres> p, boolean isFull){
        this.auctions = p;
        this.isFull = isFull;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if(viewType == VIEW_TYPE_LOADING){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_refresh, parent, false);
            return new LoadingHolder(v);
        }else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
            return new ProductCardHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ProductCardHolder){
            Encheres p = auctions.get(position);
            ((ProductCardHolder) holder).remplir(p,isFull);
        }
        else if(holder instanceof LoadingHolder)
            ((LoadingHolder) holder).show();
    }

    @Override
    public int getItemCount() {
        return (auctions != null ? auctions.size() : 0);
    }

    @Override
    public int getItemViewType(int position) {
        return auctions.get(position) != null ? VIEW_TYPE_PRODUCT:VIEW_TYPE_LOADING;
    }
}

class ProductCardHolder extends RecyclerView.ViewHolder {

    private final TextView nom_produit;
    private final TextView prix_produit;
    private final TextView etat_produit;
    private final TextView date_enchere;

    private final ImageView photo1_produit;
    private final ImageView photo2_produit;
    private final ImageView photo3_produit;

    private final Button bt_participer;

    public ProductCardHolder(@NonNull View itemView) {
        super(itemView);

        nom_produit = itemView.findViewById(R.id.tv_product_name);
        prix_produit = itemView.findViewById(R.id.tv_product_price);
        etat_produit = itemView.findViewById(R.id.tv_product_state);
        date_enchere = itemView.findViewById(R.id.tv_product_date);
        photo1_produit = itemView.findViewById(R.id.iv_product_pic1);
        photo2_produit = itemView.findViewById(R.id.iv_product_pic2);
        photo3_produit = itemView.findViewById(R.id.iv_product_pic3);
        bt_participer = itemView.findViewById(R.id.bt_product_join);
    }

    public void remplir(Encheres p, boolean isFull){
        prix_produit.setText(AppContext.getContext().getString(R.string.product_price, p.getAuction_start_price()));
        date_enchere.setText(p.getAuction_start_date());
        bt_participer.setText(String.valueOf(Math.ceil(p.getAuction_register_price()/100d)));
        if (isFull){
            nom_produit.setText(p.getProduct().getProduct_name());
            etat_produit.setText(p.getProduct().getProduct_state());
            Glide.with(AppContext.getContext()).load(p.getProduct().getProduct_pic_1()).placeholder(R.drawable.ic_baseline_image_24).error(R.drawable.ic_baseline_broken_image_24).override(100,100).into(photo1_produit);
            Glide.with(AppContext.getContext()).load(p.getProduct().getProduct_pic_2()).placeholder(R.drawable.ic_baseline_image_24).error(R.drawable.ic_baseline_broken_image_24).override(100,100).into(photo2_produit);
            Glide.with(AppContext.getContext()).load(p.getProduct().getProduct_pic_3()).placeholder(R.drawable.ic_baseline_image_24).error(R.drawable.ic_baseline_broken_image_24).override(100,100).into(photo3_produit);
            photo3_produit.setMaxHeight(100);
            photo3_produit.setMaxWidth(100);
            photo3_produit.setBackground(photo3_produit.getDrawable());
            photo3_produit.setImageDrawable(ContextCompat.getDrawable(AppContext.getContext(), R.drawable.ic_image_plus));
        }
    }
}

class LoadingHolder extends RecyclerView.ViewHolder{

    public LoadingHolder(@NonNull View itemView) {
        super(itemView);
        ProgressBar p = itemView.findViewById(R.id.pb_loading_auction);
    }

    public void show(){
    }
}
