package com.douban.fm;


import java.util.ArrayList;

import com.slidingmenu.lib.SlidingMenu;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends BaseActivity {

    private ViewPager viewPager;
    private ArrayList<View> Listviews;
    private ArrayList<String> titles;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frontpage);
        setBehindContentView(new TextView(this));
        
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        viewPager.setAdapter(mPagerAdapter);
        
        LayoutInflater mLi = LayoutInflater.from(this);
        Listviews = new ArrayList<View>();
        Listviews.add(mLi.inflate(R.layout.viewpage, null));
        Listviews.add(mLi.inflate(R.layout.viewpage, null));
        Listviews.add(mLi.inflate(R.layout.viewpage, null));
        
        titles = new ArrayList<String>();
        titles.add("tab1");
        titles.add("tab2");
        titles.add("tab3");
        titles.add("tab4");
        
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) { }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) { }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                case 0:
                    getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    break;
                default:
                    getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
                    break;
                }
            }

        });
        
        viewPager.setCurrentItem(0);
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
    }
    
    PagerAdapter mPagerAdapter = new PagerAdapter() {
        
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
        
        @Override
        public int getCount() {
            return Listviews.size();
        }
        
        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager)container).removeView(Listviews.get(position));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager)container).addView(Listviews.get(position));
            return Listviews.get(position);
        }
    };
}
