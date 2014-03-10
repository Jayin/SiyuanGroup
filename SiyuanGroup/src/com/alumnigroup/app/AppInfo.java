package com.alumnigroup.app;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import com.alumnigroup.entity.User;
import com.alumnigroup.utils.DataPool;
import com.alumnigroup.utils.FilePath;
import com.alumnigroup.utils.JsonUtils;

/**
 * 应用程序配置类：用于保存用户相关信息及设置
 * 
 * @author Jayin Ton
 * 
 */
public class AppInfo {
	/** 保存用户账号 */
	public static boolean setUserID(Context context, String userid) {
		DataPool dp = new DataPool(DataPool.SP_Name_User, context);
		return dp.set(DataPool.SP_Key_User_Id, userid);
	}

	/** 获得用户账号 */
	public static String getUserID(Context context) {
		DataPool dp = new DataPool(DataPool.SP_Name_User, context);
		return (String) dp.get(DataPool.SP_Key_User_Id);
	}

	/** 保存用户密码 */
	public static boolean setUserPSW(Context context, String userpsw) {
		DataPool dp = new DataPool(DataPool.SP_Name_User, context);
		return dp.set(DataPool.SP_Key_User_PSW, userpsw);
	}

	/** 获得用户密码 */
	public static String getUserPSW(Context context) {
		DataPool dp = new DataPool(DataPool.SP_Name_User, context);
		return (String) dp.get(DataPool.SP_Key_User_Id);
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
	
    /**
     * create by vector
     * @param json
     * @param context
     * @return
     * @throws IOException
     */
	public static boolean setUser(String json, Context context)
			throws IOException {

		final DataPool dp = new DataPool(DataPool.SP_Name_User, context);

		if (JsonUtils.isOK(json)) {
			List<User> listUer = User.create_by_jsonarray(json);
			if (listUer != null) {
				dp.remove(DataPool.SP_Key_User);
				return dp.put(DataPool.SP_Key_User, listUer.get(0));
			}
		} else {
		}
		return false;
	}
	
    /**
     * create by Jayin Ton
     * @param context
     * @param user
     * @return
     */
	public static boolean setUser(Context context,User user) {
		DataPool dp = new DataPool(DataPool.SP_Name_User, context);
		dp.remove(DataPool.SP_Key_User);
		return dp.put(DataPool.SP_Key_User, user);
	}

	/**
	 * 获得自定义的主页背景图片
	 * @param context
	 * @return
	 */
	public static String getBackgroudPath(Context context) {
		// 路径
		String path = FilePath.getImageFilePath()+"main_backgroud.jpg";
		File file = new File(path);
		if (file.exists()) {
			return path;
		} else {
			return null;
		}
	}
}
