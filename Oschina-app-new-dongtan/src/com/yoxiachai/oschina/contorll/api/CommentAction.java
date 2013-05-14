package com.yoxiachai.oschina.contorll.api;

import java.util.List;

import me.maxwin.view.XListView;
import net.oschina.app.bean.Comment;
import net.oschina.app.bean.CommentList;
import net.oschina.app.bean.Tweet;
import net.oschina.app.bean.URLs;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.youxiachai.ajax.ICallback;
import com.youxiachai.ajax.NetCallback;
import com.youxiachai.ajax.NetOption;
import com.youxiachai.oschina.app.api.Api;
import com.youxiachai.oschina.app.api.ApiUrl;
import com.youxiachai.oschina.app.service.ApiService;
import com.youxiachai.oschina.view.adapter.LvCommentAdpater;
import com.yoxiachai.oschina.contorll.base.BaseAction;
import com.yoxiachai.oschina.contorll.base.DataState;

public class CommentAction extends BaseAction {
	
	
	public static void initAdapter(Activity act, List<Comment> lvComments,XListView mListView){
		mListView.setAdapter(new LvCommentAdpater(act, lvComments));
	}
	
	public static void updateAdapter(List<Comment> lvComments, LvCommentAdpater lvAdapter){
		lvAdapter.update(lvComments);
	}
	
	public static Tweet isTweet(Bundle b){
		return (Tweet) b.getSerializable(URLs.TWEET_LIST);
	}
	
	public static void getApiCommentList(final AQuery ajaxQy, Bundle queryMap){
		DataState dataState = (DataState) queryMap
				.getSerializable(URLs.COMMENT_LIST);
		queryMap.remove(URLs.TWEET_LIST);
		String path = ApiUrl.getCommentList(queryMap);
		NetOption option = new NetOption(path, dataState);
		ICallback<CommentList> iCallback = new ICallback<CommentList>() {

			@Override
			public void onSuccess(CommentList result, Enum<?> type, AjaxStatus status) {
				ApiService.isRunning = false;
				if (status.getCode() == AjaxStatus.NETWORK_ERROR) {
					Toast.makeText(ajaxQy.getContext(),
							"network error" + status.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
				
				Bundle queryMap = new Bundle();
				queryMap.putSerializable(URLs.COMMENT_LIST, result);
				queryMap.putSerializable(Api.Key.dataState, type);
				CommentAction.sendBroadcast(ajaxQy.getContext(), queryMap);
			}

			@Override
			public void onError(int code, String message) {
				// TODO Auto-generated method stub
				
			}
		};
		
		NetCallback<CommentList> call = new NetCallback<CommentList>(CommentList.class, option, iCallback);
		CommentTransformer ctf = new CommentTransformer();
		
		ApiUrl.buildHeader(call);
		
		ajaxQy.transformer(ctf).ajax(call);
	}
	
	public static Bundle buildMap(int id, int catalog, int pageIndex, DataState state){
		Bundle query = new Bundle();
		query.putInt(Api.Query.id, id);
		query.putInt(Api.Query.catalog, catalog);
		query.putInt(Api.Query.pageIndex, pageIndex);
		query.putSerializable(URLs.COMMENT_LIST, state);
		return query;
	}
}
