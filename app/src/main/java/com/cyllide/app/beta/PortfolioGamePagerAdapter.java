package com.cyllide.app.beta;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.cyllide.app.beta.portfolio.PortfolioGameFragment;
import com.cyllide.app.beta.portfolio.PortfolioLeaderboardFragment;

public class PortfolioGamePagerAdapter extends FragmentPagerAdapter {
    Context context;

    public PortfolioGamePagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context = context;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new PortfolioGameFragment(context);
            case 1:
                return new PortfolioGamePositionsFragment(context);
            case 2:
                return new PortfolioLeaderboardFragment(context);
        }
        return null;

    }


    @Override
    public int getCount() {
        return 3;
    }
}
