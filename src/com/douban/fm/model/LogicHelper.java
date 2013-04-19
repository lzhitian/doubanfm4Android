package com.douban.fm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.douban.fm.util.ContextUtil;
import com.douban.fm.util.DoubanFMConstants;
import com.douban.fm.util.NetHelper;

import android.R.menu;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.AvoidXfermode.Mode;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;

public class LogicHelper {

    private static String lOG_TAG = "LogicHelper";
    public static ArrayList<String> getChannelsList(){
        SharedPreferences sharedPreferences = ContextUtil.getInstance().getSharedPreferences(
                DoubanFMConstants.SHARED_PREF_CHANNEL, ContextUtil.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
        if (!sharedPreferences.contains("flag_isempty")) {
            try {
                String channelsJson = NetHelper.GetMethod(DoubanFMConstants.URL_DOUBANFM_CHANNELS);
                JSONArray jsonArray = new JSONObject(channelsJson).getJSONArray("channels");
                Editor editor = sharedPreferences.edit();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    editor.putString(jsonObject.getString("name"), jsonObject.getString("channel_id"));
                }
                editor.putBoolean("flag_isempty", true);
                editor.commit();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Map<String, ?> channelsMap = sharedPreferences.getAll();
        ArrayList<String> arrayList = new ArrayList<String>();
        for (String key : channelsMap.keySet()) {
            arrayList.add(key);
        }
        arrayList.remove("flag_isempty");
        return arrayList;
    }
    /**
     * 获取必要的网络访问参数：app_name，version，user_id，expire，token；
     * @return 参数字符串
     */
    public static String getIndispensableParams(){
        SharedPreferences sharedPreferences = ContextUtil.getInstance().getSharedPreferences(
                DoubanFMConstants.SHARED_PREF_ACCOUNT, ContextUtil.MODE_PRIVATE);
        StringBuilder sb = new StringBuilder();
        sb.append("?app_name=radio_desktop_win&version=100");
        if(sharedPreferences.getString(DoubanFMConstants.SHARED_PREF_ACCOUNT_USER_ID, null)==null){
            return sb.toString();
        }else {
            sb.append("&user_id=\"").append(sharedPreferences.getString(DoubanFMConstants.SHARED_PREF_ACCOUNT_USER_ID, "")).append("\"")
            .append("&expire=").append(sharedPreferences.getInt(DoubanFMConstants.SHARED_PREF_ACCOUNT_EXPIRE, 0))
            .append("&token=\"").append(sharedPreferences.getString(DoubanFMConstants.SHARED_PREF_ACCOUNT_TOKEN, "")).append("\"");
            return sb.toString();
        }
    }
    
    public static String getSongUrlByID(String sid) {
        SharedPreferences sharedPreferences = ContextUtil.getInstance().getSharedPreferences(
                DoubanFMConstants.SHARED_PREF_FM, ContextUtil.MODE_PRIVATE);
        String songsInforJson = sharedPreferences.getString(DoubanFMConstants.SHARED_PREF_FM_SONGS_INFOR_LIST, "");
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(songsInforJson);
            JSONArray jsonSongsArray = jsonObject.getJSONArray("song");
            for (int i = 0; i < jsonSongsArray.length(); i++) {
                Log.i(lOG_TAG, "sid:"+jsonSongsArray.getJSONObject(i).getString("sid"));
                if (jsonSongsArray.getJSONObject(i).getString("sid").equals(sid)) {
                    return jsonSongsArray.getJSONObject(i).getString("url");
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }
    
    @SuppressLint("CommitPrefEdits")
    public static String setRecentSongs_20(String sid, String type) {
        SharedPreferences sharedPreferences = ContextUtil.getInstance().getSharedPreferences(
                DoubanFMConstants.SHARED_PREF_FM, ContextUtil.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        String recentSongs = sharedPreferences.getString(DoubanFMConstants.SHARED_PREF_FM_RECENT_SONGS, "");
        String [] arrayRecentSongs = recentSongs.split("|");
        if(arrayRecentSongs.length == 20){
            int indexOfSecond = recentSongs.indexOf("|");
            recentSongs = recentSongs.substring(indexOfSecond+1);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(recentSongs).append("|").append(sid).append(":").append(type);
        editor.putString(DoubanFMConstants.SHARED_PREF_FM_RECENT_SONGS, recentSongs);
        return null;
    }
    /**
     * 
     * @param sid 不为空表示有音乐播放。
     * @param type 首次打开，type为n，表示初始播放。type为p，表示有正在播放的。
     * @return
     */
    public static String getNextSongID(String sid, String type) {
        SharedPreferences sharedPreferences = ContextUtil.getInstance().getSharedPreferences(
                DoubanFMConstants.SHARED_PREF_FM, ContextUtil.MODE_PRIVATE);
        StringBuilder urlString = new StringBuilder();
        urlString.append(DoubanFMConstants.URL_DOUBANFM_SONG_ADDR);
        urlString.append(getIndispensableParams());
        if(sid!=null){
            urlString.append("&sid=").append(sid);
        }
        if(!sharedPreferences.getString(DoubanFMConstants.SHARED_PREF_FM_CURRENT_CHANNEL,"").equals("")){
            urlString.append("&channel=").append(sharedPreferences.getString(DoubanFMConstants.SHARED_PREF_FM_CURRENT_CHANNEL, ""));
        }
        if(type.equals(DoubanFMConstants.DOUBAN_FM_TYPE_U)||type.equals(DoubanFMConstants.DOUBAN_FM_TYPE_R)){//喜欢或不喜欢。不需要跳转下首歌
            urlString.append("&type=").append(type);
            String temp_result = NetHelper.GetMethod(urlString.toString());
            Log.i(lOG_TAG, "temp_result_UR"+temp_result);
            return sid;
        }else {//以下需要跳转下首歌
            if(type.equals(DoubanFMConstants.DOUBAN_FM_TYPE_E)){
                setRecentSongs_20(sid, DoubanFMConstants.DOUBAN_FM_TYPE_P);
            }
            if (type.equals(DoubanFMConstants.DOUBAN_FM_TYPE_S)) {
                setRecentSongs_20(sid, DoubanFMConstants.DOUBAN_FM_TYPE_S);
            }
            urlString.append("&type=").append(type);
            String temp_result = NetHelper.GetMethod(urlString.toString());
            Log.i(lOG_TAG, "temp_result"+temp_result);
            
            if(sharedPreferences.getString(DoubanFMConstants.SHARED_PREF_FM_SONGS_LIST, "").equals("")){
                if(type.equals(DoubanFMConstants.DOUBAN_FM_TYPE_N)){
                    urlString.append("&type=").append(DoubanFMConstants.DOUBAN_FM_TYPE_N);
                }else {
                    urlString.append("&type=").append(DoubanFMConstants.DOUBAN_FM_TYPE_P);
                    sharedPreferences.getString(DoubanFMConstants.SHARED_PREF_FM_RECENT_SONGS, "");
                }
                try {
                    String songListJson = NetHelper.GetMethod(urlString.toString());
                    SharedPreferences shared_pref = ContextUtil.getInstance().getSharedPreferences(
                            DoubanFMConstants.SHARED_PREF_FM, ContextUtil.MODE_PRIVATE);
                    Editor mEditor = shared_pref.edit();
                    mEditor.clear();
                    mEditor.putString(DoubanFMConstants.SHARED_PREF_FM_SONGS_INFOR_LIST, songListJson);
                    mEditor.commit();
                    JSONObject jsonObject = new JSONObject(songListJson);
                    JSONArray jsonSongArray = jsonObject.getJSONArray("song");
                    if(jsonObject.getInt("r") == 0){
                        StringBuilder sbSongsIDList = new StringBuilder();
                        for (int i = 0; i < jsonSongArray.length(); i++) {
                            sbSongsIDList.append(jsonSongArray.getJSONObject(i).getString("sid")).append("|");
                        }
                        Editor editor = sharedPreferences.edit();
                        editor.putString(DoubanFMConstants.SHARED_PREF_FM_SONGS_LIST, sbSongsIDList.toString());
                        editor.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                urlString.append("&sid=").append(sid);
                urlString.append("&type=").append(type);
                Log.i(lOG_TAG, NetHelper.GetMethod(urlString.toString()));
            }
            String SongsIDList_str = sharedPreferences.getString(DoubanFMConstants.SHARED_PREF_FM_SONGS_LIST, "");
            Log.i(lOG_TAG, "SongsIDList_str:"+SongsIDList_str);
            String nextSongID = SongsIDList_str.substring(0, SongsIDList_str.indexOf("|"));
            Log.i(lOG_TAG, "SongsIDList_str:"+nextSongID);
            Editor editor = sharedPreferences.edit();
            if(SongsIDList_str.indexOf("|")==SongsIDList_str.length()-1){//最后一个
                editor.clear();
            }else {
                SongsIDList_str = SongsIDList_str.substring(SongsIDList_str.indexOf("|")+1);
                editor.putString(DoubanFMConstants.SHARED_PREF_FM_SONGS_LIST, SongsIDList_str);
            }
            editor.commit();
            return nextSongID;
        }
        
    }
//    private static final String LOGIN_RESUTL = "login_result";
//    private static Handler mHandler;
//    public static boolean loginWithPassword() {
//        
//        mHandler = new Handler(){
//            public void handleMessage(Message Msg){
//                Log.i(lOG_TAG, "123:"+Msg.getData().getString(LOGIN_RESUTL));
//            }
//        };
//        new Thread(){
//            public void run() {
//                super.run();
//                List<NameValuePair> params = new ArrayList<NameValuePair>();
//                params.add(new BasicNameValuePair("email", "zhenchentl@gmail.com"));
//                params.add(new BasicNameValuePair("password", "wykqinghua18"));
//                params.add(new BasicNameValuePair("app_name", "radio_desktop_win"));
//                params.add(new BasicNameValuePair("version", "100"));
//                String login_result = NetHelper.PostMethod(DoubanFMConstants.URL_DOUBANFM_LOGIN, params);
//                Message message = new Message();
//                Bundle bundle = new Bundle();
//                bundle.putString(LOGIN_RESUTL, login_result);
//                message.setData(bundle);
//                mHandler.sendMessage(message);
//            }
//        }.start();
//    }
}
