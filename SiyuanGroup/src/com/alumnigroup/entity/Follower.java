package com.alumnigroup.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;



/**
 * 粉丝实体类 为了方便解析才用到这类
 * 
 * @author Jayin Ton
 * 
 */
@SuppressWarnings("serial")
public class Follower implements Serializable {
	/**
	 * helper that get user list from  List<Following>
	 * @param list
	 * @return
	 */
	public static List<User> getUsesList(List<Follower> list) {
		if (list == null)
			return null;
		List<User> res = new ArrayList<User>();
		for (Follower f : list) {
			res.add(f.getFollowee());
		}
		return res;
	}

	public static Follower create_by_json(String json) {
		Gson gson = new Gson();
		try {
			return (Follower) gson.fromJson(json, Follower.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<Follower> create_by_jsonarray(String jsonarray) {
		List<Follower> list = new ArrayList<Follower>();
		JSONObject obj = null;
		JSONArray array = null;
		try {
			obj = new JSONObject(jsonarray);
			array = obj.getJSONArray("followers");
			for (int i = 0; i < array.length(); i++) {
				list.add(create_by_json(array.getJSONObject(i).toString()));
			}
		} catch (Exception e) {
			list = null;
		}
		return list;
	}

	
    /** 备注*/
	private String remark;
	/** 用户信息*/
	private User followee;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public User getFollowee() {
		return followee;
	}

	public void setFollowee(User followee) {
		this.followee = followee;
	}

	@Override
	public String toString() {
		return "Following [remark=" + remark + ", followee=" + followee + "]";
	}
	
	
}
