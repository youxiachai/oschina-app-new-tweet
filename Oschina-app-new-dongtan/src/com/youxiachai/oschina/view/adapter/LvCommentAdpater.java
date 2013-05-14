package com.youxiachai.oschina.view.adapter;

import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.oschina.app.bean.Comment;
import net.oschina.app.common.StringUtils;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.androidquery.AQuery;
import com.youxiachai.oschina.tweet.R;

public class LvCommentAdpater extends BaseAdapter {
	private List<Comment> commentLv;
	public AQuery ctxQuery;

	private LayoutInflater mInflater;

	public LvCommentAdpater(Activity act, List<Comment> lvComments) {
		this.ctxQuery = new AQuery(act);
		this.commentLv = lvComments;
		this.mInflater = act.getLayoutInflater();
		Collections.sort(commentLv, new Comparator<Comment>() {
			@Override
			public int compare(Comment lhs, Comment rhs) {
				// TODO Auto-generated method stub
				long time = StringUtils.toDate(lhs.getPubDate()).getTime();
				long anTome = StringUtils.toDate(rhs.getPubDate()).getTime();
				if(time > anTome){
					return -1;
				}else if(time < anTome){
					return 1;
				}else{
					return 0;
				}
			
			}
		});
	}
	
	public void update(List<Comment> comments){
		commentLv.addAll(comments);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return commentLv.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return commentLv.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	public View getItemView(View v, int id) {
		return v.findViewById(id);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ItemView itemHolder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_lv_comment, null);
			itemHolder = new ItemView();
			itemHolder.userFace = getItemView(convertView, R.id.face);
			itemHolder.userName = getItemView(convertView, R.id.commentName);
			itemHolder.date = getItemView(convertView, R.id.commentBefore);
			itemHolder.body = getItemView(convertView, R.id.commentBody);
			convertView.setTag(itemHolder);
		}else{
			itemHolder = (ItemView) convertView.getTag();
		}
		
		AQuery itemQy = ctxQuery.recycle(convertView);
		Comment comment = commentLv.get(position);
		String faceURL = comment.getFace();
		if (faceURL.endsWith("portrait.gif") || StringUtils.isEmpty(faceURL)) {
			itemQy.id(itemHolder.userFace).image(R.drawable.widget_dface);
		} else {
			itemQy.id(itemHolder.userFace).image(faceURL);
		}
		itemQy.id(itemHolder.userName).text(comment.getAuthor());
		itemQy.id(itemHolder.body).text(comment.getContent());
		itemQy.id(itemHolder.date).text(StringUtils.friendly_time(comment.getPubDate()));
			
		return convertView;
	}
	
	static class ItemView{
		public View userFace;
		public View userName;
		public View date;
		public View body;
	}

}
