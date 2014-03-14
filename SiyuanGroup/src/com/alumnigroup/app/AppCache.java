package com.alumnigroup.app;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

import android.content.Context;

import com.alumnigroup.entity.Cooperation;
import com.alumnigroup.entity.Dynamic;
import com.alumnigroup.entity.Issue;
import com.alumnigroup.entity.MActivity;
import com.alumnigroup.entity.MGroup;
import com.alumnigroup.entity.User;
import com.alumnigroup.utils.DataPool;

/**
 * 数据缓存<br>
 * 活动 商务合作 话题交流 圈子
 * 
 * @author Jayin Ton
 * 
 */
public class AppCache {

	public static final String Cache_Name = "AppCache";
	// 全站会员3中数据
	public static final String Key_Allmember_all = "Allmember_all";
	public static final String Key_Allmember_following = "Allmember_following";
	public static final String Key_Allmember_followers = "Allmember_followers";
	// 活动的3种数据
	public static final String Key_Activity_all = "Activity_all";
	public static final String Key_Activity_my = "Activity_my";
	public static final String Key_Activity_favourite = "Activity_favourite";
	// 商务合作3种数据
	public static final String Key_Business_all = "Business_all";
	public static final String Key_Business_my = "Key_Business_my";
	public static final String Key_Business_favourite = "Business_favourite";
	// 话题-校友交流3种数据
	public static final String Key_Communication_all = "Communication_all";
	public static final String Key_Communication_my = "Communication_my";
	public static final String Key_Communication_favourite = "Communication_favourite";
	// 圈子3种数据
	public static final String Key_Group_all = "Group_all";
	public static final String Key_Group_my = "Group_my";
	public static final String Key_Group_favourite = "Group_favourite";
	// 动态2种
	public static final String Key_Dynamic_all = "Dynamic_all";
	public static final String Key_Dynamic_friend= "Dynamic_friend";

	private static DataPool getDataPool(Context context) {
		return new DataPool(Cache_Name, context);
	}

	public static Serializable get(Context context, String key) {
		return getDataPool(context).get(key);
	}

	public static boolean save(Context context, String key, Serializable value) {
		DataPool dp = getDataPool(context);
		return dp.put(key, value);
	}

	public static void remove(Context context, String key) {
		getDataPool(context).remove(key);
	}
	
	/**
	 * 改变全站会员的一个用户数据
	 */
	public static void changeAllmemberAll(Context context,User change){
		ArrayList<User> all = getAllmemberAll(context);
		for(int i=0;i<all.size();i++){
			if(all.get(i).getId()==change.getId()){
				all.set(i, change);
			}
		}
		setAllmemberAll(context, all);
	}
	/**
	 * 修改一个圈子的资料
	 * @param context
	 * @param change
	 */
	public static void changeGroupItem(Context context,MGroup change){
		ArrayList<MGroup> group_all = getGroupAll(context);
		ArrayList<MGroup> group_my = getGroupMy(context);
		for(int i=0;i<group_all.size();i++){
			MGroup g = group_all.get(i);
			if(g.getId() == change.getId()){
				group_all.set(i, change);
			}
		}
		for(int i=0;i<group_my.size();i++){
			MGroup g = group_my.get(i);
			if(g.getId() == change.getId()){
				group_my.set(i, change);
			}
		}
	}

	/** 获得全站会员all列表 */
	@SuppressWarnings("unchecked")
	public static ArrayList<User> getAllmemberAll(Context context) {
		return (ArrayList<User>) get(context, Key_Allmember_all);
	}

	/** 保存全站会员all列表 */
	public static void setAllmemberAll(Context context, ArrayList<User> value) {
		save(context, Key_Allmember_all, value);
	}

	/**获得全站会员follow关注列表 */
	@SuppressWarnings("unchecked")
	public static ArrayList<User> getAllmemberFollowing(Context context) {
		return (ArrayList<User>) get(context, Key_Allmember_following);
	}

	/** 删除关注的一个**/
	public static void removeAllmemberFollowing(Context context,User remove){
		ArrayList<User> followers = getAllmemberFollowers(context);
		for(int i=0;i<followers.size();i++){
			if(followers.get(i).getId()==remove.getId()){
				followers.remove(i);
			}
		}
		setAllmemberFollowing(context, followers);
	}
	
	/** 保存全站会员follow关注列表*/
	public static void setAllmemberFollowing(Context context,ArrayList<User> value){
		save(context, Key_Allmember_following, value);
	}

	/** 获得全站会员followers关注列表 */
	@SuppressWarnings("unchecked")
	public static ArrayList<User> getAllmemberFollowers(Context context) {
		return (ArrayList<User>) get(context, Key_Allmember_followers);
	}

	/** 保存全站会员followers关注列表 */
	public static void setAllmemberFollowers(Context context,
			ArrayList<User> value) {
		save(context, Key_Allmember_followers, value);
	}

