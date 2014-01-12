package com.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 圈子实体类
 * 
 * @author Jayin Ton
 * 
 */
public class Group implements Serializable {

	public static Group creat_by_json(String json) {
		Group group = new Group();

		return group;
	}

	public static List<Group> create_by_jsonarray(String jsonarray) {
		List<Group> list = new ArrayList<Group>();

		return list;
	}

	public Group() {

	}

	/** 圈子id **/
	private int id;
	/** 圈子拥有者id **/
	private int ownerid;
	/** 圈子名称 **/
	private String name;
	/** 圈子描述 **/
	private String description;
	/** 圈子创建时间 **/
	private long createtime;
	/** 圈子图标文件 **/
	private String avatar;
	/** 圈子成员列表 **/
	private List<Member> members;

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
	public List<Member> getMembers() {
		return members;
	}

	public void setMembers(List<Member> members) {
		this.members = members;
	}

	/**
	 * 圈子成员
	 * 
	 * @author Jayin Ton
	 * 
	 */
	class Member implements Serializable {
		/** 所属圈子id **/
		private int groupid;
		/** 用户id **/
		private int userid;
		/** 是否是圈子的拥有者 **/
		private int isowner;
		/** 是否是圈子的管理员 **/
		private int isadmin;
		/** 备注 **/
		private int remark;

		public int getGroupid() {
			return groupid;
		}

		public void setGroupid(int groupid) {
			this.groupid = groupid;
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

		public int getRemark() {
			return remark;
		}

		public void setRemark(int remark) {
			this.remark = remark;
		}

	}

}
