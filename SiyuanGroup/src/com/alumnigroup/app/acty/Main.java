package com.alumnigroup.app.acty;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.RemoteViews;

import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.utils.AndroidUtils;
import com.alumnigroup.utils.L;
import com.alumnigroup.widget.ADView;

/**
 * 主界面
 * 
 * @author Jayin Ton
 * @since 2013.12.11;
 * 
 */
public class Main extends BaseActivity implements OnClickListener {
	private ADView adview;
	private LinearLayout content;
	private RelativeLayout parent_content;
	private int width = 0, height = 0;;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_main);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		width = AndroidUtils.getScreenSize(getContext())[0];
		height = AndroidUtils.getScreenSize(getContext())[1];
		L.i(width + "");
	}

	@Override
	protected void initLayout() {
		adapteScreent();
		adview = (ADView) _getView(R.id.acty_main_adview);
		String[] urls = new String[] {
				"http://lh3.ggpht.com/_yJtfXORDDe4/Sg-GN-1Mo4I/AAAAAAAAJQk/SVsfr1Iy7_I/s400/IMGP4048.jpg",
				"http://img.szhome.com/images/sznews/2012/11/20121102144231234.JPG",
				"http://www.carnews.com/Files/Editor_Files/image/Lee/minor5.jpg.pagespeed.ce.XG7AxB1en9.jpg" };
		adview.setURL(urls);
		adview.displayAD();

	}

	// 适配屏幕
	private void adapteScreent() {

		content = (LinearLayout) _getView(R.id.acty_main_content_one);
		parent_content = (RelativeLayout) _getView(R.id.acty_main_content);
		android.view.ViewGroup.LayoutParams params = content.getLayoutParams();
		params.height = width;
		RelativeLayout.LayoutParams rely_params = (RelativeLayout.LayoutParams) content
				.getLayoutParams();
		rely_params.addRule(RelativeLayout.CENTER_VERTICAL);

		content.setLayoutParams(params);
		content.setLayoutParams(rely_params);
	}

	@Override
	public void onClick(View v) {

	}

}
