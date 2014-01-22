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
		try{
             Gson gson  = new Gson();
             return (MGroup)gson.fromJson(json, MGroup.class);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public static  List<MGroup> create_by_jsonarray(String jsonarray) {
		List<MGroup> list = new ArrayList<MGroup>();
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
	/** 圈子名单 */
	private List<Memberships> memberships;
	/** 圈子拥有者*/
	private User owner;

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

	public List<Memberships> getMemberships() {
		return memberships;
	}

	public void setMemberships(List<Memberships> memberships) {
		this.memberships = memberships;
	}



	/**
	 *一条 成员名单数据
	 * 
	 * @author Jayin Ton
	 * 
	 */
	public  class Memberships implements Serializable {
		
	
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
		/** 成员信息 */
		private User user;
		
		public Memberships(){
			
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
	}
}
