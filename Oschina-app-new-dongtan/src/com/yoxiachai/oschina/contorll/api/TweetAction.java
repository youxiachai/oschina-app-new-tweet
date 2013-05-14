package com.yoxiachai.oschina.contorll.api;

import java.util.List;

import net.oschina.app.bean.Tweet;
import net.oschina.app.bean.TweetList;
import net.oschina.app.bean.URLs;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.youxiachai.ajax.ICallback;
import com.youxiachai.ajax.NetCallback;
import com.youxiachai.ajax.NetOption;
import com.youxiachai.onexlistview.XStickyListHeadersView;
import com.youxiachai.oschina.app.api.Api;
import com.youxiachai.oschina.app.api.ApiUrl;
import com.youxiachai.oschina.app.service.ApiService;
import com.youxiachai.oschina.view.adapter.LvTweetAdpater;
import com.yoxiachai.oschina.contorll.base.BaseAction;
import com.yoxiachai.oschina.contorll.base.DataState;

public class TweetAction extends BaseAction {

	public static void initAdapter(Activity act, List<Tweet> lvTweets,
			XStickyListHeadersView mXlistView) {
		if (lvTweets != null) {
			mXlistView
					.setAdapter(new LvTweetAdpater(act, lvTweets, mXlistView));
		}

	}
	
	
	
	public static void updateAdapter(List<Tweet> lvTweets,
			LvTweetAdpater lvAdapter) {
		if (lvTweets != null && lvAdapter != null) {
			lvAdapter.update(lvTweets);
		}

	}

	public static Bundle buildMap(int uid, int index, int pageSize, DataState ds) {
		Bundle querymap = new Bundle();
		querymap.putInt(Api.Query.uid, uid);
		querymap.putInt(Api.Query.pageIndex, index);
		querymap.putInt(Api.Query.pageSize, pageSize);
		querymap.putSerializable(URLs.TWEET_LIST, ds);
		return querymap;
	}

	public static void getApiTweetList(final AQuery query, Bundle queryMap) {
		DataState dataState = (DataState) queryMap
				.getSerializable(URLs.TWEET_LIST);
		//remove not use query param
		queryMap.remove(URLs.TWEET_LIST);
		String path = ApiUrl.getTweetList(queryMap);
		NetOption option = new NetOption(path, dataState);

		NetCallback<TweetList> netCallback = new NetCallback<TweetList>(
				TweetList.class, option, new ICallback<TweetList>() {

					@Override
					public void onSuccess(TweetList result, Enum<?> type,
							AjaxStatus status) {
						// TODO Auto-generated method stub
						ApiService.isRunning = false;
						if (status.getCode() == AjaxStatus.NETWORK_ERROR) {
							Toast.makeText(query.getContext(),
									"network error" + status.getMessage(),
									Toast.LENGTH_SHORT).show();
						}

						Bundle queryMap = new Bundle();
						queryMap.putSerializable(URLs.TWEET_LIST, result);
						queryMap.putSerializable(Api.Key.dataState, type);
						TweetAction.sendBroadcast(query.getContext(), queryMap);
					}

					@Override
					public void onError(int code, String message) {
						// TODO Auto-generated method stub

					}
				});
		TweetListTransformer ttf = new TweetListTransformer();

		switch (dataState) {
		case REFRESH:
			netCallback.refresh(true);
			break;
		case LOADMORE:
		case RESTORE:
			netCallback.expire(1000 * 60 * 1000);
			break;
		}
		ApiUrl.buildHeader(netCallback);
		// if(cookie != null){
		// for (Cookie x : cookie) {
		// Log.d("api", x.getName()+"----"+ x.getValue());
		// netCallback.cookie(x.getName(), x.getValue());
		// }
		// }
		query.transformer(ttf).ajax(netCallback);

	}
}
