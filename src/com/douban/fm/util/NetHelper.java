package com.douban.fm.util;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
/**
 * 封装Get、Post方法。
 * @author Administrator
 *
 */
public class NetHelper {

    private static final String LOG_TAG = "NetHelper";

    private static final String COOKIE = "Cookie";
    
    public static String PostMethod(String url,List<NameValuePair> params){
        HttpPost httpRequest = new HttpPost(url);
//        httpRequest.addHeader(COOKIE, LogicHelper.getCookieFromPreference());
        try {
            httpRequest.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
            DefaultHttpClient defaultHttpClient=new DefaultHttpClient();
//            defaultHttpClient.setCookieStore(LogicHelper.getCookieFromPreference());
            HttpResponse httpResponse=defaultHttpClient.execute(httpRequest);
            
            if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String strResult = EntityUtils.toString(httpResponse.getEntity());
                return strResult;
            }else {
                return null;
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i(LOG_TAG, "UnsupportedEncodingException"+e.toString());
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i(LOG_TAG, "ClientProtocolException"+e.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            
            Log.i(LOG_TAG, "IOException"+e.toString());
        }
        return null;
    }
    
    public static String GetMethod(String url){

        HttpGet httpRequest = new HttpGet(url);
//        httpRequest.addHeader(COOKIE, LogicHelper.getCookieFromPreference());
        try {
            DefaultHttpClient defaultHttpClient=new DefaultHttpClient();
            HttpResponse httpResponse=defaultHttpClient.execute(httpRequest);
            if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String strResult = EntityUtils.toString(httpResponse.getEntity());
                return strResult;
            }else {
                return null;
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i(LOG_TAG, "UnsupportedEncodingException"+e.toString());
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i(LOG_TAG, "ClientProtocolException"+e.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i(LOG_TAG, "IOException"+e.toString());
        }
        return null;
    }
}
