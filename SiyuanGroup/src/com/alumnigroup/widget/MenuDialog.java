package com.alumnigroup.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alumnigroup.app.R;

/**
 * 菜单对话框
 *  <li>使用 show(int),setData() 
 * <li>	  listView->弹出对话框,所以要记录parentPosition
 * @author Jayin Ton
 * 
 */
public class MenuDialog extends Dialog {
	private Context context;
	private ListView lv;
	private View close;
	private List<String> data;
	private MenuAdapter adapter;
	/** 点击哪(位置为parentPosition)一个item导致MenuDialog显示出来 */
	private int parentPosition;

	public MenuDialog(Context context) {
		super(context, R.style.Dialog_Theme_BaseDialog);
		this.context = context;
		init();
	}

	private void init() {
		this.setContentView(R.layout.dlg_menu);
		lv = (ListView) findViewById(R.id.lv_listview);
		close = findViewById(R.id.btn_close);
		close.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				MenuDialog.this.dismiss();
			}
		});
		data = new ArrayList<String>();
		adapter = new MenuAdapter(data);
		lv.setAdapter(adapter);
	}

	/**
	 * 设置内容
	 * 
	 * @param data
	 */
	public void setData(List<String> data) {
		this.data.clear();
		this.data.addAll(data);
		adapter.notifyDataSetChanged();
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		lv.setOnItemClickListener(listener);
	}

	/** listView->弹出*/
	public void show(int position) {
		super.show();
		setParentPosition(position);
	}

	public int getParentPosition() {
		return parentPosition;
	}

	public void setParentPosition(int parentPosition) {
		this.parentPosition = parentPosition;
	}

	class MenuAdapter extends BaseAdapter {

		private List<String> data;

		public MenuAdapter(List<String> data) {
			this.data = data;
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
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_lv_dlg_menu, null);
			((TextView) convertView).setText(data.get(position));
			return convertView;
		}

	}

}
