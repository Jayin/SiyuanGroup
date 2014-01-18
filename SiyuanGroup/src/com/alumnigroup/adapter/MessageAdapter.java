package com.alumnigroup.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alumnigroup.app.R;

/**
 * 系统消息ListView 的适配器
 * 
 * @author vector
 * 
 */
public class MessageAdapter extends BaseAdapter{

	/**
	 * 要适配的数据
	 */
	private List<MessageData> messageDatas;
	private Context context;

	/**
	 * 对应布局文件的控件
	 */
	private ImageView ivPortrait;
	private TextView tvName, tvAppend, tvTypeTile, tvDateTime,tvAgreed;
	private View btnAgree;

	/**
	 * 用来反射布局文件的
	 */
	private LayoutInflater inflater = null;

	public MessageAdapter(List<MessageData> messageDatas, Context context) {
		this.messageDatas = messageDatas;
		this.context = context;
		inflater = LayoutInflater.from(this.context);
	}

	public int getCount() {
		return messageDatas.size();
	}

	// 返回一个数据实体
	public Object getItem(int position) {
		return messageDatas.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	/**
	 * 返回一个view来显示
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final MessageData data = messageDatas.get(position);
		
		/**
		 * 解析布局,填充数据
		 */
		convertView = inflater.inflate(
				R.layout.item_lv_message, null);
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				data.getOnClick().other();
			}
		});
		
		ivPortrait = (ImageView) convertView.findViewById(R.id.item_lv_message_iv_portrait);
		ivPortrait.setImageBitmap(data.getIbPortrait());
		ivPortrait.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				data.getOnClick().portrait();
			}
		});
		
		tvAppend = (TextView) convertView.findViewById(R.id.item_lv_message_tv_append);
		tvAppend.setText(data.getAppend());
		
		tvDateTime = (TextView) convertView.findViewById(R.id.item_lv_message_tv_datetime);
		tvDateTime.setText(data.getDateTime());
		
		tvName = (TextView) convertView.findViewById(R.id.item_lv_message_tv_name);
		tvName.setText(data.getName());
		
		tvTypeTile = (TextView) convertView.findViewById(R.id.item_lv_message_tv_typetitle);
		tvTypeTile.setText(data.getTypeTitle());
		
		/**
		 * 同意按钮
		 */
		switch (data.getAgreeButtonType()) {
		case 0:
			break;
			
		case 1:
			btnAgree = convertView.findViewById(R.id.item_lv_message_btn_apree);
			btnAgree.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					data.getOnClick().agree();
				}
			});
			btnAgree.setVisibility(View.VISIBLE);
			break;
			
		case 2:
			tvAgreed =  (TextView) convertView.findViewById(R.id.item_lv_message_tv_apreeed);
			tvAgreed.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
		
		
		
		return convertView;
	}

	/**
	 * 封装要显示的数据
	 * 
	 * @author vector
	 * 
	 */
	public static class MessageData {

		/**
		 * 最右边同意按钮类型
		 * 0 : 没有，
		 * 1   ： 有
		 * 2 ：同意了
		 */
		private int agreeButtonType = 0;

		/**
		 * 最右边同意按钮类型
		 * 0 : 没有，
		 * 1   ： 有
		 * 2 ：同意了
		 */
		public int getAgreeButtonType() {
			return agreeButtonType;
		}
		
		/**
		 * 最右边同意按钮类型
		 * 0 : 没有，
		 * 1   ： 有
		 * 2 ：同意了
		 */
		public void setAgreeButtonType(int agreeButtonType) {
			this.agreeButtonType = agreeButtonType;
		}

		/**
		 * 头像，是谁做出了这个动作
		 */
		private Bitmap ibPortrait;

		/**
		 * name ，对应的名字
		 */
		private String name;

		/**
		 * 描述或者附加信息
		 */

		private String append;

		/**
		 * 回调对象
		 */
		private OnClick onClick;
		/**
		 * 类型说明，如：好友申请、好友邀请
		 */
		private String typeTitle;

		/**
		 * 时间
		 */
		private String dateTime;

		public String getTypeTitle() {
			return typeTitle;
		}

		public void setTypeTitle(String typeTitle) {
			this.typeTitle = typeTitle;
		}

		public String getDateTime() {
			return dateTime;
		}

		public void setDateTime(String dateTime) {
			this.dateTime = dateTime;
		}

		public MessageData() {
		}

		public MessageData(Bitmap ibPortrait, String name, String append,
				OnClick onClick, String typeTitle, String dateTime) {
			super();
			this.ibPortrait = ibPortrait;
			this.name = name;
			this.append = append;
			this.onClick = onClick;
			this.typeTitle = typeTitle;
			this.dateTime = dateTime;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAppend() {
			return append;
		}

		public Bitmap getIbPortrait() {
			return ibPortrait;
		}

		public void setIbPortrait(Bitmap ibPortrait) {
			this.ibPortrait = ibPortrait;
		}

		public void setAppend(String append) {
			this.append = append;
		}

		public OnClick getOnClick() {
			return onClick;
		}

		public void setOnClick(OnClick onClick) {
			this.onClick = onClick;
		}

	}

	public interface OnClick {
		/**
		 * 点击同意按钮回调
		 */
		public void agree();

		/**
		 * 点击头像回调
		 */
		public void portrait();

		/**
		 * 点击其他地方回调
		 */
		public void other();
	}

}
