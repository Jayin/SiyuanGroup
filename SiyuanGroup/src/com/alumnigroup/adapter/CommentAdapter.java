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
import com.alumnigroup.entity.Comment;
import com.alumnigroup.utils.CalendarUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CommentAdapter extends BaseAdapter {

	private List<Comment> data;
	private Context context;

	public CommentAdapter(Context context, List<Comment> data) {
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
		ViewHolder h = null;
		if (convertView == null) {
			h = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_lv_acty_communicationdetail, null);
			h.name = (TextView) convertView
					.findViewById(R.id.item_lv_acty_communicationdetai_name);
			h.positime = (TextView) convertView
					.findViewById(R.id.item_lv_acty_communicationdetai_posttime);
			h.body = (TextView) convertView
					.findViewById(R.id.item_lv_acty_communicationdetai_body);
			h.avater = (ImageView) convertView
					.findViewById(R.id.item_lv_acty_communicationdetai_avater);
			convertView.setTag(h);
		} else {
			h = (ViewHolder) convertView.getTag();
		}
		final Comment  c = data.get(position);
		h.name.setText(c.getUser().getProfile().getName());
		h.positime.setText(CalendarUtils.getTimeFromat(c
				.getPosttime(), CalendarUtils.TYPE_timeline));
		h.body.setText(c.getBody());
		if(c.getUser().getAvatar()!=null){
			ImageLoader.getInstance().displayImage(
					RestClient.BASE_URL
							+ c.getUser().getAvatar(),
					h.avater);
		}else{
			ImageLoader.getInstance().displayImage(
					"drawable://"+R.drawable.ic_image_load_normal,
					h.avater);
		}
		
		h.avater.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 Intent intent = null;
				 if(c.getUser().getId() == AppInfo.getUser(context).getId()){
					  intent = new Intent(context,SpacePersonal.class);
				 }else{
					  intent = new Intent(context,SpaceOther.class);
				 }
				 intent.putExtra("user", c.getUser());
				 context.startActivity(intent);
			}
		});
		
		return convertView;
	}

	class ViewHolder {
		TextView name, positime, body;
		ImageView avater;
	}
	
}
