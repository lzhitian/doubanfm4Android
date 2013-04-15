package com.douban.fm.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class LogicHelper {
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
