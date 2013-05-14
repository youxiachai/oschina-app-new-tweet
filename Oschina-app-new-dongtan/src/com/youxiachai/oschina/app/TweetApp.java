package com.youxiachai.oschina.app;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.youxiachai.oschina.app.service.ApiService;


public class TweetApp extends Application{
	public static int currentIndex = 0;
	public static int currentPageSize = 20;
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	public static void clean(){
		currentIndex = 0;
		currentPageSize = 20;
		ApiService.isRunning = false;
	}
	
	/**
	 * 获取App安装包信息
	 * @return
	 */
	public static PackageInfo getPackageInfo(Context ctx) {
		PackageInfo info = null;
		try { 
			info = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
		} catch (NameNotFoundException e) {    
			e.printStackTrace(System.err);
		} 
		if(info == null) info = new PackageInfo();
		return info;
	}
	public final static String CONF_APP_UNIQUEID = "APP_UNIQUEID";
	/**
	 * 获取App唯一标识
	 * @return
	 */
//	public String getAppId() {
//		String uniqueID = getProperty(CONF_APP_UNIQUEID);
//		if(StringUtils.isEmpty(uniqueID)){
//			uniqueID = UUID.randomUUID().toString();
//			setProperty(CONF_APP_UNIQUEID, uniqueID);
//		}
//		return uniqueID;
//	}
//	
	public static String appUserAgent;
	public static String getUserAgent(Context appContext) {
		if(appUserAgent == null || appUserAgent == "") {
			StringBuilder ua = new StringBuilder("OSChina.NET");
			ua.append("/Android");//手机系统平台
			ua.append("/"+android.os.Build.VERSION.RELEASE);//手机系统版本
			ua.append("/"+android.os.Build.MODEL); //手机型号
//			ua.append("/"+appContext.getAppId());//客户端唯一标识
			appUserAgent = ua.toString();
		}
		return appUserAgent;
	}
}
