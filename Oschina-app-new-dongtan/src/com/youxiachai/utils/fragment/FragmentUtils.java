package com.youxiachai.utils.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentUtils {
	public static void replaceDefault(int toReplaceId, FragmentManager fm,
			Fragment showfragment) {
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(toReplaceId, showfragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.addToBackStack(null);
		ft.commit();
	}
	
	public static void addFrgment(int toReplaceId, FragmentManager fm,
			Fragment showfragment){
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(toReplaceId, showfragment);
		ft.addToBackStack(null);
		ft.commit();
	}
	
	public static void addFrgment(int toReplaceId, FragmentManager fm,
			Fragment showfragment, String tag){
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(toReplaceId, showfragment , tag);
		ft.addToBackStack(null);
		ft.commit();
	}
	
	public static void renderFgm(int toReplaceId, FragmentManager fm,
			Fragment showfragment){
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(toReplaceId, showfragment);
		ft.disallowAddToBackStack();
		ft.commit();
	}
	
	public static void renderFgm(int toReplaceId, FragmentManager fm,
			Fragment showfragment, String tag){
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(toReplaceId, showfragment, tag);
		ft.disallowAddToBackStack();
		ft.commit();
	}
}
