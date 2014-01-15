package com.alumnigroup.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * JSON工具类 <li>复写所有get方法，对于基本类型,即使数据返回是null 会有自动默认值
 * <li>引用类型，数据返回null or NULL 均返回null
 * 
 * @author Jayin Ton
 * 
 */
public class JsonUtils {
	
	/**
	 * 复写JSONObject。getInt()
	 * @param obj
	 * @param name
	 * @return -1 if it's return "name":null
	 * @throws JSONException
	 */
	public int getInt(JSONObject obj, String name) throws JSONException {
		if (obj.isNull(name))
			return -1;
		else
			return obj.getInt(name);
	}

	/**
	 * 复写JSONObject。getLong()
	 * @param obj
	 * @param name
	 * @return   -1 if it's return "name":null
	 * @throws JSONException
	 */
	public long getLong(JSONObject obj, String name) throws JSONException {
		if (obj.isNull(name))
			return -1;
		else
			return obj.getLong(name);
	}

	/**
	 * 复写JSONObject。getString()
	 * @param obj
	 * @param name
	 * @return null if it's return "name":null;
	 * @throws JSONException
	 */
	public String getString(JSONObject obj, String name) throws JSONException {
		if (obj.isNull(name))
			return null;
		else
			return obj.getString(name);
	}

	/**
	 * 复写JSONObject。getBoolean()
	 * @param obj
	 * @param name
	 * @return false if it's return "name":null;
	 * @throws JSONException
	 */
	public boolean getBoolean(JSONObject obj, String name) throws JSONException {
		if (obj.isNull(name))
			return false;
		else
			return obj.getBoolean(name);
	}

	/**
	 * 复写JSONObject。getDouble()
	 * @param obj
	 * @param name
	 * @return  -1.0 if it's return "name":null;
	 * @throws JSONException
	 */
	public double getDouble(JSONObject obj, String name) throws JSONException {
		if (obj.isNull(name))
			return -1.0;
		else
			return obj.getDouble(name);
	}

}
