package com.youxiachai.oschina.app.api;

import net.oschina.app.bean.URLs;
import android.os.Bundle;
import android.util.Log;

import com.androidquery.callback.AjaxCallback;
import com.youxiachai.api.ApiCommon;

public abstract class ApiUrl {
	
	public static void buildHeader(AjaxCallback<?> callback){
		callback.header("Host", URLs.HOST);
		callback.header("Connection", "Keep-Alive");
		callback.header("User-Agent",
				"	Mozilla/5.0 (Windows NT 6.1; WOW64; rv:20.0) Gecko/20100101 Firefox/20.0");
		
	}
	
	public static String getTweetList(Bundle query) {
		Log.d("api", URLs.TWEET_LIST + ApiCommon.encodeUrl(query));
		return URLs.TWEET_LIST + ApiCommon.encodeUrl(query);
	}
	
	public static String getCommentList(Bundle query){
		Log.d("api", URLs.COMMENT_LIST + ApiCommon.encodeUrl(query));
		return  URLs.COMMENT_LIST + ApiCommon.encodeUrl(query);
	}
	
}
