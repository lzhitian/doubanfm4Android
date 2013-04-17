package com.douban.fm;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.douban.fm.util.DoubanFMConstants;
import com.douban.fm.util.NetHelper;
import com.slidingmenu.lib.SlidingMenu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

    private static String lOG_TAG = "MainActivity";
    
    private static final int MSG_WHAT_GETCHANNELS = 0;
    private static final String MSG_KEY_GETCHANNELS = "Channels";
    
    private ViewPager viewPager;
    private ArrayList<View> Listviews;
    private ArrayList<String> titles;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frontpage);
        setBehindContentView(R.layout.menu_frame);
        getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.menu_frame, new ChannelFragment())
        .commit();
        
        initDoubanFM();
        
    }
    private void initDoubanFM(){
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        viewPager.setAdapter(mPagerAdapter);
        findViewById(R.id.btn_like_dislike).setOnClickListener(mOnClickListener);
        findViewById(R.id.btn_del).setOnClickListener(mOnClickListener);
        findViewById(R.id.btn_indicator).setOnClickListener(mOnClickListener);
        findViewById(R.id.btn_next).setOnClickListener(mOnClickListener);
        findViewById(R.id.btn_share).setOnClickListener(mOnClickListener);
        
        
        LayoutInflater mLi = LayoutInflater.from(this);
        Listviews = new ArrayList<View>();
        Listviews.add(mLi.inflate(R.layout.viewpage, null));
        Listviews.add(mLi.inflate(R.layout.viewpage, null));
        Listviews.add(mLi.inflate(R.layout.viewpage, null));
        
        titles = new ArrayList<String>();
        titles.add("tab1");
        titles.add("tab2");
        titles.add("tab3");
        //设置viewpager的滑动事件
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
    private OnClickListener mOnClickListener = new OnClickListener() {
        
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
            case R.id.btn_del:
                stopService(new Intent(MainActivity.this,MusicOpService.class));
                break;
            case R.id.btn_like_dislike:
                startService(new Intent(MainActivity.this,MusicOpService.class));
                
//                new Thread(){
//                    public void run() {
//                        super.run();
//                        List<NameValuePair> params = new ArrayList<NameValuePair>();
////                        params.add(new BasicNameValuePair("email", "zhenchentl@gmail.com"));
////                        params.add(new BasicNameValuePair("password", "wykqinghua18"));
////                        params.add(new BasicNameValuePair("app_name", "radio_desktop_win"));
////                        params.add(new BasicNameValuePair("version", "100"));
////                        String login_result = NetHelper.PostMethod(DoubanFMConstants.URL_DOUBANFM_LOGIN, params);
//                        
//                        params.add(new BasicNameValuePair("type", "n"));
////                      params.add(new BasicNameValuePair("password", "wykqinghua18"));
//                        params.add(new BasicNameValuePair("app_name", "radio_desktop_win"));
//                        params.add(new BasicNameValuePair("version", "100"));
//                        params.add(new BasicNameValuePair("channel", "0"));
//                        String login_result = NetHelper.PostMethod(DoubanFMConstants.URL_DOUBANFM_SONG_ADDR, params);
//                        Message message = new Message();
////                        message.what = 
//                        Bundle bundle = new Bundle();
//                        bundle.putString(ACCOUNT_SERVICE, login_result);
//                        message.setData(bundle);
//                        mHandler.sendMessage(message);
//                    }
//                }.start();
                
                break;

            default:
                break;
            }
        }
    };
    
    private Handler mHandler = new Handler(){
        public void handleMessage(Message Msg){
            switch (Msg.what) {
            case MSG_WHAT_GETCHANNELS:
                ArrayList<String> channels = Msg.getData().getStringArrayList(MSG_KEY_GETCHANNELS);
                if(channels==null){
                    Toast.makeText(MainActivity.this, R.string.err_getchannels, Toast.LENGTH_SHORT).show();
                }else {
                    
                }
                break;

            default:
                break;
            }
        }
    };
    
    public void swichChannel(String channelID) {
        Log.i(lOG_TAG, "getchannelinmain:"+channelID);
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
