package com.yoxiachai.oschina.contorll.api;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import net.oschina.app.bean.TweetList;

import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.Transformer;

public class TweetListTransformer implements Transformer {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T transform(String url, Class<T> type, String encoding,
			byte[] data, AjaxStatus status) {
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		try {
			return (T) TweetList.parse(bis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}



}
