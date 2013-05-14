package com.youxiachai.oschina.view.fgm;

import java.util.Calendar;
import java.util.List;

import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;
import net.oschina.app.bean.Tweet;
import net.oschina.app.bean.URLs;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.huewu.pla.lib.internal.PLA_HeaderViewListAdapter;
import com.youxiachai.onexlistview.XMultiColumnListView;
import com.youxiachai.onexlistview.XStickyListHeadersView;
import com.youxiachai.onexlistview.XStickyListHeadersView.OnHeaderClickListener;
import com.youxiachai.oschina.app.TweetApp;
import com.youxiachai.oschina.app.service.ApiService;
import com.youxiachai.oschina.tweet.R;
import com.youxiachai.oschina.view.adapter.LvTweetAdpater;
import com.youxiachai.utils.fragment.FragmentUtils;
import com.yoxiachai.oschina.contorll.api.TweetAction;
import com.yoxiachai.oschina.contorll.base.DataState;

public class TweetListFgm extends Fragment {
	public final static int NEWEST = 0;
	public final static int HOTEST = -1;
	public final static String TWEETTYPE = "listtype";
	public int currentType = 0;
	public static TweetListFgm newInstance(int type){
		Bundle b = new Bundle();
		if(type == NEWEST){
			b.putInt(TWEETTYPE, type);
		}else{
			b.putInt(TWEETTYPE, HOTEST);
		}
		
		
		TweetListFgm tlf = new TweetListFgm();
		tlf.setArguments(b);
		return tlf;
	}
	
	
	XStickyListHeadersView mXlistView;
	XMultiColumnListView mTablet;
	// 用于判断滚动状态
	int currentFirstVisibleItem;
	View fgmView;

	IXListViewRefreshListener refreshListener = new IXListViewRefreshListener() {

		@Override
		public void onRefresh() {
			if (!ApiService.isRunning) {
				ApiService.startService(getActivity(),
						TweetAction.buildMap(currentType, 0, 20, DataState.REFRESH));
			} 
		}
	};

	IXListViewLoadMore loadMore = new IXListViewLoadMore() {

		@Override
		public void onLoadMore() {

			ApiService.startService(getActivity(), TweetAction.buildMap(currentType,
					++TweetApp.currentIndex, 20, DataState.LOADMORE));
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("fgm", TweetListFgm.class.getSimpleName() + "onCreateView");
		// View tempView = inflater.inflate(R.layout.fgm_tweetlist, null,
		// false);
		// Log.d("fgm", TweetListFgm.class.getSimpleName() + "onCreateView");
		return init(inflater);
	}

	public void initPhone(AQuery aq) {
		mXlistView = (XStickyListHeadersView) aq.id(android.R.id.list)
				.getView();
		ProgressBar emptyView = aq.id(android.R.id.empty).getProgressBar();
		mXlistView.setEmptyView(emptyView);

		aq.id(mXlistView).scrolled(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (currentFirstVisibleItem > firstVisibleItem) {
					// scrolling to top of list
					Log.d("xlistview", "scrolling to top of list");
				} else if (currentFirstVisibleItem < firstVisibleItem) {
					// scrolling to bottom of list
					Log.d("xlistview", "scrolling to bottom of list");
				}

				currentFirstVisibleItem = firstVisibleItem;

			}
		});

		mXlistView.setFastScrollEnabled(true);
		mXlistView.setPullRefreshEnable(refreshListener);

		mXlistView.setPullLoadEnable(loadMore);
		mXlistView.setOnHeaderClickListener(new OnHeaderClickListener() {

			@Override
			public void onHeaderClick(XStickyListHeadersView l, View header,
					int itemPosition, long headerId, boolean currentlySticky) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "" + header.getTag(),
						Toast.LENGTH_SHORT).show();
			}
		});

		mXlistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LvTweetAdpater adapter = (LvTweetAdpater) mXlistView
						.getWrappedAdapter();
				Tweet tweet = (Tweet) adapter.getItem((int) id);

				Bundle b = new Bundle();
				b.putSerializable(URLs.TWEET_LIST, tweet);
				CommentFgm cf = CommentFgm.newInstance(b);
				
				FragmentUtils.addFrgment(android.R.id.content,
						getFragmentManager(), cf, "comment");
			}
		});
	}

	private View init(LayoutInflater inflater) {
		fgmView = inflater.inflate(R.layout.fgm_tweetlist, null);

		AQuery aq = new AQuery(fgmView);
		boolean isTable = getResources().getBoolean(R.bool.has_two_panes);
		if (isTable) {
			initTablet(aq);
		} else {
			initPhone(aq);
		}

		return fgmView;
	}

	private void initTablet(AQuery aq) {
		ProgressBar emptyView = aq.id(android.R.id.empty).getProgressBar();

		mTablet = (XMultiColumnListView) aq.id(android.R.id.list).getView();
		mTablet.setEmptyView(emptyView);

		mTablet.setPullRefreshEnable(refreshListener);

		mTablet.setPullLoadEnable(loadMore);
	}

	public void updateXlistView(List<Tweet> tweets, DataState ds) {
		switch (ds) {
		case LOADMORE:
			Log.d("api", "loadmore" + TweetApp.currentIndex);
			if (mXlistView != null) {
				TweetAction.updateAdapter(tweets,
						(LvTweetAdpater) mXlistView.getWrappedAdapter());
				mXlistView.stopLoadMore();
			}
			
			if(mTablet != null){
				PLA_HeaderViewListAdapter lpv = (PLA_HeaderViewListAdapter) mTablet.getAdapter();
				
				TweetAction.updateAdapter(tweets,
						(LvTweetAdpater) lpv.getWrappedAdapter());
				mTablet.stopLoadMore();
			}

			break;
		case REFRESH:
		case RESTORE:
			Log.d("api", "refresh");
			if (mXlistView != null) {
				TweetAction.initAdapter(getActivity(), tweets, mXlistView);
				mXlistView.stopRefresh(String.format("%1$tF %1$tT",
						Calendar.getInstance()));
			}
			if (mTablet != null) {
				mTablet.setAdapter(new LvTweetAdpater(getActivity(), tweets));
				mTablet.stopRefresh(String.format("%1$tF %1$tT",
						Calendar.getInstance()));
			}

			break;

		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d("fgm", TweetListFgm.class.getSimpleName() + "onCreate");
		currentType = getArguments().getInt(TWEETTYPE);
		ApiService.startService(getActivity(),
				TweetAction.buildMap(currentType, 0, 20, DataState.RESTORE));
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d("fgm", TweetListFgm.class.getSimpleName() + "onPause");
	}

}
