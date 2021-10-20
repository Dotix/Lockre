package com.brainixdev.lokre.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.brainixdev.lokre.R;
import com.brainixdev.lokre.Utils.Utilisateur;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

public class SplashActivity extends AppCompatActivity {

    Animation splash_slogan, splash_logo_e, splash_logo_money;
    TextView text_slogan;
    ImageView image_logo_e,image_logo_money;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();

        splash_slogan = AnimationUtils.loadAnimation(this,R.anim.splash_slogan_anim);
        //splash_logo_l = AnimationUtils.loadAnimation(this,R.anim.splash_logo_anim_l);
        //splash_logo_k = AnimationUtils.loadAnimation(this,R.anim.splash_logo_anim_k);
        splash_logo_e = AnimationUtils.loadAnimation(this,R.anim.splash_logo_anim_e);
        splash_logo_money = AnimationUtils.loadAnimation(this,R.anim.splash_logo_anim_coin);

        text_slogan = findViewById(R.id.splash_tv_slogan);
        //image_logo_l = findViewById(R.id.splash_iv_logo_l);
        //image_logo_k = findViewById(R.id.splash_iv_logo_k);
        image_logo_e = findViewById(R.id.splash_iv_logo_e);
        image_logo_money = findViewById(R.id.splash_iv_logo_coin);

        text_slogan.setAnimation(splash_slogan);
        //image_logo_l.setAnimation(splash_logo_l);
        image_logo_money.setAnimation(splash_logo_money);
        //image_logo_k.setAnimation(splash_logo_k);
        image_logo_e.setAnimation(splash_logo_e);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        SharedPreferences sp = getBaseContext().getSharedPreferences("app_use",MODE_PRIVATE);
        final boolean hasAcceptedCGU = sp.getBoolean(getString(R.string.cgu_key),false);
        final boolean hasPassedSlide = sp.getBoolean(getString(R.string.slide_key),false);
        final boolean hasFilledProfil = sp.getBoolean(getString(R.string.profil_key),false);
        final String json = sp.getString("userData","");
        Gson gson = new Gson();
        final Utilisateur user = gson.fromJson(json,Utilisateur.class);


        final int SPLASH_TIMEOUT = 2000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(currentUser == null)
                    startActivity(new Intent(SplashActivity.this, NumberVerificationActivity.class));
                else {
                    if(!hasFilledProfil)
                        startActivity(new Intent(SplashActivity.this, NewProfilActivity.class).putExtra("pays",user.getUser_country()).putExtra("numero",user.getUser_first_number()));
                    else if(!hasAcceptedCGU)
                        startActivity(new Intent(SplashActivity.this, CGUActivity.class));
                    else if(!hasPassedSlide)
                        startActivity(new Intent(SplashActivity.this, SliderActivity.class));
                    else
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}