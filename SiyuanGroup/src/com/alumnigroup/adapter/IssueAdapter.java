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
import com.alumnigroup.app.R;
import com.alumnigroup.app.acty.ImageDisplay;
import com.alumnigroup.entity.Issue;
import com.alumnigroup.entity.MPicture;
import com.alumnigroup.utils.CalendarUtils;
import com.alumnigroup.utils.L;
import com.nostra13.universalimageloader.core.ImageLoader;
/**
 * 话题适配器
 * @author Jayin Ton
 *
 */
public class IssueAdapter extends BaseAdapter{
	private List<Issue> data;
	private Context context;

	public IssueAdapter(Context context, List<Issue> data) {
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
					R.layout.item_lv_acty_communication, null);
			h.name = (TextView) convertView
					.findViewById(R.id.item_lv_acty_comminication_name);
			h.major = (TextView) convertView
					.findViewById(R.id.item_lv_acty_comminication_major);
			h.posttime = (TextView) convertView
					.findViewById(R.id.item_lv_acty_comminication_posttime);
			h.title = (TextView) convertView
					.findViewById(R.id.item_lv_acty_comminication_title);
			h.body = (TextView) convertView
					.findViewById(R.id.item_lv_acty_comminication_body);
			h.numComment = (TextView) convertView
					.findViewById(R.id.item_lv_acty_comminication_numComment);
			h.avatar = (ImageView) convertView
					.findViewById(R.id.item_lv_acty_comminication_avatar);
			h.pic1 = (ImageView) convertView.findViewById(R.id.iv_pic1);
			convertView.setTag(h);
		} else {
			h = (ViewHolder) convertView.getTag();
		}
		Issue issue = data.get(position);
		h.name.setText(issue.getUser().getProfile().getName());
		h.major.setText(issue.getUser().getProfile().getMajor());
		h.posttime.setText(CalendarUtils.getTimeFromat(issue.getPosttime(),
				CalendarUtils.TYPE_timeline));
		h.title.setText(issue.getTitle());
		h.body.setText(issue.getBody());
		h.numComment.setText(issue.getNumComments() + "");
		if (issue.getUser().getAvatar() != null) {
			ImageLoader.getInstance()
					.displayImage(
							RestClient.BASE_URL + issue.getUser().getAvatar(),
							h.avatar);
		} else {
			ImageLoader.getInstance().displayImage(
					"drawable://" + R.drawable.ic_img_avatar_default, h.avatar);
		}
		// 暂时1张图片
		h.pic1.setVisibility(View.GONE);
		if (issue.getPictures() != null && issue.getPictures().size() > 0) {
			h.pic1.setVisibility(View.VISIBLE);
			// for (MPicture pic : issue.getPictures()) {
			final MPicture pic = issue.getPictures().get(0);
			L.i(pic.toString());
			h.pic1.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(
					RestClient.BASE_URL + pic.getPath(), h.pic1);
			// }

			h.pic1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, ImageDisplay.class);
					intent.putExtra("url", RestClient.BASE_URL + pic.getPath());
					context.startActivity(intent);
				}
			});
		}

		return convertView;
	}

	class ViewHolder {
		TextView name, major, posttime, title, body, numComment;
		ImageView avatar, pic1, pic2, pic3;
	}
}
