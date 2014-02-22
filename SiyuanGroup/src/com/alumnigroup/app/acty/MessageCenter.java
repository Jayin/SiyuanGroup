package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.alumnigroup.adapter.MessageAdapter;
import com.alumnigroup.adapter.MessageAdapter.MessageData;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;

public class MessageCenter extends BaseActivity {

	/**
	 * header
	 */
	private View btnBack;
	
	
	/**
	 * 内容
	 */
	private ListView lvMessageContent;
	private List<MessageData> messageDatas;
	private MessageData data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_message);
		initData();
		initLayout();
	}

	protected void initData() {
		/**
		 * 拿到一张图片
		 */
		Drawable drawable = getResources().getDrawable(R.drawable.aa_po2);
		BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
		final Bitmap ibPortrait = bitmapDrawable.getBitmap();

		/**
		 * 回调函数
		 */
		final MessageAdapter.OnClick callback = new MessageAdapter.OnClick() {

			@Override
			public void portrait() {
				toast("portrait");
			}

			@Override
			public void other() {
				toast("other");
			}

			@Override
			public void agree() {
				toast("agree");
			}
		};

		data = new MessageData(ibPortrait, "黄廉温", "我是黄廉温，加我加我...",
				callback, "好友申请", "2014.1.11");
		
		
		/**
		 * 容器
		 */
		messageDatas = new ArrayList<MessageAdapter.MessageData>();
		
		/**
		 * 添加
		 */
		data = new MessageData(ibPortrait, "黄廉温", "我是黄廉温，加我加我...",
				callback, "好友申请", "2014.1.11");
		data.setAgreeButtonType(1);
		messageDatas.add(data);
		
		data = new MessageData(ibPortrait, "黄廉温", "我是黄廉温，加我加我...",
				callback, "好友申请", "2014.1.11");
		data.setAgreeButtonType(0);
		messageDatas.add(data);
		
		data = new MessageData(ibPortrait, "黄廉温", "同意了我的好友请求",
				callback, "好友申请", "2014.1.11");
		data.setAgreeButtonType(2);
		messageDatas.add(data);
		
		
		data = new MessageData(ibPortrait, "黄廉温", "邀请你加入 高尔夫 圈子",
				callback, "好友申请", "2014.1.11");
		data.setAgreeButtonType(1);
		messageDatas.add(data);
	}

	protected void initLayout() {
		lvMessageContent = (ListView) _getView(R.id.acty_message_lv_content);
		lvMessageContent.setAdapter(new MessageAdapter(messageDatas, this));
		
		btnBack = _getView(R.id.acty_head_btn_back);
		btnBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		finish();
	}
}
