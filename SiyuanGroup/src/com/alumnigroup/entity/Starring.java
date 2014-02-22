package com.alumnigroup.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.alumnigroup.api.StarAPI;

/**
 * 一条收藏记录
 * 
 * @author Jayin Ton
 * 
 */
@SuppressWarnings("serial")
public class Starring implements Serializable {

	public static Starring create_by_json(String json) {
		JSONObject obj = null;
		Starring s = new Starring();
		try {
			obj = new JSONObject(json);
			s.setId(obj.getInt("id"));
			s.setUserid(obj.getInt("userid"));
			s.setItemtype(obj.getInt("itemtype"));
			s.setItemid(obj.getInt("itemid"));
			s.setRemark(obj.getString("remark"));
			s.setTypename(obj.getString("typename"));
			if (s.getItemtype() == StarAPI.Item_type_activity) {
				s.setItem(MActivity.create_by_json(obj.getJSONObject("item")
						.toString()));
			} else if (s.getItemtype() == StarAPI.Item_type_business) {
				s.setItem(Cooperation.create_by_json(obj.getJSONObject("item")
						.toString()));
			} else if (s.getItemtype() == StarAPI.Item_type_issue) {
				s.setItem(Issue.create_by_json(obj.getJSONObject("item")
						.toString()));
			}
			if (s.getItem() == null)
				return null;// 构建item失败
			return s;

		} catch (Exception e) {
			return null;
		}
	}

	public static List<Starring> create_by_jsonarray(String jsonarray) {
		List<Starring> list = new ArrayList<Starring>();
		JSONObject obj = null;
		JSONArray array = null;
		try {
			obj = new JSONObject(jsonarray);
			array = obj.getJSONArray("starring");
			for (int i = 0; i < array.length(); i++) {
				list.add(create_by_json(array.getJSONObject(i).toString()));
			}
		} catch (Exception e) {
			list = null;
		}
		return list;
	}

	public Starring() {

	}

	/** 在所有收藏里的id */
	private int id;
	/** 收藏用户id */
	private int userid;
	/** 收藏类型 */
	private int itemtype;
	/** 在一收藏类型的id */
	private int itemid;
	/** 备注 */
	private String remark;
	/** 收藏类型名称 */
	private String typename;
	/** 收藏的元素（MActivity.Issue.Cooperation） */
	private Serializable item;

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

	public int getItemtype() {
		return itemtype;
	}

	public void setItemtype(int itemtype) {
		this.itemtype = itemtype;
	}

	public int getItemid() {
		return itemid;
	}

	public void setItemid(int itemid) {
		this.itemid = itemid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	public Serializable getItem() {
		return item;
	}

	public void setItem(Serializable item) {
		this.item = item;
	}

}
