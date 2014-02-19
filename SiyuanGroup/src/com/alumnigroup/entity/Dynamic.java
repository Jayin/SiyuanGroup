package com.alumnigroup.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alumnigroup.utils.L;
import com.google.gson.Gson;

/**
 * 封装一个动态的属性<br>
 * 
 * @author vector
 */
public class Dynamic implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

	private int id;
	private int userid;
	private int proupid;
	private int itemtype;
	private int itemid;
	private String message;
	private User user;

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

	public int getProupid() {
		return proupid;
	}

	public void setProupid(int proupid) {
		this.proupid = proupid;
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

}