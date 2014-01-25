package com.alumnigroup.app.acty;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;

public class BusinessDetail extends BaseActivity {
	private Communication c;
	private TextView tv_username, tv_projectname, tv_deadline, tv_description;
	private PopupWindow mPopupWindow;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_businessdetail);
		initData();
		initLayout();
	}
	
	private void initPopupWindow() {
		View view = getLayoutInflater()
				.inflate(R.layout.popup_acty_group, null);
		View btn_search = view.findViewById(R.id.search), btn_create = view
				.findViewById(R.id.create);
		btn_search.setOnClickListener(this);
		btn_create.setOnClickListener(this);
		mPopupWindow = new PopupWindow(view);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setOutsideTouchable(true);

		// 控制popupwindow的宽度和高度自适应
		view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		mPopupWindow.setWidth(view.getMeasuredWidth());
		mPopupWindow.setHeight(view.getMeasuredHeight());
	}

	@Override
	protected void initData() {
		c = getSerializableExtra("cooperation");
	}

	@Override
	protected void initLayout() {

	}

}
