package com.alumnigroup.app.acty;

import org.apache.http.Header;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.alumnigroup.api.GroupAPI;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.StringUtils;
import com.alumnigroup.utils.WidgetUtils;

/**
 * 创建圈子
 * 
 * @author Jayin Ton
 * 
 */
public class GroupCreate extends BaseActivity {
	private View btn_back, btn_create, btn_invite;
	private EditText et_name, et_description;
	private GroupAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_groupcreate);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		api = new GroupAPI();
	}

	@Override
	protected void initLayout() {
		btn_back = _getView(R.id.acty_head_btn_back);
		btn_create = _getView(R.id.acty_head_btn_create);
		btn_invite = _getView(R.id.invite);
		et_name = (EditText) _getView(R.id.et_name);
		et_description = (EditText) _getView(R.id.et_description);

		btn_back.setOnClickListener(this);
		btn_create.setOnClickListener(this);
		btn_invite.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.acty_head_btn_create:
			String name = WidgetUtils.getTextTrim(et_name);
			String description = WidgetUtils.getTextTrim(et_description);
            if(name==null || StringUtils.isEmpty(name)){
            	toast("圈子名称不能为空");
                return ;	
            }
            
			api.createGroup(name, description, new JsonResponseHandler() {

				@Override
				public void onOK(Header[] headers, JSONObject obj) {
                     toast("创建成功");
                     closeActivity();
				}

				@Override
				public void onFaild(int errorType, int errorCode) {
                       toast("创建失败 错误码:"+errorCode);
				}
			});
			break;
		case R.id.invite:
			toast("to friend list");
			break;

		default:
			break;
		}
	}

}
