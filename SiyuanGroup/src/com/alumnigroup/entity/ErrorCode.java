package com.alumnigroup.entity;

import java.util.HashMap;
import java.util.Map;

public class ErrorCode {
	public static Map<Integer, String> errorList;

	static {
		errorList = new HashMap<Integer, String>();
		errorList.put(302, "重定向 ");
		errorList.put(400, "错误请求");
		errorList.put(401, "未授权");
		errorList.put(403, "无权访问");
		errorList.put(404, "服务器找不到给定的资源");
		errorList.put(500, "服务器内部错误");
		errorList.put(501, "服务器不支持请求");
		errorList.put(502, "错误网关 ");
		errorList.put(503, "服务器无法处理请求");

		errorList.put(10001, "System error");
		errorList.put(10008, "Parameter error");
		errorList.put(10020, "Invalid api");

		errorList.put(20003, "User not found");
		errorList.put(20005, "Unsupported image type, only support jpg");
		errorList.put(20006, "Image size too large");
		errorList.put(20007, "No file input");

		errorList.put(20102, "Not your own");
		errorList.put(20506, "Record not exists");
		errorList.put(20603, "Record not exists");
		errorList.put(20604, "Resource type not exists");
		errorList.put(20605, "Resource not exists");

		errorList.put(21300, "Register fail");
		errorList.put(21301, "Auth fail");
		errorList.put(21302, "Login fail");
		errorList.put(30000, "File reading error");
		errorList.put(30001, "File writing error");
		errorList.put(30002, "File deleting error");

		errorList.put(40001, "Not the member of the group");
		errorList.put(40002, "Already apply");
		errorList.put(40012, "Deadline for application");
		errorList.put(40013, "Activity ended");
		errorList.put(40014, "Activity canceled");
		errorList.put(40015, "Activity-status error");
		errorList.put(40016, "Application accepted, not allowed to cancel");
		errorList.put(40017, "User not the owner");
		errorList.put(40018, "Not found");
	}
}
