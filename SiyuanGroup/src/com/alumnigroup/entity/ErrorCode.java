package com.alumnigroup.entity;

import android.annotation.SuppressLint;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("UseSparseArrays")
public class ErrorCode {
	public static Map<Integer, String> errorList;

	static {
		errorList = new HashMap<Integer, String>();
		errorList.put(0, "网络连接超时");
		errorList.put(302, "重定向 ");
		errorList.put(400, "错误请求");
		errorList.put(401, "未授权");
		errorList.put(403, "无权访问");
		errorList.put(404, "服务器找不到给定的资源");
		errorList.put(500, "服务器内部错误");
		errorList.put(501, "服务器不支持请求");
		errorList.put(502, "错误网关 ");
		errorList.put(503, "服务器无法处理请求");


		errorList.put(10001, "服务器系统错误");//服务器系统错误System error
		errorList.put(10008, "请求参数错误");//Parameter error
		errorList.put(10020, "非法API");

		errorList.put(20003, "没有找到该用户");
		errorList.put(20005, "不支持图片类型，只支持jpg格式");//不支持图片类型，只支持jpg格式Unsupported image type, only support jpg
		errorList.put(20006, "图片太大了");//图片太大了Image size too large
		errorList.put(20007, "没有文件输入");//没有文件输入No file input

		errorList.put(20102, "不是属于你的");//不是属于你的Not your own
		errorList.put(20506, "用户已存在");//记录不存在Record  already exists  <!--怎么相同有2个？
		errorList.put(20603, "记录不存在");//记录不存在Record not exists
		errorList.put(20604, "资源类型不存在");//资源类型不存在Resource type not exists
		errorList.put(20605, "资源不存在");//资源不存在Resource not exists

		errorList.put(21300, "注册失败");//注册失败Register fail
		errorList.put(21301, "用户验证失败,请重新登录");//用户验证失败,请重新登录Auth fail
		errorList.put(21302, "登录失败");//登录失败Login fail
		errorList.put(30000, "文件读取错误");//文件读取错误File reading error
		errorList.put(30001, "文件写入错误");//文件写入错误File writing error
		errorList.put(30002, "文件删除错误");//文件删除错误File deleting error

		errorList.put(40001, "你还不是该圈的成员");//你还不是该圈的成员Not the member of the group
		errorList.put(40002, "已经申请了");//已经申请了Already apply
		errorList.put(40012, "申请已过期");//申请已过期Deadline for application
		errorList.put(40013, "活动已结束");//活动已结束Activity ended
		errorList.put(40014, "活动取消");//活动取消Activity canceled
		errorList.put(40015, "活动状态错误");//活动状态错误Activity-status error
		errorList.put(40016, "申请已接受，不能取消");//申请已接受，不能取消Application accepted, not allowed to cancel
		errorList.put(40017, "该用户不是拥有者");//该用户不是拥有者User not the owner
		errorList.put(40018, "找不到");//找不到Not found
	}
}
