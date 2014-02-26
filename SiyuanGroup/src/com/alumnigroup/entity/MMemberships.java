package com.alumnigroup.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

@SuppressWarnings("serial")
public class MMemberships implements Serializable {
	public static MMemberships create_by_json(String json) {
		try {
			Gson gson = new Gson();
			return (MMemberships) gson.fromJson(json, MMemberships.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<MMemberships> create_by_jsonarray(String jsonarray) {
		List<MMemberships> list = new ArrayList<MMemberships>();
		JSONObject obj = null;
		JSONArray array = null;
		try {
			obj = new JSONObject(jsonarray);
			array = obj.getJSONArray("memberships");
			for (int i = 0; i < array.length(); i++) {
				list.add(create_by_json(array.getJSONObject(i).toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			list = null;
		}
		return list;
	}
    /**membership id */
	private int id;
	/** 用户id */
	private int userid;
	/** 是否圈子拥有者 */
	private int isowner;
	/** 是否是管理员 */
	private int isadmin;
	/** 备注 */
	private String remark;
	/** 限制 */
	private String restrict;
	/** 用户信息 */
	private User user;
	/** 是否已被审核通过*/
	private int isaccepted;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIsaccepted() {
		return isaccepted;
	}

	public void setIsaccepted(int isaccepted) {
		this.isaccepted = isaccepted;
	}


	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getIsowner() {
		return isowner;
	}

	public void setIsowner(int isowner) {
		this.isowner = isowner;
	}

	public int getIsadmin() {
		return isadmin;
	}

	public void setIsadmin(int isadmin) {
		this.isadmin = isadmin;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRestrict() {
		return restrict;
	}

	public void setRestrict(String restrict) {
		this.restrict = restrict;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "MMemberships [id=" + id + ", userid=" + userid + ", isowner="
				+ isowner + ", isadmin=" + isadmin + ", remark=" + remark
				+ ", restrict=" + restrict + ", user=" + user + ", isaccepted="
				+ isaccepted + "]";
	}

}
