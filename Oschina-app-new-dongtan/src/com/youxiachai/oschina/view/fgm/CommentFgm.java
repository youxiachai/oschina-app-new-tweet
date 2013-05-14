package com.youxiachai.oschina.view.fgm;

import me.maxwin.view.XListView;
import net.oschina.app.bean.CommentList;
import net.oschina.app.bean.Tweet;
import net.oschina.app.common.StringUtils;
import android.app.v4.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HeaderViewListAdapter;
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.youxiachai.actiontitlebar.ActionTitleBar;
import com.youxiachai.oschina.app.service.ApiService;
import com.youxiachai.oschina.tweet.R;
import com.youxiachai.oschina.view.adapter.LvCommentAdpater;
import com.yoxiachai.oschina.contorll.api.CommentAction;
import com.yoxiachai.oschina.contorll.base.DataState;

public class CommentFgm extends Fragment {
	
	public static CommentFgm newInstance(Bundle b){
		CommentFgm clf = new CommentFgm();
		clf.setArguments(b);
		return clf;
	}
	public XListView commentLV;
	AQuery fgmQy;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fgmView = inflater.inflate(R.layout.fgm_tweetdetails, null, false);
		fgmQy = new AQuery(fgmView);
		commentLV = (XListView) fgmQy.id(R.id.commentLV).getView();
		ProgressBar emptyView = fgmQy.id(android.R.id.empty).getProgressBar();
		commentLV.setEmptyView(emptyView);
		return fgmView;
	}
	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//ActionBarCompat.mActionBar.setTitle("评论");
		if(CommentAction.isTweet(getArguments()) != null){
			Tweet tweet = CommentAction.isTweet(getArguments());
			
			initTweetDetail(tweet);
			if(tweet.getCommentCount() == 0){
				commentLV.getEmptyView().setVisibility(View.GONE);
			}else{
				ApiService.startService(getActivity(), CommentAction.buildMap(tweet.getId(), 3, 0, DataState.RESTORE));
			}
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		ActionTitleBar.getActionBar(getActivity()).setDisplayShowTitleEnabled(true);
		ActionTitleBar.getActionBar(getActivity()).setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ActionTitleBar.getActionBar(getActivity()).setDisplayShowTitleEnabled(false);
		ActionTitleBar.getActionBar(getActivity()).setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
	}
	private void initTweetDetail(Tweet tweet){
		String faceURL = tweet.getFace();
		if (faceURL.endsWith("portrait.gif") || StringUtils.isEmpty(faceURL)) {
			fgmQy.id(R.id.tweetPortrait).image(R.drawable.widget_dface);
		} else {
			fgmQy.id(R.id.tweetPortrait).image(faceURL);
		}
		fgmQy.id(R.id.commentCount).text(tweet.getCommentCount() + "条评论");
		fgmQy.id(R.id.tweetName).text(tweet.getAuthor());
		fgmQy.id(R.id.tweetBody).text(tweet.getBody());
		fgmQy.id(R.id.tweetBefore).text(StringUtils.friendly_time(tweet.getPubDate()));
		fgmQy.id(R.id.tweetCommentButton).gone();
	}
	
	public void updateXlistView(CommentList comments, DataState type){
		switch (type) {
		case LOADMORE:
			HeaderViewListAdapter hlv = (HeaderViewListAdapter) commentLV.getAdapter();
			CommentAction.updateAdapter(comments.getCommentlist(), (LvCommentAdpater) hlv.getWrappedAdapter());
			break;
		case REFRESH:
		case RESTORE:
			CommentAction.initAdapter(getActivity(), comments.getCommentlist(), commentLV);
			break;
		}
	}

}
