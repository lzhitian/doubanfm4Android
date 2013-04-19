package com.douban.fm;

import java.io.IOException;

import com.douban.fm.model.LogicHelper;
import com.douban.fm.util.ContextUtil;
import com.douban.fm.util.DoubanFMConstants;

import android.R.integer;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class MusicOpService extends Service{

    private static String lOG_TAG = "MusicOpService";
    private MediaPlayer mMediaPlayer;
    private String currentSid;
    private static String CURRENTURI = "currenturi";
    
    public static final int DOUBAN_FM_TYPE_B = 0;
    public static final int DOUBAN_FM_TYPE_R = 1;
    public static final int DOUBAN_FM_TYPE_U = 2;
    public static final int DOUBAN_FM_TYPE_S = 3;
    public static final int DOUBAN_FM_TYPE_N = 4;
    public static final int DOUBAN_FM_TYPE_P = 5;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        SharedPreferences sharedPreferences = getSharedPreferences(
                DoubanFMConstants.SHARED_PREF_FM, ContextUtil.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putString(DoubanFMConstants.SHARED_PREF_FM_CURRENT_CHANNEL, "0");
        editor.commit();
        SharedPreferences sharedSongIDList = getSharedPreferences(
                DoubanFMConstants.SHARED_PREF_FM_SONGS_LIST, ContextUtil.MODE_PRIVATE);
        Editor editor2 = sharedSongIDList.edit();
        editor2.putString(DoubanFMConstants.SHARED_PREF_FM_SONGS_LIST, "");
        editor2.commit();
        mMediaPlayer = new MediaPlayer();
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        try {
            mMediaPlayer.reset();
            String action = intent.getAction();
            if(action.equals(DoubanFMConstants.DOUBAN_FM_TYPE_B)){
                //不喜欢，继续下一首
                new Thread(){
                    public void run(){
                        super.run();
                        currentSid = LogicHelper.getNextSongID(currentSid, DoubanFMConstants.DOUBAN_FM_TYPE_B);
                        String mUristr = LogicHelper.getSongUrlByID(currentSid);
                        Message Msg = new Message();
                        Msg.what = DOUBAN_FM_TYPE_B;
                        Bundle bundle = new Bundle();
                        bundle.putString(CURRENTURI, mUristr);
                        Msg.setData(bundle);
                        mHandler.sendMessage(Msg);
                    }
                }.start();
            }
            if (action.equals(DoubanFMConstants.DOUBAN_FM_TYPE_R)) {
                //喜欢，继续播放
                new Thread(){
                    public void run(){
                        super.run();
                        currentSid = LogicHelper.getNextSongID(currentSid, DoubanFMConstants.DOUBAN_FM_TYPE_R);
                    }
                }.start();
            }
            if (action.equals(DoubanFMConstants.DOUBAN_FM_TYPE_U)) {
                //取消喜欢,继续播放
                new Thread(){
                    public void run(){
                        super.run();
                        currentSid = LogicHelper.getNextSongID(currentSid, DoubanFMConstants.DOUBAN_FM_TYPE_U);
                    }
                }.start();
            }
            if(action.equals(DoubanFMConstants.DOUBAN_FM_TYPE_S)){
                //跳过，继续下一首
                new Thread(){
                    public void run(){
                        super.run();
                        currentSid = LogicHelper.getNextSongID(currentSid, DoubanFMConstants.DOUBAN_FM_TYPE_S);
                        String mUristr = LogicHelper.getSongUrlByID(currentSid);
                        Message Msg = new Message();
                        Msg.what = DOUBAN_FM_TYPE_B;
                        Bundle bundle = new Bundle();
                        bundle.putString(CURRENTURI, mUristr);
                        Msg.setData(bundle);
                        mHandler.sendMessage(Msg);
                    }
                }.start();
            }
            if(action.equals(DoubanFMConstants.DOUBAN_FM_TYPE_N)){
                //新开始，播放下一首
                new Thread(){
                    public void run(){
                        super.run();
                        currentSid = LogicHelper.getNextSongID(currentSid, DoubanFMConstants.DOUBAN_FM_TYPE_N);
                        Log.i(lOG_TAG, "currentSid:"+currentSid);
                        String mUristr = LogicHelper.getSongUrlByID(currentSid);
                        Log.i(lOG_TAG, "mUristr:"+mUristr);
                        Message Msg = new Message();
                        Msg.what = DOUBAN_FM_TYPE_B;
                        Bundle bundle = new Bundle();
                        bundle.putString(CURRENTURI, mUristr);
                        Msg.setData(bundle);
                        mHandler.sendMessage(Msg);
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStart(intent, startId);
    }
    
    private Handler mHandler =new Handler(){
        public void handleMessage(Message Msg){
            switch (Msg.what) {
            case DOUBAN_FM_TYPE_B:
            case DOUBAN_FM_TYPE_S:
            case DOUBAN_FM_TYPE_N:
                Bundle bundle = Msg.getData();
                Uri mUri = Uri.parse(bundle.getString(CURRENTURI));
                try {
                    mMediaPlayer.setDataSource(MusicOpService.this, mUri);
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (SecurityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                mMediaPlayer.start();
                break;
            default:
                break;
            }
        }
    };
    @Override
    public void onDestroy() {
        mMediaPlayer.stop();
        super.onDestroy();
    }

}
