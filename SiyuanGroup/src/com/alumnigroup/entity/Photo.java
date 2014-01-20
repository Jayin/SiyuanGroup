package com.alumnigroup.entity;

import com.google.gson.Gson;
/**
 * 相片
 * @author vector
 *
 */
public class Photo {
	public static Photo creat_by_json(String json){
		 Gson gson = new Gson();
		 return gson.fromJson(json, Photo.class);
	}
	
    private int id;
    private int userid;
    private String description;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "Photo [id=" + id + ", userid=" + userid + ", description="
				+ description + "]";
	}
    
    
}
