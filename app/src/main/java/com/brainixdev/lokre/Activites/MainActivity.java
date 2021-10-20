package com.brainixdev.lokre.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;

import com.brainixdev.lokre.R;
import com.brainixdev.lokre.Utils.ViewPageAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ViewPager vp;
    BottomNavigationView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();

        vp = findViewById(R.id.main_viewpager);
        vp.setOffscreenPageLimit(3);
        ViewPageAdapter vpa = new ViewPageAdapter(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                menu.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp.setAdapter(vpa);

        menu = findViewById(R.id.bottom_nav_bar);
        menu.setItemIconTintList(null);
        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment f=null;
                switch (item.getItemId()){
                    case R.id.menu_item_dashboard:
                        vp.setCurrentItem(0);
                        break;
                    case R.id.menu_item_history:
                        vp.setCurrentItem(1);
                        break;
                    case R.id.menu_item_wallet:
                        vp.setCurrentItem(2);
                        break;
                    case R.id.menu_item_user:
                        vp.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });
    }
}