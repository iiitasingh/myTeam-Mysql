package com.ashish.myteam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class Transaction extends AppCompatActivity {

    TabLayout TransTabLayout;
    TabItem debit,credit;
    ViewPager TransViewPager;
    TransactionPageTabAdapter transactionPageAdapter;
    String profileOwner;
    User userTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction);

        userTrans = list_test.profileUser.get(0);

        profileOwner = userTrans.email;

        TransTabLayout = findViewById(R.id.TransactionTablayout);
        debit = findViewById(R.id.debits);
        credit = findViewById(R.id.credits);
        TransViewPager = findViewById(R.id.TransactionViewPager);

        transactionPageAdapter = new TransactionPageTabAdapter(getSupportFragmentManager(),TransTabLayout.getTabCount());
        TransViewPager.setAdapter(transactionPageAdapter);
        TransViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(TransTabLayout));

        TransTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TransViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
