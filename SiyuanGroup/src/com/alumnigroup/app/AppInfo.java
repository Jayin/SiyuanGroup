package com.alumnigroup.app;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import android.content.Context;
import android.preference.PreferenceActivity.Header;

import com.alumnigroup.entity.User;
import com.alumnigroup.utils.DataPool;
import com.alumnigroup.utils.JsonUtils;

/**
 * 应用程序配置类：用于保存用户相关信息及设置
 * 
 * @author Jayin Ton
 * 
 */
public class AppInfo {
	/** 保存用户账号*/
	public static boolean setUserID(Context context,String userid){
		DataPool dp = new DataPool(DataPool.SP_Name_User, context);
        return dp.set(DataPool.SP_Key_User_Id, userid);
	}
	/**  获得用户账号*/
	public static String getUserID(Context context){
		DataPool dp = new DataPool(DataPool.SP_Name_User, context);
        return (String)dp.get(DataPool.SP_Key_User_Id);
	}
	/** 保存用户密码*/
	public static boolean setUserPSW(Context context,String userpsw){
		DataPool dp = new DataPool(DataPool.SP_Name_User, context);
        return dp.set(DataPool.SP_Key_User_PSW, userpsw);
	}
	/** 获得用户密码*/
	public static String getUserPSW(Context context){
		DataPool dp = new DataPool(DataPool.SP_Name_User, context);
        return (String)dp.get(DataPool.SP_Key_User_Id);
	}
	/**
	 * 获得用户信息
	 * 
	 * @return
	 */
	public static User getUser(Context context) {
		DataPool dp = new DataPool(DataPool.SP_Name_User, context);
		return (User) dp.get(DataPool.SP_Key_User);
	}

	public static boolean setUser(String json, Context context)
			throws ClientProtocolException, IOException {

		final DataPool dp = new DataPool(DataPool.SP_Name_User, context);

		if (JsonUtils.isOK(json)) {
			List<User> listUer = User.create_by_jsonarray(json);
			if (listUer != null) {
				dp.remove(DataPool.SP_Key_User);
				dp.put(DataPool.SP_Key_User, listUer.get(0));
				return true;
			}
		} else {
		}
		return false;
	}
    /**
     * 获得自定义的主页背景图片
     * @param context
     * @return
     */
	public static String getBackgroudPath(Context context) {
		//路径
		String path = context.getFilesDir() + File.separator
				+ "main_backgroud.jpg";
		File file = new File(path);
		if (file.exists()) {
			return path;
		} else {
			return null;
		}
	}
}
