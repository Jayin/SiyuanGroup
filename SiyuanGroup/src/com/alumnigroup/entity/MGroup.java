package com.alumnigroup.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

/**
 * 
 * 圈子实体类
 * 
 * @author Jayin Ton
 * 
 */
public class MGroup implements Serializable {
	public MGroup create_by_json(String json) {
		MGroup mGroup = null;
		Gson gson = new Gson();
		try {
			mGroup = (MGroup) gson.fromJson(json, MGroup.class);
		} catch (Exception e) {
			e.printStackTrace();
			mGroup = null;
		}
		return mGroup;
	}

	public List<MGroup> create_by_jsonarray(String jsonarray) {
		List<MGroup> list = new ArrayList<MGroup>();
		JSONObject obj = null;
		JSONArray array = null;
		try {
			obj = new JSONObject(jsonarray);
			array = obj.getJSONArray("groups");
			for (int i = 0; i < array.length(); i++) {
				list.add(create_by_json(array.getJSONArray(i).toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			list = null;
		}
		return null;
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
	/** 圈子名单 */
	private Memberships memberships;

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

	public Memberships getMemberships() {
		return memberships;
	}

	public void setMemberships(Memberships memberships) {
		this.memberships = memberships;
	}

	/**
	 * 成员名单
	 * 
	 * @author Jayin Ton
	 * 
	 */
	public class Memberships implements Serializable {
		/** 用户id */
		private int userid;
		/** 是否是圈子拥有者 */
		private int isowner;
		/** 是否是管理员 */
		private int isadmin;
		/** 备注 */
		private String remark;
		/** 限制 */
		private String restrict;
		/** 成员 */
		private List<User> users;

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

		public List<User> getUsers() {
			return users;
		}

		public void setUsers(List<User> users) {
			this.users = users;
		}

		 

		 

	}
}
