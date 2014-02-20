package com.alumnigroup.app;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;

import com.alumnigroup.entity.Cooperation;
import com.alumnigroup.entity.Issue;
import com.alumnigroup.entity.MActivity;
import com.alumnigroup.utils.DataPool;

/**
 * 数据缓存<br>
 * 活动 商务合作 
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
	//商务合作3种数据
	public static final String Key_Business_all = "Business_all";
	public static final String Key_Business_my = "Key_Business_my";
	public static final String Key_Business_favourite = "Business_favourite";
	//话题-校友交流3种数据
	public static final String Key_Communication_all = "Communication_all";
	public static final String Key_Communication_my = "Communication_my";
	public static final String Key_Communication_favourite = "Communication_favourite";

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
	/**获得商务合作all列表 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Cooperation> getBusinessAll(Context context){
		return (ArrayList<Cooperation>)get(context, Key_Business_all);
	}
	/** 保存商务合作all列表*/
	public static void setBusinessAll(Context context,ArrayList<Cooperation> value){
		save(context, Key_Business_all, value);
	}
	/**获得商务合作My列表 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Cooperation> getBusinessMy(Context context){
		return (ArrayList<Cooperation>)get(context, Key_Business_my);
	}
	/** 保存商务合作My列表*/
	public static void setBusinessMy(Context context,ArrayList<Cooperation> value){
		save(context, Key_Business_my, value);
	}
	/**获得商务合作favourite列表 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Cooperation> getBusinessFavourite(Context context){
		return (ArrayList<Cooperation>)get(context, Key_Business_favourite);
	}
	/** 保存商务合作favourite列表*/
	public static void setBusinessFavourite(Context context,ArrayList<Cooperation> value){
		save(context, Key_Business_favourite, value);
	}
	
	/**获得校友交流话题all列表 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Issue> getCommunicationAll(Context context){
		return (ArrayList<Issue>)get(context, Key_Communication_all);
	}
	/** 保存校友交流话题all列表*/
	public static void setCommunicationAll(Context context,ArrayList<Issue> value){
		save(context, Key_Communication_all, value);
	}
	
	/**获得校友交流话题all列表 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Issue> getCommunicationMy(Context context){
		return (ArrayList<Issue>)get(context, Key_Communication_my);
	}
	/** 保存校友交流话题all列表*/
	public static void setCommunicationMy(Context context,ArrayList<Issue> value){
		save(context, Key_Communication_my, value);
	}
	
	/**获得校友交流话题all列表 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Issue> getCommunicationFavourite(Context context){
		return (ArrayList<Issue>)get(context, Key_Communication_favourite);
	}
	/** 保存校友交流话题all列表*/
	public static void setCommunicationFavourite(Context context,ArrayList<Issue> value){
		save(context, Key_Communication_favourite, value);
	}
	
}
