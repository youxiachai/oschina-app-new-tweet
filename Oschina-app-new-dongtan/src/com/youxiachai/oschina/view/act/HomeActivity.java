package com.youxiachai.oschina.view.act;

import net.oschina.app.bean.CommentList;
import net.oschina.app.bean.TweetList;
import net.oschina.app.bean.URLs;
import android.app.v4.ActionBar;
import android.app.v4.ActionBar.OnNavigationListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.youxiachai.actiontitlebar.ActionTitleBar;
import com.youxiachai.actiontitlebar.ActionTitleBarOption;
import com.youxiachai.oschina.app.api.Api;
import com.youxiachai.oschina.tweet.R;
import com.youxiachai.oschina.view.fgm.CommentFgm;
import com.youxiachai.oschina.view.fgm.TweetListFgm;
import com.youxiachai.utils.fragment.FragmentUtils;
import com.yoxiachai.oschina.contorll.base.DataState;
import com.yoxiachai.oschina.contorll.base.IAction;

public class HomeActivity extends FragmentActivity {

	// 本地广播接收器
	private LocalBroadcastManager mlocalBroad;
	ActionTitleBarOption ao ;
	
	private static final String[] m = { "最新动弹", "最热动弹" };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_home);
		
		
		ao = new ActionTitleBarOption();
		ao.title = "動彈";
		ImageView refresh = new ImageView(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 0, 5, 0);
		refresh.setImageResource(R.drawable.ic_refresh);
		//refresh.setPadding(20, 20, 20, 20);
		refresh.setLayoutParams(lp);
		refresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ImageView refresh = new ImageView(HomeActivity.this);
				ViewGroup.LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				
				refresh.setImageResource(R.drawable.ic_refresh);
				//refresh.setPadding(20, 20, 20, 20);
				refresh.setLayoutParams(lp);
//				TweetApp.mActionBar.addBarItem(refresh);
				Toast.makeText(getApplicationContext(), "refresh", Toast.LENGTH_SHORT).show();
			}
		});
		
		ao.itemViews.add(refresh);
		ao.homeAsUp = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		};
	ActionTitleBar.getActionBar(this, ao);
	
//	TweetApp.mActionBar  = new ActionBarCompat(this, ao);
		
//	TweetApp.mActionBar.setListNavigationCallbacks(null, null);
	ActionTitleBar.getActionBar(this, ao).setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		ArrayAdapter<String> mSpinnerAdapter = new ArrayAdapter<String>(this,
				R.layout.item_spinner, android.R.id.text1, m);
		ActionTitleBar.getActionBar(this, ao).setDisplayShowTitleEnabled(false);
		ActionTitleBar.getActionBar(this, ao).setListNavigationCallbacks(mSpinnerAdapter, new OnNavigationListener() {
			
			@Override
			public boolean onNavigationItemSelected(int itemPosition, long itemId) {
				Toast.makeText(HomeActivity.this, "" + itemPosition, Toast.LENGTH_SHORT).show();
				
				
				FragmentUtils.renderFgm(android.R.id.content,
						getSupportFragmentManager(), TweetListFgm.newInstance(itemPosition), "tweetlist");
				
				return false;
			}
		});
		
	
	}

	BroadcastReceiver mReceive = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null) {
				DataState ds = (DataState) intent
						.getSerializableExtra(Api.Key.dataState);
				if (intent.getSerializableExtra(URLs.TWEET_LIST) != null) {
					TweetList tweetList = (TweetList) intent
							.getSerializableExtra(URLs.TWEET_LIST);
					TweetListFgm tlf = (TweetListFgm) getSupportFragmentManager().findFragmentByTag("tweetlist");
					tlf.updateXlistView(tweetList.getTweetlist(), ds);
				}else if(intent.getSerializableExtra(URLs.COMMENT_LIST) != null){
					CommentList commentList = (CommentList) intent.getSerializableExtra(URLs.COMMENT_LIST);
					CommentFgm cf = (CommentFgm) getSupportFragmentManager().findFragmentByTag("comment");
					cf.updateXlistView(commentList, ds);
				}else{
					Toast.makeText(HomeActivity.this, "network error",
							Toast.LENGTH_SHORT).show();
				}

				

			}

		}
	};

	@Override
	protected void onResume() {
		super.onResume();
	
		mlocalBroad = LocalBroadcastManager.getInstance(this);
		IntentFilter intentFileter = new IntentFilter(IAction.SEND);
		mlocalBroad.registerReceiver(mReceive, intentFileter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mlocalBroad.unregisterReceiver(mReceive);
	}

}
