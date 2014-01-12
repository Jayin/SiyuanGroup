package com.alumnigroup.app.acty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.utils.AndroidUtils;
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

	/**
	 * 个人空间
	 */
	private View btnOneSpace;

	/**
	 * 设置
	 */
	private View btnSetting;
	
	/**
	 * 消息
	 */
	private View btnMessage;

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
		adview.display();

		btnOneSpace = _getView(R.id.frame_main_one_myspace);
		btnOneSpace.setOnClickListener(this);
		
		btnSetting = _getView(R.id.frame_main_one_setting);
		btnSetting.setOnClickListener(this);
		
		btnMessage = _getView(R.id.frame_main_one_message);
		btnMessage.setOnClickListener(this);

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
		int id = v.getId();

		Intent intent = new Intent();

		switch (id) {
		case R.id.frame_main_one_myspace:
			intent.setClass(Main.this, SpacePersonal.class);
			startActivity(intent);
			break;
			
		case R.id.frame_main_one_setting:
			intent.setClass(Main.this, Setting.class);
			startActivity(intent);
			break;
			
		case R.id.frame_main_one_message:
			intent.setClass(Main.this, Message.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

}
