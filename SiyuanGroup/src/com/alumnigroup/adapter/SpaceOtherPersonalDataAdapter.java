package com.alumnigroup.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alumnigroup.app.R;

/**
 * 动态页面，ListView 的适配器
 * 
 * @author vector
 * 
 */
public class SpaceOtherPersonalDataAdapter extends BaseAdapter {

	/**
	 * 要适配的数据
	 */
	private List<PersonalData> personalData;
	private Context context;

	/**
	 * 对应的控件
	 */
	private TextView tvName,tvValue;
	/**
	 * 用来反射布局文件的
	 */
	private LayoutInflater inflater = null;

	public SpaceOtherPersonalDataAdapter(List<PersonalData> personalData,
			Context context) {
		this.personalData = personalData;
		this.context = context;
		inflater = LayoutInflater.from(this.context);
	}

	public int getCount() {
		return personalData.size();
	}

	// 返回一个数据实体
	public Object getItem(int position) {
		return personalData.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	/**
	 * 返回一个view来显示
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.item_lv_space_other_personaldata, null);
		
		tvName = (TextView) convertView.findViewById(R.id.item_lv_space_other_personaldata_name);
		tvValue = (TextView) convertView.findViewById(R.id.item_lv_space_other_personaldata_value);
		
		/**
		 * 填充数据
		 */
		PersonalData data = personalData.get(position);
		tvName.setText(data.getName());
		tvValue.setText(data.getValue());
		
		
		return convertView;
	}

	/**
	 * 封装个人资料的一条数据
	 * 
	 * @author vector
	 * 
	 */
	public static class PersonalData {
		String name;
		String value;

		
		public PersonalData(String name, String value) {
			super();
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}
}
