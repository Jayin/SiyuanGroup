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
	/** 消息标题 */
	private String title;
	/** 是否已读 */
	private int isread;
	/** 是否已回复 */
	private int isreplied;
	/** 发送时间 */
	private long sendtime;
	/** 发送者 */
	private User sender;
	/** 接受者 */
	private User receiver;
	/** 内容*/
	private String body;

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIsread() {
		return isread;
	}

	public void setIsread(int isread) {
		this.isread = isread;
	}

	public int getIsreplied() {
		return isreplied;
	}

	public void setIsreplied(int isreplied) {
		this.isreplied = isreplied;
	}

	public long getSendtime() {
		return sendtime;
	}

	public void setSendtime(long sendtime) {
		this.sendtime = sendtime;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	@Override
	public String toString() {
		return "MMessage [id=" + id + ", title=" + title + ", isread=" + isread
				+ ", isreplied=" + isreplied + ", sendtime=" + sendtime
				+ ", sender=" + sender + ", receiver=" + receiver + ", body="
				+ body + "]";
	}

}
