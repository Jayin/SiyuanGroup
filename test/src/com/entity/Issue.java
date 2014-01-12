package com.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * 一条话题
 * 
 * @author Jayin Ton
 * 
 */
public class Issue implements Serializable {
	public static Issue creat_by_json(String json) {
		Issue issue = new Issue();

		return issue;
	}

	public static List<Issue> create_by_jsonarray(String jsonarray) {
		List<Issue> list = new ArrayList<Issue>();

		return list;
	}

	public Issue() {

	}

	/**
	 * 话题id
	 */
	private int id;
	/**
	 * 话题发起人id
	 */
	private int userId;
	/**
	 * 话题标题
	 */
	private String title;
	/**
	 * 话题内容/描述
	 */
	private String body;
	/**
	 * 发布时间
	 */
	private long posttime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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

}
