package com.alumnigroup.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.alumnigroup.app.R;
import com.alumnigroup.entity.Issue;

/**
 * 话题适配器
 * @author vector
 *
 */
public class CommunicationAdapter extends BaseAdapter {

	private ArrayList<Issue> alCommunications;
	private Context context;
	private LayoutInflater inflater;
	
	public CommunicationAdapter(ArrayList<Issue> alCommunications,
			Context context) {
		this.alCommunications = alCommunications;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return alCommunications.size();
	}

	@Override
	public Object getItem(int position) {
		return alCommunications.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.item_lv_acty_communication, null);
		return convertView;
	}

}
