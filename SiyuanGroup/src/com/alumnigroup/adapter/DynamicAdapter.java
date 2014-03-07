package com.alumnigroup.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alumnigroup.api.RestClient;
import com.alumnigroup.app.AppInfo;
import com.alumnigroup.app.R;
import com.alumnigroup.app.acty.SpaceOther;
import com.alumnigroup.app.acty.SpacePersonal;
import com.alumnigroup.entity.Dynamic;
import com.alumnigroup.entity.User;
import com.alumnigroup.utils.CalendarUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

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
	private List<Dynamic> dynamics;
	private Context context;

	/**
	 * 用来反射布局文件的
	 */
	private LayoutInflater inflater = null;

	public DynamicAdapter(List<Dynamic> dynamics, Context context) {
		this.dynamics = dynamics;
		this.context = context;
		inflater = LayoutInflater.from(this.context);
	}

	public int getCount() {
		return dynamics.size();
	}

	// 返回一个数据实体
	public Object getItem(int position) {
		return dynamics.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	/**
	 * 返回一个view来显示
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Dynamic dynamic = dynamics.get(position);
		convertView = inflater.inflate(
				R.layout.item_lv_alldynamic_update_spatial, null);

		ImageView portrait = (ImageView) convertView
				.findViewById(R.id.item_lv_alldynamic_iv_portrait);

		portrait.setOnClickListener(new PortraitOnClick(position));

		ImageLoader.getInstance().displayImage(
				RestClient.BASE_URL + dynamic.getUser().getAvatar(), portrait);
		TextView name = (TextView) convertView
				.findViewById(R.id.item_lv_alldynamic_tv_name);
		name.setText(dynamic.getUser().getProfile().getName());
		TextView content = (TextView) convertView
				.findViewById(R.id.item_lv_alldynamic_tv_content);
		content.setText(dynamic.getMessage());
		TextView time = (TextView) convertView
				.findViewById(R.id.item_lv_alldynamic_tv_datetime);
		time.setText(CalendarUtils.getTimeFromat(dynamic.getCreatetime(),
				CalendarUtils.TYPE_timeline));
		return convertView;
	}

	class PortraitOnClick implements OnClickListener {

		int position;

		public PortraitOnClick(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			/**
			 * 如果是自己就进自己的空间，不然就进别人的空间
			 */
			User user = dynamics.get(position).getUser();
			if (user.getId() == AppInfo.getUser(context).getId()) {
				Intent intent = new Intent(context, SpacePersonal.class);
				context.startActivity(intent);
				return;
			}
			Intent intent = new Intent(context, SpaceOther.class);
			intent.putExtra("user", dynamics.get(position).getUser());
			context.startActivity(intent);
		}

	}

}
