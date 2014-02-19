package com.alumnigroup.app;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;

import com.alumnigroup.entity.MActivity;
import com.alumnigroup.utils.DataPool;

/**
 * 数据缓存
 * 
 * @author Jayin Ton
 * 
 */
public class AppCache {

	public static final String Cache_Name = "AppCache";
	// 活动的3种数据
	public static final String Key_Activity_all = "Activity_all";
	public static final String Key_Activity_my = "Activity_my";
	public static final String Key_Activity_favourite = "Activity_favourite";

	private static DataPool getDataPool(Context context) {
		return new DataPool(Cache_Name, context);
	}
	
	private static Serializable get(Context context, String key){
		return getDataPool(context).get(key);
	}

	private static boolean save(Context context, String key, Serializable value) {
		DataPool dp = getDataPool(context);
		return dp.put(key, value);
	}
   
	public static void remove(Context context, String key) {
		getDataPool(context).remove(key);
	}
	/**获得活动all列表 */
	@SuppressWarnings("unchecked")
	public static ArrayList<MActivity> getActivityAll(Context context){
		return (ArrayList<MActivity>)get(context, Key_Activity_all);
	}
	/** 保存活动all列表*/
	public static void setActivityAll(Context context,ArrayList<MActivity> value){
		save(context, Key_Activity_all, value);
	}
	/** 获得活动My列表*/
	@SuppressWarnings("unchecked")
	public static ArrayList<MActivity> getActivityMy(Context context){
		return (ArrayList<MActivity>)get(context, Key_Activity_my);
	}
	/** 保存活动My列表*/
	public static void setActivityMy(Context context,ArrayList<MActivity> value){
		save(context, Key_Activity_my, value);
	}
	
	/** 获得活动Favourite列表*/
	@SuppressWarnings("unchecked")
	public static ArrayList<MActivity> getActivityFavourite(Context context){
		return (ArrayList<MActivity>)get(context, Key_Activity_favourite);
	}
	/** 保存活动Favourite列表*/
	public static void setActivityFavourite(Context context,ArrayList<MActivity> value){
		save(context, Key_Activity_favourite, value);
	}
	
	
}
