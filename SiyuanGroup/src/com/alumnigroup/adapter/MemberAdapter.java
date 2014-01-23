package com.alumnigroup.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alumnigroup.api.RestClient;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.User;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 会员适配器
 */
public class MemberAdapter extends BaseAdapter {
	private List<User> data;
    private Context context;
	public MemberAdapter(List<User> data,Context context) {
		this.data = data;
		this.context = context;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder h;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_lv_allmember, null);
			h = new ViewHolder();

			h.avatar = (ImageView) convertView
					.findViewById(R.id.item_lv_allmember_avatar);
			h.online = (ImageView) convertView
					.findViewById(R.id.item_lv_allmember_online);
			h.grade = (TextView) convertView
					.findViewById(R.id.item_lv_allmemeber_grade);
			h.name = (TextView) convertView
					.findViewById(R.id.item_lv_allmemeber_name);
			h.major = (TextView) convertView
					.findViewById(R.id.item_lv_allmemeber_major);
			convertView.setTag(h);
		} else {
			h = (ViewHolder) convertView.getTag();
		}
		User u = data.get(position);
		ImageLoader.getInstance().displayImage(
				RestClient.BASE_URL + u.getAvatar(), h.avatar);
		h.grade.setText(u.getProfile().getGrade() + "");
		h.name.setText(u.getProfile().getName());
		h.major.setText(u.getProfile().getMajor());
		if (u.getIsonline() == 1) {
			h.online.setVisibility(View.VISIBLE);
		} else {
			h.online.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}

	class ViewHolder {
		ImageView avatar, online;
		TextView name, grade, major;
		ImageLoader loader;
	}
}
