package com.brainixdev.lokre.Activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brainixdev.lokre.R;
import com.brainixdev.lokre.Utils.AdaptateurSlides;
import com.brainixdev.lokre.Utils.Slides;

import java.util.ArrayList;

public class SliderActivity extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout dotsLayout;
    AdaptateurSlides sliderAdapter;
    TextView[] dots;
    Button  bt_suivant, bt_precedent, bt_passer;
    com.google.android.material.button.MaterialButton bt_commencer;
    Animation animation;
    int pos, previous_pos;

    ArrayList <Slides> elements = new ArrayList<>();

    SharedPreferences sp ;
    SharedPreferences.Editor edit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        sp = getBaseContext().getSharedPreferences("app_use",MODE_PRIVATE);
        edit = sp.edit();
        viewPager = findViewById(R.id.viewPager);
        dotsLayout = findViewById(R.id.dots);
        bt_commencer = findViewById(R.id.bt_commencer_slider);
        bt_suivant = findViewById(R.id.bt_suivant_slider);
        bt_precedent = findViewById(R.id.bt_precendent_slider);
        bt_passer = findViewById(R.id.bt_passer_slider);

        charger();

        sliderAdapter = new AdaptateurSlides(this,elements);

        addDots(0);
        viewPager.setAdapter(sliderAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(changeListener);

        bt_commencer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passer();
            }
        });

        bt_passer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passer();
            }
        });
    }

    public void charger()
    {

        int[] titres = {R.string.tv_titre_slide1, R.string.tv_titre_slide2, R.string.tv_titre_slide3, R.string.tv_titre_slide4};
        int[] desc = {R.string.tv_description_slide1, R.string.tv_description_slide2, R.string.tv_description_slide3, R.string.tv_description_slide4};
        int[] images = {R.drawable.slide1, R.drawable.slide2, R.drawable.slide3, R.drawable.slide4};

        for(int i=0;i<images.length;i++)
        {
            Slides item=new Slides();
            item.setIdImage(images[i]);
            item.setTitre(getResources().getString(titres[i]));
            item.setDescription(getResources().getString(desc[i]));
            elements.add(item);
        }
    }

    public void passer() {
        edit.putBoolean(getString(R.string.slide_key),true);
        edit.apply();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void suivant(View view) {
        viewPager.setCurrentItem(pos + 1);
    }

    public void precedent(View view) {
        viewPager.setCurrentItem(pos - 1);
    }

    private void addDots(int position) {

        dots = new TextView[4];
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("•"));
            dots[i].setTextSize(35);

            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.button_background));
        }

    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            int p = position +1 ;
            pos = position;

            if(p == dots.length && previous_pos ==(dots.length-1)) {
                show_animation();
                bt_suivant.setVisibility(View.GONE);
                bt_passer.setVisibility(View.GONE);
            }
            else if(p == (dots.length-1) && previous_pos == dots.length) {
                hide_animation();
                bt_suivant.setVisibility(View.VISIBLE);
                bt_passer.setVisibility(View.VISIBLE);
            }
            else if(pos > 0)
                bt_precedent.setVisibility(View.VISIBLE);
            else
                bt_precedent.setVisibility(View.GONE);

            previous_pos = p;
        }

        @Override
        public void onPageScrollStateChanged(int state) { }
    };

    public void show_animation()
    {
        Animation show = AnimationUtils.loadAnimation(this, R.anim.bt_commencer_rise);

        bt_commencer.startAnimation(show);

        show.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                bt_commencer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                bt_commencer.clearAnimation();
            }

        });
    }

    public void hide_animation()
    {
        Animation hide = AnimationUtils.loadAnimation(this, R.anim.bt_commencer_fall);

        bt_commencer.startAnimation(hide);

        hide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                bt_commencer.clearAnimation();
                bt_commencer.setVisibility(View.GONE);
            }

        });


    }
}