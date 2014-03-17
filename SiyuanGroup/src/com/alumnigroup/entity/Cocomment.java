package com.alumnigroup.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

/**
 * 商务合作评论<br>
 * 为啥独立一个?server端用的评论实体类不一致，纯粹是为了提醒这一不一致方便解析
 * 
 * @author Jayin Ton
 * 
 */
@SuppressWarnings("serial")
public class Cocomment implements Serializable {

	public static Cocomment create_by_json(String json) {
		Gson gson = new Gson();
		try {
			return (Cocomment) gson.fromJson(json, Cocomment.class);
		} catch (Exception e) {
			return null;
		}
	}

	public static List<Cocomment> create_by_jsonarray(String jsonarray) {
		List<Cocomment> list = new ArrayList<Cocomment>();
		JSONObject obj = null;
		JSONArray array = null;
		try {
			obj = new JSONObject(jsonarray);
			array = obj.getJSONArray("cocomments");
			for (int i = 0; i < array.length(); i++) {
				list.add(create_by_json(array.getJSONObject(i).toString()));
			}
		} catch (Exception e) {
			list = null;
		}
		return list;
	}

	/** 评论id */
	private int id;
	/** 合作id */
	private int cooperationid;
	/** 用户id */
	private int userid;
	/** 评论体 */
	private String body;
	/** 发送时间 */
	private long posttime;
	/** 评论者信息 */
	private User user;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCooperationid() {
		return cooperationid;
	}

	public void setCooperationid(int cooperationid) {
		this.cooperationid = cooperationid;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
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
		return "Cocomment [id=" + id + ", cooperationid=" + cooperationid
				+ ", userid=" + userid + ", body=" + body + ", posttime="
				+ posttime + ", user=" + user + "]";
	}

}
