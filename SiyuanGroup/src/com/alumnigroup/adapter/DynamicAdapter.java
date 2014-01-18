package com.alumnigroup.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.alumnigroup.app.R;
import com.alumnigroup.entity.Tweet;
import com.alumnigroup.utils.ImageUtils;

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
	private List<Tweet> dynamic;
	private Context context;
	
	/**
	 * 对应布局文件的控件
	 */
	private ImageView ivAlldynamicPortrait;

	/**
	 *  更新空间类型
	 */
	public static final int TYPE_UPDATE_SPATIAL = 0; 
	/**
	 *  转发微博
	 */
	public static final int TYPE_TRANSPOND_TWEET = 1; 
	/**
	 *  转发微博
	 */
	public static final int TYPE_RELEASE_TWEET = 2; 

	/**
	 * 用来反射布局文件的
	 */
	private LayoutInflater inflater = null;

	public DynamicAdapter(List<Tweet> Dynamic, Context context) {
		this.dynamic = Dynamic;
		this.context = context;
		inflater = LayoutInflater.from(this.context);
	}

	public int getCount() {
		return dynamic.size();
	}

	// 返回一个数据实体
	public Object getItem(int position) {
		return dynamic.get(position);
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
			
		case TYPE_TRANSPOND_TWEET:
			convertView = inflater.inflate(
					R.layout.item_lv_alldynamic, null);
			break;
			
			
		case TYPE_RELEASE_TWEET:
			convertView = inflater.inflate(
					R.layout.item_lv_alldynamic_release_tweet, null);
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

		Tweet tweet = dynamic.get(position);
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
