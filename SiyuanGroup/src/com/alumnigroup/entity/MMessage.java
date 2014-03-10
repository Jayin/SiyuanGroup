package com.alumnigroup.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

/**
 * 消息实体类
 * 
 * @author Jayin Ton
 * 
 */
@SuppressWarnings("serial")
public class MMessage implements Serializable {
	/**
	 * 根据一个消息里表，统计未读消息数
	 * 
	 * @param messages
	 * @return
	 */
	public static int getUnreadCount(ArrayList<MMessage> messages) {
		int count = 0;
		for (MMessage m : messages) {
			if (m.getIsread() == 0)
				count++;
		}
		return count;
	}

	public static MMessage create_by_json(String json) {
		try {
			Gson gson = new Gson();
			return (MMessage) gson.fromJson(json, MMessage.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ArrayList<MMessage> create_by_jsonarray(String jsonarray) {
		ArrayList<MMessage> list = new ArrayList<MMessage>();
		JSONObject obj = null;
		JSONArray array = null;
		try {
			obj = new JSONObject(jsonarray);
			array = obj.getJSONArray("messages");
			for (int i = 0; i < array.length(); i++) {
				list.add(create_by_json(array.getJSONObject(i).toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			list = null;
		}
		return list;
	}

	public MMessage() {

	}

	/** 消息id */
	private int id;
	/** 发送人id */
	private int senderid;
	/** 接受者 id */
	private User receiverid;
	/** 消息标题 */
	private String title;
	/** 内容 */
	private String body;
	/** 是否已读 */
	private int isread;
	/** 发送时间 */
	private long sendtime;
	/** 未读数 */
	private int unreadcount;
	/** 发送者 姓名 */
	private String sendername;
	/** 发送者头像 */
	private String senderavatar;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSenderid() {
		return senderid;
	}

	public void setSenderid(int senderid) {
		this.senderid = senderid;
	}

	public User getReceiverid() {
		return receiverid;
	}

	public void setReceiverid(User receiverid) {
		this.receiverid = receiverid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getIsread() {
		return isread;
	}

	public void setIsread(int isread) {
		this.isread = isread;
	}

	public long getSendtime() {
		return sendtime;
	}

	public void setSendtime(long sendtime) {
		this.sendtime = sendtime;
	}

	public int getUnreadcount() {
		return unreadcount;
	}

	public void setUnreadcount(int unreadcount) {
		this.unreadcount = unreadcount;
	}

	public String getSendername() {
		return sendername;
	}

	public void setSendername(String sendername) {
		this.sendername = sendername;
	}

	public String getSenderavatar() {
		return senderavatar;
	}

	public void setSenderavatar(String senderavatar) {
		this.senderavatar = senderavatar;
	}

	@Override
	public String toString() {
		return "MMessage [id=" + id + ", senderid=" + senderid
				+ ", receiverid=" + receiverid + ", title=" + title + ", body="
				+ body + ", isread=" + isread + ", sendtime=" + sendtime
				+ ", unreadcount=" + unreadcount + ", sendername=" + sendername
				+ ", senderavatar=" + senderavatar + "]";
	}

}
