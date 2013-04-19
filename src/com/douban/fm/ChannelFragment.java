package com.douban.fm;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.douban.fm.model.LogicHelper;
import com.douban.fm.util.ContextUtil;
import com.douban.fm.util.DoubanFMConstants;
import com.douban.fm.util.NetHelper;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Administrator
 *
 */
public class ChannelFragment extends ListFragment{

    private static String lOG_TAG = "ChannelFragment";
    
    private static final int MSG_WHAT_GETCHANNELS = 0;
    private static final String MSG_KEY_GETCHANNELS = "Channels";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
        Log.i(lOG_TAG, "oncreateView");
        return inflater.inflate(R.layout.behindpage, null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String channelName = ((TextView)v).getText().toString();
        SharedPreferences sharedPreferences = ContextUtil.getInstance().getSharedPreferences(
                DoubanFMConstants.SHARED_PREF_CHANNEL, ContextUtil.MODE_PRIVATE);
        String channelID = sharedPreferences.getString(channelName, "0");
        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.swichChannel(channelID);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
      //获取频道列表
        Log.i(lOG_TAG, "onActivitycreated");
        new Thread(){
            public void run(){
                super.run();
                Log.i(lOG_TAG, "run()");
                Message message = new Message();
                message.what = MSG_WHAT_GETCHANNELS;
                Bundle bundle = new Bundle();
                ArrayList<String> channels = LogicHelper.getChannelsList();
                bundle.putStringArrayList(MSG_KEY_GETCHANNELS, channels);
                message.setData(bundle);
                mHandler.sendMessage(message);
            }
        }.start();
    }
    private Handler mHandler = new Handler(){
        public void handleMessage(Message Msg){
            switch (Msg.what) {
            case MSG_WHAT_GETCHANNELS:
                ArrayList<String> channelsList = Msg.getData().getStringArrayList(MSG_KEY_GETCHANNELS);
                if(channelsList==null){
                    Log.i(lOG_TAG, "channels_null");
                }else {
                  String[] channels = (String[])channelsList.toArray(new String[channelsList.size()]);
                  ArrayAdapter<String> channelsAdapter = new ArrayAdapter<String>(getActivity(), 
                          android.R.layout.simple_list_item_1, android.R.id.text1, channels);
                  setListAdapter(channelsAdapter);
                }
                break;

            default:
                break;
            }
        }
    };
}
