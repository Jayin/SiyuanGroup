package com.alumnigroup.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.alumnigroup.utils.JsonUtils;

/**
 * 话题
 * 
 * @author vector / code by Jayin Ton
 * 
 */
public class Issue implements Serializable {
	/**
	 * 以json字符串构建一个Issue实例
	 * 
	 * @param json
	 *            json string of Issue
	 * @return Issue实例
	 */
	public static Issue create_by_json(String json) {
		Issue issue = new Issue();
		JSONObject obj = null;
		try {
			obj = new JSONObject(json);
			issue.setId(JsonUtils.getInt(obj, "id"));
			issue.setUser(User.create_by_json(obj.getJSONObject("user")
					.toString()));
			issue.setTitle(JsonUtils.getString(obj, "title"));
			issue.setBody(JsonUtils.getString(obj, "body"));
			issue.setPosttime(JsonUtils.getLong(obj, "posttime"));
			issue.setNumComments(JsonUtils.getInt(obj, "numComments"));
		} catch (Exception e) {
			e.printStackTrace();
			issue = null;
		}
		return issue;
	}

	/**
	 * 以json字符串构建一个List<Comment>实例
	 * @param jsonarray
	 * @return
	 */
	public static List<Issue> create_by_jsonarray(String jsonarray) {
		List<Issue> list = new ArrayList<Issue>();
		JSONObject obj = null;
		JSONArray array = null;
		try {
			obj = new JSONObject(jsonarray);
			array = obj.getJSONArray("issues");
			for(int i=0;i<array.length();i++){
				list.add(create_by_json(array.getJSONObject(i).toString()));
			}

		} catch (Exception e) {
			e.printStackTrace();
			list = null;
		}
		return list;
	}

	public Issue() {

	}

	/** 话题id */
	private int id;
	/** 用户信息 */
	private User user;
	/** 话题标题 */
	private String title;
	/** 话题内容 */
	private String body;
	/** 发送时间 */
	private long posttime;
	/** 评论数 */
	private int numComments;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public long getPosttime() {
		return posttime;
	}

	public void setPosttime(long posttime) {
		this.posttime = posttime;
	}

	public int getNumComments() {
		return numComments;
	}

	public void setNumComments(int numComments) {
		this.numComments = numComments;
	}

}
