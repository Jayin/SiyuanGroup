package com.alumnigroup.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.alumnigroup.app.R;
import com.alumnigroup.entity.Tweet;

/**
 * 动态页面，ListView 的适配器
 * 
 * @author vector
 * 
 */
public class DynamicAdapter extends BaseAdapter {

	/**
	 * 要适配的数据
	 */
	private List<Tweet> Dynamic;
	private Context context;

	/**
	 * 动态的不同类型
	 */
	public static final int TYPE_UPDATE_SPATIAL = 0;

	/**
	 * 用来反射布局文件的
	 */
	private LayoutInflater inflater = null;

	public DynamicAdapter(List<Tweet> Dynamic, Context context) {
		this.Dynamic = Dynamic;
		this.context = context;
		inflater = LayoutInflater.from(this.context);
	}

	public int getCount() {
		return Dynamic.size();
	}

	// 返回一个数据实体
	public Object getItem(int position) {
		return Dynamic.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	/**
	 * 返回一个view来显示
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int type = getItemViewType(position);
		switch (type) {
		case TYPE_UPDATE_SPATIAL:
			convertView = inflater.inflate(
					R.layout.item_lv_alldynamic_update_spatial, null);
			break;

		default:
			break;
		}
		/**
		 * 填充数据
		 */
		// ....

		return convertView;
	}

	/**
	 * 根据数据源的position返回需要显示的的layout的type
	 * 
	 * type的值必须从0开始
	 * 
	 * 
	 */
	@Override
	public int getItemViewType(int position) {

		Tweet tweet = Dynamic.get(position);
		int type = tweet.getType();
		return type;
	}

	/**
	 * 返回所有的layout的数量
	 * 
	 */
	@Override
	public int getViewTypeCount() {
		return 1;
	}
}
