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
	
	
	
	
}
