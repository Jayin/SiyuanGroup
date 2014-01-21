package com.alumnigroup.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

/**
 * 活动实体类
 * 
 * @author Jayin Ton
 * 
 */
public class MActivity implements Serializable {
	public static MActivity creat_by_json(String json) {
		MActivity acty = null;
		Gson gson = new Gson();
		try {
			acty = (MActivity) gson.fromJson(json, MActivity.class);
		} catch (Exception e) {
			e.printStackTrace();
			acty = null;
		}
		return acty;
	}

	public static List<MActivity> create_by_jsonarray(String jsonarray) {
		List<MActivity> list = new ArrayList<MActivity>();
		JSONObject obj = null;
		JSONArray array = null;
		try {
			obj = new JSONObject(jsonarray);
			array = obj.getJSONArray("activities");
			for (int i = 0; i < array.length(); i++) {
				list.add(creat_by_json(array.getJSONObject(i).toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			list = null;
		}
		return list;
	}

	public MActivity() {

	}

	/** 活动id **/
	private int id;
	/** 活动发起人id **/
	private int ownerid;
	/** 所属圈子id **/
	private int groupid;
	/** 活动内容 **/
	private String content;
	/** 参与最大人数 **/
	private int maxnum;
	/** 创建活动时间 **/
	private long createtime;
	/** 活动开始时间 **/
	private long starttime;
	/** 活动时长 **/
	private long duration;
	/** 活动状态 */
	private Status status;
	/** 活动状态id 活动状态 0接受报名、1截止报名、2活动结束、3活动取消**/
	private int statusid;
	/** 活动花费 **/
	private String money;
	/** 活动图标 **/
	private String avater;
	/** 活动名称*/
	private String name;
	/** 活动地点 */
	private String site;
	/** 活动名单 **/
	private List<UserShip> userships;

	public int getId() {
		return id;
	}

	public int getStatusid() {
		return statusid;
	}

	public void setStatusid(int statusid) {
		this.statusid = statusid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getGroupid() {
		return groupid;
	}

	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getMaxnum() {
		return maxnum;
	}

	public void setMaxnum(int maxnum) {
		this.maxnum = maxnum;
	}

	public long getCreatetime() {
		return createtime;
	}

	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}

	public long getStarttime() {
		return starttime;
	}

	public void setStarttime(long starttime) {
		this.starttime = starttime;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public List<UserShip> getUserships() {
		return userships;
	}

	public void setUserships(List<UserShip> userships) {
		this.userships = userships;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getAvater() {
		return avater;
	}

	public void setAvater(String avater) {
		this.avater = avater;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * 活动状态码<br>
	 * 活动状态 0接受报名、1截止报名、2活动结束、3活动取消
	 * @author Jayin Ton
	 * 
	 */
	public class Status implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/** 活动状态码id **/
		private int id;
		/** 活动状态码描述 **/
		private String name;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	/**
	 * 用户名单?
	 * 
	 * @author Jayin Ton
	 * 
	 */
	public class UserShip implements Serializable {
		/** 参与活动者列表id **/
		private int id;
		/** 用户id **/
		private int userid;
		/** 活动id **/
		private int activityid;
		/** 是否被接受申请参加活动 **/
		private int isaccepted;

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

	}
}