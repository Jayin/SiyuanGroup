package com.alumnigroup.adapter;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alumnigroup.api.ActivityAPI;
import com.alumnigroup.api.BusinessAPI;
import com.alumnigroup.api.GroupAPI;
import com.alumnigroup.api.IssuesAPI;
import com.alumnigroup.api.RestClient;
import com.alumnigroup.app.AppInfo;
import com.alumnigroup.app.R;
import com.alumnigroup.app.acty.ActivitiesInfo;
import com.alumnigroup.app.acty.BusinessDetail;
import com.alumnigroup.app.acty.CommunicationDetail;
import com.alumnigroup.app.acty.GroupInfo;
import com.alumnigroup.app.acty.SpaceOther;
import com.alumnigroup.app.acty.SpacePersonal;
import com.alumnigroup.entity.Cooperation;
import com.alumnigroup.entity.Dynamic;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.Issue;
import com.alumnigroup.entity.MActivity;
import com.alumnigroup.entity.MGroup;
import com.alumnigroup.entity.User;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.CalendarUtils;
import com.alumnigroup.widget.LoadingDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 动态页面
 * 
 * @author vector;restructured by Jayin Ton
 * 
 */
public class DynamicAdapter extends BaseAdapter {
	private LoadingDialog dialog;
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
		dialog = new LoadingDialog(context);
		dialog.setCancelable(false);
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
		ViewHolder h = null;
		if (convertView == null) {
			h = new ViewHolder();
			convertView = inflater.inflate(
					R.layout.item_lv_alldynamic_update_spatial, null);

			h.portrait = (ImageView) convertView
					.findViewById(R.id.item_lv_alldynamic_iv_portrait);
			h.name = (TextView) convertView
					.findViewById(R.id.item_lv_alldynamic_tv_name);
			h.content = (TextView) convertView
					.findViewById(R.id.item_lv_alldynamic_tv_content);
			h.time = (TextView) convertView
					.findViewById(R.id.item_lv_alldynamic_tv_datetime);
			convertView.setTag(h);
		} else {
			h = (ViewHolder) convertView.getTag();
		}
		final Dynamic dynamic = dynamics.get(position);

		h.portrait.setOnClickListener(new PortraitOnClick(position));

		if (dynamic.getUser().getAvatar() != null) {
			ImageLoader.getInstance().displayImage(
					RestClient.BASE_URL + dynamic.getUser().getAvatar(),
					h.portrait);
		} else {
			ImageLoader.getInstance()
					.displayImage(
							"drawable://" + R.drawable.ic_image_load_normal,
							h.portrait);
		}
		h.name.setText(dynamic.getUser().getProfile().getName());
		h.content.setText(dynamic.getMessage());
		h.time.setText(CalendarUtils.getTimeFromat(dynamic.getCreatetime(),
				CalendarUtils.TYPE_timeline));
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				 
				switch (dynamic.getItemtype()) {
				case Dynamic.Itme_type_user:
					User user = dynamic.getUser();
					Intent intent = new Intent(context, SpacePersonal.class);
					if (user.getId() == AppInfo.getUser(context).getId()) {
						intent = new Intent(context, SpacePersonal.class);
					}else{
						intent = new Intent(context, SpaceOther.class);
					}
					intent.putExtra("user", user);
					context.startActivity(intent);
					break;
				case Dynamic.Item_type_activity:
					ActivityAPI aapi = new ActivityAPI();
					aapi.view(dynamic.getItemid(), new JsonResponseHandler() {
						public void onStart() {
							dialog.show();
						};

						public void onFinish() {
							dialog.dismiss();
						};

						@Override
						public void onOK(Header[] headers, JSONObject obj) {
							try {
								MActivity acty = MActivity.create_by_json(obj
										.getJSONObject("activity").toString());
								Intent activityIntent = new Intent(context,
										ActivitiesInfo.class);
								activityIntent.putExtra("activity", acty);
								context.startActivity(activityIntent);
							} catch (JSONException e) {
								e.printStackTrace();
								Toast.makeText(context, "网络异常，解析错误",
										Toast.LENGTH_SHORT).show();
							}
						}

						@Override
						public void onFaild(int errorType, int errorCode) {
							Toast.makeText(context,
									ErrorCode.errorList.get(errorCode),
									Toast.LENGTH_SHORT).show();
						}

					});

					break;
				case Dynamic.Item_type_business:
					BusinessAPI bapi = new BusinessAPI();
					bapi.view(dynamic.getItemid(), new JsonResponseHandler() {
						public void onStart() {
							dialog.show();
						};

						public void onFinish() {
							dialog.dismiss();
						};

						@Override
						public void onOK(Header[] headers, JSONObject obj) {

							try {
								Cooperation c = Cooperation.create_by_json(obj
										.getJSONObject("cooperation")
										.toString());
								Intent cooperationIntent = new Intent(context,
										BusinessDetail.class);
								cooperationIntent.putExtra("cooperation", c);
								context.startActivity(cooperationIntent);
							} catch (JSONException e) {
								e.printStackTrace();
								Toast.makeText(context, "网络异常，解析错误",
										Toast.LENGTH_SHORT).show();
							}
						}

						@Override
						public void onFaild(int errorType, int errorCode) {
							Toast.makeText(context,
									ErrorCode.errorList.get(errorCode),
									Toast.LENGTH_SHORT).show();
						}
					});
					break;
				case Dynamic.Item_type_issue:
					IssuesAPI iapi = new IssuesAPI();
					iapi.view(dynamic.getItemid(), new JsonResponseHandler() {
						public void onStart() {
							dialog.show();
						};

						public void onFinish() {
							dialog.dismiss();
						};

						@Override
						public void onOK(Header[] headers, JSONObject obj) {
							try {
								Issue issue = Issue.create_by_json(obj
										.getJSONObject("issue").toString());
								Intent issueIntent = new Intent(context,
										CommunicationDetail.class);
								issueIntent.putExtra("issue", issue);
								context.startActivity(issueIntent);
							} catch (JSONException e) {
								e.printStackTrace();
								Toast.makeText(context, "该话题已删除",
										Toast.LENGTH_SHORT).show();
							}
						}

						@Override
						public void onFaild(int errorType, int errorCode) {
							Toast.makeText(context,
									ErrorCode.errorList.get(errorCode),
									Toast.LENGTH_SHORT).show();
						}
					});
					break;
				case Dynamic.Item_type_group:
					GroupAPI gapi = new GroupAPI();
					gapi.view(dynamic.getItemid(), new JsonResponseHandler() {
						public void onStart() {
							dialog.show();
						};

						public void onFinish() {
							dialog.dismiss();
						};

						@Override
						public void onOK(Header[] headers, JSONObject obj) {
							try {
								MGroup group = MGroup.create_by_json(obj
										.getJSONObject("group").toString());
								Intent groupIntent = new Intent(context,
										GroupInfo.class);
								groupIntent.putExtra("group", group);
								context.startActivity(groupIntent);
							} catch (JSONException e) {
								e.printStackTrace();
								Toast.makeText(context, "网络异常，解析错误",
										Toast.LENGTH_SHORT).show();
							}

						}

						@Override
						public void onFaild(int errorType, int errorCode) {
							Toast.makeText(context,
									ErrorCode.errorList.get(errorCode),
									Toast.LENGTH_SHORT).show();
						}
					});
					break;
				default:
					break;
				}

			}
		});
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

	class ViewHolder {
		ImageView portrait;
		TextView name, content, time;

	}

}
