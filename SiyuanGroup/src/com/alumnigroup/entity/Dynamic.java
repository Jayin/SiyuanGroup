package com.alumnigroup.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

/**
 * 一条动态
 * 
 * @author vector;Jayin since 2014.3.7
 */
@SuppressWarnings("serial")
public class Dynamic implements Serializable {

	/**
	 * 解析单个动态
	 * 
	 * @param json
	 *            单个动态的json字符串
	 * @return Dynamic
	 */
	public static Dynamic create_by_json(String json) {
		try {
			Gson gson = new Gson();
			return (Dynamic) gson.fromJson(json, Dynamic.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 解析一个动态列表的列表
	 * 
	 * @param jsonarray
	 *            动态列表json字符串
	 * @return List<Dynamic> return null if parse faild
	 */
	public static List<Dynamic> create_by_jsonarray(String jsonarray) {
		List<Dynamic> list = new ArrayList<Dynamic>();
		JSONObject obj = null;
		JSONArray array = null;
		try {
			obj = new JSONObject(jsonarray);
			array = obj.getJSONArray("events");
			for (int i = 0; i < array.length(); i++) {
				list.add(create_by_json(array.getJSONObject(i).toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			list = null;
		}
		return list;
	}

	/**
	 * 解析一个动态列表的列表
	 * 
	 * @param jsonarray
	 *            动态列表json字符串
	 * @return List<Dynamic> return null if parse faild
	 */
	public static List<Dynamic> create_by_jsonarray(JSONObject jsonoObj) {
		List<Dynamic> list = new ArrayList<Dynamic>();
		JSONArray array = null;
		try {
			array = jsonoObj.getJSONArray("events");

			for (int i = 0; i < array.length(); i++) {
				list.add(create_by_json(array.getJSONObject(i).toString()));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public Dynamic() {

	}
    /** 动态id*/
	private int id;
	 /** 用户id*/
	private int userid;
	 /** 圈子ID*/
	private int groupid;
	 /** 类别ID*/
	private int itemtype;
	 /** 资源ID*/
	private int itemid;
	 /** 动态描述(仅限搜索)*/
	private String message;
	 /** 该动态的用户*/
	private User user;
	/** 创建时间*/
	private long createtime;
	/** 类型名称*/
	private String typename;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getItemtype() {
		return itemtype;
	}

	public void setItemtype(int itemtype) {
		this.itemtype = itemtype;
	}

	public int getItemid() {
		return itemid;
	}

	public void setItemid(int itemid) {
		this.itemid = itemid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getGroupid() {
		return groupid;
	}

	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}

	public long getCreatetime() {
		return createtime;
	}

	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}
}