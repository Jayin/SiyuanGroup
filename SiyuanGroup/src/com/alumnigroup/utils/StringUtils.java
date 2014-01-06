package com.alumnigroup.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格(' ')、制表符('\t')、回车符'\r'、换行符'\n'组成的字符串
	 * 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input.trim())) {
			return true;
		}
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\n' && c != '\r') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 检测字符串中只能包含：中文、数字、下划线、横线,英文a-z A-Z
	 * 
	 * @param sequence
	 * @return true if it's ok
	 */
	public static boolean isNickname(String sequence) {
		final String format = "[^\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w-_a-zA-Z]";
		Pattern pattern = Pattern.compile(format);
		Matcher matcher = pattern.matcher(sequence);
		return !matcher.find();
	}

	/**
	 * 判断是否是邮箱
	 * 
	 * @param sequence
	 * @return
	 */
	public static boolean isEmail(String sequence) {
		Pattern pattern = Pattern
				.compile("^([a-z0-9A-Z]+[-_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
		Matcher matcher = pattern.matcher(sequence);
		return matcher.find();
	}
}
