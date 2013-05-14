package com.youxiachai.oschina.app.service;

import net.oschina.app.bean.URLs;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.androidquery.AQuery;
import com.youxiachai.oschina.app.TweetApp;
import com.yoxiachai.oschina.contorll.api.CommentAction;
import com.yoxiachai.oschina.contorll.api.TweetAction;

/**
 * @author youxiachai
 * @date 2013/5/13
 */
public class ApiService extends Service {
	AQuery apiQy;

	public static boolean isRunning;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		// 防止service重启导致的服务出错
		if (intent != null) {
			if (apiQy == null) {
				Log.d("aquery", "init");
				apiQy = new AQuery(this);
			} else {
				Log.d("aquery", "reuse-->" + isRunning);
			}
			Bundle b = intent.getExtras();
			if (b != null) {
				if (b.getSerializable(URLs.TWEET_LIST) != null) {
					//isRunning = true;
					TweetAction.getApiTweetList(apiQy, b);
				}else if(b.getSerializable(URLs.COMMENT_LIST) != null){
					CommentAction.getApiCommentList(apiQy, b);
				}
			}

		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("aquery", "onDestory" + isRunning);
		TweetApp.clean();
	}

	public static void startService(Activity act, Bundle querymap) {
		Intent i = new Intent();
		i.setClass(act, ApiService.class);
		i.putExtras(querymap);
		act.startService(i);
	}
}
