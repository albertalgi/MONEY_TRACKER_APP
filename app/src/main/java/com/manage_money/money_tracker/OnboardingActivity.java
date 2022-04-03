package com.manage_money.money_tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.WindowManager;

import com.manage_money.money_tracker.R;

public class OnboardingActivity extends AppCompatActivity {

    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_layout);
        this.pager = findViewById(R.id.vpMain);
        this.pager.setAdapter(new OnboardingPagerAdapter(getSupportFragmentManager()));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle("WELCOME");
    }
}