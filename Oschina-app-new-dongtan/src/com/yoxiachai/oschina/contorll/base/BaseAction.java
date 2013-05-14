package com.yoxiachai.oschina.contorll.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

public class BaseAction {
	

	public static void sendBroadcast(Context ctx, Bundle queryMap){
		LocalBroadcastManager lb = LocalBroadcastManager.getInstance(ctx);
		Intent i = new Intent();
		i.setAction(IAction.SEND);
		i.putExtras(queryMap);
		lb.sendBroadcast(i);
	}
}
