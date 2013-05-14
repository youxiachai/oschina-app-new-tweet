package com.youxiachai.oschina.view.adapter;

import java.util.List;

import net.oschina.app.bean.Tweet;
import net.oschina.app.common.StringUtils;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.emilsjolander.components.stickylistheaders.StickyListHeadersAdapter;
import com.youxiachai.onexlistview.XStickyListHeadersView;
import com.youxiachai.oschina.tweet.R;

public class LvTweetAdpater extends BaseAdapter  implements StickyListHeadersAdapter{
	
	private List<Tweet> tweetLv;
	
	public AQuery ctxQuery;
	
	private LayoutInflater mInflater;
	private Activity mAct;
	public LvTweetAdpater(Activity act,List<Tweet> listTweet){
		this.ctxQuery = new AQuery(act);
		this.tweetLv = listTweet;
		this.mInflater = act.getLayoutInflater();
		this.mAct = act;
	}
	
	private XStickyListHeadersView xtv;
	public LvTweetAdpater(Activity act,List<Tweet> listTweet,XStickyListHeadersView stcilv){
		this.ctxQuery = new AQuery(act);
		this.tweetLv = listTweet;
		this.mInflater = act.getLayoutInflater();
		this.mAct = act;
		this.xtv = stcilv;
	}
	

	
	public void update(List<Tweet> tweets){
		tweetLv.addAll(tweets);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return tweetLv.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return tweetLv.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	private View getItemView(View v, int id) {
		return v.findViewById(id);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ItemView itemHolder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_lv_tweet, null);
			itemHolder = new ItemView();
		
			itemHolder.body = getItemView(convertView, R.id.tweetBody);
			itemHolder.image = getItemView(convertView, R.id.tweetImage);
			itemHolder.imageBg = getItemView(convertView, R.id.tweetImageBg);
			itemHolder.imageShow = getItemView(convertView, R.id.imageshow);
		
			itemHolder.commentCount = getItemView(convertView, R.id.tweetCommentCount);

			convertView.setTag(itemHolder);
		}else{
			itemHolder = (ItemView) convertView.getTag();
		}
		
		AQuery itemQy = ctxQuery.recycle(convertView);
		Tweet tweet = tweetLv.get(position);
	
//		String imageUrl = tweet.getImgSmall();
//		
//	
	
//		if(itemQy.shouldDelay(position, convertView, xtv, imageUrl)){
//			Log.d("aquery", "delay");
//		}else{
			
//			itemQy.id(itemHolder.image).image(imageUrl, io);
//			itemQy.id(itemHolder.imageBg).image(imageUrl, io);
//		}
		
		
		
//		itemQy.id(itemHolder.userface).clicked(clickListener)
//		.tag(tweet.getAuthorId());
		itemQy.id(itemHolder.commentCount).text(String.valueOf(tweet.getCommentCount()));
		itemQy.id(itemHolder.body).text(tweet.getBody());
		
		
//itemQy.id(itemHolder.commentButton).clicked(clickListener)
//		.tag(position);
		
		itemQy.id(R.id.tweetBefore).text(
				StringUtils.friendly_time(tweet.getPubDate()));
		itemQy.id(R.id.tweetName).text(tweet.getAuthor());
		String faceURL = tweet.getFace();
		if (faceURL.endsWith("portrait.gif") || StringUtils.isEmpty(faceURL)) {
			itemQy.id(R.id.tweetPortrait).image(R.drawable.widget_dface);
		} else {
			ImageOptions io = new ImageOptions();
			itemQy.id(R.id.tweetPortrait).image(faceURL, io);
		}
		
		return convertView;
	}
	
	
	static class ItemView { // 自定义控件集合
		public View userface;
		public View username;
		public View date;
		public View body;
		public View commentCount;
		public View commentButton;
		public View client;
		public View image;
		public View imageBg;
		public View imageShow;
	}


	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			convertView =  mInflater.inflate(R.layout.item_lv_head, null);
		}
		
		Tweet tweet = tweetLv.get(position);
		String faceURL = tweet.getFace();
		
	
		AQuery headQy = ctxQuery.recycle(convertView);
		ImageOptions io = new ImageOptions();
		
		
		if (faceURL.endsWith("portrait.gif") || StringUtils.isEmpty(faceURL)) {
			headQy.id(R.id.tweetPortrait).image(R.drawable.widget_dface);
		} else {
			headQy.id(R.id.tweetPortrait).image(faceURL, io);
		}
		String time = StringUtils.friendly_time(tweet.getPubDate());
		
		headQy.id( R.id.tweetBefore).text(
				time);
		headQy.id(R.id.tweetName).text(tweet.getAuthor());
		//
		convertView.setTag(tweet.getAuthorId());
//		itemHolder.commentCount = getItemView(convertView,
//				R.id.tweetCommentCount);
//		itemHolder.commentButton = getItemView(convertView,
//				R.id.tweetCommentButton);
//		itemHolder.userface = getItemView(convertView, R.id.tweetPortrait);
//		itemHolder.username = getItemView(convertView, R.id.tweetName);
//		itemQy.id(itemHolder.username).text(tweet.getAuthor());
		
		return convertView;
	}

	@Override
	public long getHeaderId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
}
