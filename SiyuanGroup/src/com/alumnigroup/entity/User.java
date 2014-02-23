package com.alumnigroup.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.alumnigroup.utils.L;
import com.google.gson.Gson;

/**
 * 封装一个用户 的属性<br>
 * <li>加入两个静态方法创建一个User实例和List< User > username,name,nickname分别为：用户名，真实姓名，昵称
 * 
 * @author vector /Jayin Ton
 */
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 解析单个用户
	 * 
	 * @param json
	 *            单个用户的json字符串
	 * @return User
	 */
	public static User create_by_json(String json) {
		try {
			Gson gson = new Gson();
			return (User) gson.fromJson(json, User.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 解析一个用户列表的列表
	 * 
	 * @param jsonarray
	 *            用户列表json字符串
	 * @return List<User> return null if parse faild
	 */
	public static List<User> create_by_jsonarray(String jsonarray) {
		List<User> list = new ArrayList<User>();
		JSONObject obj = null;
		JSONArray array = null;
		try {
			obj = new JSONObject(jsonarray);
			array = obj.getJSONArray("users");
			for (int i = 0; i < array.length(); i++) {
				list.add(create_by_json(array.getJSONObject(i).toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			list = null;
		}
		return list;
	}

	public User() {

	}
	/** 是否关注 1：关注 0：还没有关注 **/
	private int isfollowed;

	public int getIsfollowed() {
		return isfollowed;
	}

	public void setIsfollowed(int isfollowed) {
		this.isfollowed = isfollowed;
	}
	/** 用户id **/
	private int id;
	/** 用户名(登录) */
	private String username;
	/** 注册时间 */
	private String regTime;
	/** 是否在线 */
	private int isonline;
	/** 用户的头像相对url **/
	private String avatar;
	/** 关注数 */
	private int numFollowing;
	/** 被关注数(分数量) */
	private int numFollowers;
	/** 话题数 */
	private int numIssues;
	/** 照片数 */
	private int numPhotos;
	/** 收藏数 */
	private int numStarring;
	/** 动态数 */
	private int numEvents;
	/** 个人资料 */
	private Profile profile;
	/** 空间背景图片*/
	private String cover;

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}
 

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRegTime() {
		return regTime;
	}

	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}

	public int getIsonline() {
		return isonline;
	}

	public void setIsonline(int isonline) {
		this.isonline = isonline;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getNumFollowing() {
		return numFollowing;
	}

	public void setNumFollowing(int numFollowing) {
		this.numFollowing = numFollowing;
	}

	public int getNumFollowers() {
		return numFollowers;
	}

	public void setNumFollowers(int numFollowers) {
		this.numFollowers = numFollowers;
	}

	public int getNumIssues() {
		return numIssues;
	}

	public void setNumIssues(int numIssues) {
		this.numIssues = numIssues;
	}

	public int getNumPhotos() {
		return numPhotos;
	}

	public void setNumPhotos(int numPhotos) {
		this.numPhotos = numPhotos;
	}

	public int getNumStarring() {
		return numStarring;
	}

	public void setNumStarring(int numStarring) {
		this.numStarring = numStarring;
	}

	public int getNumEvents() {
		return numEvents;
	}

	public void setNumEvents(int numEvents) {
		this.numEvents = numEvents;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", regTime="
				+ regTime + ", isonline=" + isonline + ", avatar=" + avatar
				+ ", numFollowing=" + numFollowing + ", numFollowers="
				+ numFollowers + ", numIssues=" + numIssues + ", numPhotos="
				+ numPhotos + ", numStarring=" + numStarring + ", numEvents="
				+ numEvents + ", profile=" + profile + ", cover=" + cover + "]";
	}

	public class Profile implements Serializable {
		/**
		 * 个性签名
		 */
		private String summary;
		/** 邮箱 */
		private String email;
		/** 昵称 **/
		private String nickname;
		/** 用戶名字 **/
		private String name;
		/**
		 * 性别<br>
		 * <li>m = man ;f = female
		 */
		private String gender;
		/** 年龄 */
		private int age;
		/** 毕业届数 (年份) **/
		private int grade;
		/** 大学 **/
		private String university;
		/** 专业 **/
		private String major;
		/**
		 * 关键字，用， 号隔开
		 */
		private String tag;
		
		public String getTag() {
			return tag;
		}

		public void setTag(String tag) {
			this.tag = tag;
		}

		public String getSummary() {
			return summary;
		}

		public void setSummary(String summary) {
			this.summary = summary;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getGender() {
			return gender;
		}

		public void setGender(String gender) {
			this.gender = gender;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		public int getGrade() {
			return grade;
		}

		public void setGrade(int grade) {
			this.grade = grade;
		}

		public String getUniversity() {
			return university;
		}

		public void setUniversity(String university) {
			this.university = university;
		}

		public String getMajor() {
			return major;
		}

		public void setMajor(String major) {
			this.major = major;
		}

		@Override
		public String toString() {
			return "Profile [email=" + email + ", nickname=" + nickname
					+ ", name=" + name + ", gender=" + gender + ", age=" + age
					+ ", grade=" + grade + ", university=" + university
					+ ", major=" + major + "]";
		}

	}

}