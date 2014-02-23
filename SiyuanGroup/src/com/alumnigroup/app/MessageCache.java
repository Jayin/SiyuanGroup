package com.alumnigroup.app;

import java.util.ArrayList;

import android.content.Context;

import com.alumnigroup.entity.MMessage;

/**
 * 消息缓存
 * 
 * @author Jayin Ton
 * 
 */
public class MessageCache {
	// 消息
	public static final String Key_Messages_Reveive = "Messages_Reveive"; // 收信息列表
	public static final String Key_Messages_Send = "Messages_Send"; // 发送信息列表
	public static final String Key_Messages_unread_count = "Messages_unread_count";//未读数量
    /** 获取未读数*/
	public static int getUnreadCount(Context context){
		try{
			return Integer.parseInt((String)AppCache.get(context, Key_Messages_unread_count));
		}catch(Exception e){
			setUnreadCount(context,0);
			return 0;
		}
	}
	/** 保存未读数量*/
	public static void setUnreadCount(Context context,int unreadCount){
		AppCache.save(context, Key_Messages_unread_count, unreadCount+"");
	}
	/** 获取收信息列表 */
	@SuppressWarnings("unchecked")
	public static ArrayList<MMessage> getReceiveMessages(Context context) {
		return (ArrayList<MMessage>) AppCache.get(context, Key_Messages_Reveive) == null ? new ArrayList<MMessage>()
				: (ArrayList<MMessage>) AppCache.get(context, Key_Messages_Reveive);
	}

	/** 获取发送信息列表 */
	public static void setReceiveMessages(Context context,
			ArrayList<MMessage> value) {
		AppCache.save(context, Key_Messages_Reveive, value);
	}
	

}
