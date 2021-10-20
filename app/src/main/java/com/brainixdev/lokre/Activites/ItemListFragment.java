package com.brainixdev.lokre.Activites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brainixdev.lokre.R;
import com.brainixdev.lokre.Utils.AdaptateurEncheres;
import com.brainixdev.lokre.Utils.ChargerEncheres;
import com.brainixdev.lokre.Utils.Encheres;
import com.brainixdev.lokre.Utils.Utilisateur;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ItemListFragment extends Fragment {

    private final int SCROLL_UP = -1;
    private final int SCROLL_DOWN = 1;
    private String FECTH_START  = null;

    private SwipeRefreshLayout srly;
    private RecyclerView rv;
    private ArrayList<Encheres> auctions = new ArrayList<>();
    private AdaptateurEncheres adapt;

    private Utilisateur loggedUser;
    private ImageView iv_profil_pic, iv_level;
    private TextView tv_pseudo, tv_balance;
    private Boolean isLoading = false;
    private Boolean isFirstLoad = true;
    private ProgressBar bar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item_list, container, false);

        rv = v.findViewById(R.id.rv_products);
        iv_profil_pic = v.findViewById(R.id.iv_profil_pic);
        tv_pseudo = v.findViewById(R.id.tv_pseudo_txt);
        tv_balance = v.findViewById(R.id.tv_balance);
        srly = v.findViewById(R.id.main_content);
        bar = v.findViewById(R.id.main_progress_bar);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseReference dbUserRef = FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance().getUid() + "/Identity");
        dbUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    loggedUser = snapshot.getValue(Utilisateur.class);
                assert loggedUser != null;
                initUserInfo(loggedUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        load();
    }

    @Override
    public void onStart() {
        super.onStart();

        if(isFirstLoad) {
            bar.setVisibility(View.VISIBLE);
            srly.setVisibility(View.GONE);
            auctions=new ArrayList<>();
            adapt = new AdaptateurEncheres(auctions,true);
            rv.setAdapter(adapt);
        }

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0 && !srly.isRefreshing())
                    handleScroll();
            }
        });

        srly.setColorSchemeResources(R.color.item_card_info);
        srly.setOnRefreshListener(() -> {
            FECTH_START = null;
            load();
        });
    }

    public void initUserInfo(Utilisateur user) {
        Glide.with(this).load(user.getUser_profil_pic()).placeholder(R.drawable.ic_user_profile).apply(RequestOptions.circleCropTransform()).into(iv_profil_pic);
        tv_pseudo.setText(user.getUser_pseudo());
        tv_balance.setText(getString(R.string.default_balance, user.getUser_balance()));
    }

    public void handleScroll() {
        LinearLayoutManager lm = (LinearLayoutManager) rv.getLayoutManager();
        if (!isLoading) {
            if (lm != null && (lm.findLastCompletelyVisibleItemPosition() == auctions.size() - 1 || Integer.parseInt(FECTH_START) == 0)) {
                isLoading = true;
                auctions.add(null);
                rv.post(() -> adapt.notifyItemInserted(auctions.size() -1));
                load();
            }
        }
    }

    public void load(){
        int FECTH_LIMIT = 5;
        ChargerEncheres.chargerEncheres((en, lastKey) -> {
            //...................... première requête de chargement des enchères
            if(isFirstLoad) {
                isFirstLoad = false;
                bar.setVisibility(View.GONE);
                srly.setVisibility(View.VISIBLE);
            }
            //....................... requêtes classique de chargement supplémentaire
            if(isLoading) {
                isLoading = false;
                auctions.remove(auctions.size() - 1);
                adapt.notifyItemRemoved(auctions.size()-1);
            }
            //....................... requête de refraichissement des enchères
            if(srly.isRefreshing()){
                srly.setRefreshing(false);
                auctions.clear();
            }

            for(int i=en.size(); i>0;i--)
                auctions.add(en.get(i-1));

            if(lastKey != null)
                FECTH_START = Integer.toString(Integer.parseInt(lastKey) -1);
            adapt.notifyDataSetChanged();
        }, FECTH_START, FECTH_LIMIT, null);
    }

}