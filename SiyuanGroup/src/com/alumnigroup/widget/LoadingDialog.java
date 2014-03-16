package com.alumnigroup.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.alumnigroup.app.R;

/**
 * 通用的进度条 提供一个setText()来更新装填<br>
 * 默认:<br>
 * 1.点击cancle没有,对话框不会消失<br>
 * 2.点击对话框外部会不消失<br>
 * 自定义：this.setCancelable(false); this.setCanceledOnTouchOutside(false);
 * 
 * @author Jayin Ton
 * 
 */
public class LoadingDialog extends Dialog {
	private TextView tv_updateinfo;
	private ImageView iv;
	private Context context;
    private Animation round ;
	public LoadingDialog(Context context) {
		super(context, R.style.Dialog_Theme_BaseDialog);
		this.context = context;
		init();
	}

	private void init() {
		// this.setCancelable(false);
		this.setCanceledOnTouchOutside(false);
		this.setCancelable(true);
		setContentView(R.layout.dlg_progress);
		iv = (ImageView) findViewById(R.id.dlg_progress_iv_progress);
		tv_updateinfo = (TextView) findViewById(R.id.dlg_progress_tv_updateinfo);

		// 加载动画
		round = AnimationUtils.loadAnimation(context,
				R.anim.load_animation);
	}

	public void setText(CharSequence text) {
		this.tv_updateinfo.setText(text);
	}
	
	@Override
	public void show() {
		super.show();
		// 使用ImageView显示动画
		iv.startAnimation(round);
	}
}
