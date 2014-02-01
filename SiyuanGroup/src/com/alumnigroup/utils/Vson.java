package com.alumnigroup.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.stream.JsonReader;

/**
 * 一个解析json 数据的工具类
 * 
 * @author vector
 * 
 */
public class Vson {

	/**
	 * 保存key
	 */
	private ArrayList<String> alKey = new ArrayList<String>() {
		{
			add("");
		}
	};

	/**
	 * 用来装解析后的结果
	 */
	private Map<String, String> relute;

	public Map<String, String> parse(String json) {
		relute = new HashMap<String, String>();
		JsonReader jr = new JsonReader(new StringReader(json));
		try {
			try {
				jr.beginObject();
				System.out.println("开始对象 -- parse");
				parseObject(jr);

			} catch (IllegalStateException e) {
				jr.beginArray();
				System.out.println("开始数组 -- parse");
				parseArray(jr);

			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return relute;
	}

	/**
	 * 解析数组类型的
	 * 
	 * @throws IOException
	 */
	private void parseArray(JsonReader jsonReader) throws IOException {
		int i = 0;
		while (jsonReader.hasNext()) {
			updateKey(i + "");
			jsonReader.beginObject();
			parseObject(jsonReader);
			i++;
		}
		jsonReader.endArray();
	}

	/**
	 * 解析对象类型的
	 * 
	 * @throws IOException
	 */
	private void parseObject(JsonReader jsonReader) throws IOException {
		while (jsonReader.hasNext()) {

			String tagName = jsonReader.nextName();
			updateKey(tagName);
			String value = null;
			try {
				try {
					/**
					 * 判断是不是null
					 */
					jsonReader.nextNull();
				} catch (IllegalStateException e) {
					/**
					 * 不是空才拿数据
					 */
					value = jsonReader.nextString();
				}
			} catch (IllegalStateException e) {

				/**
				 * 如果能进入这样说明这个key 对应的值是对象或数组
				 */
				parse(jsonReader);
				continue;

			}
			System.out.println("parseObject --- 000");
			relute.put(alKey.get(alKey.size() - 1), value);
			rollbackKey();
		}
		jsonReader.endObject();
		rollbackKey();
	}

	public void parse(JsonReader jsonReader) throws IOException {
		try {
			jsonReader.beginObject();
			System.out.println("开始对象 -- parse");
			parseObject(jsonReader);

		} catch (IllegalStateException e) {
			jsonReader.beginArray();
			System.out.println("开始数组 -- parse");
			parseArray(jsonReader);

		}
	}

	/**
	 * 以追加的方式更新key,就是那最后一个字符串和传过来的相加，放在最后面
	 * 
	 * @param append
	 *            要追加的字符串
	 */
	private void updateKey(String append) {
		String newKey = alKey.get(alKey.size() - 1);
		if ("".equals(newKey)) {
			alKey.add(append);
		} else {
			alKey.add(newKey + ":" + append);
		}
	}

	/**
	 * 回滚
	 */
	private void rollbackKey() {
		if (alKey.size() > 1)
			alKey.remove(alKey.size() - 1);
	}

}
