package com.ashish.myteam;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TransactionPageTabAdapter extends FragmentPagerAdapter {

    private int numTabs;
    public TransactionPageTabAdapter(FragmentManager fm, int numTabs) {
        super(fm);
        this.numTabs = numTabs;
        ;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment =  new Debits_Fragment();
                break;
            case 1:
                fragment = new Credits_Fragment();
                break;
            default:
                fragment = null;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
