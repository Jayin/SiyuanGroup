package com.alumnigroup.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import android.widget.ImageView;

/**
 * 封装一个用户 的属性
 * 
 * @author vector
 * @version 1
 * @since 2013-12-20 10:56:22
 */
public class User implements Serializable{
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 用户的密码
	 */
	private String passworrd;
	/**
	 * 用户的电子邮箱
	 */
	private String email;
	/**
	 * 是否验证了电子邮箱
	 */
	private boolean isEmailVerified;
	/**
	 * 是否在线
	 */
	private boolean isOnline;
	/**
	 * 注册的ip
	 */
	private String regIp;
	
	/**
	 * 注册的日期
	 */
	private Date regDate;
	
	/**
	 * 性别
	 */
	private int gender;
	
	/**
	 * 生日
	 */
	private Date brithday;
	
	/**
	 * 空间签名
	 */
	private String signature;
	
	/**
	 * 空间标签
	 */
	private String tags;
	
	/**
	 * 用户参加的讨论组 
	 */
	private List<Groups> groups;
	
	/**
	 * 用户的相册
	 */
	private Album album;
	
	/**
	 * 用户发的微博
	 */
	private List<Tweet> tweets;
	
	/**
	 * 评论？
	 */
//	private Comment comments;
	
	/**
	 * 用户的职位
	 */
	private Post post;
	
	/**
	 * 用户参与的活动
	 */
	private List<MActivity> mActivitys;
	
	//--------------------------------
	
	/**
	 * 用户的头像地址
	 */
	private String  portraitPath;
	
	/**
	 * 会员简介
	 */
	private String  intro;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassworrd() {
		return passworrd;
	}

	public void setPassworrd(String passworrd) {
		this.passworrd = passworrd;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEmailVerified() {
		return isEmailVerified;
	}

	public void setEmailVerified(boolean isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public String getRegIp() {
		return regIp;
	}

	public void setRegIp(String regIp) {
		this.regIp = regIp;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public Date getBrithday() {
		return brithday;
	}

	public void setBrithday(Date brithday) {
		this.brithday = brithday;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public List<Groups> getGroups() {
		return groups;
	}

	public void setGroups(List<Groups> groups) {
		this.groups = groups;
	}

	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	public List<Tweet> getTweets() {
		return tweets;
	}

	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public List<MActivity> getmActivitys() {
		return mActivitys;
	}

	public void setmActivitys(List<MActivity> mActivitys) {
		this.mActivitys = mActivitys;
	}

	public String getPortraitPath() {
		return portraitPath;
	}

	public void setPortraitPath(String portraitPath) {
		this.portraitPath = portraitPath;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}
	
	
	
	
}
