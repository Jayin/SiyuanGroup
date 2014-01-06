package com.alumnigroup.utils;

import android.widget.EditText;
/**
 * 控件工具类
 * @author Jayin Ton
 *
 */
public class WidgetUtils {
	/**
	 * 获得EditText上的内容
	 * @param et
	 * @return 
	 */
	public static String getText(EditText et) {
		if (et == null)
			return null;
		return et.getText().toString();
	}
    /**
     * 获得EditText上的内容,去除前后空格
     * @param et
     * @return
     */
	public static String getTextTrim(EditText et) {
		return getText(et).trim();
	}
}
