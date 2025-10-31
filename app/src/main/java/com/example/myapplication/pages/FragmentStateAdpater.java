package com.example.myapplication.pages;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * @author Vermouth
 * @date 2024.09.19 19:36
 */
public class FragmentStateAdpater extends FragmentStatePagerAdapter {
    //fragmentAdpater滑动之后还会存在在内存中，不会回收，当前的adapter会回收

    private List<Fragment> fragmentList;

    public FragmentStateAdpater(@NonNull FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragmentList = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
