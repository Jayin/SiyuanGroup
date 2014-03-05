package com.alumnigroup.entity;

import java.io.Serializable;
/**
 * 图片
 * @author Jayin Ton
 *
 */
public class MPicture implements Serializable {
    /** 图片id*/
	private int id;
	 /** 话题id*/
	private int issueid;
	 /** 活动id*/
	private int activityid;
	 /** 合作id*/
	private int cooperationid;
	 /** 图片路径*/
	private String path;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIssueid() {
		return issueid;
	}

	public void setIssueid(int issueid) {
		this.issueid = issueid;
	}

	public int getActivityid() {
		return activityid;
	}

	public void setActivityid(int activityid) {
		this.activityid = activityid;
	}

	public int getCooperationid() {
		return cooperationid;
	}

	public void setCooperationid(int cooperationid) {
		this.cooperationid = cooperationid;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "MPicture [id=" + id + ", issueid=" + issueid + ", activityid="
				+ activityid + ", cooperationid=" + cooperationid + ", path="
				+ path + "]";
	}
}
