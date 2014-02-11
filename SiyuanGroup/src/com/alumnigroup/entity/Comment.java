package com.alumnigroup.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.alumnigroup.utils.JsonUtils;
import com.alumnigroup.utils.L;

/**
 * 评论
 * 
 * @author vector/code by Jayin Ton
 * 
 */
public class Comment implements Serializable {
    /**
     * 以json字符串构建一个Comment实例
     * @param json
     * @return
     */
	public static Comment create_by_json(String json) {
		Comment comment = new Comment();
		JSONObject obj = null;
		try {
			obj = new JSONObject(json);
			comment.setBody(JsonUtils.getString(obj, "body"));
			comment.setPosttime(JsonUtils.getLong(obj, "posttime"));
			comment.setUser(User.create_by_json(obj.getJSONObject("user")
					.toString()));
		} catch (Exception e) {
			e.printStackTrace();
			comment = null;
		}
		return comment;
	}
    /**
     * 以json字符串构建一个List<Comment>实例
     * @param jsonarray json字符串
     * @return List<Comment>实例
     */
	public static List<Comment> create_by_jsonarray(String jsonarray) {
		List<Comment> list = new ArrayList<Comment>();
		JSONObject obj = null;
		JSONArray array = null;
		try {
			obj = new JSONObject(jsonarray);
			obj = obj.getJSONObject("issue");
			array = obj.getJSONArray("comments");
			for (int i = 0; i < array.length(); i++) {
				list.add(create_by_json(array.getJSONObject(i).toString()));
			}

		} catch (Exception e) {
			e.printStackTrace();
			list = null;
		}
		return list;
	}
	

	public Comment() {

	}

	private static final long serialVersionUID = 1L;
	/** 评论内容 */
	private String body;
	/** 发布时间 */
	private long posttime;
	/** 评论者信息 */
	private User user;

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
