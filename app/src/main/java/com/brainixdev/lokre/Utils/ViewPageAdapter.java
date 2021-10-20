package com.brainixdev.lokre.Utils;

import com.brainixdev.lokre.Activites.AccountFragment;
import com.brainixdev.lokre.Activites.ItemListFragment;
import com.brainixdev.lokre.Activites.StatisticFragment;
import com.brainixdev.lokre.Activites.WalletFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPageAdapter extends FragmentStatePagerAdapter {

    FragmentManager manager;

    public ViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.manager = fm;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ItemListFragment();
            case 1:
                return new StatisticFragment();
            case 2:
                return new WalletFragment();
            case 3:
                return new AccountFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }


}
