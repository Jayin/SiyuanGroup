package com.alumnigroup.app.acty;

import java.util.Calendar;

import org.apache.http.Header;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alumnigroup.api.ActivityAPI;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.MGroup;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.CalendarUtils;
import com.alumnigroup.utils.EditTextUtils;
import com.alumnigroup.widget.TimePickDialog;
import com.alumnigroup.widget.TimePickDialog.OnSiglePickFinishedListener;

/**
 * 发起活动页面
 * 
 * @author Jayin Ton
 * 
 */
public class ActivitiesPublish extends BaseActivity {
	private View btn_back, btn_create, btn_invite, btn_starttime, btn_deadline;
	private EditText et_name, et_description, et_maxNum, et_money, et_duration,
			et_site;
	private TextView tv_startTime, tv_deadline;

	private MGroup group;
	private ActivityAPI api;
	private TimePickDialog dialog;
	private int sy_pickwhat = 1; // 1 pick: tv_startTime ; 2 tv_deadline
	private long starttime = 0, deadline = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_activitiespublish);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		group = (MGroup) getSerializableExtra("group");
		api = new ActivityAPI();
		if (group == null) {
			closeActivity();
			toast("error");
		}
	}

	@Override
	protected void initLayout() {
		btn_back = _getView(R.id.acty_head_btn_back);
		btn_create = _getView(R.id.acty_head_btn_create);
		btn_invite = _getView(R.id.btn_invite);
		btn_starttime = _getView(R.id.btn_starttime);
		btn_deadline = _getView(R.id.btn_deadline);

		et_name = (EditText) _getView(R.id.et_name);
		et_description = (EditText) _getView(R.id.et_description);
		et_maxNum = (EditText) _getView(R.id.et_maxNum);
		et_money = (EditText) _getView(R.id.et_money);
		tv_startTime = (TextView) _getView(R.id.et_startTime);
		et_duration = (EditText) _getView(R.id.et_duration);
		tv_deadline = (TextView) _getView(R.id.et_deadline);
		et_site = (EditText) _getView(R.id.et_site);
		dialog = new TimePickDialog(this);
		dialog.setOnSiglePickFinishedListener(new OnSiglePickFinishedListener() {

			@Override
			public void onFinish(long selecttime) {
				if (sy_pickwhat == 1) {
					starttime = selecttime;
					tv_startTime.setText(CalendarUtils.getTimeFromat(
							selecttime, CalendarUtils.TYPE_TWO));
				} else if (sy_pickwhat == 2) {
					deadline = selecttime;
					tv_deadline.setText(CalendarUtils.getTimeFromat(selecttime,
							CalendarUtils.TYPE_TWO));
				}

			}
		});
		btn_back.setOnClickListener(this);
		btn_create.setOnClickListener(this);
		btn_invite.setOnClickListener(this);
		btn_starttime.setOnClickListener(this);
		btn_deadline.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.acty_head_btn_create:
			// create..
			if (check()) {
				int groupid = group.getId();
				int maxnum = Integer.parseInt(EditTextUtils
						.getTextTrim(et_maxNum));
				int duration = Integer.parseInt(EditTextUtils
						.getTextTrim(et_duration));
				int statusid = 0;// 0接受报名、1截止报名、2活动结束、3活动取消
				long money = Integer.parseInt(EditTextUtils
						.getTextTrim(et_money));
				String name = EditTextUtils.getTextTrim(et_name);
				String content = EditTextUtils.getTextTrim(et_description);
				debug("发起活动:");
				debug("groupid->"+groupid+"");
				debug("maxnum->"+maxnum+"");
				debug("duration->"+duration+"");
				debug("statusid->"+statusid+"");
				debug("money->"+money+"");
				debug("name->"+name+"");
				debug("content->"+content+"");
				debug("starttime->"+starttime+"");
				api.creatAcivity(groupid, maxnum, starttime, duration,
						statusid, money, name, content,
						new JsonResponseHandler() {

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								toast("活动发起成功");
								closeActivity();
							}

							@Override
							public void onFaild(int errorType, int errorCode) {
								toast("网络异常 错误码:" + errorCode);
							}
						});
			}
			break;
		case R.id.btn_invite:
			// to 好友列表
			toast("to friend list");
			break;
		case R.id.btn_starttime:
			// select time
			sy_pickwhat = 1;
			dialog.show();
			break;
		case R.id.btn_deadline:
			// select time
			sy_pickwhat = 2;
			dialog.show();
			break;
		default:
			break;
		}
	}

	private boolean check() {
		if (EditTextUtils.isEmpty(et_name)) {
			toast("活动名称不能为空");
			return false;
		}
		// if(EditTextUtils.isEmpty(et_description)){
		// toast("活动简介不能为空");
		// return false;
		// }
		if (EditTextUtils.isEmpty(et_maxNum)) {
			toast("活动人数不能为空");
			return false;
		}
		if (EditTextUtils.isEmpty(et_money)) {
			toast("活动费用不能为空");
			return false;
		}
		// if(EditTextUtils.isEmpty(et_startTime)){
		// toast("活动开始时间不能为空");
		// return false;
		// }
		if (EditTextUtils.isEmpty(et_duration)) {
			toast("活动时长不能为空");
			return false;
		}
		// if (EditTextUtils.isEmpty(tv_deadline)) {
		// toast("活动截止日期不能为空");
		// return false;
		// }
		if (EditTextUtils.isEmpty(et_site)) {
			toast("活动地点不能为空");
			return false;
		}
		return true;

	}

}
