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

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.AvoidXfermode.Mode;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

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
        sb.append("?app_name=\"radio_desktop_win\"&version=100");
        if(sharedPreferences.getString(DoubanFMConstants.SHARED_PREF_ACCOUNT_USER_ID, null)==null){
            return sb.toString();
        }else {
            sb.append("&user_id=\"").append(sharedPreferences.getString(DoubanFMConstants.SHARED_PREF_ACCOUNT_USER_ID, "")).append("\"")
            .append("&expire=").append(sharedPreferences.getInt(DoubanFMConstants.SHARED_PREF_ACCOUNT_EXPIRE, 0))
            .append("&token=\"").append(sharedPreferences.getString(DoubanFMConstants.SHARED_PREF_ACCOUNT_TOKEN, "")).append("\"");
            return sb.toString();
        }
    }
    
    public static String setRecentSongs_20(String sid, String type) {
        SharedPreferences sharedPreferences = ContextUtil.getInstance().getSharedPreferences(
                DoubanFMConstants.SHARED_PREF_FM, ContextUtil.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        return null;
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