	/** 获得活动all列表 */
	@SuppressWarnings("unchecked")
	public static ArrayList<MActivity> getActivityAll(Context context) {
		return (ArrayList<MActivity>) get(context, Key_Activity_all);
	}

	/** 保存活动all列表 */
	public static void setActivityAll(Context context,
			ArrayList<MActivity> value) {
		save(context, Key_Activity_all, value);
	}

	/** 获得活动My列表 */
	@SuppressWarnings("unchecked")
	public static ArrayList<MActivity> getActivityMy(Context context) {
		return (ArrayList<MActivity>) get(context, Key_Activity_my);
	}

	/** 保存活动My列表 */
	public static void setActivityMy(Context context, ArrayList<MActivity> value) {
		save(context, Key_Activity_my, value);
	}

	/** 获得活动Favourite列表 */
	@SuppressWarnings("unchecked")
	public static ArrayList<MActivity> getActivityFavourite(Context context) {
		return (ArrayList<MActivity>) get(context, Key_Activity_favourite);
	}

	/** 保存活动Favourite列表 */
	public static void setActivityFavourite(Context context,
			ArrayList<MActivity> value) {
		save(context, Key_Activity_favourite, value);
	}

	/** 获得商务合作all列表 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Cooperation> getBusinessAll(Context context) {
		return (ArrayList<Cooperation>) get(context, Key_Business_all);
	}

	/** 保存商务合作all列表 */
	public static void setBusinessAll(Context context,
			ArrayList<Cooperation> value) {
		save(context, Key_Business_all, value);
	}

	/** 获得商务合作My列表 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Cooperation> getBusinessMy(Context context) {
		return (ArrayList<Cooperation>) get(context, Key_Business_my);
	}

	/** 保存商务合作My列表 */
	public static void setBusinessMy(Context context,
			ArrayList<Cooperation> value) {
		save(context, Key_Business_my, value);
	}

	/** 获得商务合作favourite列表 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Cooperation> getBusinessFavourite(Context context) {
		return (ArrayList<Cooperation>) get(context, Key_Business_favourite);
	}

	/** 保存商务合作favourite列表 */
	public static void setBusinessFavourite(Context context,
			ArrayList<Cooperation> value) {
		save(context, Key_Business_favourite, value);
	}

	/** 获得校友交流话题all列表 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Issue> getCommunicationAll(Context context) {
		return (ArrayList<Issue>) get(context, Key_Communication_all);
	}

	/** 保存校友交流话题all列表 */
	public static void setCommunicationAll(Context context,
			ArrayList<Issue> value) {
		save(context, Key_Communication_all, value);
	}

	/** 获得校友交流话题My列表 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Issue> getCommunicationMy(Context context) {
		return (ArrayList<Issue>) get(context, Key_Communication_my);
	}

	/** 保存校友交流话题My列表 */
	public static void setCommunicationMy(Context context,
			ArrayList<Issue> value) {
		save(context, Key_Communication_my, value);
	}

	/** 获得校友交流话题Favourite列表 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Issue> getCommunicationFavourite(Context context) {
		return (ArrayList<Issue>) get(context, Key_Communication_favourite);
	}

	/** 保存校友交流话题Favourite列表 */
	public static void setCommunicationFavourite(Context context,
			ArrayList<Issue> value) {
		save(context, Key_Communication_favourite, value);
	}

	/** 获得圈子all列表 */
	@SuppressWarnings("unchecked")
	public static ArrayList<MGroup> getGroupAll(Context context) {
		return (ArrayList<MGroup>) get(context, Key_Group_all);
	}

	/** 保存圈子all列表 */
	public static void setGroupAll(Context context, ArrayList<MGroup> value) {
		save(context, Key_Group_all, value);
	}

	/** 获得圈子My列表 */
	@SuppressWarnings("unchecked")
	public static ArrayList<MGroup> getGroupMy(Context context) {
		return (ArrayList<MGroup>) get(context, Key_Group_my);
	}

	/** 保存圈子My列表 */
	public static void setGroupMy(Context context, ArrayList<MGroup> value) {
		save(context, Key_Group_my, value);
	}
	
	/** 获得动态all列表 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Dynamic> getDynamicAll(Context context) {
		return (ArrayList<Dynamic>) get(context, Key_Dynamic_all);
	}

	/** 保存动态all列表 */
	public static void setDynamicAll(Context context, ArrayList<Dynamic> value) {
		save(context, Key_Dynamic_all, value);
	}
	
	/** 获得动态Friend列表 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Dynamic> getDynamicFriend(Context context) {
		return (ArrayList<Dynamic>) get(context, Key_Dynamic_friend);
	}

	/** 保存动态Friend列表 */
	public static void setDynamicFriend(Context context, ArrayList<Dynamic> value) {
		save(context, Key_Dynamic_friend, value);
	}
}
