package com.alumnigroup.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.alumnigroup.entity.MActivity.Status;
import com.google.gson.Gson;

/**
 * 合作项目实体类
 * 
 * @author Jayin Ton
 * 
 */
public class Cooperation implements Serializable {

	public static Cooperation create_by_json(String json) {
		Gson gson = new Gson();
		try {
			return (Cooperation) gson.fromJson(json, Cooperation.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<Cooperation> create_by_jsonarray(String jsonarray) {
		List<Cooperation> list = new ArrayList<Cooperation>();
		JSONObject obj = null;
		JSONArray array = null;
		try {
			obj = new JSONObject(jsonarray);
			array = obj.getJSONArray("cooperations");
			for (int i = 0; i < array.length(); i++) {
				list.add(create_by_json(array.getJSONObject(i).toString()));
			}
		} catch (Exception e) {
			list = null;
		}
		return list;
	}

	public Cooperation() {

	}

	private int id;
	private String name;
	private int ownerid;
	private String description;
	private String company;
	private long deadline; 
	private String avatar;
	private int statusid;
	private int isprivate;
	private User user;
	private Status status;

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

	public int getOwnerid() {
		return ownerid;
	}

	public void setOwnerid(int ownerid) {
		this.ownerid = ownerid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public long getDeadline() {
		return deadline;
	}

	public void setDeadline(long deadline) {
		this.deadline = deadline;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getStatusid() {
		return statusid;
	}

	public void setStatusid(int statusid) {
		this.statusid = statusid;
	}

	public int getIsprivate() {
		return isprivate;
	}

	public void setIsprivate(int isprivate) {
		this.isprivate = isprivate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
