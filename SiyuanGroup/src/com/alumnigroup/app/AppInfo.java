package com.alumnigroup.app;

import android.content.Context;

import com.alumnigroup.entity.User;
import com.alumnigroup.utils.DataPool;

/**
 * 应用程序配置类：用于保存用户相关信息及设置
 * @author Jayin Ton
 *
 */
public class AppInfo {
	private Context context;
	public AppInfo(Context context){
		this.context = context;
	}
	/**
	 * 获得用户信息
	 * @return
	 */
	public User getUser(){
		DataPool dp = new DataPool(DataPool.SP_Name_User, context);
		return (User)dp.get(DataPool.SP_Key_User);
	}

}
