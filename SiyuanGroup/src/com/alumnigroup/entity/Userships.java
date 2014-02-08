package com.alumnigroup.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

/**
 * 用户列表
 * 
 * @author Jayin Ton
 * 
 */
public class Userships implements Serializable {
	public static Userships create_by_json(String json) {
		Gson gson = new Gson();
		try {
			return (Userships) gson.fromJson(json, Userships.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<Userships> create_by_jsonarray(String jsonarray) {
		List<Userships> list = new ArrayList<Userships>();
		JSONObject obj = null;
		JSONArray arr = null;
		try {
			obj = new JSONObject(jsonarray);
			arr = obj.getJSONArray("userships");
			for (int i = 0; i < arr.length(); i++) {
				list.add(Userships.create_by_json(arr.getJSONObject(i)
						.toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}

	/** 活动名单(报名表)的id */
	private int id;
	/** 活动发起人id */
	private int userid;
	/** 活动id */
	private int activityid;
	/** 非0即为被接受 */
	private int isaccepted;
	/** 申请者 */
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

	public int getActivityid() {
		return activityid;
	}

	public void setActivityid(int activityid) {
		this.activityid = activityid;
	}

	public int getIsaccepted() {
		return isaccepted;
	}

	public void setIsaccepted(int isaccepted) {
		this.isaccepted = isaccepted;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Userships [id=" + id + ", userid=" + userid + ", activityid="
				+ activityid + ", isaccepted=" + isaccepted + ", user=" + user
				+ "]";
	}

}
