package com.alumnigroup.adapter;

import java.util.ArrayList;

import com.alumnigroup.app.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 相册适配器
 * 
 * @author vector
 * 
 */
public class AlbumAdapter extends BaseAdapter {

	/**
	 * 要适配的数据
	 */
	private ArrayList<String> alAlbumUrl;
	private Context context;

	/**
	 * 用来反射布局文件的
	 */
	private LayoutInflater inflater = null;

	public AlbumAdapter(ArrayList<String> alAlbumUrl, Context context) {
		this.alAlbumUrl = alAlbumUrl;
		this.context = context;
		inflater = LayoutInflater.from(this.context);
	}

	@Override
	public int getCount() {
		return alAlbumUrl.size();
	}

	@Override
	public Object getItem(int position) {
		return alAlbumUrl.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.item_acty_edit_album_gv_image, null);
		return convertView;
	}

}