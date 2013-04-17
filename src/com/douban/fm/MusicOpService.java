package com.douban.fm;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

public class MusicOpService extends Service{
    
    private MediaPlayer mMediaPlayer;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        mMediaPlayer = new MediaPlayer();
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        try {
            mMediaPlayer.reset();
            Uri mUri = intent.getData();
            mMediaPlayer.setDataSource(this, mUri);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (Exception e) {
            // TODO: handle exception
        }
        mMediaPlayer.start();
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        mMediaPlayer.stop();
        super.onDestroy();
    }

}
