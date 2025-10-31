package com.example.myapplication.pages;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;

public class ViewpagerBottomNavacationView extends AppCompatActivity {
    private ViewPager viewPager;
    private FragmentStateAdpater fragmentStateAdpater;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager_bottom_navacation_view);
        viewPager = findViewById(R.id.vp);
        bottomNavigationView = findViewById(R.id.bottom_menu);

        // 构建可滑动的 Fragment 列表
        ArrayList<Fragment> fragments = new ArrayList<>(
                Arrays.asList(new HomeFragment(), new FindFragment(), new MineFragment())
        );
        fragmentStateAdpater = new FragmentStateAdpater(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(fragmentStateAdpater);
        viewPager.setOffscreenPageLimit(fragments.size());

        // ViewPager 滑动时联动底部导航选中状态
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.home);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.find);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.mine);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // 底部导航点击时联动切换 ViewPager 页面
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    viewPager.setCurrentItem(0, true);
                    return true;
                } else if (item.getItemId() == R.id.find) {
                    viewPager.setCurrentItem(1, true);
                    return true;
                } else if (item.getItemId() == R.id.mine) {
                    viewPager.setCurrentItem(2, true);
                    return true;
                }
                return false;
            }
        });
    }
}