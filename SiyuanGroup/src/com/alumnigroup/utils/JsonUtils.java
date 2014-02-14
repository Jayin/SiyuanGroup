package com.alumnigroup.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;

/**
 * JSON工具类 <li>1.复写所有get方法，对于基本类型,即使数据返回是null 会有自动默认值 <li>2.引用类型，数据返回null or
 * NULL <li>3.对请求返回的信息解析的一些封装 get(key) 均返回null
 * 
 * @author Jayin Ton
 * 
 */
public class JsonUtils {
	/**
	 * 根据给定的json对象字符串获取对应的int类型值
	 * 
	 * @param jsonobj
	 *            json对象字符串
	 * @param name
	 *            key
	 * @return int
	 */
	public static int getInt(String jsonobj, String name) {
		JSONObject obj = null;
		try {
			obj = new JSONObject(jsonobj);
			return getInt(obj, name);
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * 根据给定的json对象字符串获取对应的String类型值
	 * 
	 * @param jsonobj
	 *            json对象字符串
	 * @param name
	 *            key
	 * @return String
	 */
	public static String getString(String jsonobj, String name) {
		JSONObject obj = null;
		try {
			obj = new JSONObject(jsonobj);
			return getString(obj, name);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 复写JSONObject。getInt()
	 * 
	 * @param obj
	 * @param name
	 * @return -1 if it's return "name":null
	 * @throws JSONException
	 */
	public static int getInt(JSONObject obj, String name) throws JSONException {
		if (obj.isNull(name))
			return -1;
		else
			return obj.getInt(name);
	}

	/**
	 * 复写JSONObject。getLong()
	 * 
	 * @param obj
	 * @param name
	 * @return -1 if it's return "name":null
	 * @throws JSONException
	 */
	public static long getLong(JSONObject obj, String name)
			throws JSONException {
		if (obj.isNull(name))
			return -1;
		else
			return obj.getLong(name);
	}

	/**
	 * 复写JSONObject。getString()
	 * 
	 * @param obj
	 * @param name
	 * @return null if it's return "name":null;
	 * @throws JSONException
	 */
	public static String getString(JSONObject obj, String name)
			throws JSONException {
		if (obj.isNull(name))
			return null;
		else
			return obj.getString(name);
	}

	/**
	 * 复写JSONObject。getBoolean()
	 * 
	 * @param obj
	 * @param name
	 * @return false if it's return "name":null;
	 * @throws JSONException
	 */
	public static boolean getBoolean(JSONObject obj, String name)
			throws JSONException {
		if (obj.isNull(name))
			return false;
		else
			return obj.getBoolean(name);
	}

	/**
	 * 复写JSONObject。getDouble()
	 * 
	 * @param obj
	 * @param name
	 * @return -1.0 if it's return "name":null;
	 * @throws JSONException
	 */
	public static double getDouble(JSONObject obj, String name)
			throws JSONException {
		if (obj.isNull(name))
			return -1.0;
		else
			return obj.getDouble(name);
	}

	/**
	 * 复写JSONObject。getJSONObject()
	 * 
	 * @param obj
	 *            json - object
	 * @param name
	 *            key
	 * @return 当属性为null NULL 或不存在这个key时，返回null;
	 * @throws JSONException
	 */
	public static JSONObject getJSONObject(JSONObject obj, String name)
			throws JSONException {
		if (obj == null)
			return null;
		if (obj.isNull(name))
			return null;
		else
			return obj.getJSONObject(name);
	}

	/**
	 * 复写JSONObject。getJSONArray(
	 * 
	 * @param obj
	 *            json - object
	 * @param name
	 *            key
	 * @return 当属性为null NULL 或不存在这个key时，返回null;
	 * @throws JSONException
	 */
	public static JSONArray getJSONArray(JSONObject obj, String name)
			throws JSONException {
		if (obj == null)
			return null;
		if (obj.isNull(name))
			return null;
		else
			return obj.getJSONArray(name);
	}

	/**
	 * 判断是否有正确请求api
	 * 
	 * @param json
	 *            请求返回体
	 * @return true if parse error and request error
	 */
	public static boolean isOK(String json) {
		if (json == null)
			return true;
		JSONObject obj = null;
		try {
			obj = new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		try {
			if (getInt(obj, "errorCode") > 0) {
				return false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return true;
		}
		return true;
	}

	/**
	 * 获取请求返回体错误信息
	 * 
	 * @param json
	 *            请求返回体json
	 * @return null if has not error or parse json error
	 */
	public static String getErrorString(String json) {
		if (json == null)
			return null;
		JSONObject obj = null;
		if (!isOK(json)) {
			try {
				obj = new JSONObject(json);
				return JsonUtils.getString(obj, "error");
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	/**
	 * 获取请求返回的error_code
	 * 
	 * @param json
	 *            请求返回体json
	 * @return error_code
	 */
	public static int getErrorCode(String json) {
		return getInt(json, "errorCode");
	}

}
