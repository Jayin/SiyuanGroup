package com.alumnigroup.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alumnigroup.utils.JsonUtils;
import com.alumnigroup.utils.L;
import com.google.gson.Gson;

/**
 * 
 * 圈子实体类
 * 
 * @author Jayin Ton
 * 
 */
public class MGroup implements Serializable {
	public static MGroup create_by_json(String json) {
		try {
			Gson gson = new Gson();
			return (MGroup) gson.fromJson(json, MGroup.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ArrayList<MGroup> create_by_jsonarray(String jsonarray) {
		ArrayList<MGroup> list = new ArrayList<MGroup>();
		JSONObject obj = null;
		JSONArray array = null;
		try {
			obj = new JSONObject(jsonarray);
			array = obj.getJSONArray("groups");
			for (int i = 0; i < array.length(); i++) {
				list.add(create_by_json(array.getJSONObject(i).toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			list = null;
		}
		return list;
	}

	public MGroup() {

	}

	/** 圈子id */
	private int id;
	/** 圈子拥有者id */
	private int ownerid;
	/** 圈子名称 */
	private String name;
	/** 圈子描述 */
	private String description;
	/** 创建时间 */
	private long createtime;
	/** 圈子图标 */
	private String avatar;
	/** 圈子拥有者 */
	private User owner;
	/** 圈子人数 */
	private int numMembers;

	public int getNumMembers() {
		return numMembers;
	}

	public void setNumMembers(int numMembers) {
		this.numMembers = numMembers;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOwnerid() {
		return ownerid;
	}

	public void setOwnerid(int ownerid) {
		this.ownerid = ownerid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getCreatetime() {
		return createtime;
	}

	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Override
	public String toString() {
		return "MGroup [id=" + id + ", ownerid=" + ownerid + ", name=" + name
				+ ", description=" + description + ", createtime=" + createtime
				+ ", avatar=" + avatar + ", owner=" + owner + ", numMembers="
				+ numMembers + "]";
	}

 

	 
	
}
