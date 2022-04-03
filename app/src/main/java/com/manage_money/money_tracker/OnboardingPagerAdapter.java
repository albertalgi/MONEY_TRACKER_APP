package com.manage_money.money_tracker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class OnboardingPagerAdapter extends FragmentPagerAdapter {

    OnboardingFragment p1,p2,p3,p4,p5,p6;

    public OnboardingPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        p1 = new OnboardingFragment();
        p1.setPosition(1);

        p2 = new OnboardingFragment();
        p2.setPosition(2);

        p3 = new OnboardingFragment();
        p3.setPosition(3);

        p4 = new OnboardingFragment();
        p4.setPosition(4);

        p5 = new OnboardingFragment();
        p5.setPosition(5);

        p6 = new OnboardingFragment();
        p6.setPosition(6);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return p1;
            case 1:
                return p2;
            case 2:
                return p3;
            case 3:
                return p4;
            case 4:
                return p5;
            case 5:
                return p6;
        }
        return null;
    }

    /**
     *
     * @return Número de pàgines de l'onboarding
     */
    @Override
    public int getCount() {
        return 6;
    }
}
