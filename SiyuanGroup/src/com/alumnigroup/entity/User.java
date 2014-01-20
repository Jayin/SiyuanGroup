package com.alumnigroup.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.alumnigroup.utils.JsonUtils;

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
                User user = new User();
                JSONObject obj = null;
                JSONObject profile = null;
                try {   
                        obj = new JSONObject(json);
//                        user.setId(obj.getString("id"));
//                        user.setUsername(obj.getString("username"));
//                        user.setRegTime(obj.getString("regtime"));
//                        user.setOnline(obj.getInt("isonline") == 0 ? false : true);
//                        user.setAvatar(obj.getString("avatar"));
                        
                        user.setId(JsonUtils.getString(obj,"id"));
                        user.setUsername(JsonUtils.getString(obj,"username"));
                        user.setRegTime(JsonUtils.getString(obj,"regtime"));
                        user.setOnline(JsonUtils.getInt(obj,"isonline") == 0 ? false : true);
                        user.setAvatar(JsonUtils.getString(obj,"avatar"));
                        // parse profile
                        profile = obj.getJSONObject("profile");
//                        user.setEmail(profile.getString("email"));
//                        user.setNickname(profile.getString("nickname"));
//                        user.setName(profile.getString("name"));
//                        user.setGender(profile.getString("gender"));
//                        user.setAge(profile.getInt("age"));
//                        user.setGrade(profile.getInt("grade"));
//                        user.setUniversity(profile.getString("university"));
//                        user.setMajor(profile.getString("major"));
                        user.setEmail(JsonUtils.getString(profile,"email"));
                        user.setNickname(JsonUtils.getString(profile,"nickname"));
                        user.setName(JsonUtils.getString(profile,"name"));
                        user.setGender(JsonUtils.getString(profile,"gender"));
                        user.setAge(JsonUtils.getInt(profile,"age"));
                        user.setGrade(JsonUtils.getInt(profile,"grade"));
                        user.setUniversity(JsonUtils.getString(profile,"university"));
                        user.setMajor(JsonUtils.getString(profile,"major"));
                } catch (Exception e) {
                        e.printStackTrace();
                        user = null;
                }
                return user;
        }

        @Override
		public String toString() {
			return "User [avatar=" + avatar + ", username=" + username
					+ ", passworrd=" + passworrd + ", email=" + email
					+ ", isEmailVerified=" + isEmailVerified + ", isOnline="
					+ isOnline + ", regIp=" + regIp + ", regTime=" + regTime
					+ ", gender=" + gender + ", brithday=" + brithday
					+ ", signature=" + signature + ", tags=" + tags
					+ ", groups=" + groups + ", album=" + album + ", tweets="
					+ tweets + ", post=" + post + ", mActivitys=" + mActivitys
					+ ", name=" + name + ", major=" + major + ", id=" + id
					+ ", intro=" + intro + ", grade=" + grade + ", university="
					+ university + ", nickname=" + nickname + ", age=" + age
					+ "]";
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

        /** 用户的头像相对url **/
        private String avatar;

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
        private String regTime;

        /**
         * 性别<br>
         * <li>m = man ;f = female
         */
        private String gender;

        /**
         * 生日
         */
        private String brithday;

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
        // private Comment comments;

        /**
         * 用户的职位
         */
        private Post post;

        /**
         * 用户参与的活动
         */
        private List<MActivity> mActivitys;

        // --------------------------------
        /** 用戶名字 **/
        private String name;
        /** 专业 **/
        private String major;
        /** 用户id **/
        private String id;
        /**
         * 会员简介
         */
        private String intro;
        /** 毕业届数 (年份) **/
        private int grade;
        /** 大学 **/
        private String university;
        /** 昵称 **/
        private String nickname;

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

        public String getNickname() {
                return nickname;
        }

        public void setNickname(String nickname) {
                this.nickname = nickname;
        }

        public int getAge() {
                return age;
        }

        public void setAge(int age) {
                this.age = age;
        }

        public static long getSerialversionuid() {
                return serialVersionUID;
        }

        private int age;

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

        public String getGender() {
                return gender;
        }

        public void setGender(String gender) {
                this.gender = gender;
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

        public String getIntro() {
                return intro;
        }

        public void setIntro(String intro) {
                this.intro = intro;
        }

        public String getAvatar() {
                return avatar;
        }

        public void setAvatar(String avatar) {
                this.avatar = avatar;
        }

        public String getRegTime() {
                return regTime;
        }

        public void setRegTime(String regTime) {
                this.regTime = regTime;
        }

        public String getBrithday() {
                return brithday;
        }

        public void setBrithday(String brithday) {
                this.brithday = brithday;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getMajor() {
                return major;
        }

        public void setMajor(String major) {
                this.major = major;
        }

        public String getId() {
                return id;
        }

        public void setId(String id) {
                this.id = id;
        }

}