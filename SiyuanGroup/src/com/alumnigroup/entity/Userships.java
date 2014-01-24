package com.alumnigroup.entity;

import java.io.Serializable;

import com.google.gson.Gson;

/**
 * 用户列表
 * @deprecated
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

	private int id;
	private int userid;
	private int activityid;
	private int isaccepted;
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
	

}
