package com.alumnigroup.entity;

import java.io.Serializable;
/**
 * 项目
 * @author Jayin Ton
 * @deprecated use Cooperation instead 
 *
 */
public class Project implements Serializable {
  
	
	private String name;
	private String content;
	private long endtime;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getEndtime() {
		return endtime;
	}
	public void setEndtime(long endtime) {
		this.endtime = endtime;
	}
	
}
