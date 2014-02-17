package com.alumnigroup.utils;

import java.util.ArrayList;
import java.util.List;
/**
 * 常用工具类
 * @author Jayin Ton
 *
 */
public class CommonUtils {
	/**
	 * 逆转一个队列
	 * 
	 * @param list
	 *            要逆转的队列
	 * @return 逆转后的队列
	 */
	public static <T> List<T> reverse(List<T> list) {
		List<T> tmp = new ArrayList<T>(list);
		list.clear();
		for (int index = tmp.size() - 1; index >= 0; index--) {
			list.add(tmp.get(index));
		}
		return list;
	}
}
