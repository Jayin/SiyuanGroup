package com.alumnigroup.utils;

import java.util.Calendar;

/**
 * 日期工具类
 * 
 * @author Jayin Ton
 * 
 */
public class CalendarUtils {
	/**
	 * 时间轴，类似于新浪微博/微信的时间 :前xx分钟，今日hh:mm 昨日hh:mm
	 */
	public static String TYPE_timeline = "timeline";

	/**
	 * 根据给定的时间和格式生成一时间字符串
	 * 
	 * @param milliseconds
	 *            时间戳
	 * @param fromat
	 *            格式 ，请查看本工具类的常量TYPE_
	 * @return 时间字符串
	 */
	public static String getTimeFromat(long milliseconds, String fromat) {
		StringBuilder sb = new StringBuilder();
		if ("timeline".equals(fromat)) {
			Calendar cur = getCurrent();
			Calendar pre = getCurrent();
			pre.setTimeInMillis(milliseconds);
			int tmp = cur.get(Calendar.DAY_OF_YEAR)
					- pre.get(Calendar.DAY_OF_YEAR);
			switch (tmp) {
			case 0:

				tmp = cur.get(Calendar.HOUR_OF_DAY)
						- pre.get(Calendar.HOUR_OF_DAY);
				if (tmp <= 1) {
					tmp = cur.get(Calendar.MINUTE) - pre.get(Calendar.MINUTE);
					sb.append(tmp).append("分钟").append("前");
				} else {
					sb.append("今日 ").append(_formatNmber(pre.get(Calendar.HOUR_OF_DAY)))
							.append(":").append(_formatNmber(pre.get(Calendar.MINUTE)));
				}
				break;
			case 1:
				sb.append("昨日 ").append(pre.get(Calendar.HOUR_OF_DAY))
						.append(":").append(pre.get(Calendar.MINUTE));
				break;
			default:
				sb.append(pre.get(Calendar.MONTH) + 1).append("月")
						.append(pre.get(Calendar.DAY_OF_MONTH)).append("日 ")
						.append(_formatNmber(pre.get(Calendar.HOUR_OF_DAY))).append(":")
						.append(_formatNmber(pre.get(Calendar.MINUTE)));
				break;
			}
		}

		return sb.toString();
	}

	/**
	 * 获得当前时间的Calendar实例
	 * 
	 * @return
	 */
	public static Calendar getCurrent() {
		return Calendar.getInstance();
	}
    /**
     * 格式化时间
     * 早上9:9 -> 09:09
     * @param number
     * @return
     */
	private static String _formatNmber(int number) {
		if (number < 10)
			return "0" + number;
		else
			return number + "";
	}
}
