package com.alumnigroup.utils;

/**
 * 常用类
 * 
 * @author Jayin Ton
 * 
 */
public class Constants {
	/** 服务Action */
	/** 检测新版本 */
	public static final String Action_checkVersion = "check_version";
	/** 去取未读消息数量 */
	public static final String Action_To_Get_Unread = "To_Get_Unread";
 	/** 得到未读消息数目 */
 	public static final String Action_Receive_UnreadCount = "Receive_UnreadCount";
 	/** 停止轮询得到未读消息数目 */
 	public static final String Action_Stop_Receive_UnreadCount = "Stop_Receive_UnreadCount";
 	/** 开始得到未读消息数目 */
 	public static final String Action_Start_Receive_UnreadCount = "Start_Receive_UnreadCount";
	/** 去登录 */
    public static final String Action_To_Login_In = "To_Login_in";
	/** 用户登录成功 */
    public static final String Action_Login_In_Successful = "Login_In_Successful";
	/** 用户登录失败  */
    public static final String Action_Login_In_Faild ="Login_In_Faild";
	
	// 广播Action 
    public static final String Action_User_edit = "Action_User_edit";
    /** 取消关注*/
    public static final String Action_User_unfollow= "Action_User_unfollow";
	/** 话题(包含圈子分享 )发布成功*/
	public static final String Action_Issue_Comment_Ok = "Issue_Comment_Ok";
	/** 话题修改*/
	public static final String Action_Issue_Edit = "Action_Issue_Edit";
	/** 话题删除*/
	public static final String Action_Issue_delete ="Action_Issue_delete";
	/** 取消收藏*/
	public static final String Action_Issue_unFavourite = "Action_Issue_unFavourite";
	
	/** 商务合作评论成功*/	
	public static final String Action_Bussiness_Comment_Ok = "Bussiness_Comment_Ok"; 
	/** 商务合作修改*/
	public static final String Action_Bussiness_Edit = "Bussiness_Edit";
	/** 商务合作取消关注*/
	public static final String Action_Bussiness_unfavourite ="Action_Bussiness_unfavourite";
	
	/** 获得版本信息*/
	public static final String Action_Receive_VersionInfo = "Receive_VersionInfo"; 
	
	/** 圈子信息修改*/
	public static final String Action_GroupInfo_Edit = "GroupInfo_Edit";
 
	
	/** 活动分享修改*/
	public static final String Action_ActivityShare_Edit  = "ActivityShare_Edit";
	/** 活动信息修改*/
	public static final String Action_ActivityInfo_Edit = "ActivityInfo_Edit ";
	
	/** 主页面图片更换*/
	public static final String Action_Backgroud_switch ="Backgroud_switch";
	/** 用户退出*/
	public static final String Action_User_Login_Out ="User_Login_Out";

}
