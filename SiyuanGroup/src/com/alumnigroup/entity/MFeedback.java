package com.alumnigroup.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

/**
 * 一条反馈记录
 * 
 * @author Jayin Ton
 * 
 */
@SuppressWarnings("serial")
public class MFeedback implements Serializable {
	public final static String TYPE_BUG = "BUG反馈";
	public final static String TYPE_SUGGESTION = "软件建议";
 
	public static MFeedback create_by_json(String json) {
		MFeedback feedback = null;
		Gson gson = new Gson();
		try {
			feedback = (MFeedback) gson.fromJson(json, MFeedback.class);
		} catch (Exception e) {
			e.printStackTrace();
			feedback = null;
		}
		return feedback;
	}

	public static ArrayList<MFeedback> create_by_jsonarray(String jsonarray) {
		ArrayList<MFeedback> list = new ArrayList<MFeedback>();
		JSONObject obj = null;
		JSONArray array = null;
		try {
			obj = new JSONObject(jsonarray);
			array = obj.getJSONArray("feedbacks");
			for(int i=0;i<array.length();i++){
				list.add(create_by_json(array.getJSONObject(i).toString()));
			}

		} catch (Exception e) {
			e.printStackTrace();
			list = null;
		}
		return list;
	}
	/** 该条反馈的id*/
	private int id;
	/** 该条反馈的类型*/
	private String type;
	/** 该条反馈的内容*/
	private String body;
	/** 该条反馈的版本*/
	private String versioncode;
	/** 该条反馈的设备*/
	private String device;
	/** 该条反馈的时间*/
	private long posttime;
	/** 该条反馈的发布人*/
	private User user;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getVersioncode() {
		return versioncode;
	}
	public void setVersioncode(String versioncode) {
		this.versioncode = versioncode;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public long getPosttime() {
		return posttime;
	}
	public void setPosttime(long posttime) {
		this.posttime = posttime;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "Feedback [id=" + id + ", type=" + type + ", body=" + body
				+ ", versioncode=" + versioncode + ", device=" + device
				+ ", posttime=" + posttime + ", user=" + user + "]";
	}
	
	
}
