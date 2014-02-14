package com.alumnigroup.app;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;

import com.alumnigroup.api.RestClient;
import com.alumnigroup.api.StarAPI;
import com.alumnigroup.api.UserAPI;
import com.alumnigroup.app.acty.Login;
import com.alumnigroup.app.acty.Main;
import com.alumnigroup.entity.Dynamic;
import com.alumnigroup.entity.User;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.DataPool;
import com.alumnigroup.utils.JsonUtils;
import com.alumnigroup.utils.L;
import com.loopj.android.http.RequestParams;

/**
 * 应用程序配置类：用于保存用户相关信息及设置
 * 
 * @author Jayin Ton
 * 
 */
public class AppInfo {
	/**
	 * 获得用户信息
	 * 
	 * @return
	 */
	public static User getUser(Context context) {
		DataPool dp = new DataPool(DataPool.SP_Name_User, context);
		return (User) dp.get(DataPool.SP_Key_User);
	}

	public static boolean setUser(String json, Context context) throws ClientProtocolException, IOException {

		final DataPool dp = new DataPool(DataPool.SP_Name_User, context);
		
		if (JsonUtils.isOK(json)) {
			List<User> listUer = User
					.create_by_jsonarray(json);
			if (listUer != null) {
				listUer.get(0).setCover(listUer.get(0).getCover()+"?time="+new Date().getTime());
				listUer.get(0).setAvatar(listUer.get(0).getAvatar()+"?time="+new Date().getTime());
				dp.remove(DataPool.SP_Key_User);
				dp.put(DataPool.SP_Key_User,
						listUer.get(0));
				return true;
			}
		} else {
		}
		
		return false;
	}

	
}
