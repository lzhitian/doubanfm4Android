package com.douban.fm.util;
import android.app.Application;
/**
 * 获取Application的Context，方便工具类的资源调用
 * @author Administrator
 *
 */
public class ContextUtil extends Application {
    private static ContextUtil instance;
    
    public static ContextUtil getInstance() {
        return instance;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}